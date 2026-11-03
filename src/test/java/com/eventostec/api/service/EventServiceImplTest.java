package com.eventostec.api.service;

class EventServiceImplTest {
/*
    @Mock
    private S3Client s3Client;

    @Mock
    private S3Utilities s3Utilities;

    @Mock
    private AddressService addressService;

    @Mock
    private CouponService couponService;

    @Mock
    private JpaEventRepository repository;

    @InjectMocks
    private EventServiceImpl eventServiceImpl;

    private final String adminKey = "test-admin-key";
    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventServiceImpl = new EventServiceImpl(s3Client, addressService, couponService, repository);
        // Configurando valores diretamente nos atributos usando ReflectionTestUtils
        ReflectionTestUtils.setField(eventServiceImpl, "adminKey", adminKey);
        ReflectionTestUtils.setField(eventServiceImpl, "bucketName", bucketName);
        // Mockando S3Utilities
        when(s3Client.utilities()).thenReturn(s3Utilities);
    }

    @Test
    void test_shouldSaveEvent() {
        EventRequestDTO requestDTO = new EventRequestDTO("Evento Teste", "Descrição do evento", new Date().getTime(), "Cidade Teste", "UF", true, "https://evento.com", null);
        Event event = new Event();

        when(repository.save(any(Event.class))).thenReturn(event);

        Event savedEvent = eventServiceImpl.createEvent(requestDTO);

        assertNotNull(savedEvent);
        verify(repository, times(1)).save(any(Event.class));
    }

    @Test
    void test_shouldReturnListOfEvents() {
        Pageable pageable = PageRequest.of(0, 10);
        List<EventAddressProjection> events = List.of(mock(EventAddressProjection.class));
        Page<EventAddressProjection> eventsPage = new PageImpl<>(events);

        when(repository.findUpcomingEvents(any(Date.class), eq(pageable))).thenReturn(eventsPage);

        List<EventResponseDTO> result = eventServiceImpl.getUpcomingEvents(0, 10);

        assertFalse(result.isEmpty());
        verify(repository, times(1)).findUpcomingEvents(any(Date.class), eq(pageable));
    }

    @Test
    void test_shouldReturnEventDetails() {
        UUID eventId = UUID.randomUUID();
        Event event = new Event();
        event.setId(eventId);
        event.setTitle("Teste de evento");
        event.setDescription("Descrição do evento");
        event.setDate(new Date());
        event.setEventUrl("https://evento.com");

        when(repository.findById(eventId)).thenReturn(Optional.of(event));
        when(addressService.findByEventId(eventId)).thenReturn(Optional.empty());
        when(couponService.consultCoupons(eventId, new Date())).thenReturn(Collections.emptyList());

        EventDetailsDTO result = eventServiceImpl.getEventDetails(eventId);

        assertNotNull(result);
        assertEquals(eventId, result.id());
        verify(repository, times(1)).findById(eventId);
    }

    @Test
    void test_shouldDeleteEvent() {
        UUID eventId = UUID.randomUUID();
        Event event = new Event();
        event.setId(eventId);

        when(repository.findById(eventId)).thenReturn(Optional.of(event));

        // Use a mesma chave de administrador configurada
        eventServiceImpl.deleteEvent(eventId, adminKey);

        verify(repository, times(1)).delete(event);
    }

    @Test
    void test_shouldReturnMatchingEvents() {
        String title = "Evento Teste";
        List<EventAddressProjection> events = List.of(mock(EventAddressProjection.class));

        when(repository.findEventsByTitle(title)).thenReturn(events);

        List<EventResponseDTO> result = eventServiceImpl.searchEvents(title);

        assertFalse(result.isEmpty());
        verify(repository, times(1)).findEventsByTitle(title);
    }

    @Test
    void test_shouldReturnFilteredEvents() {
        Pageable pageable = PageRequest.of(0, 10);
        List<EventAddressProjection> events = List.of(mock(EventAddressProjection.class));
        Page<EventAddressProjection> eventsPage = new PageImpl<>(events);

        when(repository.findFilteredEvents(anyString(), anyString(), any(Date.class), any(Date.class), eq(pageable))).thenReturn(eventsPage);

        List<EventResponseDTO> result = eventServiceImpl.getFilteredEvents(0, 10, "Cidade Teste", "UF", new Date(), new Date());

        assertFalse(result.isEmpty());
        verify(repository, times(1)).findFilteredEvents(anyString(), anyString(), any(Date.class), any(Date.class), eq(pageable));
    }

    @Test
    void test_shouldReturnUrlOnUploadImage() throws Exception {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenReturn(new byte[0]);
        when(multipartFile.getOriginalFilename()).thenReturn("imagem.jpg");
        when(s3Utilities.getUrl(any(GetUrlRequest.class))).thenReturn(URI.create("https://s3.amazonaws.com/teste/imagem.jpg").toURL());

        ReflectionTestUtils.setField(eventServiceImpl, "bucketName", bucketName);
        String result = ReflectionTestUtils.invokeMethod(eventServiceImpl, "uploadImg", multipartFile);

        assertNotNull(result);
        assertTrue(result.contains("https://s3.amazonaws.com"));
    }
    */
}
