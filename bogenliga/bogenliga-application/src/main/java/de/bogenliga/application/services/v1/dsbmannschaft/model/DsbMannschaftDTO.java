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


    public DsbMannschaftDTO(Long id, Long vereinId, Long nummer,
                            Long benutzerId, Long veranstaltungId) {
        this.id = id;
        this.vereinId = vereinId;
        this.nummer = nummer;
        this.benutzerId=benutzerId;
        this.veranstaltungId=veranstaltungId;

    }
    public Long getId(){ return id; }

    public void setId(Long id){this.id=id;}


    public Long getVereinId(){return vereinId;}

    public void setVereinId(Long vereinId){this.vereinId=vereinId;}


    public Long getNummer(){return nummer;}

    public void setNummer(Long nummer){this.nummer=nummer;}


    public Long getBenutzerId(){return benutzerId;}

    public void setBenutzerId(Long benutzerId){this.benutzerId=benutzerId;}


    public Long getVeranstaltungId(){return veranstaltungId;}

    public void setVeranstaltungId(Long veranstaltungId){this.veranstaltungId=veranstaltungId;}

}
