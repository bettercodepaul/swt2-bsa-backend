package de.bogenliga.application.business.schuetzenstatistikwettkampf.api;

import java.util.List;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.types.SchuetzenstatistikWettkampftageDO;
import de.bogenliga.application.common.component.ComponentFacade;
/**
 * Responsible for the user database requests.
 *
 * @author Anna Baur
 */
public interface SchuetzenstatistikWettkampfComponent  extends ComponentFacade{

    /**
     * gibt die aktuelle Schuetzenstatistik der Wettkämpfe der Veranstaltung zurück
     */
    List<SchuetzenstatistikWettkampftageDO> getSchuetzenstatistikWettkampfVeranstaltung(Long veranstaltungId, Long vereinId);


    /**
     * gibt die Schuetzenstatistik zu einer WettkampfId zurück
     */
    List<SchuetzenstatistikWettkampftageDO> getSchuetzenstatistikWettkampf(Long wettkampfId, Long vereinId);



}
