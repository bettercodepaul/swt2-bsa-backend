//======================================================================================================================
// Module: BMW Remote Software Update (RSU) - Zentrales Fahrzeug Update System (ZFUS)
// Copyright (c) 2018 BMW Group. All rights reserved.
//======================================================================================================================
package online.bogenliga.application.services.common;

import java.util.List;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public interface CrudServiceInterface {


    List<DataTransferObject> findAll();

    DataTransferObject findById(Long id);

    DataTransferObject create(DataTransferObject dataTransferObject);

    DataTransferObject update(DataTransferObject DataTransferObject);

    void delete(Long id);
}
