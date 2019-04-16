package de.bogenliga.application.business.Passe.api.types;

import java.time.OffsetDateTime;
import de.bogenliga.application.common.component.types.CommonDataObject;

/**
 * Contains the values of the Passe business entity.
 * @author Kay Scheerer
 */
public class PasseDO extends CommonDataObject {
    private long id;
    private long passeMannschaftId;
    private long passeWettkampfId;
    private long passeMatchNr;
    private long passeLfdnr;
    private long passeDsbMitgliedId;

    @Override
    public String toString(){
        return  "Passe: "
                +"MitgliedID: "+id
                +"MannschaftID: "+passeMannschaftId
                +"WettkampfID: "+passeWettkampfId
                +"MatchNr: "+passeMatchNr;
    }

    public PasseDO(long id, long passeMannschaftId, long passeWettkampfId, long passeMatchNr, long passeLfdnr,
                   long passeDsbMitgliedId, final OffsetDateTime createdAtUtc,
                   final Long createdByUserId, final OffsetDateTime lastModifiedUtc,
                   final Long lastModifiedByUserId, final Long version){
        this.id = id;
        this.passeMannschaftId = passeMannschaftId;
        this.passeWettkampfId = passeWettkampfId;
        this.passeMatchNr = passeMatchNr;
        this.passeLfdnr = passeLfdnr;
        this.passeDsbMitgliedId = passeDsbMitgliedId;

        //set param from CommonDataObject

        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }


    public PasseDO(long id, long passeMannschaftId, long passeWettkampfId, long passeMatchNr, long passeLfdnr,
                   long passeDsbMitgliedId) {
        this.id = id;
        this.passeMannschaftId = passeMannschaftId;
        this.passeWettkampfId = passeWettkampfId;
        this.passeMatchNr = passeMatchNr;
        this.passeLfdnr = passeLfdnr;
        this.passeDsbMitgliedId = passeDsbMitgliedId;
    }




    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public long getPasseMannschaftId() {
        return passeMannschaftId;
    }


    public void setPasseMannschaftId(long passeMannschaftId) {
        this.passeMannschaftId = passeMannschaftId;
    }


    public long getPasseWettkampfId() {
        return passeWettkampfId;
    }


    public void setPasseWettkampfId(long passeWettkampfId) {
        this.passeWettkampfId = passeWettkampfId;
    }


    public long getPasseMatchNr() {
        return passeMatchNr;
    }


    public void setPasseMatchNr(long passeMatchNr) {
        this.passeMatchNr = passeMatchNr;
    }


    public long getPasseLfdnr() {
        return passeLfdnr;
    }


    public void setPasseLfdnr(long passeLfdnr) {
        this.passeLfdnr = passeLfdnr;
    }


    public long getPasseDsbMitgliedId() {
        return passeDsbMitgliedId;
    }


    public void setPasseDsbMitgliedId(long passeDsbMitgliedId) {
        this.passeDsbMitgliedId = passeDsbMitgliedId;
    }



}
