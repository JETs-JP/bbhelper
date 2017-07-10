package com.oracle.poco.bbhelper;

import com.oracle.poco.bbhelper.exception.BbhelperBeehive4jException;
import com.oracle.poco.bbhelper.exception.BbhelperBeehive4jParallelInvocationException;
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

import java.util.List;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private static final BbhelperLogger logger =
            BbhelperLogger.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler
    public ResponseEntity<Object> handleBbhelperBeehive4jParallelInvocationException(
            BbhelperBeehive4jParallelInvocationException ex,
            WebRequest request) {
        List<BbhelperBeehive4jException> causes = ex.getCauses();
        if (causes.size() == 1) {
            return handleExceptionInternal(
                    causes.get(0), null, null, causes.get(0).getStatus(), request);
        }
        ErrorResponse error = new ErrorResponse(
                ex.getStatus().value(), ex.getStatus().getReasonPhrase(),
                ex.getClass().getName(), ex.getLocalizedMessage());
        causes.forEach(e -> error.addDetail(e.getLocalizedMessage()));
        return super.handleExceptionInternal(ex, error, null, ex.getStatus(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleBbhelperException(BbhelperException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, null, ex.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse error = new ErrorResponse(status.value(), status.getReasonPhrase(),
                ex.getClass().getName(), ex.getLocalizedMessage());
        return super.handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidationFailure(ex, ex.getBindingResult(), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return handleValidationFailure(ex, ex.getBindingResult(), headers, status, request);
    }

    private ResponseEntity<Object> handleValidationFailure(
            Throwable cause, BindingResult bindingResult, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        BbhelperValidationFailureException bbhe =
                new BbhelperValidationFailureException(cause, bindingResult);
        /*
         * バリデーション時の例外の処理はこの例外ハンドラにに集約するので、ロギングもここで行う。
         * 個別のControllerメソッドではバリデーションのロギングは記述しない。
         */
        logger.info(new ErrorMessage(Operation.VALIDATION, bbhe));
        ErrorResponse error = new ErrorResponse(status.value(), status.getReasonPhrase(),
                bbhe.getClass().getName(), bbhe.getLocalizedMessage());
        bindingResult.getGlobalErrors().stream().forEach(
                e -> error.addDetail(e.getDefaultMessage()));
        bindingResult.getFieldErrors().stream().forEach(
                e -> error.addDetail(e.getDefaultMessage()));
        return super.handleExceptionInternal(bbhe, error, headers, status, request);
    }

}
