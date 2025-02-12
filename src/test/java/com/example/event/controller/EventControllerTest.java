package com.example.event.controller;

import com.example.event.exception.ResourceNotFoundException;
import com.example.event.model.Event;
import com.example.event.model.EventStatus;
import com.example.event.payload.request.EventRequestObj;
import com.example.event.payload.response.GenericResponse;
import com.example.event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.example.event.utils.TestUtil.eventList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {
    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setName("Hockey Match");
        event.setSport("Hockey");
        event.setStatus(EventStatus.ACTIVE);
        event.setStart_time(LocalDateTime.now().plusHours(1));
    }

    @Test
    void testGetEventById_Success() {
        when(eventService.getEventById(1L)).thenReturn(event);

        ResponseEntity<?> response = eventController.getEventById(1L);
        GenericResponse<?> responseBody = (GenericResponse<?>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("Event fetched successfully", responseBody.getMessage());
        assertEquals(event, responseBody.getData());
    }

    @Test
    void testGetEventById_NotFound() {
        when(eventService.getEventById(1L)).thenThrow(new ResourceNotFoundException("Event not found"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            eventController.getEventById(1L);
        });

        assertEquals("Event not found", exception.getMessage());
    }

    @Test
    void testGetEventsBySportAndStatus_Success() {
        List<Event> events = eventList();

        when(eventService.getEventsBySportAndStatus("Football", EventStatus.ACTIVE)).thenReturn(events);
        when(eventService.validateStatus("ACTIVE")).thenReturn(EventStatus.ACTIVE);

        ResponseEntity<?> response = eventController.getEventsBySportAndStatus("Football", "ACTIVE");
        GenericResponse<?> responseBody = (GenericResponse<?>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("Events fetched successfully", responseBody.getMessage());
        assertEquals(events, responseBody.getData());
    }

    @Test
    void testCreateEvent_Success() throws Exception {
        EventRequestObj request = new EventRequestObj();
        request.setName("Football Match");
        request.setSport("Football");
        request.setStatus(EventStatus.ACTIVE);
        request.setStart_time(LocalDateTime.now().plusHours(1));
        when(eventService.createEvent(any(EventRequestObj.class))).thenReturn(event);

        ResponseEntity<?> response = eventController.createEvent(request);
        GenericResponse<?> responseBody = (GenericResponse<?>) response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("Event created successfully", responseBody.getMessage());
        assertEquals(event, responseBody.getData());
    }

    @Test
    void testUpdateEventStatus_Success() {
        when(eventService.validateStatus("FINISHED")).thenReturn(EventStatus.FINISHED);
        when(eventService.updateEventById(eq(1L), eq(EventStatus.FINISHED))).thenReturn(event);

        ResponseEntity<?> response = eventController.updateEventStatus(1L, "FINISHED");
        GenericResponse<?> responseBody = (GenericResponse<?>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseBody);
        assertEquals("Event status changed successfully", responseBody.getMessage());
        assertEquals(event, responseBody.getData());
    }
}
