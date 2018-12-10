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
    private Long klasseJahrgangMin;
    private Long klasseJahrgangMax;
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


    public Long getKlasseJahrgangMin() {
        return klasseJahrgangMin;
    }


    public void setKlasseJahrgangMin(Long klasseJahrgangMin) {
        this.klasseJahrgangMin = klasseJahrgangMin;
    }


    public Long getKlasseJahrgangMax() {
        return klasseJahrgangMax;
    }


    public void setKlasseJahrgangMax(Long klasseJahrgangMax) {
        this.klasseJahrgangMax = klasseJahrgangMax;
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
                ", klasseJahrgangMin = '" + klasseJahrgangMin + '\'' +
                ", klasseJahrgangMax = '" + klasseJahrgangMax + '\'' +
                ", klasseNr = '" + klasseNr +
                '}';
    }



}