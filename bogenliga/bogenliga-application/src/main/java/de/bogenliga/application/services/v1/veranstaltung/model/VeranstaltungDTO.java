package de.bogenliga.application.services.v1.veranstaltung.model;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.service.types.DataTransferObject;
import java.sql.Date;


/**
 * I'm the data transfer Object of Vereine.
 * <p>
 * I define the payload for the external REST interface of the Vereine business entity
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
public class VeranstaltungDTO implements DataTransferObject {
    private Long id;
    private Long wettkampfTypId;
    private String name;
    private Long sportjahr;
    private Date meldeDeadline;
    private Long kampfrichterAnzahl;
    private Long hoehere;
    private Long ligaleiterID;
    /**
     * Constructors
     */

    public VeranstaltungDTO() {
        //empty
    }


    /**
     *

     */
    public VeranstaltungDTO(Long id, Long wettkampfTypId, String name, Long sportjahr,
                            Date meldeDeadline, Long kampfrichterAnzahl,
                            Long hoehere, Long ligaleiterID) {
        this.id = id;
        this.wettkampfTypId = wettkampfTypId;
        this.name = name;
        this.sportjahr = sportjahr;
        this.meldeDeadline = meldeDeadline;
        this.kampfrichterAnzahl = kampfrichterAnzahl;
        this.hoehere = hoehere;
        this.ligaleiterID = ligaleiterID;
    }

}
