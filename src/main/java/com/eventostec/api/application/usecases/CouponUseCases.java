package com.eventostec.api.application.usecases;

import com.eventostec.api.domain.coupon.Coupon;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CouponUseCases {
    public Coupon addCouponToEvent(UUID eventId, AddCouponCommand command);
    public List<Coupon> consultCoupons(UUID eventId, Date currentDate);
}
