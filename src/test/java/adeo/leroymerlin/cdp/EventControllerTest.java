package adeo.leroymerlin.cdp;

import adeo.leroymerlin.cdp.controller.EventController;
import adeo.leroymerlin.cdp.dto.EventDTO;
import adeo.leroymerlin.cdp.entity.Event;
import adeo.leroymerlin.cdp.exception.ResourceNotFoundException;
import adeo.leroymerlin.cdp.service.EventService;
import adeo.leroymerlin.cdp.util.TestFileLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {
    private static final String URL = "/api/events/";

    private static final String EXPECTED_RESULT_FIND_ALL_EVENTS = "controller/response/expected-result-find-all-events.json";
    private static final String EXPECTED_RESULT_FIND_EVENTS_WITH_QUERY = "controller/response/expected-result-find-events-with-query.json";
    private static final String REQUEST_BODY_UPDATE_EVENT = "controller/request/request-body-update-event.json";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private EventService eventService;

    // ================================
    // SUCCESSFUL RESPONSE TEST CASES
    // ================================

    @Test
    @DisplayName("Successful event access")
    void whenFindEventsThenSuccessfulResponse() throws Exception {

        // GIVEN REQUESTED DATA IS IN DB
        List<Event> mockedEvents = TestFileLoader.getListFromJsonFile(EXPECTED_RESULT_FIND_ALL_EVENTS, Event.class);
        when(eventService.getEvents()).thenReturn(mockedEvents);
        String expectedJson = TestFileLoader.getFileContent(EXPECTED_RESULT_FIND_ALL_EVENTS);
        assert expectedJson != null;

        // WHEN A GOOD REQUEST IS RECEIVED
        mvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .accept(MediaType.APPLICATION_JSON))
                // THEN THE RETURNED STATUS IS 200
                .andExpect(status().isOk())
                // THEN THE RETURNED BODY IS A JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // THEN THE RETURNED JSON IS AS EXPECTED
                .andExpect(content().json(expectedJson));
    }

    @Test
    @DisplayName("Successful event access with query")
    void whenFindEventsWithQueryThenSuccessfulResponse() throws Exception {
        // GIVEN REQUESTED DATA IS IN DB
        List<EventDTO> mockedEvents = TestFileLoader.getListFromJsonFile(EXPECTED_RESULT_FIND_EVENTS_WITH_QUERY, EventDTO.class);
        when(eventService.getFilteredEvents("Wa")).thenReturn(mockedEvents);
        String expectedJson = TestFileLoader.getFileContent(EXPECTED_RESULT_FIND_EVENTS_WITH_QUERY);
        assert expectedJson != null;

        // WHEN A GOOD REQUEST IS RECEIVED
        mvc.perform(MockMvcRequestBuilders
                        .get(URL + "search/Wa")
                        .accept(MediaType.APPLICATION_JSON))
                // THEN THE RETURNED STATUS IS 200
                .andExpect(status().isOk())
                // THEN THE RETURNED BODY IS A JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // THEN THE RETURNED JSON IS AS EXPECTED
                .andExpect(content().json(expectedJson));
    }

    @Test
    @DisplayName("Successful event deletion")
    void whenDeleteEventThenSuccessfulResponse() throws Exception {
        // GIVEN REQUESTED DATA IS IN DB
        long eventId = 1L;

        // WHEN A GOOD REQUEST IS RECEIVED
        mvc.perform(MockMvcRequestBuilders
                        .delete(URL + eventId)
                        .accept(MediaType.APPLICATION_JSON))
                // THEN THE RETURNED STATUS IS 200
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Successful event update")
    void whenUpdateEventThenSuccessfulResponse() throws Exception {
        // GIVEN REQUESTED DATA IS IN DB
        long eventId = 1L;
        String requestBody = TestFileLoader.getFileContent(REQUEST_BODY_UPDATE_EVENT);
        assert requestBody != null;

        // WHEN A GOOD REQUEST IS RECEIVED
        mvc.perform(MockMvcRequestBuilders
                        .put(URL + eventId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // THEN THE RETURNED STATUS IS 200
                .andExpect(status().isOk());
    }

    // ================================
    // BAD REQUEST TEST CASES
    // ================================

    @Test
    @DisplayName("[NOT_FOUND] Event not found for update")
    void whenUpdateNonExistentEventThenNotFoundResponse() throws Exception {
        // GIVEN REQUESTED DATA IS NOT IN DB
        long nonExistentEventId = 666L;
        doThrow(new ResourceNotFoundException("Event with id " + nonExistentEventId + " does not exist"))
                .when(eventService).updateEvent(eq(nonExistentEventId), any(Event.class));
        String expectedJson = TestFileLoader.getFileContent(REQUEST_BODY_UPDATE_EVENT);
        assert expectedJson != null;

        // WHEN A BAD REQUEST IS RECEIVED
        mvc.perform(MockMvcRequestBuilders
                        .put(URL + nonExistentEventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson))
                // THEN THE RETURNED STATUS IS 404
                .andExpect(status().isNotFound())
                // THEN THE RETURNED BODY IS A JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // THEN THE EXPECTED STRING BODY IS RETURNED
                .andExpect(jsonPath("$.message",
                        is("Event with id " + nonExistentEventId + " does not exist")))
                .andExpect(jsonPath("$.code",
                        is("not_found")));
    }

    @Test
    @DisplayName("[NOT_FOUND] Event not found for deletion")
    void whenDeleteNonExistentEventThenNotFoundResponse() throws Exception {
        // GIVEN REQUESTED DATA IS NOT IN DB
        long nonExistentEventId = 666L;
        doThrow(new ResourceNotFoundException("Event with id " + nonExistentEventId + " does not exist"))
                .when(eventService).delete(nonExistentEventId);

        // WHEN A BAD REQUEST IS RECEIVED
        mvc.perform(MockMvcRequestBuilders
                        .delete(URL + nonExistentEventId)
                        .contentType(MediaType.APPLICATION_JSON))
                // THEN THE RETURNED STATUS IS 404
                .andExpect(status().isNotFound())
                // THEN THE RETURNED BODY IS A JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // THEN THE EXPECTED STRING BODY IS RETURNED
                .andExpect(jsonPath("$.message",
                        is("Event with id " + nonExistentEventId + " does not exist")))
                .andExpect(jsonPath("$.code",
                        is("not_found")));
    }

    @Test
    @DisplayName("[BAD_REQUEST] Event update with invalid request body")
    void whenUpdateEventWithInvalidRequestBodyThenBadRequestResponse() throws Exception {
        // GIVEN REQUESTED DATA IS IN DB
        long eventId = 1L;
        String invalidJson = "{invalidJson}";

        // WHEN A BAD REQUEST IS RECEIVED
        mvc.perform(MockMvcRequestBuilders
                        .put(URL + eventId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                // THEN THE RETURNED STATUS IS 400
                .andExpect(status().isBadRequest())
                // THEN THE RETURNED BODY IS A JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // THEN THE EXPECTED STRING BODY IS RETURNED
                .andExpect(jsonPath("$.message",
                        containsString("JSON parse error")))
                .andExpect(jsonPath("$.code",
                        is("invalid_body_format")));
    }
}

