package com.service_booking_system.service.Exception;
public class FormatException extends RuntimeException{
    public FormatException(String fieldName){
        super("Invalid format for " + fieldName + ".");
    }
}
