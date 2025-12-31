package com.ebank.authService.exception;



public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
