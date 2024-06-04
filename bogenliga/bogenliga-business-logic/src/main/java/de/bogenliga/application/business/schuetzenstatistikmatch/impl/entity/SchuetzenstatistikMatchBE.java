package de.bogenliga.application.business.schuetzenstatistikmatch.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 *
 * Business entity
 *
 * @author Lennart Raach
 */
public class SchuetzenstatistikMatchBE extends CommonBusinessEntity implements BusinessEntity {

    private int rueckennummer;
    private String dsbMitgliedName;
    private float match3;
    private float match4;
    private float match5;
    private float match6;
    private float match7;
    private float match1;
    private float match2;
    private float pfeilpunkteSchnitt;

    public SchuetzenstatistikMatchBE() {
        // empty
    }

    public int getRueckennummer() {
        return rueckennummer;
    }

    public String getDsbMitgliedName() {
        return dsbMitgliedName;
    }

    public float getMatch1() {
        return match1;
    }
    public float getMatch2() {
        return match2;
    }
    public float getMatch3() {
        return match3;
    }

    public float getMatch4() {
        return match4;
    }
    public float getMatch5() {
        return match5;
    }

    public float getMatch6() {
        return match6;
    }

    public float getMatch7() {
        return match7;
    }

    public float getPfeilpunkteSchnitt() {
        return pfeilpunkteSchnitt;
    }

    public void setRueckennummer(int rueckennummer) {
        this.rueckennummer = rueckennummer;
    }

    public void setDsbMitgliedName(String dsbMitgliedName) {
        this.dsbMitgliedName = dsbMitgliedName;
    }

    public void setMatch1(float match1) {
        this.match1 = match1;
    }

    public void setMatch2(float match2) {
        this.match2 = match2;
    }

    public void setMatch3(float match3) {
        this.match3 = match3;
    }

    public void setMatch4(float match4) {
        this.match4 = match4;
    }

    public void setMatch5(float match5) {
        this.match5 = match5;
    }

    public void setMatch6(float match6) {
        this.match6 = match6;
    }

    public void setMatch7(float match7) {
        this.match7 = match7;
    }

    public void setPfeilpunkteSchnitt(float pfeilpunkteSchnitt) {
        this.pfeilpunkteSchnitt = pfeilpunkteSchnitt;
    }


    @Override
    public String toString() {
        return "SchuetzenstatistikMatchBE{" +
                "rueckennummer=" + rueckennummer +
                ", dsbMitgliedName='" + dsbMitgliedName + '\'' +
                ", match1=" + match1 +
                ", match2=" + match2 +
                ", match3=" + match3 +
                ", match4=" + match4 +
                ", match5=" + match5 +
                ", match6=" + match6 +
                ", match7=" + match7 +
                ", pfeilpunkteSchnitt=" + pfeilpunkteSchnitt +
                '}';
    }
}

