package com.eventostec.api.adapters.inbound.exceptions;

import com.eventostec.api.application.InvalidAdminKeyException;
import com.eventostec.api.domain.event.EventNotFoundException;
import com.eventostec.api.domain.image.ImageUploadException;
import com.eventostec.api.utils.ExceptionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            DataIntegrityViolationException.class,
            MethodArgumentNotValidException.class,
            ConversionFailedException.class,
    })
    public ResponseEntity<ProblemDetails> handleException(Exception ex, HttpServletRequest request) {
        ProblemDetails problemDetails = ExceptionUtil.getProblemDetails(request, ex);
        return new ResponseEntity<>(problemDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleNotFound(EventNotFoundException ex, HttpServletRequest request) {
        ProblemDetails body = new ProblemDetails("Não encontrado", 404, "NOT_FOUND", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(InvalidAdminKeyException.class)
    public ResponseEntity<ProblemDetails> handleForbidden(InvalidAdminKeyException ex, HttpServletRequest request) {
        ProblemDetails body = new ProblemDetails("Acesso negado", 403, "FORBIDDEN", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ProblemDetails> handleUpload(ImageUploadException ex, HttpServletRequest request) {
        ProblemDetails body = new ProblemDetails("Falha no upload", 502, "BAD_GATEWAY", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
    }
}