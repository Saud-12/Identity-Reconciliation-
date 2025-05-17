package com.bitespeed.identityReconciliation.advices;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse <T>{
    private T data;
    private ApiError apiError;
    private LocalDateTime localDateTime;

    public ApiResponse(){
        this.localDateTime=LocalDateTime.now();
    }

    public ApiResponse(T data){
        this();
        this.data=data;
    }
    public ApiResponse(ApiError apiError){
        this();
        this.apiError=apiError;
    }
}
