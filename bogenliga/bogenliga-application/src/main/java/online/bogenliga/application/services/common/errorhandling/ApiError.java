//======================================================================================================================
// Module: BMW Remote Software Update (RSU) - Zentrales Fahrzeug Update System (ZFUS)
// Copyright (c) 2018 BMW Group. All rights reserved.
//======================================================================================================================
package online.bogenliga.application.services.common.errorhandling;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class ApiError {

    private final HttpStatus status;
    private final List<ErrorDTO> errors;


    public ApiError(final HttpStatus status, final List<ErrorDTO> errors) {
        super();
        this.status = status;
        this.errors = errors;
    }


    public ApiError(final HttpStatus status, final ErrorDTO error) {
        super();
        this.status = status;
        errors = Arrays.asList(error);
    }


    public HttpStatus getStatus() {
        return status;
    }


    public List<ErrorDTO> getErrors() {
        return errors;
    }
}