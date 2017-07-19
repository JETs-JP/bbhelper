package com.oracle.poco.bbhelper;

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

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private static final BbhelperLogger logger =
            BbhelperLogger.getLogger(ExceptionControllerAdvice.class);

    /**
     * このクラスで明示的にハンドリングしていない例外を処理する
     * 明示的にハンドリングしていない例外は、システムエラーとして固定のレスポンスを返却する
     *
     * @param ex        発生した例外
     * @param request   リクエスト
     * @return          レスポンス
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleSystemException(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "System Error", "System error is occurred.");
        return super.handleExceptionInternal(
                ex, error, null, INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Beehiveサーバーへの複数回のアクセスを並列実行したときの例外をハンドリングする
     *
     * @param ex        発生した例外を束ねる例外クラス
     * @param request   リクエスト
     * @return          レスポンス
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleBbhelperBeehive4jParallelInvocationException(
            BbhelperBeehive4jParallelInvocationException ex,
            WebRequest request) {
        List<BbhelperException> causes = ex.getCauses();
        if (causes.size() == 1) {
            return handleBbhelperException(causes.get(0), request);
        }
        ErrorResponse error = new ErrorResponse(
                ex.getStatus().value(), ex.getStatus().getReasonPhrase(),
                ex.getClass().getName(), ex.getLocalizedMessage());
        causes.forEach(e -> error.addDetail(e.getLocalizedMessage()));
        return super.handleExceptionInternal(ex, error, null, ex.getStatus(), request);
    }

    /**
     * このアプリケーションで定義しているカスタム例外をハンドリングする
     *
     * @param ex        発生した例外
     * @param request   リクエスト
     * @return          レスポンス
     */
    @ExceptionHandler
    public ResponseEntity<Object> handleBbhelperException(BbhelperException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                ex.getStatus().value(), ex.getStatus().getReasonPhrase(),
                ex.getClass().getName(), ex.getLocalizedMessage());
        return super.handleExceptionInternal(ex, error, null, ex.getStatus(), request);
    }

    /*
     * 入力チェック時の例外をハンドリングするためにこのメソッドをオーバーライドする
     */
    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidationFailure(ex, ex.getBindingResult(), headers, status, request);
    }

    /*
     * 入力チェック時の例外をハンドリングするためにこのメソッドをオーバーライドする
     */
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
