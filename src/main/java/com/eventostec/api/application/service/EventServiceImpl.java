package com.eventostec.api.application.service;

import com.eventostec.api.application.InvalidAdminKeyException;
import com.eventostec.api.application.usecases.*;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventAddressProjection;
import com.eventostec.api.domain.event.EventNotFoundException;
import com.eventostec.api.domain.event.EventRepositoryPort;
import com.eventostec.api.domain.image.ImageUploaderPort;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EventServiceImpl implements EventUseCases {


    private String adminKey;
    //private EventMapper mapper;
    private final EventRepositoryPort repository;
    private final ImageUploaderPort imageUploader;

    //private final AddressServiceImpl addressServiceImpl;
    private final AddressUseCases addressUseCases;
    //private final CouponServiceImpl couponServiceImpl;
    private final CouponUseCases couponUseCases;



    public EventServiceImpl(AddressUseCases addressUseCases,
                            CouponUseCases couponUseCases,
                            EventRepositoryPort repository,
                            ImageUploaderPort imageUploader,
                           // EventMapper mapper,
                            String adminKey){
        this.addressUseCases = addressUseCases;
        this.couponUseCases = couponUseCases;
        this.repository = repository;
        this.imageUploader = imageUploader;
        //this.mapper = mapper;
        this.adminKey = adminKey;
    }

    public Event createEvent(CreateEventCommand command) {
        String imgUrl = "";

        if (command.image() != null) {
            imgUrl = imageUploader.uploadImage(command.image());
        }
        Event newEvent = new Event(null, command.title(), command.description(), imgUrl,
                command.eventUrl(), command.remote(), command.date());

        Event saved = repository.save(newEvent);

        if (Boolean.FALSE.equals(command.remote())) {
            addressUseCases.createAddress(command.city(), command.state(), saved);
        }
        return saved;
    }

    public List<EventAddressProjection> getUpcomingEvents(int page, int size) {

        return repository.findUpcomingEvents(page, size);

        /*
        return this.repository.findUpcomingEvents(page, size).stream().map(
                event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getCity() != null ? event.getCity() : "",
                        event.getUf() != null ? event.getUf() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
        ).toList();
        */
    }

    public EventDetails getEventDetails(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        Optional<Address> address = addressUseCases.findByEventId(eventId);
        List<Coupon> coupons = couponUseCases.consultCoupons(eventId, new Date());

        //return mapper.domainToDetailsDto(event, address, coupons);
        return new EventDetails(event, address, coupons);
    }

    public void deleteEvent(UUID eventId, String adminKey){
        if(adminKey == null || !adminKey.equals(this.adminKey)){
            throw new InvalidAdminKeyException();
        }

        this.repository.deleteById(eventId);

    }

    public List<EventAddressProjection> searchEvents(String title){
        return repository.findEventsByTitle(title != null ? title : "");
        /*
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
        */
    }

    public List<EventAddressProjection> getFilteredEvents(int page, int size, String city, String uf, Date startDate, Date endDate){
        city = (city != null) ? city : "";
        uf = (uf != null) ? uf : "";
        startDate = (startDate != null) ? startDate : new Date(0);
        endDate = (endDate != null) ? endDate : new Date();

        return repository.findFilteredEvents(city, uf, startDate, endDate, page, size);
        /*
        return this.repository.findFilteredEvents(city, uf, startDate, endDate, page, size).stream().map(
                event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getCity() != null ? event.getCity() : "",
                        event.getUf() != null ? event.getUf() : "",
                        event.getRemote(),
                        event.getEventUrl(),
                        event.getImgUrl())
        ).toList();
        */
    }


}
