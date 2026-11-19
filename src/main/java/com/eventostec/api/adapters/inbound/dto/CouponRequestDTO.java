package com.eventostec.api.adapters.inbound.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CouponRequestDTO(
        @NotBlank(message = "O código do cupom é obrigatório") String code,
        @NotNull(message = "O percentual de desconto é obrigatório") @Positive(message = "O desconto deve ser positivo") Integer discount,
        @NotNull(message = "A data de validade é obrigatória") Long valid) {
}
