package com.hubosm.turingsimulator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public ProblemDetail integrityConflict(AccessDeniedException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Integrity conflict");
        problemDetail.setProperty("errorCode", "INTEGRITY_CONFLICT");
        return problemDetail;
    }

    @ExceptionHandler(StorageLimitException.class)
    public ProblemDetail storageLimit(AccessDeniedException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Storage limit error");
        problemDetail.setProperty("errorCode", "STORAGE_LIMIT_ERROR");
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail error(Exception ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setTitle("Unidentified error");
        return problemDetail;
    }
}
