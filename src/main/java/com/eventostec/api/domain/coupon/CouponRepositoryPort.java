package com.eventostec.api.domain.coupon;

import java.util.Date;
import java.util.List;
import java.util.UUID;

// Porta de saída da aplicação.
// A camada de aplicação fala com esta interface e não com o banco/JPA diretamente.
public interface CouponRepositoryPort {
    Coupon save(Coupon coupon);
    List<Coupon> findByEventIdAndValidAfter(UUID eventId, Date currentDate);
}
