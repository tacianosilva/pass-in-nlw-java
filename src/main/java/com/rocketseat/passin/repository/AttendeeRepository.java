package com.rocketseat.passin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocketseat.passin.domain.attendee.Attendee;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {

    public List<Attendee> findByEventId(String eventId);

}
