//======================================================================================================================
// Module: BMW Remote Software Update (RSU) - Zentrales Fahrzeug Update System (ZFUS)
// Copyright (c) 2018 BMW Group. All rights reserved.
//======================================================================================================================
package online.bogenliga.application.services.common.errorhandling;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class ErrorDTO {
    private final String message;


    public ErrorDTO(final String message) {
        super();

        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}
