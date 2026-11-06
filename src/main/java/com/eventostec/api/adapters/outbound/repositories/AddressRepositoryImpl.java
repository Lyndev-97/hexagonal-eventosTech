package com.eventostec.api.adapters.outbound.repositories;

import com.eventostec.api.adapters.outbound.entities.AddressEntity;
import com.eventostec.api.adapters.outbound.repositories.jpa.JpaAddressRepository;
import com.eventostec.api.adapters.outbound.repositories.mapper.PersistenceMapper;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.address.AddressRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AddressRepositoryImpl implements AddressRepositoryPort {
    private final JpaAddressRepository jpaAddressRepository;
    //private final EventMapper mapper;

    public AddressRepositoryImpl(JpaAddressRepository jpaAddressRepository) {
        this.jpaAddressRepository = jpaAddressRepository;
    }

    @Override
    public Optional<Address> findByEventId(UUID eventId) {
        return jpaAddressRepository.findByEventId(eventId).map(PersistenceMapper::toDomain);
    }

    @Override
    public Address save(Address address) {
        return PersistenceMapper.toDomain(jpaAddressRepository.save(new AddressEntity(address)));
    }
}
