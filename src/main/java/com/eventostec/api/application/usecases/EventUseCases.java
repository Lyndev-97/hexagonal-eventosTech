package com.eventostec.api.application.usecases;

import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventAddressProjection;

import java.util.Date;
import java.util.List;

public interface EventUseCases {

    public Event createEvent(CreateEventCommand command);
    public List<EventAddressProjection> getUpcomingEvents(int page, int size);
    public EventDetails getEventDetails(java.util.UUID eventId);
    public void deleteEvent(java.util.UUID eventId, String adminKey);
    public List<EventAddressProjection> searchEvents(String title);
    public List<EventAddressProjection> getFilteredEvents(int page, int size, String city, String uf, Date startDate, Date endDate);
}
