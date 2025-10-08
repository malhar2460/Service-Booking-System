package com.service_booking_system.service.Exception;

public class PasswordMisMatchException extends RuntimeException{
    public PasswordMisMatchException(){
        super("Password and confirm password do not match.");
    }
}
