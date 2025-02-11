package com.example.event.model;

import com.example.event.exception.InvalidEventStatusException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum EventStatus {
    ACTIVE,
    INACTIVE,
    FINISHED;

    @JsonCreator
    public static EventStatus fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidEventStatusException("Status field is required");
        }
        for (EventStatus status : EventStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new InvalidEventStatusException("Invalid Event status value: " + value);
    }
}
