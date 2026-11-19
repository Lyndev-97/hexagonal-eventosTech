package com.eventostec.api.adapters.inbound.exceptions;

import com.eventostec.api.application.InvalidAdminKeyException;
import com.eventostec.api.domain.event.EventNotFoundException;
import com.eventostec.api.domain.image.ImageUploadException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.UncheckedIOException;
import java.util.Optional;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    private static final String TIPO_DESCONHECIDO = "tipo desconhecido";
    private static final String VALOR_NAO_INFORMADO = "valor não informado";

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleNotFound(EventNotFoundException ex, HttpServletRequest request) {
        ProblemDetails body = new ProblemDetails("Não encontrado", HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(InvalidAdminKeyException.class)
    public ResponseEntity<ProblemDetails> handleForbidden(InvalidAdminKeyException ex, HttpServletRequest request) {
        ProblemDetails body = new ProblemDetails("Acesso negado", HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ProblemDetails> handleUpload(ImageUploadException ex, HttpServletRequest request) {
        ProblemDetails body = new ProblemDetails("Falha no upload", HttpStatus.BAD_GATEWAY.value(),
                HttpStatus.BAD_GATEWAY.name(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ProblemDetails> handleEmptyResult(HttpServletRequest request) {
        ProblemDetails body = new ProblemDetails("Não encontrado", HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(), "O recurso informado não foi encontrado.", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Cobre falhas de leitura de arquivos multipart (ex.: upload de imagem corrompido),
     * lançadas como UncheckedIOException pelo mapper de entrada.
     */
    @ExceptionHandler(UncheckedIOException.class)
    public ResponseEntity<ProblemDetails> handleUncheckedIO(HttpServletRequest request) {
        ProblemDetails body = new ProblemDetails("Falha ao processar arquivo", HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(), "Não foi possível ler o arquivo enviado.", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }



    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetails> handleDataIntegrityViolation(HttpServletRequest request) {
        String title = "Violação de integridade de campo";
        String detail = "Violação de integridade detectada no banco de dados.";

        return badRequest(title, detail, request);
    }


    /**
     * Rede de segurança final: qualquer exceção não mapeada acima cai aqui, garantindo
     * que toda resposta de erro da API siga o contrato {@link ProblemDetails}, em vez de
     * vazar a página de erro padrão do Spring Boot.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Erro não tratado ao processar a requisição {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        ProblemDetails body = new ProblemDetails("Erro interno", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(), "Ocorreu um erro inesperado ao processar a requisição.",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private ResponseEntity<ProblemDetails> badRequest(String title, String detail, HttpServletRequest request) {
        ProblemDetails body = new ProblemDetails(title, HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(), detail, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}