package de.bogenliga.application.business.altsystem.wettkampfdaten.dataobject;

import de.bogenliga.application.common.altsystem.AltsystemDO;

/**
 * Bean to store the data of the entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemWettkampfdatenDO extends AltsystemDO {


    private long id;
    private int ligaID;
    private int mannschaft;
    private int gegner;
    private int match;
    private int satzPlus;
    private int satzMinus;
    private int satz1;
    private int satz2;
    private int satz3;
    private int satz4;
    private int satz5;
    private int saisonID;
    private int sec;


    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public int getLigaID() {
        return ligaID;
    }

    public void setLigaID(int ligaID) {
        this.ligaID = ligaID;
    }

    public int getMannschaft() {
        return mannschaft;
    }

    public void setMannschaft(int mannschaft) {
        this.mannschaft = mannschaft;
    }

    public int getGegner() {
        return gegner;
    }

    public void setGegner(int gegner) {
        this.gegner = gegner;
    }

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }

    public int getSatzPlus() {
        return satzPlus;
    }

    public void setSatzPlus(int satzPlus) {
        this.satzPlus = satzPlus;
    }

    public int getSatzMinus() {
        return satzMinus;
    }

    public void setSatzMinus(int satzMinus) {
        this.satzMinus = satzMinus;
    }

    public int getSatz1() {
        return satz1;
    }

    public void setSatz1(int satz1) {
        this.satz1 = satz1;
    }

    public int getSatz2() {
        return satz2;
    }

    public void setSatz2(int satz2) {
        this.satz2 = satz2;
    }

    public int getSatz3() {
        return satz3;
    }

    public void setSatz3(int satz3) {
        this.satz3 = satz3;
    }

    public int getSatz4() {
        return satz4;
    }

    public void setSatz4(int satz4) {
        this.satz4 = satz4;
    }

    public int getSatz5() {
        return satz5;
    }

    public void setSatz5(int satz5) {
        this.satz5 = satz5;
    }

    public int getSaisonID() {
        return saisonID;
    }

    public void setSaisonID(int saisonID) {
        this.saisonID = saisonID;
    }

    public int getSec() {
        return sec;
    }


    public void setSec(int sec) {
        this.sec = sec;
    }


}