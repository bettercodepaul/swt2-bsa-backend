package de.bogenliga.application.business.ligatabelle.api;

import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;
import de.bogenliga.application.common.component.ComponentFacade;

import java.util.List;

/**
 * Responsible for the user database requests.
 */
public interface LigatabelleComponent extends ComponentFacade {

    /**
     * gibt die aktuelle Ligatabelle der Veranstaltung
     * mit allen Mannschaften der Liga und deren Punkten/ Platzierung zurück
     * Liste ist leer - wenn keine Mannschaften gefunden werden
     */
    List<LigatabelleDO> getLigatabelleVeranstaltung(Long veranstaltungId);


    /**
     * gibt die Ligatabelle zu einer WettkampfId zurück
     * mit allen Mannschaften der Liga und deren Punkten/ Platzierung zurück
     * Liste ist leer - wenn keine Mannschaften gefunden werden
     *
     * so kann statt der aktuellen Tabelle (siehe oben) auch ältere Stände abfragen
     */
    List<LigatabelleDO> getLigatabelleWettkampf(Long wettkampfId);



}
