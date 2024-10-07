package de.bogenliga.application.business.schuetzenstatistikwettkampf.api.types;

import java.util.Objects;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * Contains the values of the schuetzenstatistikWettkampf business entity.
 *
 * @author Anna Baur
 */
public class SchuetzenstatistikWettkampftageDO extends CommonDataObject implements DataObject {
    /**
     * DO attributes
     */

    private String dsbMitgliedName;
    private int rueckenNummer;
    private float wettkampftag1;
    private float wettkampftag2;
    private float wettkampftag3;
    private float wettkampftag4;
    private float wettkampftageSchnitt;

    public SchuetzenstatistikWettkampftageDO(){
        //default constructor
    }

    /**
     * A Constructor with optional parameters
     *
     * @param dsbMitgliedName;
     * @param rueckenNummer;
     * @param wettkampftag1;
     * @param wettkampftag2;
     * @param wettkampftag3;
     * @param wettkampftag4;
     * @param wettkampftageSchnitt;
     */
    public SchuetzenstatistikWettkampftageDO(
            String dsbMitgliedName,
            int rueckenNummer,
            float wettkampftag1,
            float wettkampftag2,
            float wettkampftag3,
            float wettkampftag4,
            float wettkampftageSchnitt
    ) {
        this.dsbMitgliedName = dsbMitgliedName;
        this.rueckenNummer = rueckenNummer;
        this.wettkampftag1 = wettkampftag1;
        this.wettkampftag2 = wettkampftag2;
        this.wettkampftag3 = wettkampftag3;
        this.wettkampftag4 = wettkampftag4;
        this.wettkampftageSchnitt = wettkampftageSchnitt;
    }


    public String getDsbMitgliedName() {
        return dsbMitgliedName;
    }
    public void setDsbMitgliedName(String dsbMitgliedName) {
        this.dsbMitgliedName = dsbMitgliedName;
    }

    public int getRueckenNummer() {
        return rueckenNummer;
    }
    public void setRueckenNummer(int rueckenNummer) {
        this.rueckenNummer = rueckenNummer;
    }

    public float getWettkampftag1() {return wettkampftag1;}
    public void setWettkampftag1(float wettkampftag1) {this.wettkampftag1 = wettkampftag1;}

    public float getWettkampftag2() {return wettkampftag2;}
    public void setWettkampftag2(float wettkampftag2) {this.wettkampftag2 = wettkampftag2;}

    public float getWettkampftag3() {return wettkampftag3;}
    public void setWettkampftag3(float wettkampftag3) {this.wettkampftag3 = wettkampftag3;}

    public float getWettkampftag4() {return wettkampftag4;}
    public void setWettkampftag4(float wettkampftag4) {this.wettkampftag4 = wettkampftag4;}

    public float getWettkampftageSchnitt() {return wettkampftageSchnitt;}
    public void setWettkampftageSchnitt(float wettkampftageSchnitt) {this.wettkampftageSchnitt = wettkampftageSchnitt;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchuetzenstatistikWettkampftageDO that = (SchuetzenstatistikWettkampftageDO) o;
        return Objects.equals(dsbMitgliedName, that.dsbMitgliedName) &&
                rueckenNummer == that.rueckenNummer &&
                wettkampftag1 == that.wettkampftag1 &&
                wettkampftag2 == that.wettkampftag2 &&
                wettkampftag3 == that.wettkampftag3 &&
                wettkampftag4 == that.wettkampftag4 &&
                wettkampftageSchnitt == that.wettkampftageSchnitt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dsbMitgliedName, rueckenNummer, wettkampftag1, wettkampftag2, wettkampftag3, wettkampftag4, wettkampftageSchnitt);
    }
}
