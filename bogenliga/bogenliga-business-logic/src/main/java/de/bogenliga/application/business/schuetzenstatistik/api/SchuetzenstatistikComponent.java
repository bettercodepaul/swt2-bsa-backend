package de.bogenliga.application.business.schuetzenstatistik.api;

import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.common.component.ComponentFacade;

import java.util.List;

/**
 * Responsible for the user database requests.
 */
public interface SchuetzenstatistikComponent extends ComponentFacade {

    /**
     * gibt die aktuelle Schuetzenstatistik der Veranstaltung zurück
     */
    List<SchuetzenstatistikDO> getSchuetzenstatistikVeranstaltung(Long veranstaltungId, Long vereinId);


    /**
     * gibt die Schuetzenstatistik zu einer WettkampfId zurück
     */
    List<SchuetzenstatistikDO> getSchuetzenstatistikWettkampf(Long wettkampfId, Long vereinId);

}
