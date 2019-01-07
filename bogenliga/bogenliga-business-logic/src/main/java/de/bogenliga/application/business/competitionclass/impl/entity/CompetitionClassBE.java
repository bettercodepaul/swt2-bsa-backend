package de.bogenliga.application.business.competitionclass.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the klassen business entity.
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class CompetitionClassBE extends CommonBusinessEntity implements BusinessEntity {
    private Long klasseId;
    private String klasseName;
    private Long klasseAlterMin;
    private Long klasseAlterMax;
    private Long klasseNr;


    public CompetitionClassBE() {
        //empty constructor
    }

    public Long getKlasseId() {
        return klasseId;
    }

    public void setKlasseId(Long klasseId) {
        this.klasseId = klasseId;
    }

    public String getKlasseName() {
        return klasseName;
    }


    public void setKlasseName(String klasseName) {
        this.klasseName = klasseName;
    }


    public Long getKlasseAlterMin() {
        return klasseAlterMin;
    }


    public void setKlasseAlterMin(Long klasseAlterMin) {
        this.klasseAlterMin = klasseAlterMin;
    }


    public Long getKlasseAlterMax() {
        return klasseAlterMax;
    }


    public void setKlasseAlterMax(Long klasseAlterMax) {
        this.klasseAlterMax = klasseAlterMax;
    }


    public Long getKlasseNr() {
        return klasseNr;
    }


    public void setKlasseNr(Long klasseNr) {
        this.klasseNr = klasseNr;
    }


    @Override
    public String toString(){

        return "CompetitionClassBE {" +
                "klasseId = " + klasseId + '\'' +
                ", klasseName = '" + klasseName + '\'' +
                ", klasseAlterMin = '" + klasseAlterMin + '\'' +
                ", klasseAlterMax = '" + klasseAlterMax + '\'' +
                ", klasseNr = '" + klasseNr +
                '}';
    }



}