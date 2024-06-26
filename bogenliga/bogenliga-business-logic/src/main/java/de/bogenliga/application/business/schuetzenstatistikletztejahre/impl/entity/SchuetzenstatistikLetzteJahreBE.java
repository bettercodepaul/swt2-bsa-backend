package de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * @author Alessa Hackh
 */
public class SchuetzenstatistikLetzteJahreBE extends CommonBusinessEntity implements BusinessEntity {

    private static final long serialVersionUID = 5734330182051890905L;

    private String schuetzenname;
    private long sportjahr1;
    private long sportjahr2;
    private long sportjahr3;
    private long sportjahr4;
    private long sportjahr5;
    private long allejahre_schnitt;

    @Override
    public String toString() {
        return "SchuetzenstatistikLetzteJahreBE{" +
                ", schuetzenname='" + schuetzenname +
                ", sportjahr1='" + sportjahr1 +
                ", sportjahr2='" + sportjahr2 +
                ", sportjahr3='" + sportjahr3 +
                ", sportjahr4='" + sportjahr4 +
                ", sportjahr5='" + sportjahr5 +
                ", allejahre_schnitt='" + allejahre_schnitt +
                '}';
    }

    public String getSchuetzenname() {
        return schuetzenname;
    }


    public void setSchuetzenname(String schuetzenname) {
        this.schuetzenname = schuetzenname;
    }


    public long getAllejahre_schnitt() {
        return allejahre_schnitt;
    }


    public void setAllejahre_schnitt(long allejahre_schnitt) {
        this.allejahre_schnitt = allejahre_schnitt;
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


    public void setSportjahr2(long sportjahr2) {
        this.sportjahr2 = sportjahr2;
    }


    public long getSportjahr4() {
        return sportjahr4;
    }


    public void setSportjahr4(long sportjahr4) {
        this.sportjahr4 = sportjahr4;
    }


    public long getSportjahr3() {
        return sportjahr3;
    }


    public void setSportjahr3(long sportjahr3) {
        this.sportjahr3 = sportjahr3;
    }


    public long getSportjahr5() {
        return sportjahr5;
    }


    public void setSportjahr5(long sportjahr5) {
        this.sportjahr5 = sportjahr5;
    }

}
