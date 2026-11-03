package com.eventostec.api.application.usercases;

import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;

import java.util.Optional;
import java.util.UUID;

public interface AddressUseCases {
    public void createAddress(EventRequestDTO data, Event event);
    public Optional<Address> findByEventId(UUID eventId);
}
