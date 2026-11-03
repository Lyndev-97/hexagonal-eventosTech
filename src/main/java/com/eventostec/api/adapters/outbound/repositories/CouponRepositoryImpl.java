package com.eventostec.api.adapters.outbound.repositories;

import com.eventostec.api.adapters.outbound.entities.CouponEntity;
import com.eventostec.api.adapters.outbound.repositories.jpa.JpaCouponRepository;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class CouponRepositoryImpl implements CouponRepositoryPort {

    private final JpaCouponRepository jpaCouponRepository;

    public CouponRepositoryImpl(JpaCouponRepository jpaCouponRepository) {
        this.jpaCouponRepository = jpaCouponRepository;
    }

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity entity = new CouponEntity(coupon);
        this.jpaCouponRepository.save(entity);
        return new Coupon(entity.getId(), entity.getCode(), entity.getDiscount(), entity.getValid(), entity.getEvent());
    }

    @Override
    public List<Coupon> findByEventIdAndValidAfter(UUID eventId, Date currentDate) {
        return jpaCouponRepository.findByEventIdAndValidAfter(eventId, currentDate);
    }
}
