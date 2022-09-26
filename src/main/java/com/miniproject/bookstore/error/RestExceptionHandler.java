package com.miniproject.bookstore.error;

import com.miniproject.bookstore.error.exceptions.EntityNotFoundException;
import com.miniproject.bookstore.error.exceptions.EntityAlreadyExistsException;
import com.miniproject.bookstore.error.exceptions.InvalidEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex){
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatusCode(HttpStatus.NOT_FOUND);
        errorMessage.setErrorMessage(ex.getMessage());
        return buildResponseEntity(errorMessage);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    protected ResponseEntity<Object> handleEntityAlreadyExists(EntityAlreadyExistsException ex){
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatusCode(HttpStatus.CONFLICT);
        errorMessage.setErrorMessage(ex.getMessage());
        return buildResponseEntity(errorMessage);
    }

    @ExceptionHandler(InvalidEntityException.class)
    protected ResponseEntity<Object> handleInvalidEntity(InvalidEntityException ex){
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        errorMessage.setErrorMessage(ex.getMessage());
        return buildResponseEntity(errorMessage);
    }



    private ResponseEntity<Object> buildResponseEntity(ErrorMessage errorMessage){
        return new ResponseEntity<>(errorMessage,errorMessage.getStatusCode());
    }
}
