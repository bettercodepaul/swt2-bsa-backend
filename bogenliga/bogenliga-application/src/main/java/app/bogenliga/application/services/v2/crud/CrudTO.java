//======================================================================================================================
// Module: BMW Remote Software Update (RSU) - Zentrales Fahrzeug Update System (ZFUS)
// Copyright (c) 2018 BMW Group. All rights reserved.
//======================================================================================================================
package app.bogenliga.application.services.v2.crud;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import app.bogenliga.application.services.common.DataTransferObject;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class CrudTO extends DataTransferObject {

    @NotNull
    @NotEmpty
    public String name;
}
