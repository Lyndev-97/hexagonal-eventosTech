package com.eventostec.api.controller;

import com.eventostec.api.adapters.inbound.controller.EventController;
import com.eventostec.api.adapters.inbound.mapper.EventWebMapper;
import com.eventostec.api.application.usecases.CreateEventCommand;
import com.eventostec.api.application.usecases.EventDetails;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.application.service.EventServiceImpl;
import com.eventostec.api.domain.event.EventSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@Import(EventWebMapper.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventServiceImpl eventServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test_createEventSuccess() throws Exception {
        CreateEventCommand commando = new CreateEventCommand("Evento Teste", "Descrição do evento", new Date(), "Cidade Teste", "UF", true, "https://evento.com", null);
        Event responseEvent = new Event();

        when(eventServiceImpl.createEvent(commando)).thenReturn(responseEvent);

        mockMvc.perform(multipart("/api/event")
                        .file("image", new byte[0])
                        .param("title", commando.title())
                        .param("description", commando.description())
                        .param("date", String.valueOf(commando.date().getTime()))
                        .param("city", commando.city())
                        .param("state", commando.state())
                        .param("remote", String.valueOf(commando.remote()))
                        .param("eventUrl", commando.eventUrl())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    void test_getEventDetails() throws Exception {
        UUID eventId = UUID.randomUUID();
        Event event = new Event(
                UUID.fromString("b4919825-dd11-4f6c-b77d-faffb48c1801"),
                "Teste de evento",
                "A maior conferencia do mundo",
                "",
                "https://www.teste.com",
                false,
                new Date()
        );
        Address address = new Address(UUID.randomUUID(), "Brasilia", "DF", event);
        List<Coupon> coupons = List.of(
                new Coupon(UUID.randomUUID(), "CODE123", 10, new Date(), event),
                new Coupon(UUID.randomUUID(), "CODE456", 20, new Date(), event)
        );
        EventDetails eventDetails = new EventDetails(event, Optional.of(address), coupons);

        when(eventServiceImpl.getEventDetails(eventId)).thenReturn(eventDetails);

        mockMvc.perform(get("/api/event/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    void test_getEvents() throws Exception {
        // List<EventResponseDTO> responseList = getEventResponseDTO();
        List<EventSummary> summaryList = List.of(mock(EventSummary.class));
        when(eventServiceImpl.getUpcomingEvents(0, 10)).thenReturn(summaryList);

        mockMvc.perform(get("/api/event")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void test_deleteEvent() throws Exception {
        UUID eventId = UUID.randomUUID();
        String adminKey = "testAdminKey";

        doNothing().when(eventServiceImpl).deleteEvent(eventId, adminKey);

        mockMvc.perform(delete("/api/event/{eventId}", eventId)
                        .content(adminKey)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_getFilteredEvents() throws Exception {
        List<EventSummary> summaryList = getEventSummaries();
        String startDate = "2024-10-24";
        String endDate = "2024-10-25";

        when(eventServiceImpl.getFilteredEvents(0, 10, "Brasilia", "DF",
                new SimpleDateFormat("yyyy-MM-dd").parse(startDate),
                new SimpleDateFormat("yyyy-MM-dd").parse(endDate))).thenReturn(summaryList);

        mockMvc.perform(get("/api/event/filter")
                        .param("page", "0")
                        .param("size", "10")
                        .param("city", "Brasilia")
                        .param("uf", "DF")
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void test_getSearchEvents() throws Exception {
        //List<EventResponseDTO> responseList = getEventResponseDTO();
        List<EventSummary> summaryList = List.of(mock(EventSummary.class));
        when(eventServiceImpl.searchEvents("evento")).thenReturn(summaryList);

        mockMvc.perform(get("/api/event/search")
                        .param("title", "evento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    private static List<EventSummary> getEventSummaries() {
        return List.of(new EventSummary(
                UUID.fromString("b4919825-dd11-4f6c-b77d-faffb48c1801"),
                "Teste de evento",
                "A maior conferencia do mundo",
                new Date(),
                "",
                "https://www.teste.com",
                false,
                "Brasilia",
                "DF"
        ));
    }
}
