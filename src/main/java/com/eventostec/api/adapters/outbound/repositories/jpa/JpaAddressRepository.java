package com.eventostec.api.adapters.outbound.repositories.jpa;

import com.eventostec.api.adapters.outbound.entities.AddressEntity;
import com.eventostec.api.domain.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaAddressRepository extends JpaRepository<AddressEntity, UUID> {
    Optional<Address> findByEventId(UUID eventId);
}
