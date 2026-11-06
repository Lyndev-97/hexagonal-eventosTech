package com.eventostec.api.adapters.outbound.repositories;

import com.eventostec.api.adapters.outbound.entities.CouponEntity;
import com.eventostec.api.adapters.outbound.repositories.jpa.JpaCouponRepository;
import com.eventostec.api.adapters.outbound.repositories.mapper.PersistenceMapper;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CouponRepositoryImpl implements CouponRepositoryPort {

    private final JpaCouponRepository jpaCouponRepository;
    //private final EventMapper mapper;

    public CouponRepositoryImpl(JpaCouponRepository jpaCouponRepository) {
        this.jpaCouponRepository = jpaCouponRepository;
    }

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity saved = jpaCouponRepository.save(new CouponEntity(coupon));
        return PersistenceMapper.toDomain(saved);
    }

    @Override
    public List<Coupon> findByEventIdAndValidAfter(UUID eventId, Date currentDate) {
        return jpaCouponRepository.findByEventIdAndValidAfter(eventId, currentDate)
                .stream().map(PersistenceMapper::toDomain).toList();
    }
}
