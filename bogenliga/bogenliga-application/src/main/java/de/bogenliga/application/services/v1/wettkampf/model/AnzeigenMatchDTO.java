package de.bogenliga.application.services.v1.wettkampf.model;

import de.bogenliga.application.common.service.types.DataTransferObject;

public class AnzeigenMatchDTO implements DataTransferObject {
    private static final long serialVersionUID = 5036117832594594995L;

    private int matchNr;
    private String verein1;
    private String verein2;
    private Integer[] schuesseVerein1;
    private Integer[] schuesseVerein2;
    private int totalVerein1;
    private int totalVerein2;
    private int satzpunkteVerein1;
    private int satzpunkteVerein2;

    public AnzeigenMatchDTO() {
        this.matchNr = 0;
        this.verein1 = "";
        this.verein2 = "";
        this.schuesseVerein1 = null;
        this.schuesseVerein2 = null;
        this.totalVerein1 = 0;
        this.totalVerein2 = 0;
        this.satzpunkteVerein1 = 0;
        this.satzpunkteVerein2 = 0;
    }

    public AnzeigenMatchDTO(int matchNr, String verein1, String verein2, Integer[] schuesseVerein1, Integer[] schuesseVerein2, int totalVerein1, int totalVerein2, int satzpunkteVerein1, int satzpunkteVerein2) {
        this.matchNr = matchNr;
        this.verein1 = verein1;
        this.verein2 = verein2;
        this.schuesseVerein1 = schuesseVerein1;
        this.schuesseVerein2 = schuesseVerein2;
        this.totalVerein1 = totalVerein1;
        this.totalVerein2 = totalVerein2;
        this.satzpunkteVerein1 = satzpunkteVerein1;
        this.satzpunkteVerein2 = satzpunkteVerein2;
    }

    public int getMatchNr() {
        return matchNr;
    }

    public void setMatchNr(int matchNr) {
        this.matchNr = matchNr;
    }

    public String getVerein1() {
        return verein1;
    }

    public void setVerein1(String verein1) {
        this.verein1 = verein1;
    }

    public String getVerein2() {
        return verein2;
    }

    public void setVerein2(String verein2) {
        this.verein2 = verein2;
    }

    public Integer[] getSchuesseVerein1() {
        return schuesseVerein1;
    }

    public void setSchuesseVerein1(Integer[] schuesseVerein1) {
        this.schuesseVerein1 = schuesseVerein1;
    }

    public Integer[] getSchuesseVerein2() {
        return schuesseVerein2;
    }

    public void setSchuesseVerein2(Integer[] schuesseVerein2) {
        this.schuesseVerein2 = schuesseVerein2;
    }

    public int getTotalVerein1() {
        return totalVerein1;
    }

    public void setTotalVerein1(int totalVerein1) {
        this.totalVerein1 = totalVerein1;
    }

    public int getTotalVerein2() {
        return totalVerein2;
    }

    public void setTotalVerein2(int totalVerein2) {
        this.totalVerein2 = totalVerein2;
    }

    public int getSatzpunkteVerein1() {
        return satzpunkteVerein1;
    }

    public void setSatzpunkteVerein1(int satzpunkteVerein1) {
        this.satzpunkteVerein1 = satzpunkteVerein1;
    }

    public int getSatzpunkteVerein2() {
        return satzpunkteVerein2;
    }

    public void setSatzpunkteVerein2(int satzpunkteVerein2) {
        this.satzpunkteVerein2 = satzpunkteVerein2;
    }

}
