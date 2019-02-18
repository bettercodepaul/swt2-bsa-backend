package de.bogenliga.application.services.v1.dsbmannschaft.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * I´m the data transfer object of the dsbMannschaft.
 *
 * I define the payload for the external REST interface of the dsbMannschaft business entity.
 *
 * @author Kaywan Barzani
 * @see DataTransferObject
 */
public class DsbMannschaftDTO implements DataTransferObject {
    private static final long serialVersionUID = 8559563978424033907L;

    private Long id;
    private Long vereinId;
    private Long nummer;
    private Long benutzerId;
    private Long veranstaltungId;


    /**
     * Constructors
     */
    public DsbMannschaftDTO() {
        // empty constructor
    }


    public DsbMannschaftDTO(final Long id, final long vereinId, final long nummer,
                            final long benutzerId, final long veranstaltungId) {
        this.id = id;
        this. vereinId=vereinId;
        this.nummer = nummer;
        this.benutzerId=benutzerId;
        this.veranstaltungId=veranstaltungId;

    }
    public long getId(){ return id; }

    public void setId(final long id){this.id=id;}


    public long getVereinId(){return vereinId;}

    public void setVereinId(final long vereinId){this.vereinId=vereinId;}


    public long getNummer(){return nummer;}

    public void setNummer(final long nummer){this.nummer=nummer;}


    public long getBenutzerId(){return benutzerId;}

    public void setBenutzerId(final long benutzerId){this.benutzerId=benutzerId;}


    public long getVeranstaltungId(){return veranstaltungId;}

    public void setVeranstaltungId(final long veranstaltungId){this.veranstaltungId=veranstaltungId;}

}
