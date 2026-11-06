package com.eventostec.api.application.service;

import com.eventostec.api.application.usecases.AddCouponCommand;
import com.eventostec.api.application.usecases.CouponUseCases;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRepositoryPort;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventNotFoundException;
import com.eventostec.api.domain.event.EventRepositoryPort;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CouponServiceImpl implements CouponUseCases {

    private final CouponRepositoryPort couponRepository;
    private final EventRepositoryPort eventRepositoryPort;

    public CouponServiceImpl(CouponRepositoryPort couponRepository, EventRepositoryPort eventRepositoryPort) {
        this.couponRepository = couponRepository;
        this.eventRepositoryPort = eventRepositoryPort;
    }

    @Override
    public Coupon addCouponToEvent(UUID eventId, AddCouponCommand command) {

        Event event = eventRepositoryPort.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));

        Coupon coupon = new Coupon();
        coupon.setCode(command.code());
        coupon.setDiscount(command.discount());
        coupon.setValid(command.valid());
        coupon.setEvent(event);

        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> consultCoupons(UUID eventId, Date currentDate) {
        return couponRepository.findByEventIdAndValidAfter(eventId, currentDate);
    }
}

