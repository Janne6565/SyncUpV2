package com.janne.syncupv2.exception;


import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestException extends RuntimeException {
    private int status;
    private String message;
    private Object errorObject;
}
