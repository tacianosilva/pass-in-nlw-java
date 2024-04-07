package com.rocketseat.passin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.rocketseat.passin.domain.attendee.Attendee;
import com.rocketseat.passin.domain.attendee.exception.AttendeeAlreadyExistException;
import com.rocketseat.passin.domain.attendee.exception.AttendeeNotFoundException;
import com.rocketseat.passin.domain.checkin.CheckIn;
import com.rocketseat.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.rocketseat.passin.dto.attendee.AttendeeDetailDTO;
import com.rocketseat.passin.dto.attendee.AttendeesListResponseDTO;
import com.rocketseat.passin.dto.attendee.AttendeeBadgeDTO;
import com.rocketseat.passin.repository.AttendeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
        private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendees(String eventId) {
        List<Attendee> attendees = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetailDTO> attendeeDetailList = attendees.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkIn.<LocalDateTime>map(CheckIn::getCreatedAt).orElse(null);
            return new AttendeeDetailDTO(attendee.getId(), attendee.getName(), attendee.getEmail(),
                    attendee.getCreatedAt(), checkedInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailList);
    }

    public void verifyAttendeeSubscription(String email, String eventId) {
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if (isAttendeeRegistered.isPresent())
            throw new AttendeeAlreadyExistException("Attendee is already registered!");
    }

    public Attendee registerAttendee(Attendee newAttendee) {
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    public void checkInAttendee(String attendeeId) {
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }

    private Attendee getAttendee(String attendeeId) {
        Attendee attendee = this.attendeeRepository.findById(attendeeId).orElseThrow(
            () -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));

        return attendee;
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        Attendee attendee = this.getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO attendeeBadgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);
    }
}
