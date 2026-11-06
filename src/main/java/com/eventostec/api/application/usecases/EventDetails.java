package com.eventostec.api.application.usecases;

import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.event.Event;

import java.util.List;
import java.util.Optional;

public record EventDetails(Event event, Optional<Address> address, List<Coupon> coupons) {}
