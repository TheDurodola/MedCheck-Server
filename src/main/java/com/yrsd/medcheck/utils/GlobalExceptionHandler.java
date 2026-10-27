package com.yrsd.medcheck.utils;

import com.yrsd.medcheck.exceptions.*;
import com.yrsd.medcheck.security.exceptions.AuthenticationNotSupportedException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ProblemDetail buildProblemDetail(HttpStatus status, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                status,
                detail != null ? detail : status.getReasonPhrase()
        );
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ProblemDetail handleException(@NonNull AccountNotFoundException e) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ProblemDetail handleException(@NonNull UserNotFoundException e) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = EmailAlreadyExistException.class)
    public ProblemDetail handleException(@NonNull EmailAlreadyExistException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = FailedFileUploadException.class)
    public ProblemDetail handleException(@NonNull FailedFileUploadException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = InvalidDateOfBirthException.class)
    public ProblemDetail handleException(@NonNull InvalidDateOfBirthException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = InvalidEmailException.class)
    public ProblemDetail handleException(@NonNull InvalidEmailException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = InvalidGenderException.class)
    public ProblemDetail handleException(@NonNull InvalidGenderException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = InvalidNameException.class)
    public ProblemDetail handleException(@NonNull InvalidNameException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = InvalidNationalIdentityNumberException.class)
    public ProblemDetail handleException(@NonNull InvalidNationalIdentityNumberException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = InvalidPasswordException.class)
    public ProblemDetail handleException(@NonNull InvalidPasswordException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = InvalidProfilePictureException.class)
    public ProblemDetail handleException(@NonNull InvalidProfilePictureException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = InvalidUsernameException.class)
    public ProblemDetail handleException(@NonNull InvalidUsernameException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = UsernameAlreadyExistException.class)
    public ProblemDetail handleException(@NonNull UsernameAlreadyExistException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = AuthenticationNotSupportedException.class)
    public ProblemDetail handleException(@NonNull AuthenticationNotSupportedException e) {
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(value = InvalidPhoneNumberException.class)
    public ProblemDetail handleException(@NonNull InvalidPhoneNumberException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = PackDoesntExistException.class)
    public ProblemDetail handleException(@NonNull PackDoesntExistException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = SachetDoesntExistException.class)
    public ProblemDetail handleException(@NonNull SachetDoesntExistException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ProblemDetail handleException(@NonNull UnauthorizedException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = DrugDoesntExistException.class)
    public ProblemDetail handleException(@NonNull DrugDoesntExistException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = BatchDoesntExistException.class)
    public ProblemDetail handleException(@NonNull BatchDoesntExistException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = OrganizationDoesntExistException.class)
    public ProblemDetail handleException(@NonNull OrganizationDoesntExistException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = InvalidOrganisationCodeException.class)
    public ProblemDetail handleException(@NonNull InvalidOrganisationCodeException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = RestrictedTransferException.class)
    public ProblemDetail handleException(@NonNull RestrictedTransferException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = ReportNotFoundException.class)
    public ProblemDetail handleException(@NonNull ReportNotFoundException e) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = UnitNotFoundException.class)
    public ProblemDetail handleException(@NonNull UnitNotFoundException e) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = UnitUnassignableException.class)
    public ProblemDetail handleException(@NonNull UnitUnassignableException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = PaymentGatewayClientException.class)
    public ProblemDetail handleException(@NonNull PaymentGatewayClientException e) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = PaymentGatewayServerException.class)
    public ProblemDetail handleException(@NonNull PaymentGatewayServerException e) {
        return buildProblemDetail(HttpStatus.BAD_GATEWAY, e.getMessage());
    }

    @ExceptionHandler(value = EmailDeliveryException.class)
    public ProblemDetail handleException(@NonNull EmailDeliveryException e) {
        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
