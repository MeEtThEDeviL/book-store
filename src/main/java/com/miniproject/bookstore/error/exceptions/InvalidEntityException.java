package com.miniproject.bookstore.error.exceptions;

public class InvalidEntityException extends RuntimeException{
    public InvalidEntityException(String message){
        super(message);
    }
}
