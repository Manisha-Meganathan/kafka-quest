package com.kafkaquest.kq.api.KESAApiService.controller;

import com.kafkaquest.kq.api.KESAApiService.exception.BadCredentialsException;
import com.kafkaquest.kq.api.KESAApiService.exception.EmailDeliveryException;
import com.kafkaquest.kq.api.KESAApiService.exception.UsernameExistsException;
import com.kafkaquest.kq.api.KESAApiService.exception.UsernameValidationFailed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@RestControllerAdvice
public class ApiExceptionHandler implements ProblemHandling {

    @ExceptionHandler
    public ResponseEntity<Problem> handleUserNameExistsException(UsernameExistsException ex, NativeWebRequest request) {
        ThrowableProblem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle("User Name Exists")
                .withDetail(ex.getMessage())
                .build();

        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadCredentialsException(BadCredentialsException ex, NativeWebRequest request) {
        ThrowableProblem problem = Problem.builder()
                .withStatus(Status.UNAUTHORIZED)
                .withTitle("Invalid Username")
                .withDetail(ex.getMessage())
                .build();

        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleEmailDeliveryException(EmailDeliveryException ex, NativeWebRequest request) {
        ThrowableProblem problem = Problem.builder()
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .withTitle("An error occurred while sending the email")
                .withDetail(ex.getMessage())
                .build();

        return create(problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleEmailDeliveryException(UsernameValidationFailed ex, NativeWebRequest request) {
        ThrowableProblem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle("Username Validation Failed")
                .withDetail(ex.getMessage())
                .build();

        return create(problem, request);
    }

}
