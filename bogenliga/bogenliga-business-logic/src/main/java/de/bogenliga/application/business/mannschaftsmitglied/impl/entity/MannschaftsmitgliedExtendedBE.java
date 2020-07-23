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
        return super.getId();
    }

    @Override
    public Long getMannschaftId() {
        return super.getMannschaftId();
    }


    @Override
    public void setMannschaftId(Long mannschaftId) {
        super.setMannschaftId(mannschaftId);
    }


    @Override
    public Long getDsbMitgliedId() {
        return super.getDsbMitgliedId();
    }


    @Override
    public void setDsbMitgliedId(Long dsbMitgliedId) {
        super.setDsbMitgliedId(dsbMitgliedId);
    }


    @Override
    public Integer getDsbMitgliedEingesetzt() {
        return super.getDsbMitgliedEingesetzt();
    }


    @Override
    public void setDsbMitgliedEingesetzt(Integer dsbMitgliedEingesetzt) {
        super.setDsbMitgliedEingesetzt(dsbMitgliedEingesetzt);
    }


    @Override
    public void setId(Long id) {
        super.setId(id);
    }


    @Override
    public Long getRueckennummer() {
        return super.getRueckennummer();
    }


    @Override
    public void setRueckennummer(Long rueckennummer) {
        super.setRueckennummer(rueckennummer);
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
