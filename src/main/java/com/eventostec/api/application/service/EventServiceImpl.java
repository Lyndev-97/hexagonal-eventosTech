package com.eventostec.api.application.service;

import com.eventostec.api.adapters.outbound.storage.ImageUploaderPort;
import com.eventostec.api.application.usercases.AddressUseCases;
import com.eventostec.api.application.usercases.CouponUseCases;
import com.eventostec.api.application.usercases.EventUseCases;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.event.*;
import com.eventostec.api.utils.mappers.EventMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EventServiceImpl implements EventUseCases {


    private String adminKey;
    private EventMapper mapper;
    private final EventRepository repository;
    private final ImageUploaderPort imageUploader;

    //private final AddressServiceImpl addressServiceImpl;
    private final AddressUseCases addressUseCases;
    //private final CouponServiceImpl couponServiceImpl;
    private final CouponUseCases couponUseCases;



    public EventServiceImpl(AddressUseCases addressUseCases,
                            CouponUseCases couponUseCases,
                            EventRepository repository,
                            ImageUploaderPort imageUploader,
                            EventMapper mapper,
                            String adminKey){
        this.addressUseCases = addressUseCases;
        this.couponUseCases = couponUseCases;
        this.repository = repository;
        this.imageUploader = imageUploader;
        this.mapper = mapper;
        this.adminKey = adminKey;
    }

    public Event createEvent(EventRequestDTO data) {
        String imgUrl = "";


        if (data.image() != null) {
            imgUrl = imageUploader.uploadImage(data.image());
        }
        Event newEvent = mapper.dtoToEntity(data, imgUrl);
        repository.save(newEvent);

        if (Boolean.FALSE.equals(data.remote())) {
            this.addressUseCases.createAddress(data, newEvent);
        }

        return newEvent;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventAddressProjection> eventsPage = this.repository.findUpcomingEvents(page, size);
        return eventsPage.map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getCity() != null ? event.getCity() : "",
                        event.getUf() != null ? event.getUf() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
                )
                .stream().toList();
    }

    public EventDetailsDTO getEventDetails(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Optional<Address> address = addressUseCases.findByEventId(eventId);
        List<Coupon> coupons = couponUseCases.consultCoupons(eventId, new Date());

        return mapper.domainToDetailsDto(event, address, coupons);
    }

    public void deleteEvent(UUID eventId, String adminKey){
        if(adminKey == null || !adminKey.equals(this.adminKey)){
            throw new IllegalArgumentException("Invalid admin key");
        }

        this.repository.deleteById(eventId);

    }

    public List<EventResponseDTO> searchEvents(String title){
        title = (title != null) ? title : "";

        List<EventAddressProjection> eventsList = this.repository.findEventsByTitle(title);
        return eventsList.stream().map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getCity() != null ? event.getCity() : "",
                        event.getUf() != null ? event.getUf() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
                )
                .toList();
    }

    public List<EventResponseDTO> getFilteredEvents(int page, int size, String city, String uf, Date startDate, Date endDate){
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : new Date(0);
        endDate = (endDate != null) ? endDate : new Date();

        Page<EventAddressProjection> eventsPage = this.repository.findFilteredEvents(city, uf, startDate, endDate, page, size);
        return eventsPage.map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getCity() != null ? event.getCity() : "",
                        event.getUf() != null ? event.getUf() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
                )
                .stream().toList();
    }


}
