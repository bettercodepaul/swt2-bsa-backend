package de.bogenliga.application.services.common.errorhandling;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<ErrorDTO> handleBusinessException(final BusinessException ex,
                                                                  final WebRequest request) {
        final HttpStatus errorStatus;
        switch (ex.getErrorCode()) {
            case ENTITY_NOT_FOUND_ERROR:
                errorStatus = HttpStatus.NOT_FOUND;
                break;
            case ENTITY_CONFLICT_ERROR:
                errorStatus = HttpStatus.CONFLICT;
                break;
            case INVALID_ARGUMENT_ERROR:
                errorStatus = HttpStatus.BAD_REQUEST;
                break;
            case NO_SESSION_ERROR:
                errorStatus = HttpStatus.UNAUTHORIZED;
                break;
            case NO_PERMISSION_ERROR:
                errorStatus = HttpStatus.FORBIDDEN;
                break;
            default:
                errorStatus = HttpStatus.BAD_REQUEST;

        }
        final ErrorDTO errorDetails = new ErrorDTO(ex.getErrorCode(), ex.getParameters(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, errorStatus);
    }


    @ExceptionHandler(TechnicalException.class)
    public final ResponseEntity<ErrorDTO> handleTechnicalException(final TechnicalException ex,
                                                                   final WebRequest request) {
        final HttpStatus errorStatus;
        switch (ex.getErrorCode()) {
            case DEPRECATED_VERSION_ERROR:
                errorStatus = HttpStatus.GONE;
                break;
            case EXTERNAL_SERVICE_ERROR:
            case INTERNAL_ERROR:
            case UNEXPECTED_ERROR:
            case DATABASE_CONNECTION_ERROR:
            case DATABASE_TRANSACTION_ERROR:
            case DATABASE_ERROR:
            default:
                errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }
        final ErrorDTO errorDetails = new ErrorDTO(ex.getErrorCode(), ex.getParameters(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, errorStatus);
    }


    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<ErrorDTO> handleNullPointerException(final NullPointerException ex,
                                                                     final WebRequest request) {
        final ErrorDTO errorDetails = new ErrorDTO(ErrorCode.UNEXPECTED_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ErrorDTO> handleRuntimeException(final RuntimeException ex,
                                                                 final WebRequest request) {
        final ErrorDTO errorDetails = new ErrorDTO(ErrorCode.UNEXPECTED_ERROR, ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers, final HttpStatus status,
                                                                  final WebRequest request) {
        final List<ErrorDTO> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorDTO(ErrorCode.INVALID_ARGUMENT_ERROR,
                    error.getField() + " = " + error.getRejectedValue() + " -> " + error.getField()
                            + " " + error.getDefaultMessage()));
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(new ErrorDTO(ErrorCode.INVALID_ARGUMENT_ERROR,
                    error.getObjectName() + ": " + error.getDefaultMessage()));
        }

        return handleExceptionInternal(
                ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }
}