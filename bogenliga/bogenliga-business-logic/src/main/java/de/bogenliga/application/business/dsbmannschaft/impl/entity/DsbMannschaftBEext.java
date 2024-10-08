package de.bogenliga.application.business.dsbmannschaft.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

public class DsbMannschaftBEext extends DsbMannschaftBE implements BusinessEntity {

    private static final long serialVersionUID = -6431886856322437597L;
   private String veranstaltungName;
    private String wettkampfTag;
    private String wettkampfOrtsname;
    private String vereinName;


    public DsbMannschaftBEext()  {/*empty constructor*/}
    /**
     * Constructor with mandatory parameters
     * @param veranstaltungName
     * @param wettkampfTag
     * @param wettkampfOrtsname
     * @param vereinName
     */
    public DsbMannschaftBEext(final Long id, final Long vereinId, final Long nummer, final Long veranstaltungId,
                              final Long benutzerId, final Long sortierung, final Long sportjahr, final String veranstaltungName,
                              final String wettkampfTag, final String wettkampfOrtsname, final String vereinName) {
        super(id, vereinId, nummer, veranstaltungId, benutzerId, sortierung, sportjahr);
        this.veranstaltungName = veranstaltungName;
        this.wettkampfTag = wettkampfTag;
        this.wettkampfOrtsname = wettkampfOrtsname;
        this.vereinName = vereinName;
    }


    public String getVeranstaltungName() {
        return veranstaltungName;
    }

    public void setVeranstaltungName(final String veranstaltungName) {
        this.veranstaltungName = veranstaltungName;
    }

    public String getWettkampfTag() {
        return wettkampfTag;
    }

    public void setWettkampfTag(final String wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }

    public String getWettkampfOrtsname() {
        return wettkampfOrtsname;
    }

    public void setWettkampfOrtsname(final String wettkampfOrtsname) {
        this.wettkampfOrtsname = wettkampfOrtsname;
    }

    public String getVereinName() {
        return vereinName;
    }

    public void setVereinName(final String vereinName) {
        this.vereinName = vereinName;
    }



    @Override
    public String toString() {
        return "DsbMannschaftBEext{" +
                ", VeranstaltungName='" + veranstaltungName + '\'' +
                ", WettkampfTag='" + wettkampfTag + '\'' +
                ", WettkampfOrtsname='" + wettkampfOrtsname + '\'' +
                ", VereinName='" + vereinName + '\'' +
                '}';
    }
}
