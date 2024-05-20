package de.bogenliga.application.business.schuetzenstatistikmatch.api.types;

import java.util.Objects;

/**
 *
 * Data Object class, that contains the values of the schuetzenstatistikMatch business entity
 *
 * @author Lennart Raach
 */
public class SchuetzenstatistikMatchDO {

    /**
     * Data Object attributes
     */
    private int rueckennummer;
    private String dsbMitgliedName;
    private float match1;
    private float match2;
    private float match3;
    private float match4;
    private float pfeilpunkteSchnitt;
    private float match5;
    private float match6;
    private float match7;
    public SchuetzenstatistikMatchDO() {
        // empty constructor
    }

    /**
     * Constructor with optional parameters
     *
     * @param rueckennummer;
     * @param dsbMitgliedName;
     * @param match1;
     * @param match2;
     * @param match3;
     * @param match4;
     * @param match5;
     * @param match6;
     * @param match7;
     * @param pfeilpunkteSchnitt;
     */
    public SchuetzenstatistikMatchDO(
            int rueckennummer,
            String dsbMitgliedName,
            float match1,
            float match2,
            float match3,
            float match4,
            float match5,
            float match6,
            float match7,
            float pfeilpunkteSchnitt
    ) {
        this.rueckennummer = rueckennummer;
        this.dsbMitgliedName = dsbMitgliedName;
        this.match1 = match1;
        this.match2 = match2;
        this.match3 = match3;
        this.match4 = match4;
        this.match5 = match5;
        this.match6 = match6;
        this.match7 = match7;
        this.pfeilpunkteSchnitt = pfeilpunkteSchnitt;
    }


    public int getRueckennummer() {
        return rueckennummer;
    }


    public void setRueckennummer(int rueckenNummer) {
        this.rueckennummer = rueckenNummer;
    }


    public float getMatch2() {
        return match2;
    }


    public void setMatch2(float match2) {
        this.match2 = match2;
    }

    public String getDsbMitgliedName() {
        return dsbMitgliedName;
    }


    public void setDsbMitgliedName(String dsbMitgliedName) {
        this.dsbMitgliedName = dsbMitgliedName;
    }


    public float getMatch1() {
        return match1;
    }


    public void setMatch1(float match1) {
        this.match1 = match1;
    }


    public float getMatch3() {
        return match3;
    }


    public void setMatch3(float match3) {
        this.match3 = match3;
    }


    public float getMatch4() {
        return match4;
    }


    public void setMatch4(float match4) {
        this.match4 = match4;
    }


    public float getMatch5() {
        return match5;
    }


    public void setMatch5(float match5) {
        this.match5 = match5;
    }


    public float getMatch6() {
        return match6;
    }


    public void setMatch6(float match6) {
        this.match6 = match6;
    }


    public float getMatch7() {
        return match7;
    }


    public void setMatch7(float match7) {
        this.match7 = match7;
    }


    public float getPfeilpunkteSchnitt() {
        return pfeilpunkteSchnitt;
    }


    public void setPfeilpunkteSchnitt(float pfeilpunkteSchnitt) {
        this.pfeilpunkteSchnitt = pfeilpunkteSchnitt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRueckennummer(), getDsbMitgliedName(), getMatch1(), getMatch2(), getMatch3(),
                getMatch4(), getMatch5(), getMatch6(), getMatch7(), getPfeilpunkteSchnitt());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SchuetzenstatistikMatchDO that = (SchuetzenstatistikMatchDO) o;
        return rueckennummer == that.rueckennummer &&
                Float.compare(match1, that.match1) == 0 &&
                Float.compare(match2, that.match2) == 0 &&
                Float.compare(match3, that.match3) == 0 &&
                Float.compare(match4, that.match4) == 0 &&
                Float.compare(match5, that.match5) == 0 &&
                Float.compare(match6, that.match6) == 0 &&
                Float.compare(match7, that.match7) == 0 &&
                Float.compare(pfeilpunkteSchnitt, that.pfeilpunkteSchnitt) == 0 &&
                Objects.equals(dsbMitgliedName, that.dsbMitgliedName);
    }
}
