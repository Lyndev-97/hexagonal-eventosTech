package com.eventostec.api.infrastructure.config;

import com.eventostec.api.adapters.outbound.storage.ImageUploaderPort;
import com.eventostec.api.application.service.AddressServiceImpl;
import com.eventostec.api.application.service.CouponServiceImpl;
import com.eventostec.api.application.service.EventServiceImpl;
import com.eventostec.api.application.usercases.AddressUseCases;
import com.eventostec.api.application.usercases.CouponUseCases;
import com.eventostec.api.application.usercases.EventUseCases;
import com.eventostec.api.domain.address.AddressRepositoryPort;
import com.eventostec.api.domain.coupon.CouponRepositoryPort;
import com.eventostec.api.domain.event.EventRepository;
import com.eventostec.api.utils.mappers.EventMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeansConfig {

    // O EventServiceImpl depende de contratos (UseCases), não de implementações concretas.
    // Isso mantém a aplicação desacoplada das classes do adapter.
    @Bean
    public EventUseCases eventUseCases(
            AddressUseCases addressUseCases,
            CouponUseCases couponUseCases,
            EventRepository repository,
            ImageUploaderPort imageUploader,
            EventMapper mapper,
            @Value("${admin.key}") String adminKey
    ) {
        return new EventServiceImpl(
                addressUseCases,
                couponUseCases,
                repository,
                imageUploader,
                mapper,
                adminKey
        );
    }

    @Bean
    public AddressUseCases addressUseCases(AddressRepositoryPort addressRepository) {
        return new AddressServiceImpl(addressRepository);
    }

    @Bean
    public CouponUseCases couponUseCases(CouponRepositoryPort couponRepository,
                                         EventRepository eventRepository) {
        return new CouponServiceImpl(couponRepository, eventRepository);
    }
}
