package com.service_booking_system.service.Exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException() {
        super("User already exists.");
    }
}
