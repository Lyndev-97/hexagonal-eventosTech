package com.eventostec.api.adapters.inbound.mapper;

import com.eventostec.api.adapters.inbound.dto.EventDetailsDTO;
import com.eventostec.api.adapters.inbound.dto.EventRequestDTO;
import com.eventostec.api.adapters.inbound.dto.EventResponseDTO;
import com.eventostec.api.application.usecases.CreateEventCommand;
import com.eventostec.api.application.usecases.EventDetails;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventAddressProjection;
import com.eventostec.api.domain.image.ImageFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Date;
import java.util.List;

@Component
public class EventWebMapper {

    public CreateEventCommand toCommand(EventRequestDTO dto) {
        return new CreateEventCommand(
                dto.title(),
                dto.description(),
                dto.date() != null ? new Date(dto.date()) : null,
                dto.city(),
                dto.state(),
                dto.remote(),
                dto.eventUrl(),
                toImageFile(dto.image()));
    }

    private ImageFile toImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            return new ImageFile(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public EventResponseDTO toResponse(EventAddressProjection e) {
        return new EventResponseDTO(e.getId(),
                                    e.getTitle(),
                                    e.getDescription(),
                                    e.getDate(),
                                    e.getCity() != null ? e.getCity() : "",
                                    e.getUf() != null ? e.getUf() : "",
                                    e.getRemote(),
                                    e.getEventUrl(),
                                    e.getImgUrl());
    }

    public EventResponseDTO toCreatedResponse(Event event, String city, String uf) {
        return new EventResponseDTO(event.getId(),
                                    event.getTitle(),
                                    event.getDescription(),
                                    event.getDate(),
                                    city != null ? city : "",
                                    uf != null ? uf : "",
                                    event.getRemote(),
                                    event.getEventUrl(),
                                    event.getImgUrl());
    }

    public EventDetailsDTO toDetailsDto(EventDetails details) {
        Event event = details.event();
        String city = details.address().map(Address::getCity).orElse("");
        String uf = details.address().map(Address::getUf).orElse("");
        List<EventDetailsDTO.CouponDTO> coupons = details.coupons().stream()
                .map(c -> new EventDetailsDTO.CouponDTO(c.getCode(), c.getDiscount(), c.getValid()))
                .toList();
        return new EventDetailsDTO(event.getId(), event.getTitle(), event.getDescription(), event.getDate(),
                city, uf, event.getImgUrl(), event.getEventUrl(), coupons);
    }
}
