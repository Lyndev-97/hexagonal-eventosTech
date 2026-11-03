package com.eventostec.api.adapters.outbound.repositories;

import com.eventostec.api.adapters.outbound.entities.EventEntity;
import com.eventostec.api.adapters.outbound.repositories.jpa.JpaEventRepository;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventAddressProjection;
import com.eventostec.api.domain.event.EventRepository;
import com.eventostec.api.utils.mappers.EventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class EventRepositoryImpl implements EventRepository {

    private final JpaEventRepository jpaEventRepository;

    @Autowired
    private EventMapper mapper;

    public EventRepositoryImpl(JpaEventRepository jpaEventRepository) {
        this.jpaEventRepository = jpaEventRepository;
    }

    @Override
    public Event save(Event event) {
        EventEntity entity = new EventEntity(event);
        this.jpaEventRepository.save(entity);
        return new Event(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getImgUrl(),
                entity.getEventUrl(),
                entity.getRemote(),
                entity.getDate());
    }

    @Override
    public Optional<Event> findById(UUID id) {
        Optional<EventEntity> eventEntity = this.jpaEventRepository.findById(id);
        return eventEntity.map(mapper::jpaToDomain);
    }

    @Override
    public List<Event> findAll() {
       return this.jpaEventRepository.findAll().stream().map(entity -> new Event(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getImgUrl(),
                entity.getEventUrl(),
                entity.getRemote(),
                entity.getDate())).collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        this.jpaEventRepository.deleteById(id);
    }

    @Override
    public Page<EventAddressProjection> findUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.jpaEventRepository.findUpcomingEvents(new Date(), pageable);
    }

    @Override
    public Page<EventAddressProjection> findFilteredEvents(String city, String uf, Date startDate, Date endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.jpaEventRepository.findFilteredEvents(city, uf, startDate, endDate, pageable);
    }

    @Override
    public List<EventAddressProjection> findEventsByTitle(String title) {
        return this.jpaEventRepository.findEventsByTitle(title);
    }
}

