package com.eventostec.api.application.service;

import com.eventostec.api.application.usecases.AddressUseCases;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.address.AddressRepositoryPort;
import com.eventostec.api.domain.event.Event;

import java.util.Optional;
import java.util.UUID;

public class AddressServiceImpl implements AddressUseCases {

    private final AddressRepositoryPort addressRepository;

    public AddressServiceImpl(AddressRepositoryPort addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public void createAddress(String city, String uf, Event event) {
        Address address = new Address();
        address.setCity(city);
        address.setUf(uf);
        address.setEvent(event);
        addressRepository.save(address);
    }

    @Override
    public Optional<Address> findByEventId(UUID eventId) {
        return addressRepository.findByEventId(eventId);
    }
}

