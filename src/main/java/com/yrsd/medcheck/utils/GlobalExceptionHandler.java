package com.yrsd.medcheck.utils;

import com.yrsd.medcheck.dtos.responses.ExceptionResponse;
import com.yrsd.medcheck.exceptions.*;
import com.yrsd.medcheck.security.exceptions.AuthenticationNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(AccountNotFoundException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                LocalDateTime.now()
                );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }


    @ExceptionHandler(value = EmailAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleException(EmailAlreadyExistException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(value = FailedFileUploadException.class)
    public ResponseEntity<ExceptionResponse> handleException(FailedFileUploadException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(value = InvalidDateOfBirthException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidDateOfBirthException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(value = InvalidEmailException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidEmailException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(value = InvalidGenderException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidGenderException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(value = InvalidNameException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidNameException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }
    @ExceptionHandler(value = InvalidNationalIdentityNumberException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidNationalIdentityNumberException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }
    @ExceptionHandler(value = InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidPasswordException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }
    @ExceptionHandler(value = InvalidProfilePictureException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidProfilePictureException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }
    @ExceptionHandler(value = InvalidUsernameException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidUsernameException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(value = UsernameAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleException(UsernameAlreadyExistException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(value = AuthenticationNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleException(AuthenticationNotSupportedException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception);
    }



}
