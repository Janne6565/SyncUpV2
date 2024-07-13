package com.janne.syncupv2.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestException extends RuntimeException {
    private int status;
    private String message;
    private Object errorObject;
}
