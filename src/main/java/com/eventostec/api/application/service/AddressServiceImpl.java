package com.eventostec.api.application.service;

import com.eventostec.api.application.usercases.AddressUseCases;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.address.AddressRepositoryPort;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressUseCases {

    private final AddressRepositoryPort addressRepository;

    public AddressServiceImpl(AddressRepositoryPort addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public void createAddress(EventRequestDTO data, Event event) {
        Address address = new Address();
        address.setCity(data.city());
        address.setUf(data.state());
        address.setEvent(event);
        addressRepository.save(address);
    }

    @Override
    public Optional<Address> findByEventId(UUID eventId) {
        return addressRepository.findByEventId(eventId);
    }
}

