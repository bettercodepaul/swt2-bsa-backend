package de.bogenliga.application.business.wettkampfanzeige.api.types;

import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * I represent the anzeige data object.
 *
 * @author Mira Dietschmann, mira.dietschmann@student.reutlingen-university.de
 */
public class WettkampfAnzeigenDO extends CommonDataObject implements DataObject {
    private final Long anzeigenId;
    private final String physischeBildschirmId;
    private final Long tableTyp;
    private final Long veranstaltungId;

    /**
     * I construct a WettkampfAnzeigenDO with all required parameters.
     * @param anzeigenId The database ID of the Anzeige.
     * @param physischeBildschirmId The ID displayed by the physical screen.
     * @param tableTyp The type of table associated with the Anzeige.
     * @param veranstaltungId The Veranstaltung associated with the Anzeige.
     */
    public WettkampfAnzeigenDO(final Long anzeigenId, final String physischeBildschirmId,
                               final Long tableTyp, final Long veranstaltungId) {
        this.anzeigenId = anzeigenId;
        this.physischeBildschirmId = physischeBildschirmId;
        this.tableTyp = tableTyp;
        this.veranstaltungId = veranstaltungId;
    }

    /**
     * Get the database ID of the Anzeige.
     * @return The database ID of the Anzeige.
     */
    public Long getAnzeigenId() {
        return anzeigenId;
    }

    /**
     * Get the ID displayed on the screen.
     * @return The ID displayed on the screen.
     */
    public String getPhysischeBildschirmId() {
        return physischeBildschirmId;
    }

    /**
     * Get the type of table associated with the Anzeige.
     * @return The type of table associated with the Anzeige.
     */
    public Long getTableTyp() {
        return tableTyp;
    }

    /**
     * Get the database ID of the Veranstaltung associated with the Anzeige.
     * @return The database ID of the Veranstaltung associated with the Anzeige.
     */
    public Long getVeranstaltungId() {
        return veranstaltungId;
    }

}
