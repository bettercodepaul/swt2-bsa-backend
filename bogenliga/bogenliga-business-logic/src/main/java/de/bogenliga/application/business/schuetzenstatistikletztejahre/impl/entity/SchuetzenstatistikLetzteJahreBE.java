package de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * @author Alessa Hackh
 */
public class SchuetzenstatistikLetzteJahreBE extends CommonBusinessEntity implements BusinessEntity {

    private static final long serialVersionUID = 5734330182051890905L;

    private String schuetzenname;
    private float sportjahr1;
    private float sportjahr2;
    private float sportjahr3;
    private float sportjahr4;
    private float sportjahr5;
    private float allejahre_schnitt;

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


    public float getAllejahre_schnitt() {
        return allejahre_schnitt;
    }


    public void setAllejahre_schnitt(float allejahre_schnitt) {
        this.allejahre_schnitt = allejahre_schnitt;
    }


    public float getSportjahr1() {
        return sportjahr1;
    }


    public void setSportjahr1(float sportjahr1) {
        this.sportjahr1 = sportjahr1;
    }


    public float getSportjahr2() {
        return sportjahr2;
    }


    public void setSportjahr2(float sportjahr2) {
        this.sportjahr2 = sportjahr2;
    }


    public float getSportjahr4() {
        return sportjahr4;
    }


    public void setSportjahr4(float sportjahr4) {
        this.sportjahr4 = sportjahr4;
    }


    public float getSportjahr3() {
        return sportjahr3;
    }


    public void setSportjahr3(float sportjahr3) {
        this.sportjahr3 = sportjahr3;
    }


    public float getSportjahr5() {
        return sportjahr5;
    }


    public void setSportjahr5(float sportjahr5) {
        this.sportjahr5 = sportjahr5;
    }

}
