package com.rocketseat.passin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rocketseat.passin.domain.attendee.Attendee;
import com.rocketseat.passin.domain.checkin.CheckIn;
import com.rocketseat.passin.dto.attendee.AttendeeDetailDTO;
import com.rocketseat.passin.dto.attendee.AttendeesListResponseDTO;
import com.rocketseat.passin.repository.AttendeeRepository;
import com.rocketseat.passin.repository.CheckinRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckinRepository checkinRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendees(String eventId) {
        List<Attendee> attendees = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetailDTO> attendeeDetailList = attendees.stream().map(attendee -> {
                Optional<CheckIn> checkIn = this.checkinRepository.findByAttendeeId(attendee.getId());
                LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
                return new AttendeeDetailDTO(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkedInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailList);
    }
}
