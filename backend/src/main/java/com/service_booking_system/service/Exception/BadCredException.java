package com.service_booking_system.service.Exception;
public class BadCredException extends RuntimeException{
    public BadCredException(){
        super("Invalid username or password.");
    }
}
