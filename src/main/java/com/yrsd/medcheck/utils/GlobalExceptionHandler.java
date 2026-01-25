package com.yrsd.medcheck.utils;

import com.yrsd.medcheck.dtos.responses.ExceptionResponse;
import com.yrsd.medcheck.exceptions.*;
import com.yrsd.medcheck.security.exceptions.AuthenticationNotSupportedException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull AccountNotFoundException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                LocalDateTime.now()
                );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }


    @ExceptionHandler(value = EmailAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull EmailAlreadyExistException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(value = FailedFileUploadException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull FailedFileUploadException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(value = InvalidDateOfBirthException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull InvalidDateOfBirthException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(value = InvalidEmailException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull InvalidEmailException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(value = InvalidGenderException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull InvalidGenderException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(value = InvalidNameException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull InvalidNameException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }
    @ExceptionHandler(value = InvalidNationalIdentityNumberException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull InvalidNationalIdentityNumberException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }
    @ExceptionHandler(value = InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull InvalidPasswordException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }
    @ExceptionHandler(value = InvalidProfilePictureException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull InvalidProfilePictureException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }
    @ExceptionHandler(value = InvalidUsernameException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull InvalidUsernameException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(value = UsernameAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull UsernameAlreadyExistException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(value = AuthenticationNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull AuthenticationNotSupportedException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception);
    }

    @ExceptionHandler(value = InvalidPhoneNumberException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull InvalidPhoneNumberException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(value = PackDoesntExistException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull PackDoesntExistException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(value = SachetDoesntExistException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull SachetDoesntExistException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull UnauthorizedException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }


    @ExceptionHandler(value = DrugDoesntExistException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull DrugDoesntExistException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(value = DrugDoesntExistException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NonNull BatchDoesntExistException e) {
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }




}
