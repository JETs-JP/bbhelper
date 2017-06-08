package com.oracle.poco.bbhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.log.BbhelperLogger;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    BbhelperLogger logger;

    @ExceptionHandler
    public ResponseEntity<Object> handleBbhelperException(
            BbhelperException ex, WebRequest request) {
        logger.exception(request, ex);
        return handleExceptionInternal(ex, null, null, ex.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        ErrorResponse error = createErrorResponse(ex, status);
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ErrorResponse createErrorResponse(Exception ex, HttpStatus status) {
        // ex, statusがnullになることはない
        String errorCode = null;
        if (ex instanceof BbhelperException) {
            BbhelperException bbhe = (BbhelperException)ex;
            errorCode = bbhe.getCode();
        }
        ErrorResponse error = new ErrorResponse(
                status.value(), status.getReasonPhrase(), errorCode, ex.getMessage());
        return error;
    }

}
