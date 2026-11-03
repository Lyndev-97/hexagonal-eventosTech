package com.eventostec.api.adapters.inbound.controller;

import com.eventostec.api.application.usercases.CouponUseCases;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRepositoryPort;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.application.service.CouponServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    private final CouponUseCases couponUseCases;

    public CouponController(CouponUseCases couponUseCases) {
        this.couponUseCases = couponUseCases;
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<Coupon> addCouponsToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDTO data) {
        Coupon coupons = couponUseCases.addCouponToEvent(eventId, data);
        return ResponseEntity.ok(coupons);
    }
}
