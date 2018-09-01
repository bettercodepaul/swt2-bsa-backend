//======================================================================================================================
// Module: BMW Remote Software Update (RSU) - Zentrales Fahrzeug Update System (ZFUS)
// Copyright (c) 2018 BMW Group. All rights reserved.
//======================================================================================================================
package online.bogenliga.application.services.common.errorhandling;

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
import online.bogenliga.application.common.errorhandling.exception.EntityNotFoundException;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers, final HttpStatus status,
                                                                  final WebRequest request) {
        final List<ErrorDTO> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorDTO(
                    error.getField() + " = " + error.getRejectedValue() + " -> " + error.getField() + " " + error.getDefaultMessage()));
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(new ErrorDTO(error.getObjectName() + ": " + error.getDefaultMessage()));
        }


        final ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, errors);
        return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<ApiError> handleUserNotFoundException(final EntityNotFoundException ex,
                                                                      final WebRequest request) {
        final ErrorDTO errorDetails = new ErrorDTO(ex.getMessage());
        return new ResponseEntity<>(new ApiError(HttpStatus.NOT_FOUND, errorDetails), HttpStatus.NOT_FOUND);
    }
}