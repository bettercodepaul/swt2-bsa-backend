package de.bogenliga.application.business.rueckennummern.api;

import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the rueckennummern database requests.
 *
 * @author Jonas Winkler, jonas.winkler@student.reutlingen-university.de
 */
public interface RueckennummernComponent extends ComponentFacade {

    /**
     * Generates a pdf as binary document for one selected mannschaftsmitglied
     * @param dsbMannschaftsId ID of the mannschaft
     * @param dsbMitgliedId ID of the dsbMitglied
     * @return pdf as binary document
     */
    byte[] getRueckennummerPDFasByteArray(long dsbMannschaftsId, long dsbMitgliedId);

    /**
     * Generates a pdf as binary document for a whole mannschaft
     * @param dsbMannschaftsId ID of the mannschaft
     * @return pdf as binary document
     */
    byte[] getMannschaftsRueckennummernPDFasByteArray(long dsbMannschaftsId);

}
