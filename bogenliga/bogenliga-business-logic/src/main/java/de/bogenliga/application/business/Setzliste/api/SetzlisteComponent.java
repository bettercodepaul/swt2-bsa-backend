package de.bogenliga.application.business.Setzliste.api;

import de.bogenliga.application.common.component.ComponentFacade;

import java.util.List;

/**
 * Responsible for the setzliste database requests.
 */
public interface SetzlisteComponent extends ComponentFacade {

    /**
     * Return filename of the setzliste pdf file.
     *
     * @return String contains the name of the setzliste pdf file;
     */

    byte[] getPDFasByteArray(int wettkampfid);
}
