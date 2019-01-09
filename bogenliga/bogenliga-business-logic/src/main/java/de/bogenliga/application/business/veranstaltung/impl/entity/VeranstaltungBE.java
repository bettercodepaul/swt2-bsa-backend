package de.bogenliga.application.business.veranstaltung.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

public class VeranstaltungBE extends CommonBusinessEntity implements BusinessEntity {

    private Long id;
    private Long wettkampftyp;
    private String name;
    private Long jahr;
    private Long deadline;
    private Long ligaleiterId;

    public VeranstaltungBE(){
        //empty constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public  Long getWettkampftyp() {
        return  wettkampftyp;
    }

    public void setWettkampftyp(Long wettkampftyp) {
        this.wettkampftyp = wettkampftyp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Long getJahr() {
        return jahr;
    }


    public void setJahr(Long jahr) {
        this.jahr = jahr;
    }


    public Long getDeadline() {
        return deadline;
    }


    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }


    public Long getLigaleiterId() {
        return ligaleiterId;
    }


    public void setLigaleiterId(Long ligaleiterId) {
        this.ligaleiterId = ligaleiterId;
    }

    @Override
    public String toString(){

        return "VeranstaltungBE {" +
                "id = " + id + '\'' +
                ", wettkampftyp = '" + wettkampftyp + '\'' +
                ", name = '" + name + '\'' +
                ", jahr = '" + jahr + '\'' +
                ", deadline = '" + deadline +
                ", ligaleiterId = '" + ligaleiterId +
                '}';
    }
}
