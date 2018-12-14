package de.bogenliga.application.business.Setzliste.api;

import de.bogenliga.application.business.Setzliste.api.types.SetzlisteDO;
import de.bogenliga.application.common.component.ComponentFacade;

import java.util.List;

/**
 * Responsible for the dsbmitglied database requests.
 */
public interface SetzlisteComponent extends ComponentFacade {

    /**
     * Return all dsbmitglied entries.
     *
     * @return list of all dsbmitglied dsbmitglied in the database;
     * empty list, if no dsbmitglied is found
     */
    String getTable();
}
