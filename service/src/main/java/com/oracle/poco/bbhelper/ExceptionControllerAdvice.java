package com.oracle.poco.bbhelper;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    
    @Autowired
    BbhelperLogger logger;

    @Autowired
    MessageSource messageSource;

    // 独自に定義した例外用のハンドラ
    @ExceptionHandler
    public ResponseEntity<Object> handleBbhelperException(BbhelperException ex, WebRequest request) {
        logger.exception(request, ex);
        return handleExceptionInternal(ex, null, null, ex.getStatus(), request);
    }

    // 独自定義以外の例外（＝フレームワークのレベルで発生した例外）のハンドラ
    @ExceptionHandler
    public ResponseEntity<Object> handleHandleSystemException(Exception ex, WebRequest request) {
        // TODO 実装
        return handleExceptionInternal(
                ex, null, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /*
     * すべての例外のハンドラから呼び出される処理。
     * ここでエラーレスポンスを作成／適用することで、すべてのException型に対するエラーレスポンスに反映される。、
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        ErrorResponse error = createErrorResponse(ex, request, status);
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    private ErrorResponse createErrorResponse(Exception ex, WebRequest request, HttpStatus status) {
        // ex, statusがnullになることはない
        String errorCode = ex.getClass().getName();
        String message;
        if (ex instanceof BbhelperException) {
            message = messageSource.getMessage(
                    ((BbhelperException) ex).getMessageSourceResolvable(), request.getLocale());
        } else {
            message = messageSource.getMessage(
                    ex.getClass().getName(),null, ex.getLocalizedMessage(), request.getLocale());
        }
        ErrorResponse error =
                new ErrorResponse(status.value(), status.getReasonPhrase(), errorCode, message);
        return error;
    }

}
