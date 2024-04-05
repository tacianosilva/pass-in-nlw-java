package com.rocketseat.passin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocketseat.passin.domain.event.Event;

public interface EventRepository extends JpaRepository<Event, String> {

}
