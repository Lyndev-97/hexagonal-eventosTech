package com.eventostec.api.domain.address;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepositoryPort {
    public Optional<Address> findByEventId(UUID eventId);
    public Address save(Address address);
}
