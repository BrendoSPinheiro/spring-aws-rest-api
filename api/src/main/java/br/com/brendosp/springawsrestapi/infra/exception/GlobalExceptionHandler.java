package br.com.brendosp.springawsrestapi.infra.exception;

import br.com.brendosp.springawsrestapi.domain.usecases.exceptions.EmailAlreadyInUseException;
import br.com.brendosp.springawsrestapi.infra.http.dtos.response.ErrorResponseDTO;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> badCredentialsException(BadCredentialsException e) {
        var error = new ErrorResponseDTO(
            "Unauthorized",
            HttpStatus.UNAUTHORIZED.toString(),
            e.getLocalizedMessage(),
            null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponseDTO> emailAlreadyInUseException(EmailAlreadyInUseException e) {
        var error = new ErrorResponseDTO(
            "Bad Request",
            HttpStatus.BAD_REQUEST.toString(),
            e.getLocalizedMessage(),
            null
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        var error = new ErrorResponseDTO(
            "Bad Request",
            HttpStatus.BAD_REQUEST.toString(),
            e.getLocalizedMessage(),
            new ArrayList<>()
        );

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            error.errorMessages().add(String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()));
        });

        e.getBindingResult().getGlobalErrors().forEach(globalError -> {
            error.errorMessages().add(String.format("%s: %s", globalError.getObjectName(), globalError.getDefaultMessage()));
        });

        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e) {
        var error = new ErrorResponseDTO(
            "Internal Server Error",
            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
            e.getLocalizedMessage(),
            null
        );

        return ResponseEntity.internalServerError().body(error);
    }
}


