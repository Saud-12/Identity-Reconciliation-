package com.bitespeed.identityReconciliation.advices;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class ApiError {
    HttpStatus httpStatus;
    String message;
    List<String> subErrors;
}
