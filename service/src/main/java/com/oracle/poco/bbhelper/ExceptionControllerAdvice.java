package com.oracle.poco.bbhelper;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.BbhelperValidationFailureException;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.log.ErrorMessage;
import com.oracle.poco.bbhelper.log.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private static final BbhelperLogger logger =
            BbhelperLogger.getLogger(ExceptionControllerAdvice.class);

    // 独自に定義した例外用のハンドラ
    @ExceptionHandler
    public ResponseEntity<Object> handleBbhelperException(BbhelperException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, null, ex.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidationFalure(ex, ex.getBindingResult(), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return handleValidationFalure(ex, ex.getBindingResult(), headers, status, request);
    }

    private ResponseEntity<Object> handleValidationFalure(
            Throwable cause, BindingResult bindingResult, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        BbhelperValidationFailureException bbhe =
                new BbhelperValidationFailureException(cause, bindingResult);
        logger.info(new ErrorMessage(Operation.VALIDATION, bbhe));

        ErrorResponse error = new ErrorResponse(status.value(), status.getReasonPhrase(),
                bbhe.getClass().getName(), bbhe.getLocalizedMessage());
        bindingResult.getGlobalErrors().stream().forEach(
                e -> error.addDetail(e.getDefaultMessage()));
        bindingResult.getFieldErrors().stream().forEach(
                e -> error.addDetail(e.getDefaultMessage()));
        return super.handleExceptionInternal(bbhe, error, headers, status, request);
    }

    /*
     * すべての例外のハンドラから呼び出される処理。
     * ここでエラーレスポンスを作成／適用することで、すべてのException型に対するエラーレスポンスに反映される。
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse error = new ErrorResponse(status.value(), status.getReasonPhrase(),
                ex.getClass().getName(), ex.getLocalizedMessage());
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

}
