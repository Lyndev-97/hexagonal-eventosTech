package com.eventostec.api.utils.mappers;

import com.eventostec.api.adapters.outbound.entities.EventEntity;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventDetailsDTO;
import com.eventostec.api.domain.event.EventRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Observação didática:
// Este mapper é responsável por converter entre os modelos de domínio e os DTOs/entidades JPA,
// sem misturar regra de negócio com persistência.
@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "dto.title", target = "title")
    @Mapping(source = "dto.description", target = "description")
    @Mapping(source = "imgUrl", target = "imgUrl")
    @Mapping(source = "dto.eventUrl", target = "eventUrl")
    @Mapping(source = "dto.date", target = "date", qualifiedByName = "epochToDate")
    @Mapping(source = "dto.remote", target = "remote")
    Event dtoToEntity(EventRequestDTO dto, String imgUrl);

    // Implementação manual para evitar problemas de mapeamento com parâmetros múltiplos
    // e manter a leitura didática mais simples.
    default Event jpaToDomain(EventEntity jpa) {
        if (jpa == null) {
            return null;
        }

        return new Event(
                jpa.getId(),
                jpa.getTitle(),
                jpa.getDescription(),
                jpa.getImgUrl(),
                jpa.getEventUrl(),
                jpa.getRemote(),
                jpa.getDate()
        );
    }

    default EventDetailsDTO domainToDetailsDto(Event event, Optional<Address> address, List<Coupon> coupons) {
        String city = address.map(Address::getCity).orElse("");
        String uf = address.map(Address::getUf).orElse("");

        List<EventDetailsDTO.CouponDTO> couponDTOs = coupons.stream()
                .map(coupon -> new EventDetailsDTO.CouponDTO(
                        coupon.getCode(),
                        coupon.getDiscount(),
                        coupon.getValid()))
                .collect(Collectors.toList());

        return new EventDetailsDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                city,
                uf,
                event.getImgUrl(),
                event.getEventUrl(),
                couponDTOs
        );
    }

    @Named("epochToDate")
    default Date epochToDate(Long timestamp) {
        return timestamp != null ? new Date(timestamp) : null;
    }
}
