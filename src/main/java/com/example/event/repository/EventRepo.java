package com.example.event.repository;

import com.example.event.model.Event;
import com.example.event.model.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface EventRepo extends JpaRepository<Event, Long> {

  @Query("SELECT e FROM Event e WHERE (:sport IS NULL OR LOWER(e.sport) = LOWER(:sport)) AND (:status IS NULL OR e.status = :status)")
  List<Event> findBySportAndStatus(@Param("sport") String sport, @Param("status") EventStatus status);
}
