package com.oracle.poco.bbhelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.oracle.poco.bbhelper.exception.BbhelperBadRequestException;
import com.oracle.poco.bbhelper.exception.BbhelperBeehive4jException;
import com.oracle.poco.bbhelper.log.BbhelperLogger;

@ControllerAdvice
//public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
public class ExceptionControllerAdvice {

    @Autowired
    BbhelperLogger logger;

    @ExceptionHandler({BbhelperBeehive4jException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(BbhelperBeehive4jException e) {
        logger.exception(e);
        return "BbhelperBeehive4jException";
    }

    @ExceptionHandler({BbhelperBadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleException(BbhelperBadRequestException e) {
        logger.exception(e);
        return "BbhelperBadRequestException";
    }

//    @Override
//    protected ResponseEntity<Object> handleExceptionInternal(
//            Exception ex, Object body, HttpHeaders headers, HttpStatus status,
//            WebRequest request) {
//        ErrorResponse error = createApiError(ex);
//        return super.handleExceptionInternal(ex, error, headers, status, request);
//    }
//
//    private ErrorResponse createApiError(Exception ex) {
//        ErrorResponse apiError = new ErrorResponse(123, "hoge", "bar");
//        return apiError;
//    }
//
}
