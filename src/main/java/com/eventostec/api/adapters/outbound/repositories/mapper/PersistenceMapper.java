package com.eventostec.api.adapters.outbound.repositories.mapper;

import com.eventostec.api.adapters.outbound.entities.AddressEntity;
import com.eventostec.api.adapters.outbound.entities.CouponEntity;
import com.eventostec.api.adapters.outbound.entities.EventEntity;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.event.Event;

public final class PersistenceMapper {

    private PersistenceMapper() {}

    public static Event toDomain(EventEntity e) {
        if (e == null) return null;
        return new Event(e.getId(), e.getTitle(), e.getDescription(),
                e.getImgUrl(), e.getEventUrl(), e.getRemote(), e.getDate());
    }

    public static Coupon toDomain(CouponEntity c) {
        if (c == null) return null;
        return new Coupon(c.getId(), c.getCode(), c.getDiscount(), c.getValid(), toDomain(c.getEvent()));
    }

    public static Address toDomain(AddressEntity a) {
        if (a == null) return null;
        return new Address(a.getId(), a.getCity(), a.getUf(), toDomain(a.getEvent()));
    }
}
