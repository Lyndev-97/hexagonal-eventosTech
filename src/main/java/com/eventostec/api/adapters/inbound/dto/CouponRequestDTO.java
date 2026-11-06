package com.eventostec.api.adapters.inbound.dto;

public record CouponRequestDTO(String code, Integer discount, Long valid) {
}
