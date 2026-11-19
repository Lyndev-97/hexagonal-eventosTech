package com.eventostec.api.service;

import com.eventostec.api.adapters.inbound.dto.EventRequestDTO;
import com.eventostec.api.adapters.outbound.repositories.AddressRepositoryImpl;
import com.eventostec.api.application.service.AddressServiceImpl;
import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.event.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class AddressServiceImplTest {

    @Mock
    private AddressRepositoryImpl addressRepository;

    @InjectMocks
    private AddressServiceImpl addressServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_shouldSaveAddress() {
        Event event = new Event();
        event.setId(UUID.randomUUID());
        EventRequestDTO data = new EventRequestDTO("Teste Evento", "Descricao Evento", System.currentTimeMillis(), "Cidade Teste", "UF", true, "https://evento.com", null);
        Address address = new Address();
        address.setCity(data.city());
        address.setUf(data.state());
        address.setEvent(event);

        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address savedAddress = addressServiceImpl.createAddress(address.getCity(), address.getUf(), event);

        assertNotNull(savedAddress);
        assertEquals(data.city(), savedAddress.getCity());
        assertEquals(data.state(), savedAddress.getUf());
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void test_shouldReturnAddress() {
        UUID eventId = UUID.randomUUID();
        Address address = new Address();
        address.setCity("Cidade Teste");
        address.setUf("UF");

        when(addressRepository.findByEventId(eventId)).thenReturn(Optional.of(address));

        Optional<Address> result = addressServiceImpl.findByEventId(eventId);

        assertTrue(result.isPresent());
        assertEquals("Cidade Teste", result.get().getCity());
        assertEquals("UF", result.get().getUf());
        verify(addressRepository, times(1)).findByEventId(eventId);
    }

    @Test
    void test_shouldReturnEmptyWhenAddressNotFound() {
        UUID eventId = UUID.randomUUID();

        when(addressRepository.findByEventId(eventId)).thenReturn(Optional.empty());

        Optional<Address> result = addressServiceImpl.findByEventId(eventId);

        assertFalse(result.isPresent());
        verify(addressRepository, times(1)).findByEventId(eventId);
    }


}
