package de.bogenliga.application.business.mannschaftsmitglied.impl.entity;

/**
 * extends ManschaftsmitgliedBE by fore- and surname from dsb-Mitglied
 */
public class MannschaftsmitgliedExtendedBE extends MannschaftsmitgliedBE {

    private String dsbMitgliedVorname;
    private String dsbMitgliedNachname;

    public MannschaftsmitgliedExtendedBE() {
    }

    // IMPORTANT
    // Methods from Base class need to be overwritten because of the BasicTest-class
    // the BasicTest-class does not recognize that there are inherited methods
    // this results into failure within running the unit-tests
    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public Long getMannschaftId() {
        return this.mannschaftId;
    }

    @Override
    public void setMannschaftId(Long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }


    @Override
    public Long getDsbMitgliedId() {
        return this.dsbMitgliedId;
    }


    @Override
    public void setDsbMitgliedId(Long dsbMitgliedId) {
        this.dsbMitgliedId = dsbMitgliedId;
    }


    @Override
    public Integer getDsbMitgliedEingesetzt() {
        return this.dsbMitgliedEingesetzt;
    }


    @Override
    public void setDsbMitgliedEingesetzt(Integer dsbMitgliedEingesetzt) {
        this.dsbMitgliedEingesetzt = dsbMitgliedEingesetzt;
    }


    @Override
    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public Long getRueckennummer() {
        return this.rueckennummer;
    }


    @Override
    public void setRueckennummer(Long rueckennummer) {
        this.rueckennummer = rueckennummer;
    }


    public String getDsbMitgliedVorname() {
        return dsbMitgliedVorname;
    }

    public void setDsbMitgliedVorname(String dsbMitgliedVorname) {
        this.dsbMitgliedVorname = dsbMitgliedVorname;
    }

    public String getDsbMitgliedNachname() {
        return dsbMitgliedNachname;
    }

    public void setDsbMitgliedNachname(String dsbMitgliedNachname) {
        this.dsbMitgliedNachname = dsbMitgliedNachname;
    }

    @Override
    public String toString() {
        return "MannschaftsmitgliedExtendedBE{" +
                "id=" + id +
                "mannschaftId=" + mannschaftId +
                ", dsbMitgliedId=" + dsbMitgliedId +
                ", dsbMitgliedEingesetzt=" + dsbMitgliedEingesetzt +
                ", dsbMitgliedVorname=" + dsbMitgliedVorname +
                ", dsbMitgliedNachname=" + dsbMitgliedNachname +
                ", rueckennummer=" + rueckennummer +
                '}';
    }
}
