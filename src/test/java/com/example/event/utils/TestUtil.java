package com.example.event.utils;

import com.example.event.model.Event;
import com.example.event.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public class TestUtil {

    private static Event event;

    public static List<Event> eventList() {
        return List.of(
                createEvent(1L, "Cricket Match", "Cricket", EventStatus.INACTIVE, 2),
                createEvent(5L, "Football Match", "Football", EventStatus.FINISHED, 1),
                createEvent(3L, "Hockey Match", "Hockey", EventStatus.INACTIVE, 3)
        );
    }

    public static Event createEvent(Long id, String name, String sport, EventStatus status, int hoursToAdd) {
        event = new Event();
        event.setId(id);
        event.setName(name);
        event.setSport(sport);
        event.setStatus(status);
        event.setStart_time(LocalDateTime.now().plusHours(hoursToAdd));
        return event;
    }
}
