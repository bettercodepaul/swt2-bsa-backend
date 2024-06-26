package de.bogenliga.application.business.schuetzenstatistikletztejahre.api;

import java.util.List;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.types.SchuetzenstatistikLetzteJahreDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * @author Alessa Hackh
 */
public interface SchuetzenstatistikLetzteJahreComponent extends ComponentFacade {

    /* gibt Schützenstatistik zu Sportjahr, Veranstaltung und Verein zurück */
    List<SchuetzenstatistikLetzteJahreDO> getSchuetzenstatistikLetzteJahre(long sportjahr, long veranstaltungId, long vereinId);
}
