package com.eventostec.api.application.usercases;

import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventDetailsDTO;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;

import java.util.Date;
import java.util.List;

public interface EventUseCases {

    public Event createEvent(EventRequestDTO eventRequestDTO);
    public List<EventResponseDTO> getUpcomingEvents(int page, int size);
    public EventDetailsDTO getEventDetails(java.util.UUID eventId);
    public void deleteEvent(java.util.UUID eventId, String adminKey);
    public List<EventResponseDTO> searchEvents(String title);
    public List<EventResponseDTO> getFilteredEvents(int page, int size, String city, String uf, Date startDate, Date endDate);
}
