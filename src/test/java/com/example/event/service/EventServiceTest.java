package com.example.event.service;

import com.example.event.exception.InvalidEventStatusException;
import com.example.event.exception.ResourceNotFoundException;
import com.example.event.model.Event;
import com.example.event.model.EventStatus;
import com.example.event.payload.request.EventRequestObj;
import com.example.event.repository.EventRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.event.utils.TestUtil.createEvent;
import static com.example.event.utils.TestUtil.eventList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepo eventRepo;

    @InjectMocks
    private EventService eventService;

    private Event event;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        event = createEvent(5L, "Football Match", "Football", EventStatus.ACTIVE, 1);
    }

    @Test
    public void testGetEventById_Success() {

        Mockito.when(eventRepo.findById(5L)).thenReturn(Optional.of(event));

        Event event = eventService.getEventById(5L);
        assertNotNull(event);
        assertEquals("Football", event.getSport());
    }

    @Test
    public void testGetEventById_NotFound() {

        Mockito.when(eventRepo.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            eventService.getEventById(1L);
        });

        assertEquals("Event not found", exception.getMessage());
    }

    @Test
    public void testGetEventsBySportAndStatus_Success() {
        Mockito.when(eventRepo.findBySportAndStatus("Football", EventStatus.INACTIVE))
                .thenReturn(List.of(event));

        List<Event> events = eventService.getEventsBySportAndStatus("Football", EventStatus.INACTIVE);
        assertFalse(events.isEmpty());
        assertEquals("Football Match",events.get(0).getName());

    }

    @Test
    public void testGetEventsBySportAndStatus_GetAllEvents() {
        Mockito.when(eventRepo.findBySportAndStatus(any(),any()))
                .thenReturn(eventList());

        List<Event> events = eventService.getEventsBySportAndStatus(null, null);
        assertFalse(events.isEmpty());
        assertEquals(3,events.size());

    }

    @Test
    public void testGetEventsBySportAndStatus_NotFound() {
        Mockito.when(eventRepo.findBySportAndStatus(any(),any()))
                .thenReturn(new ArrayList<>());

        List<Event> events = eventService.getEventsBySportAndStatus("Football", EventStatus.ACTIVE);
        assertTrue(events.isEmpty());
    }

    @Test
    public void testCreateEvent_Success() throws Exception {

        EventRequestObj request = new EventRequestObj();
        request.setName("Football Match");
        request.setSport("Football");
        request.setStatus(EventStatus.ACTIVE);
        request.setStart_time(LocalDateTime.now().plusHours(1));

        when(eventRepo.save(any(Event.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(request);
        assertNotNull(createdEvent);
        assertEquals(5L,createdEvent.getId());
        assertEquals("Football", createdEvent.getSport());
    }

    @Test
    public void testUpdateEventById_Success() {
        when(eventRepo.findById(5L)).thenReturn(Optional.of(event));
        when(eventRepo.save(any(Event.class))).thenReturn(event);

        Event updatedEvent = eventService.updateEventById(5L, EventStatus.FINISHED);
        assertNotNull(updatedEvent);
        assertEquals(EventStatus.FINISHED, updatedEvent.getStatus());
    }

    @Test
    public void testUpdateEventById_EventNotFound() {
        when(eventRepo.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            eventService.updateEventById(1L, EventStatus.FINISHED);
        });

        assertEquals("event not found with id: 1", exception.getMessage());
    }

    @Test
    public void testUpdateEventById_InvalidStatusChange() {
        event.setStatus(EventStatus.FINISHED);
        when(eventRepo.findById(5L)).thenReturn(Optional.of(event));

        InvalidEventStatusException exception = assertThrows(InvalidEventStatusException.class, () -> {
            eventService.updateEventById(5L, EventStatus.ACTIVE);
        });

        assertEquals("Invalid event status: FINISHED event cannot change to ACTIVE", exception.getMessage());
    }

    @Test
    public void testUpdateEventById_InvalidStatusChangeForPastDate() {
        event.setStatus(EventStatus.INACTIVE);
        event.setStart_time(LocalDateTime.now().minusHours(4));

        when(eventRepo.findById(5L)).thenReturn(Optional.of(event));

        assertThrows(InvalidEventStatusException.class, () -> {
            eventService.updateEventById(5L, EventStatus.ACTIVE);
        });
    }

}
