package com.eventostec.api.service;

import com.eventostec.api.application.InvalidAdminKeyException;
import com.eventostec.api.application.service.EventServiceImpl;
import com.eventostec.api.application.usecases.AddressUseCases;
import com.eventostec.api.application.usecases.CouponUseCases;
import com.eventostec.api.application.usecases.CreateEventCommand;
import com.eventostec.api.application.usecases.EventDetails;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventNotFoundException;
import com.eventostec.api.domain.event.EventRepositoryPort;
import com.eventostec.api.domain.event.EventSummary;
import com.eventostec.api.domain.image.ImageFile;
import com.eventostec.api.domain.image.ImageUploaderPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class EventServiceImplTest {

    @Mock
    private AddressUseCases addressUseCases;

    @Mock
    private CouponUseCases couponUseCases;

    @Mock
    private EventRepositoryPort repository;

    @Mock
    private ImageUploaderPort imageUploaderPort;

    private EventServiceImpl eventServiceImpl;

    private final String adminKey = "test-admin-key";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventServiceImpl = new EventServiceImpl(addressUseCases, couponUseCases, repository, imageUploaderPort, adminKey);
    }

    @Test
    void test_shouldSaveEventAndCreateAddressWhenNotRemote() {
        CreateEventCommand command = new CreateEventCommand("Evento Teste", "Descrição do evento", new Date(),
                "Cidade Teste", "UF", false, "https://evento.com", null);
        Event savedEvent = new Event(UUID.randomUUID(), command.title(), command.description(), "",
                command.eventUrl(), command.remote(), command.date());

        when(repository.save(any(Event.class))).thenReturn(savedEvent);

        Event result = eventServiceImpl.createEvent(command);

        assertNotNull(result);
        assertEquals(savedEvent.getId(), result.getId());
        verify(repository, times(1)).save(any(Event.class));
        verify(addressUseCases, times(1)).createAddress(command.city(), command.state(), savedEvent);
    }

    @Test
    void test_shouldNotCreateAddressWhenEventIsRemote() {
        CreateEventCommand command = new CreateEventCommand("Evento Remoto", "Descrição", new Date(),
                "Cidade Teste", "UF", true, "https://evento.com", null);
        Event savedEvent = new Event(UUID.randomUUID(), command.title(), command.description(), "",
                command.eventUrl(), command.remote(), command.date());

        when(repository.save(any(Event.class))).thenReturn(savedEvent);

        eventServiceImpl.createEvent(command);

        verify(addressUseCases, never()).createAddress(any(), any(), any());
    }

    @Test
    void test_shouldUploadImageWhenProvided() {
        ImageFile imageFile = new ImageFile("imagem.jpg", "image/jpeg", new byte[0]);
        CreateEventCommand command = new CreateEventCommand("Evento Teste", "Descrição", new Date(),
                "Cidade Teste", "UF", true, "https://evento.com", imageFile);
        String expectedUrl = "https://s3.amazonaws.com/teste/imagem.jpg";

        when(imageUploaderPort.uploadImage(imageFile)).thenReturn(expectedUrl);
        when(repository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event result = eventServiceImpl.createEvent(command);

        assertEquals(expectedUrl, result.getImgUrl());
        verify(imageUploaderPort, times(1)).uploadImage(imageFile);
    }

    @Test
    void test_shouldReturnListOfEvents() {
        List<EventSummary> events = List.of(new EventSummary(UUID.randomUUID(), "Evento Teste", "Descrição",
                new Date(), "", "https://evento.com", false, "Cidade Teste", "UF"));

        when(repository.findUpcomingEvents(0, 10)).thenReturn(events);

        List<EventSummary> result = eventServiceImpl.getUpcomingEvents(0, 10);

        assertFalse(result.isEmpty());
        verify(repository, times(1)).findUpcomingEvents(0, 10);
    }

    @Test
    void test_shouldReturnEventDetails() {
        UUID eventId = UUID.randomUUID();
        Event event = new Event(eventId, "Teste de evento", "Descrição do evento", "", "https://evento.com", false, new Date());

        when(repository.findById(eventId)).thenReturn(Optional.of(event));
        when(addressUseCases.findByEventId(eventId)).thenReturn(Optional.empty());
        when(couponUseCases.consultCoupons(eq(eventId), any(Date.class))).thenReturn(Collections.emptyList());

        EventDetails result = eventServiceImpl.getEventDetails(eventId);

        assertNotNull(result);
        assertEquals(eventId, result.event().getId());
        verify(repository, times(1)).findById(eventId);
    }

    @Test
    void test_shouldThrowExceptionWhenEventNotFoundForDetails() {
        UUID eventId = UUID.randomUUID();
        when(repository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventServiceImpl.getEventDetails(eventId));
    }

    @Test
    void test_shouldDeleteEvent() {
        UUID eventId = UUID.randomUUID();
        Event event = new Event(eventId, "Evento Teste", "Descrição", "", "https://evento.com", true, new Date());
        when(repository.findById(eventId)).thenReturn(Optional.of(event));

        eventServiceImpl.deleteEvent(eventId, adminKey);

        verify(repository, times(1)).deleteById(eventId);
    }

    @Test
    void test_shouldThrowExceptionWhenDeletingNonExistentEvent() {
        UUID eventId = UUID.randomUUID();
        when(repository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventServiceImpl.deleteEvent(eventId, adminKey));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void test_shouldThrowExceptionWhenAdminKeyIsInvalid() {
        UUID eventId = UUID.randomUUID();

        assertThrows(InvalidAdminKeyException.class, () -> eventServiceImpl.deleteEvent(eventId, "wrong-key"));
        verify(repository, never()).deleteById(any());
        verify(repository, never()).findById(any());
    }

    @Test
    void test_shouldReturnMatchingEvents() {
        String title = "Evento Teste";
        List<EventSummary> events = List.of(new EventSummary(UUID.randomUUID(), title, "Descrição",
                new Date(), "", "https://evento.com", false, "Cidade Teste", "UF"));

        when(repository.findEventsByTitle(title)).thenReturn(events);

        List<EventSummary> result = eventServiceImpl.searchEvents(title);

        assertFalse(result.isEmpty());
        verify(repository, times(1)).findEventsByTitle(title);
    }

    @Test
    void test_shouldReturnFilteredEvents() {
        List<EventSummary> events = List.of(new EventSummary(UUID.randomUUID(), "Evento Teste", "Descrição",
                new Date(), "", "https://evento.com", false, "Cidade Teste", "UF"));
        Date startDate = new Date(0);
        Date endDate = new Date();

        when(repository.findFilteredEvents("Cidade Teste", "UF", startDate, endDate, 0, 10)).thenReturn(events);

        List<EventSummary> result = eventServiceImpl.getFilteredEvents(0, 10, "Cidade Teste", "UF", startDate, endDate);

        assertFalse(result.isEmpty());
        verify(repository, times(1)).findFilteredEvents("Cidade Teste", "UF", startDate, endDate, 0, 10);
    }
}
