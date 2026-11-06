package com.eventostec.api.domain.event;

import java.util.UUID;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(UUID id) { super("Evento não encontrado: " + id); }
}
