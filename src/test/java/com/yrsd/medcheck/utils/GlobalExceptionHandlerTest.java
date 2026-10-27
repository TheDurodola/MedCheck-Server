package com.yrsd.medcheck.utils;

import com.yrsd.medcheck.exceptions.*;
import com.yrsd.medcheck.security.exceptions.AuthenticationNotSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Handle AccountNotFoundException returns 404 ProblemDetail")
    void handleAccountNotFoundException() {
        AccountNotFoundException ex = new AccountNotFoundException("Account not found");
        ProblemDetail pd = exceptionHandler.handleException(ex);

        assertNotNull(pd);
        assertEquals(HttpStatus.NOT_FOUND.value(), pd.getStatus());
        assertEquals("Account not found", pd.getDetail());
        assertEquals("Not Found", pd.getTitle());
        assertNotNull(pd.getProperties());
        assertNotNull(pd.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("Handle UserNotFoundException returns 404 ProblemDetail")
    void handleUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("User not found");
        ProblemDetail pd = exceptionHandler.handleException(ex);

        assertNotNull(pd);
        assertEquals(HttpStatus.NOT_FOUND.value(), pd.getStatus());
        assertEquals("User not found", pd.getDetail());
        assertEquals("Not Found", pd.getTitle());
        assertNotNull(pd.getProperties());
        assertNotNull(pd.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("Handle EmailAlreadyExistException returns 400 ProblemDetail")
    void handleEmailAlreadyExistException() {
        EmailAlreadyExistException ex = new EmailAlreadyExistException("Email already exists");
        ProblemDetail pd = exceptionHandler.handleException(ex);

        assertNotNull(pd);
        assertEquals(HttpStatus.BAD_REQUEST.value(), pd.getStatus());
        assertEquals("Email already exists", pd.getDetail());
        assertEquals("Bad Request", pd.getTitle());
        assertNotNull(pd.getProperties());
        assertNotNull(pd.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("Handle AuthenticationNotSupportedException returns 401 ProblemDetail")
    void handleAuthenticationNotSupportedException() {
        AuthenticationNotSupportedException ex = new AuthenticationNotSupportedException("Auth not supported");
        ProblemDetail pd = exceptionHandler.handleException(ex);

        assertNotNull(pd);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), pd.getStatus());
        assertEquals("Auth not supported", pd.getDetail());
        assertEquals("Unauthorized", pd.getTitle());
        assertNotNull(pd.getProperties());
        assertNotNull(pd.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("Handle PaymentGatewayServerException returns 502 ProblemDetail")
    void handlePaymentGatewayServerException() {
        PaymentGatewayServerException ex = new PaymentGatewayServerException(500, "Gateway down");
        ProblemDetail pd = exceptionHandler.handleException(ex);

        assertNotNull(pd);
        assertEquals(HttpStatus.BAD_GATEWAY.value(), pd.getStatus());
        assertEquals("Gateway down", pd.getDetail());
        assertEquals("Bad Gateway", pd.getTitle());
        assertNotNull(pd.getProperties());
        assertNotNull(pd.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("Handle DrugDoesntExistException returns 400 ProblemDetail")
    void handleDrugDoesntExistException() {
        DrugDoesntExistException ex = new DrugDoesntExistException("Drug does not exist");
        ProblemDetail pd = exceptionHandler.handleException(ex);

        assertNotNull(pd);
        assertEquals(HttpStatus.BAD_REQUEST.value(), pd.getStatus());
        assertEquals("Drug does not exist", pd.getDetail());
        assertEquals("Bad Request", pd.getTitle());
        assertNotNull(pd.getProperties());
        assertNotNull(pd.getProperties().get("timestamp"));
    }

    @Test
    @DisplayName("Handle RestrictedTransferException returns 400 ProblemDetail")
    void handleRestrictedTransferException() {
        RestrictedTransferException ex = new RestrictedTransferException("Transfer restricted");
        ProblemDetail pd = exceptionHandler.handleException(ex);

        assertNotNull(pd);
        assertEquals(HttpStatus.BAD_REQUEST.value(), pd.getStatus());
        assertEquals("Transfer restricted", pd.getDetail());
        assertEquals("Bad Request", pd.getTitle());
        assertNotNull(pd.getProperties());
        assertNotNull(pd.getProperties().get("timestamp"));
    }
}
