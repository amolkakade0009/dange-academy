package com.dangeacademy.exception;

public class CloudflareException extends RuntimeException {

    public CloudflareException(String message) {
        super(message);
    }

    public CloudflareException(String message, Throwable cause) {
        super(message, cause);
    }
}
