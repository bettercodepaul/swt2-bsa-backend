package de.bogenliga.application.business.wettkampfanzeige.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the anzeige business entity.
 *
 * @author Mira Dietschmann, mira.dietschmann@student.reutlingen-university.de
 */
public class WettkampfAnzeigeBE extends CommonBusinessEntity implements BusinessEntity {
    private final Long anzeigenId;
    private final String physischeBildschirmId;
    private final Long tableTyp;
    private final Long veranstaltungsId;

    /**
     * Construct a AnzeigenDO with all required parameters.
     * @param anzeigenId The database ID of the Anzeige.
     * @param physischeBildschirmId The ID displayed by the physical screen.
     * @param tableTyp The type of table associated with the Anzeige.
     * @param veranstaltungId The Veranstaltung associated with the Anzeige.
     */
    public WettkampfAnzeigeBE(final Long anzeigenId, final String physischeBildschirmId,
                              final Long tableTyp, final Long veranstaltungId) {
        this.anzeigenId = anzeigenId;
        this.physischeBildschirmId = physischeBildschirmId;
        this.tableTyp = tableTyp;
        this.veranstaltungsId = veranstaltungId;
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
    public Long getVeranstaltungsId() {
        return veranstaltungsId;
    }

    @Override
    public String toString() {
        return "anzeige{AnzeigenID='" + anzeigenId +
                "', PhysischeBildschirmID='" + physischeBildschirmId +
               "', TableTyp='" + tableTyp +
                "', VeranstaltungsID='" + veranstaltungsId +
                "'}";
    }
}
