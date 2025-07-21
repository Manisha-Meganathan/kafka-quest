package com.kafkaquest.kq.api.KESAApiService.exception;

public class UsernameValidationFailed extends RuntimeException{
    public  UsernameValidationFailed(){super();}
    public UsernameValidationFailed(String message){
        super(message);
    }
}
