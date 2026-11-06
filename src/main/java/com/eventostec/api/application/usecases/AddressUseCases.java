package com.eventostec.api.application.usecases;

import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.event.Event;

import java.util.Optional;
import java.util.UUID;

public interface AddressUseCases {
    public void createAddress(String city, String uf, Event event);
    public Optional<Address> findByEventId(UUID eventId);
}
