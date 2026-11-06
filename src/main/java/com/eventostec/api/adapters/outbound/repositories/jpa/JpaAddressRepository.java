package com.eventostec.api.adapters.outbound.repositories.jpa;

import com.eventostec.api.adapters.outbound.entities.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaAddressRepository extends JpaRepository<AddressEntity, UUID> {
    Optional<AddressEntity> findByEventId(UUID eventId);
}
