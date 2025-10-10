package com.service_booking_system.service.Exception;

public class FieldBlankException extends RuntimeException{
    public FieldBlankException(String fieldName){
        super(fieldName + " is required.");
    }
}
