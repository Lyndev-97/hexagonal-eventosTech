package com.eventostec.api.adapters.inbound.controller;

import com.eventostec.api.adapters.inbound.dto.CouponResponseDTO;
import com.eventostec.api.application.usecases.AddCouponCommand;
import com.eventostec.api.application.usecases.CouponUseCases;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.adapters.inbound.dto.CouponRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    private final CouponUseCases couponUseCases;

    public CouponController(CouponUseCases couponUseCases) {
        this.couponUseCases = couponUseCases;
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<CouponResponseDTO> addCouponsToEvent(@PathVariable UUID eventId, @RequestBody CouponRequestDTO data) {
        AddCouponCommand command = new AddCouponCommand(data.code(), data.discount(), new Date(data.valid()));
        Coupon coupon = couponUseCases.addCouponToEvent(eventId, command);
        return ResponseEntity.ok(new CouponResponseDTO(
                coupon.getId(), coupon.getCode(), coupon.getDiscount(), coupon.getValid()));
    }
}
