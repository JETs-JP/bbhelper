package com.oracle.poco.bbhelper;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// TODO すべての例外ハンドリングでエラーがログに記録されるように実装する
@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    // 独自に定義した例外用のハンドラ
    @ExceptionHandler
    public ResponseEntity<Object> handleBbhelperException(BbhelperException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, null, ex.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse error = new ErrorResponse(status.value(), status.getReasonPhrase(),
                ex.getClass().getName(), ex.getLocalizedMessage());
        ex.getBindingResult().getGlobalErrors().stream().forEach(
                e -> error.addDetail(e.getDefaultMessage()));
        ex.getBindingResult().getFieldErrors().stream().forEach(
                e -> error.addDetail(e.getDefaultMessage()));
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        ErrorResponse error = new ErrorResponse(status.value(), status.getReasonPhrase(),
                ex.getClass().getName(), ex.getLocalizedMessage());
        ex.getBindingResult().getGlobalErrors().stream().forEach(
                e -> error.addDetail(e.getDefaultMessage()));
        ex.getBindingResult().getFieldErrors().stream().forEach(
                e -> error.addDetail(e.getDefaultMessage()));
        return super.handleExceptionInternal(ex, error, headers, status, request);
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
