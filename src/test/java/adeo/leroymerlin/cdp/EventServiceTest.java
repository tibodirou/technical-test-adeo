package adeo.leroymerlin.cdp;

import adeo.leroymerlin.cdp.dto.EventDTO;
import adeo.leroymerlin.cdp.entity.Event;
import adeo.leroymerlin.cdp.exception.ResourceNotFoundException;
import adeo.leroymerlin.cdp.repository.EventRepository;
import adeo.leroymerlin.cdp.service.EventService;
import adeo.leroymerlin.cdp.util.TestFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EventServiceTest {

    private static final String ALL_EVENTS_DB_RESULT = "service/all-events-db-result.json";
    private static final String FILTERED_EVENTS_EXAMPLE = "service/filtered-events-example.json";

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ================================
    // SUCCESSFUL TEST CASES
    // ================================

    @Test
    @DisplayName("Successful access to all events")
    void whenGetEventsThenReturnEvents() {
        // GIVEN
        List<Event> events = TestFileLoader.getListFromJsonFile(ALL_EVENTS_DB_RESULT, Event.class);

        // WHEN
        when(eventRepository.findAll()).thenReturn(events);

        // THEN
        assertEquals(5, eventService.getEvents().size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Successful deletion of an event")
    void whenDeleteEventThenDeleteEvent() {
        // GIVEN
        long eventId = 1L;

        // WHEN
        when(eventRepository.existsById(eventId)).thenReturn(true);
        eventService.delete(eventId);

        // THEN
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    @DisplayName("Successful update of an event")
    void whenUpdateEventThenUpdateEvent() {
        // GIVEN
        long eventId = 1L;
        Event updatedEvent = new Event();
        updatedEvent.setComment("Updated Comment");
        updatedEvent.setNbStars(5);
        Event existingEvent = new Event();

        // WHEN
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        eventService.updateEvent(eventId, updatedEvent);

        // THEN
        assertEquals("Updated Comment", existingEvent.getComment());
        assertEquals(5, existingEvent.getNbStars());
        verify(eventRepository, times(1)).save(existingEvent);
    }

    @Test
    @DisplayName("Successful filtering of events")
    void whenGetFilteredEventsThenReturnFilteredEvents() {
        // GIVEN
        String query = "Wa";
        List<Event> events = TestFileLoader.getListFromJsonFile(ALL_EVENTS_DB_RESULT, Event.class);
        List<EventDTO> expectedFilteredEvents = TestFileLoader.getListFromJsonFile(FILTERED_EVENTS_EXAMPLE, EventDTO.class);
        assert expectedFilteredEvents != null;

        // WHEN
        when(eventRepository.findAll()).thenReturn(events);

        // THEN
        List<EventDTO> filteredEvents = eventService.getFilteredEvents(query);
        assertEquals(1, filteredEvents.size());
        assertEquals(expectedFilteredEvents.getFirst(), filteredEvents.getFirst());
        verify(eventRepository, times(1)).findAll();
    }

    // ================================
    // FAILED TEST CASES
    // ================================

    @Test
    @DisplayName("Failed update of an event")
    void whenUpdateEventNotFoundThenThrowException() {
        // GIVEN
        long eventId = 1L;
        Event event = new Event();

        // WHEN
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> eventService.updateEvent(eventId, event));
        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    @DisplayName("Failed deletion of an event")
    void whenDeleteEventNotFoundThenThrowException() {
        // GIVEN
        long eventId = 1L;

        // WHEN
        when(eventRepository.existsById(eventId)).thenReturn(false);

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> eventService.delete(eventId));
        verify(eventRepository, times(1)).existsById(eventId);
    }
}
