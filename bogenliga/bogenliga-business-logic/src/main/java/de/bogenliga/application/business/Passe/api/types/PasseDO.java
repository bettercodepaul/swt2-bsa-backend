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

    private int pfeil1;
    private int pfeil2;
    private int pfeil3;
    private int pfeil4;
    private int pfeil5;
    private int pfeil6;


     public PasseDO(long id, long passeMannschaftId, long passeWettkampfId, long passeMatchNr, long passeLfdnr,
        long passeDsbMitgliedId, int pfeil1, int pfeil2, int pfeil3, int pfeil4, int pfeil5, int pfeil6, final OffsetDateTime createdAtUtc,
                    final Long createdByUserId, final OffsetDateTime lastModifiedUtc,
                    final Long lastModifiedByUserId, final Long version){
        this.id = id;
        this.passeMannschaftId = passeMannschaftId;
        this.passeWettkampfId = passeWettkampfId;
        this.passeMatchNr = passeMatchNr;
        this.passeLfdnr = passeLfdnr;
        this.passeDsbMitgliedId = passeDsbMitgliedId;
        this.pfeil1 = pfeil1;
        this.pfeil2 = pfeil2;
        this.pfeil3 = pfeil3;
        this.pfeil4 = pfeil4;
        this.pfeil5 = pfeil5;
        this.pfeil6 = pfeil6;

        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }


    public PasseDO(long id, long passeMannschaftId, long passeWettkampfId, long passeMatchNr, long passeLfdnr,
                   long passeDsbMitgliedId, int pfeil1, int pfeil2, int pfeil3, int pfeil4, int pfeil5, int pfeil6) {
        this.id = id;
        this.passeMannschaftId = passeMannschaftId;
        this.passeWettkampfId = passeWettkampfId;
        this.passeMatchNr = passeMatchNr;
        this.passeLfdnr = passeLfdnr;
        this.passeDsbMitgliedId = passeDsbMitgliedId;
        this.pfeil1 = pfeil1;
        this.pfeil2 = pfeil2;
        this.pfeil3 = pfeil3;
        this.pfeil4 = pfeil4;
        this.pfeil5 = pfeil5;
        this.pfeil6 = pfeil6;
    }


    public int getPfeil1() {
        return pfeil1;
    }


    public void setPfeil1(int pfeil1) {
        this.pfeil1 = pfeil1;
    }


    public int getPfeil2() {
        return pfeil2;
    }


    public void setPfeil2(int pfeil2) {
        this.pfeil2 = pfeil2;
    }


    public int getPfeil3() {
        return pfeil3;
    }


    public void setPfeil3(int pfeil3) {
        this.pfeil3 = pfeil3;
    }


    public int getPfeil4() {
        return pfeil4;
    }


    public void setPfeil4(int pfeil4) {
        this.pfeil4 = pfeil4;
    }


    public int getPfeil5() {
        return pfeil5;
    }


    public void setPfeil5(int pfeil5) {
        this.pfeil5 = pfeil5;
    }


    public int getPfeil6() {
        return pfeil6;
    }


    public void setPfeil6(int pfeil6) {
        this.pfeil6 = pfeil6;
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
