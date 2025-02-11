package com.example.event.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    private String sport;

    private LocalDateTime start_time;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public String getSport() {
        return sport;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }
}
