package com.eventostec.api.adapters.outbound.repositories.mapper;

import com.eventostec.api.adapters.outbound.entities.AddressEntity;
import com.eventostec.api.adapters.outbound.entities.CouponEntity;
import com.eventostec.api.adapters.outbound.entities.EventEntity;
import com.eventostec.api.adapters.outbound.repositories.jpa.EventAddressProjection;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventSummary;

public final class PersistenceMapper {

    private PersistenceMapper() {}

    public static Event toDomain(EventEntity e) {
        if (e == null) return null;
        return new Event(e.getId(),
                         e.getTitle(),
                         e.getDescription(),
                         e.getImgUrl(),
                         e.getEventUrl(),
                         e.getRemote(),
                         e.getDate());
    }

    public static Coupon toDomain(CouponEntity c) {
        if (c == null) return null;
        return new Coupon(c.getId(), c.getCode(), c.getDiscount(), c.getValid(), toDomain(c.getEvent()));
    }

    public static Address toDomain(AddressEntity a) {
        if (a == null) return null;
        return new Address(a.getId(), a.getCity(), a.getUf(), toDomain(a.getEvent()));
    }

    public static EventSummary toSummary(EventAddressProjection p) {
        if (p == null) return null;
        return new EventSummary(p.getId(),
                                p.getTitle(),
                                p.getDescription(),
                                p.getDate(),
                                p.getImgUrl(),
                                p.getEventUrl(),
                                p.getRemote(),
                                p.getCity(),
                                p.getUf());
    }
}
