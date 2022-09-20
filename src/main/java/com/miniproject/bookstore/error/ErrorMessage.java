package com.miniproject.bookstore.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorMessage {
    private HttpStatus statusCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timeStamp;

    private String errorMessage;

    private String debugMessage;

    public ErrorMessage(){
        timeStamp = LocalDateTime.now();
    }

    public ErrorMessage(HttpStatus statusCode){
        this();
        this.statusCode = statusCode;
    }

    public ErrorMessage(HttpStatus statusCode, Throwable ex){
        this();
        this.statusCode = statusCode;
        this.errorMessage = ex.getMessage();
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ErrorMessage(HttpStatus statusCode, String errorMessage, Throwable ex){
        this();
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
