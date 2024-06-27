package de.bogenliga.application.services.v1.schuetzenstatistikletztejahre.model;

import java.util.Objects;
import de.bogenliga.application.common.service.types.DataTransferObject;

/**
 * data transfer object for schuetzenstatistikletztejahre
 * @author Alessa Hackh
 */
public class SchuetzenstatistikLetzteJahreDTO implements DataTransferObject {

    /**
     * attributes of DTO
     */
    private String schuetzenname;
    private float sportjahr1;
    private float sportjahr2;
    private float sportjahr3;
    private float sportjahr4;
    private float sportjahr5;
    private float allejahre_schnitt;


    /**
     * The Constructor with optional parameters
     *
     * @param schuetzenname;
     * @param sportjahr1;
     * @param sportjahr2;
     * @param sportjahr3;
     * @param sportjahr4;
     * @param sportjahr5;
     * @param allejahre_schnitt;
     */
    public SchuetzenstatistikLetzteJahreDTO(
            String schuetzenname,
            float sportjahr1,
            float sportjahr2,
            float sportjahr3,
            float sportjahr4,
            float sportjahr5,
            float allejahre_schnitt
    ) {
        this.schuetzenname = schuetzenname;
        this.sportjahr1 = sportjahr1;
        this.sportjahr2 = sportjahr2;
        this.sportjahr3 = sportjahr3;
        this.sportjahr4 = sportjahr4;
        this.sportjahr5 = sportjahr5;
        this.allejahre_schnitt = allejahre_schnitt;
    }

    // Getters
    public String getSchuetzenname() {
        return schuetzenname;
    }

    public float getSportjahr3() {
        return sportjahr3;
    }

    public float getSportjahr2() {
        return sportjahr2;
    }

    public float getSportjahr1() {
        return sportjahr1;
    }

    public float getSportjahr4() {
        return sportjahr4;
    }

    public float getAllejahre_schnitt() { return allejahre_schnitt; }

    public float getSportjahr5() {
        return sportjahr5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchuetzenstatistikLetzteJahreDTO that = (SchuetzenstatistikLetzteJahreDTO)  o;
        return  Objects.equals(schuetzenname, that.schuetzenname) &&
                sportjahr1 == that.sportjahr1 &&
                sportjahr2 == that.sportjahr2 &&
                sportjahr3 == that.sportjahr3 &&
                sportjahr4 == that.sportjahr4 &&
                sportjahr5 == that.sportjahr5 &&
                allejahre_schnitt == that.allejahre_schnitt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(schuetzenname, sportjahr1, sportjahr2, sportjahr3, sportjahr4, sportjahr5, allejahre_schnitt);
    }
}
