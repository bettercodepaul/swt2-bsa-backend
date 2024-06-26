package de.bogenliga.application.business.schuetzenstatistikletztejahre.api.types;

import java.util.Objects;
import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 *
 * @author Alessa Hackh
 */
public class SchuetzenstatistikLetzteJahreDO extends CommonDataObject implements DataObject {

    private String schuetzenname;
    private long sportjahr1;
    private long sportjahr2;
    private long sportjahr3;
    private long sportjahr4;
    private long sportjahr5;
    private long allejahre_schnitt;

    public SchuetzenstatistikLetzteJahreDO(){
        //default constructor
    }

    /**
     * A Constructor with optional parameters
     *
     * @param schuetzenname;
     * @param sportjahr1;
     * @param sportjahr2;
     * @param sportjahr3;
     * @param sportjahr4;
     * @param sportjahr5;
     * @param allejahre_schnitt;
     */
    public SchuetzenstatistikLetzteJahreDO(
            String schuetzenname,
            long sportjahr1,
            long sportjahr2,
            long sportjahr3,
            long sportjahr4,
            long sportjahr5,
            long allejahre_schnitt
    ) {
        this.schuetzenname = schuetzenname;
        this.sportjahr1 = sportjahr1;
        this.sportjahr2 = sportjahr2;
        this.sportjahr3 = sportjahr3;
        this.sportjahr4 = sportjahr4;
        this.sportjahr5 = sportjahr5;
        this.allejahre_schnitt = allejahre_schnitt;
    }


    public String getSchuetzenname() {
        return schuetzenname;
    }


    public void setSchuetzenname(String schuetzenname) {
        this.schuetzenname = schuetzenname;
    }

    public long getSportjahr4() {
        return sportjahr4;
    }


    public void setSportjahr4(long sportjahr4) {
        this.sportjahr4 = sportjahr4;
    }


    public long getSportjahr1() {
        return sportjahr1;
    }


    public void setSportjahr1(long sportjahr1) {
        this.sportjahr1 = sportjahr1;
    }


    public long getSportjahr2() {
        return sportjahr2;
    }

    public void setAllejahre_schnitt(long allejahre_schnitt) {
        this.allejahre_schnitt = allejahre_schnitt;
    }

    public long getAllejahre_schnitt() {
        return allejahre_schnitt;
    }

    public long getSportjahr5() {
        return sportjahr5;
    }

    public void setSportjahr5(long sportjahr5) {
        this.sportjahr5 = sportjahr5;
    }

    public void setSportjahr2(long sportjahr2) {
        this.sportjahr2 = sportjahr2;
    }


    public long getSportjahr3() {
        return sportjahr3;
    }


    public void setSportjahr3(long sportjahr3) {
        this.sportjahr3 = sportjahr3;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchuetzenstatistikLetzteJahreDO that = (SchuetzenstatistikLetzteJahreDO) o;
        return
                schuetzenname == that.schuetzenname &&
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
