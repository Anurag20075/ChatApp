package com.chat_app.exception;

import java.time.LocalDateTime;

public class ErrorDetail {
    private String error;
    private String message;
    private int status;
    private LocalDateTime timestamp;

    public ErrorDetail(String error, String message, int status, LocalDateTime timestamp) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }   
    

}
