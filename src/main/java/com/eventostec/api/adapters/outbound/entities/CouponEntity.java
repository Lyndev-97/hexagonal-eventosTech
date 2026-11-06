package com.eventostec.api.adapters.outbound.entities;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String code;
    private Integer discount;
    private Date valid;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    public CouponEntity(Coupon coupon) {
        this.code = coupon.getCode();
        this.discount = coupon.getDiscount();
        this.valid = coupon.getValid();
        this.event = new EventEntity(coupon.getEvent());
    }
}
