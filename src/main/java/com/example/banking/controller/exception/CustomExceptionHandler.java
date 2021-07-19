package com.example.banking.controller.exception;

import com.example.banking.controller.exception.model.ErrorResponse;
import com.example.banking.controller.exception.model.ValidationErrorResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    protected ResponseEntity<ErrorResponse> handleInternalError(RuntimeException ex) {
        return ResponseEntity.internalServerError().body(new ErrorResponse(ex.getMessage()));
    }

    @Override
    protected @NotNull ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers, @NotNull HttpStatus status, @NotNull WebRequest request
    ) {
        List<String> details = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        var error = new ValidationErrorResponse("Validation Failed", details);
        return ResponseEntity.badRequest().body(error);
    }
}
