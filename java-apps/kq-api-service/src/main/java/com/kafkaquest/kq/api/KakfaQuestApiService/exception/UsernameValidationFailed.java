package com.kafkaquest.kq.api.KafkaQuestApiService.exception;

public class UsernameValidationFailed extends RuntimeException{
    public  UsernameValidationFailed(){super();}
    public UsernameValidationFailed(String message){
        super(message);
    }
}
