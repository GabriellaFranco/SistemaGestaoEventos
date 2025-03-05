package com.enterprise.gestaoeventos.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Data
public class ErrorResponse {

    private int status;
    private String message;
    private String timestamp;
}
