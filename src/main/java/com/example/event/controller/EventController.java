package com.example.event.controller;

import com.example.event.exception.ResourceNotFoundException;
import com.example.event.model.Event;
import com.example.event.model.EventStatus;
import com.example.event.payload.request.EventRequestObj;
import com.example.event.payload.response.GenericResponse;
import com.example.event.service.EventService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sport-event")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.status(200).body(new GenericResponse<>("Event fetched successfully", event));
    }

    @GetMapping("/getEvents")
    public ResponseEntity<?> getEventsBySportAndStatus( @RequestParam(required = false) String sport,
         @RequestParam(required = false) String status) {
            EventStatus validEventStatus = null;
            sport = (sport != null && sport.isEmpty()) ? null : sport;
            status = (status != null && status.isEmpty()) ? null : status;
            if (status != null){
                 validEventStatus = eventService.validateStatus(status);
            }
            List<Event> event = eventService.getEventsBySportAndStatus(sport,validEventStatus);
            return ResponseEntity.status(200).body(new GenericResponse<>("Events fetched successfully", event));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventRequestObj event) throws Exception {

            Event savedEvent = eventService.createEvent(event);
            return ResponseEntity.status(201).body(new GenericResponse<>("Event created successfully", savedEvent));
    }

    @PatchMapping("/{id}/updateStatus")
    public ResponseEntity<?> updateEventStatus(@PathVariable Long id,@RequestParam(required = true) String status) {
            EventStatus validEventStatus = eventService.validateStatus(status);
            Event updatedEvent = eventService.updateEventById(id, validEventStatus);
            return ResponseEntity.status(200).body(new GenericResponse<>("Event status changed successfully", updatedEvent));
    }
}
