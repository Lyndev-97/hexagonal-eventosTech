package com.eventostec.api.adapters.inbound.controller;

import com.eventostec.api.adapters.inbound.mapper.EventWebMapper;
import com.eventostec.api.application.usecases.EventUseCases;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.adapters.inbound.dto.EventDetailsDTO;
import com.eventostec.api.adapters.inbound.dto.EventRequestDTO;
import com.eventostec.api.adapters.inbound.dto.EventResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class EventController {

    private final EventUseCases eventUseCases;
    private final EventWebMapper webMapper;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<EventResponseDTO> create(@Valid @ModelAttribute EventRequestDTO dto) {
        Event newEvent = this.eventUseCases.createEvent(webMapper.toCommand(dto));
        return ResponseEntity.ok(webMapper.toCreatedResponse(newEvent, dto.city(), dto.state()));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsDTO> getEventDetails(@PathVariable UUID eventId) {
        return ResponseEntity.ok(webMapper.toDetailsDto(eventUseCases.getEventDetails(eventId)));
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(eventUseCases.getUpcomingEvents(page, size).stream().map(webMapper::toResponse).toList());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> getFilteredEvents(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam String city,
                                                                    @RequestParam String uf,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return ResponseEntity.ok(eventUseCases.getFilteredEvents(page, size, city, uf, startDate, endDate).stream().map(webMapper::toResponse).toList());

    }

    @GetMapping("/search")
    public ResponseEntity<List<EventResponseDTO>> getSearchEvents(@RequestParam String title) {
        return ResponseEntity.ok(eventUseCases.searchEvents(title).stream().map(webMapper::toResponse).toList());
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId, @RequestBody String adminKey) {
        eventUseCases.deleteEvent(eventId, adminKey);
        return ResponseEntity.noContent().build();
    }
}
