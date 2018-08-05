//======================================================================================================================
// Module: BMW Remote Software Update (RSU) - Zentrales Fahrzeug Update System (ZFUS)
// Copyright (c) 2018 BMW Group. All rights reserved.
//======================================================================================================================
package app.bogenliga.application.services.common.errorhandling;

import java.util.Date;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class ErrorDTO {
        private String message;

        public ErrorDTO(String message) {
            super();

            this.message = message;
        }


    public String getMessage() {
        return message;
    }
}
