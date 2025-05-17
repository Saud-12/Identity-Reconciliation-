package com.bitespeed.identityReconciliation.advices;

import com.bitespeed.identityReconciliation.exceptions.NoContactInfoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidExcpetion(MethodArgumentNotValidException exception){
        List<String> errorMessages = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        String combinedMessage = String.join("; ", errorMessages);
        ApiError apiError=ApiError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(combinedMessage)
                .build();
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(NoContactInfoException.class)
    public ResponseEntity<ApiResponse<?>> handleNoContactInfoException(NoContactInfoException exception){
        ApiError apiError=ApiError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .build();

        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerError(Exception exception){
        ApiError apiError=ApiError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();
        return buildErrorResponseEntity(apiError);
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(new ApiResponse<>(apiError),apiError.getHttpStatus());
    }
}
