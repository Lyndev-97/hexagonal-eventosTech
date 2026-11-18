package com.eventostec.api.domain.event;

import java.util.Date;
import java.util.UUID;

// Tipo de leitura (read model) desacoplado de qualquer framework de persistência.
// É o que atravessa as portas (EventRepositoryPort / EventUseCases) e chega aos
// adapters de entrada
public record EventSummary(
        UUID id,
        String title,
        String description,
        Date date,
        String imgUrl,
        String eventUrl,
        Boolean remote,
        String city,
        String uf) {
}
