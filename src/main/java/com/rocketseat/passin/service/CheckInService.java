package com.rocketseat.passin.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rocketseat.passin.domain.attendee.Attendee;
import com.rocketseat.passin.domain.checkin.CheckIn;
import com.rocketseat.passin.domain.checkin.exception.CheckInAlreadyExistsException;
import com.rocketseat.passin.repository.CheckinRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckinRepository checkinRepository;

    public void registerCheckIn(Attendee attendee) {
        this.verifyCheckInExists(attendee.getId());

        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());

        this.checkinRepository.save(newCheckIn);
    }

    private void verifyCheckInExists(String attendeeId) {
        Optional<CheckIn> isCheckedIn = this.getCheckIn(attendeeId);
        if (isCheckedIn.isPresent()) throw new CheckInAlreadyExistsException("Attendee already checked in!");

    }

    public Optional<CheckIn> getCheckIn(String attendeeId) {
        return this.checkinRepository.findByAttendeeId(attendeeId);
    }
}
