package com.hubosm.turingsimulator.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail accessDenied(AccessDeniedException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Access Denied");
        problemDetail.setProperty("errorCode", "ACCESS_DENIED");
        return problemDetail;
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ProblemDetail elementNotFound(ElementNotFoundException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Resource not found");
        problemDetail.setProperty("errorCode", "ELEMENT_NOT_FOUND");
        return problemDetail;
    }

    @ExceptionHandler(IntegrityException.class)
    public ProblemDetail integrityConflict(IntegrityException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Integrity conflict");
        problemDetail.setProperty("errorCode", "INTEGRITY_CONFLICT");
        return problemDetail;
    }

    @ExceptionHandler(StorageLimitException.class)
    public ProblemDetail storageLimit(StorageLimitException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Storage limit error");
        problemDetail.setProperty("errorCode", "STORAGE_LIMIT_ERROR");
        return problemDetail;
    }

    /*
    @ExceptionHandler(ValidationException.class)
    public ProblemDetail validationError(ValidationException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Validation error");
        problemDetail.setProperty("errorCode", "VALIDATION_ERROR");
        return problemDetail;
    }*/

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validationError(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setTitle("Validation failed");
        HashMap<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));
        problemDetail.setProperty("validationErrors", errors);
        return problemDetail;
    }

    //DuplicateTmNameException returns ResponseEntity,
    // for app to suggest overwritting existing tm
    @ExceptionHandler(DuplicateTmNameException.class)
    public ProblemDetail onDuplicate(DuplicateTmNameException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Duplicate machine name");
        pd.setDetail(ex.getMessage());
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("reason", "DUPLICATE_NAME");
        pd.setProperty("existingId", ex.getExistingId());
        pd.setProperty("name", ex.getName());
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail error(Exception ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Unidentified error");
        return problemDetail;
    }
}
