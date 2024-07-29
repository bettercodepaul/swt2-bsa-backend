package de.bogenliga.application.business.schuetzenstatistikmatch.api;

import java.util.List;
import de.bogenliga.application.business.schuetzenstatistikmatch.api.types.SchuetzenstatistikMatchDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Interface responsible for database requests
 *
 * @author Lennart Raach
 */
public interface SchuetzenstatistikMatchComponent extends ComponentFacade {


    /**
     * returns a list of schuetzenstatistik_match rows based of Veranstaltung
     */
    List<SchuetzenstatistikMatchDO> getSchuetzenstatistikMatchVeranstaltung(Long veranstaltungId, Long vereinId);


    /**
     * returns a list of schuetzenstatistik_match rows based of Wettkampf
     */
    List<SchuetzenstatistikMatchDO> getSchuetzenstatistikMatchWettkampf(Long wettkampfId, Long vereinId, Long tag);



}
