package com.eventostec.api.adapters.outbound.repositories;

import com.eventostec.api.adapters.outbound.entities.EventEntity;
import com.eventostec.api.adapters.outbound.repositories.jpa.JpaEventRepository;
import com.eventostec.api.adapters.outbound.repositories.mapper.PersistenceMapper;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventAddressProjection;
import com.eventostec.api.domain.event.EventRepositoryPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventRepositoryPortImpl implements EventRepositoryPort {

    private final JpaEventRepository jpaEventRepository;
    /*
    @Autowired
    private EventMapper mapper;
    */
    public EventRepositoryPortImpl(JpaEventRepository jpaEventRepository) {
        this.jpaEventRepository = jpaEventRepository;
    }

    @Override
    public Event save(Event event) {
        return PersistenceMapper.toDomain(jpaEventRepository.save(new EventEntity(event)));
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return jpaEventRepository.findById(id).map(PersistenceMapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return jpaEventRepository.findAll().stream().map(PersistenceMapper::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        this.jpaEventRepository.deleteById(id);
    }

    @Override
    public List<EventAddressProjection> findUpcomingEvents(int page, int size) {
        return jpaEventRepository.findUpcomingEvents(new Date(), PageRequest.of(page, size)).getContent();
    }

    @Override
    public List<EventAddressProjection> findFilteredEvents(String city, String uf, Date startDate, Date endDate, int page, int size) {
        return jpaEventRepository.findFilteredEvents(city, uf, startDate, endDate, PageRequest.of(page, size)).getContent();
    }

    @Override
    public List<EventAddressProjection> findEventsByTitle(String title) {
        return this.jpaEventRepository.findEventsByTitle(title);
    }
}

