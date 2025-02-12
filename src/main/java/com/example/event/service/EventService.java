package com.example.event.service;

import com.example.event.exception.InvalidEventStatusException;
import com.example.event.exception.ResourceNotFoundException;
import com.example.event.model.Event;
import com.example.event.model.EventStatus;
import com.example.event.payload.request.EventRequestObj;
import com.example.event.repository.EventRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepo eventRepo;

    public Event getEventById(Long id) throws ResourceNotFoundException {
            Optional<Event> event = eventRepo.findById(id);
            if(event.isEmpty()) {
                logger.error("Event not found with id: {}", id);
                throw new ResourceNotFoundException("Event not found");
            }
            logger.info("Event fetched successfully with id: {}", id);
            return event.get();
    }

    public List<Event> getEventsBySportAndStatus(String sport, EventStatus status) {
        List<Event> eventsList = eventRepo.findBySportAndStatus(sport,status);
        logger.debug("Number of patients fetched: {}", eventsList.size());
        return new ArrayList<>(eventsList);
    }

    public Event createEvent(EventRequestObj event) throws Exception {
        Event eventEntity = new Event();
        eventEntity.setName(event.getName());
        eventEntity.setSport(event.getSport());
        eventEntity.setStatus(event.getStatus());
        eventEntity.setStart_time(event.getStart_time());
        Event savedEvent = eventRepo.save(eventEntity);
        logger.debug("Saved event with id: {}",savedEvent);
        return savedEvent;
    }

    public Event updateEventById(Long id, EventStatus status) throws InvalidEventStatusException,ResourceNotFoundException {
        Optional<Event> existingEvent = eventRepo.findById(id);
        if(existingEvent.isEmpty()){
            logger.error("Failed to update event: event not found with id: {}", id);
            throw new ResourceNotFoundException("event not found with id: " + id);
        }
        EventStatus currentEventStatus = existingEvent.get().getStatus();
        LocalDateTime time = existingEvent.get().getStart_time();
        if (currentEventStatus.equals(status)) {
            return existingEvent.get();
        }
        if (currentEventStatus == EventStatus.INACTIVE && status == EventStatus.FINISHED){
            throw new InvalidEventStatusException("Invalid event status: INACTIVE event cannot change to " + status);
        }
        if (currentEventStatus == EventStatus.FINISHED) {
            throw new InvalidEventStatusException("Invalid event status: FINISHED event cannot change to " + status);
        }
        if (time.isBefore(LocalDateTime.now())) {
            throw new InvalidEventStatusException("Event start time cannot be in the past - " + time);
        }
        existingEvent.get().setStatus(status);
        Event updatedEvent = existingEvent.get();
        return eventRepo.save(updatedEvent);
    }


    public EventStatus validateStatus(String status) {
        try {
            return EventStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEventStatusException("Invalid status: " + status);
        }
    }
}
