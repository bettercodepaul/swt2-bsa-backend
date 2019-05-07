package de.bogenliga.application.business.Passe.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class PasseBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = 8445258747402691960L;

    private Long id;
    private long passeMannschaftId;
    private long passeWettkampfId;
    private long passeMatchNr;
    private long passeMatchId;
    private long passeLfdnr;
    private long passeDsbMitgliedId;

    private int pfeil1;
    private int pfeil2;
    private int pfeil3;
    private int pfeil4;
    private int pfeil5;
    private int pfeil6;


    @Override
    public String toString() {
        return "PasseBE{" +
                ", PasseId=" + id +
                ", passeMannschaftId=" + passeMannschaftId +
                ", passeWettkampfId=" + passeWettkampfId +
                ", passeMatchNr=" + passeMatchNr +
                ", passeMatchId=" + passeMatchId +
                ", passeLfdnr=" + passeLfdnr +
                ", passeDsbMitgliedId=" + passeDsbMitgliedId +
                ", pfeil1=" + pfeil1 +
                ", pfeil2=" + pfeil2 +
                ", pfeil3=" + pfeil3 +
                ", pfeil4=" + pfeil4 +
                ", pfeil5=" + pfeil5 +
                ", pfeil6=" + pfeil6 +
                '}';
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


    public long getPasseMatchId() {
        return passeMatchId;
    }


    public void setPasseMatchId(long passeMatchId) {
        this.passeMatchId = passeMatchId;
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


    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }
}
