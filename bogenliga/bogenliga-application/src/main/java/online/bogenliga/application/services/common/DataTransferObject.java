//======================================================================================================================
// Module: BMW Remote Software Update (RSU) - Zentrales Fahrzeug Update System (ZFUS)
// Copyright (c) 2018 BMW Group. All rights reserved.
//======================================================================================================================
package online.bogenliga.application.services.common;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class DataTransferObject {

    @NotNull
    @PositiveOrZero
    public Long id;

    @NotNull
    @PositiveOrZero
    public Long version;
}
