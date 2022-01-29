package de.bogenliga.application.business.mannschaftsmitglied.impl.entity;

/**
 * extends ManschaftsmitgliedBE by fore- and surname from dsb-Mitglied
 */
public class MannschaftsmitgliedLigaBE extends MannschaftsmitgliedBE {

    private long ligaId;
    private long mannschaftsId;
    private long wettkampftag;


    public MannschaftsmitgliedLigaBE() {
    }

    public long getLigaId() {
        return ligaId;
    }


    public void setLigaId(long ligaId) {
        this.ligaId = ligaId;
    }


    public long getMannschaftsId() {
        return mannschaftsId;
    }


    public void setMannschaftsId(long mannschaftsId) {
        this.mannschaftsId = mannschaftsId;
    }


    public long getWettkampftag() {
        return wettkampftag;
    }


    public void setWettkampftag(long wettkampftag) {
        this.wettkampftag = wettkampftag;
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



    @Override
    public String toString() {
        return "MannschaftsmitgliedExtendedBE{" +
                "id=" + id +
                "mannschaftId=" + mannschaftId +
                ", dsbMitgliedId=" + dsbMitgliedId +
                ", dsbMitgliedEingesetzt=" + dsbMitgliedEingesetzt +
                ", rueckennummer=" + rueckennummer +
                '}';
    }
}
