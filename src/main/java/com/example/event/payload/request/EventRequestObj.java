package com.example.event.payload.request;

import com.example.event.model.EventStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class EventRequestObj implements Serializable {

    private String id;

    @NotNull(message = "name must not be null")
    @NotEmpty
    private String name;

    @NotNull(message = "Sport type must be provided")
    @NotEmpty
    private String sport;

    //Status of the event ("ACTIVE", "INACTIVE", "FINISHED").
    @NotNull(message = "Status must be provided")
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @NotNull(message = "start time must not be null")
    private LocalDateTime start_time;

    public @NotNull(message = "name must not be null") String getName() {
        return name;
    }

    public @NotNull(message = "Status must be provided") EventStatus getStatus() {
        return status;
    }

    public @NotNull(message = "start time must not be null") LocalDateTime getStart_time() {
        return start_time;
    }

    public @NotNull(message = "Sport type must be provided") String getSport() {
        return sport;
    }

    public void setSport(@NotNull(message = "Sport type must be provided") String sport) {
        this.sport = sport;
    }

    public void setName(@NotNull(message = "name must not be null") String name) {
        this.name = name;
    }


    public void setStatus(@NotNull(message = "Status must be provided") EventStatus status) {
        this.status = status;
    }

    public void setStart_time(@NotNull(message = "start time must not be null") LocalDateTime start_time) {
        this.start_time = start_time;
    }
}
