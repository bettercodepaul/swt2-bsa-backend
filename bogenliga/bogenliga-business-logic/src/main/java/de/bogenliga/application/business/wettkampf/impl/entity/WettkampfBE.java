package de.bogenliga.application.business.wettkampf.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;
/**
 * I represent the wettkampf business entity.
 * <p>
 * A wettkampf is a registered member of the DSB. The wettkampf is not necessarily a technical user of the system.
 * <p>
 * The {@link CommonBusinessEntity} contains the technical parameter. Business entities commonly use these parameters to
 * control their lifecycle.
 *
 * @see CommonBusinessEntity
 */
public class WettkampfBE extends CommonBusinessEntity implements BusinessEntity {

    private Long id;
    private Long veranstaltungsId;
    private String datum;
    private String wettkampfOrt;
    private String wettkampfBeginn;
    private Long wettkampfTag;
    private Long wettkampfDisziplinId;
    private Long wettkampfTypId;

    public WettkampfBE(){

    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getVeranstaltungsId() {
        return veranstaltungsId;
    }


    public void setVeranstaltungsId(Long veranstaltungsId) {
        this.veranstaltungsId = veranstaltungsId;
    }


    public String getDatum() {
        return datum;
    }


    public void setDatum(String datum) {
        this.datum = datum;
    }


    public String getWettkampfOrt() {
        return wettkampfOrt;
    }


    public void setWettkampfOrt(String wettkampfOrt) {
        this.wettkampfOrt = wettkampfOrt;
    }


    public String getWettkampfBeginn() {
        return wettkampfBeginn;
    }


    public void setWettkampfBeginn(String wettkampfBeginn) {
        this.wettkampfBeginn = wettkampfBeginn;
    }


    public Long getWettkampfTag() {
        return wettkampfTag;
    }


    public void setWettkampfTag(Long wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }


    public Long getWettkampfDisziplinId() {
        return wettkampfDisziplinId;
    }


    public void setWettkampfDisziplinId(Long wettkampfDisziplinId) {
        this.wettkampfDisziplinId = wettkampfDisziplinId;
    }


    public Long getWettkampfTypId() {
        return wettkampfTypId;
    }


    public void setWettkampfTypId(Long wettkampfTypId) {
        this.wettkampfTypId = wettkampfTypId;
    }

    @Override
    public String toString(){
        return "Wettkampf ID = " + this.getId()+ "\n"+
                "Veranstaltungs ID =  " + this.getVeranstaltungsId() + "\n" +
                "Datum = " + this.getDatum() + "\n" +
                "Wettkampf Ort = " + this.getWettkampfOrt() + "\n" +
                "Wettkampfbeginn = " + this.getWettkampfBeginn() + "\n" +
                "Wettkampftag = " + this.getWettkampfTag() + "\n" +
                "Wettkampfdiziplin ID = " + this.getWettkampfDisziplinId() + "\n" +
                "Wettkampftyp ID = " + this.getWettkampfTypId();
    }
}
