package com.rocketseat.passin.domain.checkin.exception;

public class CheckInAlreadyExistsException extends RuntimeException {

    public CheckInAlreadyExistsException(String message) {
        super(message);
    }
}
