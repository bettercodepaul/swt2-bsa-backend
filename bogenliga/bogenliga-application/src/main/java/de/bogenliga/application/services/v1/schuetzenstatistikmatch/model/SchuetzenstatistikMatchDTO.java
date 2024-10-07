package de.bogenliga.application.services.v1.schuetzenstatistikmatch.model;

import java.util.Objects;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * Data transfer object of schuetzenstatistikMatch
 *
 * @author Lennart Raach
 */
public class SchuetzenstatistikMatchDTO implements DataTransferObject {
    /**
     *  attributes
     */
    private final int rueckennummer;
    private final String dsbMitgliedName;
    private final float match1;
    private final float match2;
    private final float match3;
    private final float match4;
    private final float match5;
    private final float match6;
    private final float match7;
    private final float pfeilpunkteSchnitt;


    /**
     * Constructor with optional parameters
     *
     * @param rueckennummer rueckennummer
     * @param dsbMitgliedName dsbmitgliedname
     * @param match1 match1
     * @param match2 match2
     * @param match3 match3
     * @param match4 match4
     * @param match5 match5
     * @param match6 match6
     * @param match7 match7
     * @param pfeilpunkteSchnitt pfeilpunkteSchnitt
     */
    public SchuetzenstatistikMatchDTO(
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
        SchuetzenstatistikMatchDTO that = (SchuetzenstatistikMatchDTO) o;
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
