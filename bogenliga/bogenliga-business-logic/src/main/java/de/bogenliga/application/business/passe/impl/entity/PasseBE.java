package de.bogenliga.application.business.passe.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 *
 * @author Kay Scheerer
 */
public class PasseBE extends CommonBusinessEntity implements BusinessEntity {
    private static final Long serialVersionUID = 8445258747402691960L;

    private Long id;
    private Long passeMannschaftId;
    private Long passeWettkampfId;
    private Long passeMatchNr;
    private Long passeMatchId;
    private Long passeLfdnr;
    private Long passeDsbMitgliedId;

    private Integer pfeil1;
    private Integer pfeil2;
    private Integer pfeil3;
    private Integer pfeil4;
    private Integer pfeil5;
    private Integer pfeil6;





    public Long getPasseMannschaftId() {
        return passeMannschaftId;
    }


    @Override
    public String toString() {
        return "PasseBE{" +
                "id=" + id +
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


    public void setPasseMannschaftId(Long passeMannschaftId) {
        this.passeMannschaftId = passeMannschaftId;
    }


    public Long getPasseWettkampfId() {
        return passeWettkampfId;
    }


    public void setPasseWettkampfId(Long passeWettkampfId) {
        this.passeWettkampfId = passeWettkampfId;
    }


    public Long getPasseMatchNr() {
        return passeMatchNr;
    }


    public Long getPasseMatchId() {
        return passeMatchId;
    }


    public void setPasseMatchId(Long passeMatchId) {
        this.passeMatchId = passeMatchId;
    }


    public void setPasseMatchNr(Long passeMatchNr) {
        this.passeMatchNr = passeMatchNr;
    }


    public Long getPasseLfdnr() {
        return passeLfdnr;
    }


    public void setPasseLfdnr(Long passeLfdnr) {
        this.passeLfdnr = passeLfdnr;
    }


    public Long getPasseDsbMitgliedId() {
        return passeDsbMitgliedId;
    }


    public void setPasseDsbMitgliedId(Long passeDsbMitgliedId) {
        this.passeDsbMitgliedId = passeDsbMitgliedId;
    }


    public Integer getPfeil1() {
        return pfeil1;
    }


    public void setPfeil1(Integer pfeil1) {
        this.pfeil1 = pfeil1;
    }


    public Integer getPfeil2() {
        return pfeil2;
    }


    public void setPfeil2(Integer pfeil2) {
        this.pfeil2 = pfeil2;
    }


    public Integer getPfeil3() {
        return pfeil3;
    }


    public void setPfeil3(Integer pfeil3) {
        this.pfeil3 = pfeil3;
    }


    public Integer getPfeil4() {
        return pfeil4;
    }


    public void setPfeil4(Integer pfeil4) {
        this.pfeil4 = pfeil4;
    }


    public Integer getPfeil5() {
        return pfeil5;
    }


    public void setPfeil5(Integer pfeil5) {
        this.pfeil5 = pfeil5;
    }


    public Integer getPfeil6() {
        return pfeil6;
    }


    public void setPfeil6(Integer pfeil6) {
        this.pfeil6 = pfeil6;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }
}
