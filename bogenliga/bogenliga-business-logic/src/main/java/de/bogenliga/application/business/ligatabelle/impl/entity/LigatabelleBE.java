package de.bogenliga.application.business.ligatabelle.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I´m a composed business entity of the user and the permission business entity.
 *
 * The user permissions are resolved with a JOIN via the user roles, roles, role permissions and permissions.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class LigatabelleBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -7930719922483666804L;

    private long veranstaltungId;
    private int wettkampfTag;
    private long mannschaftId;
    private int mannschaftNummer;
    private int tabellenplatz;
    private int matchpkt;
    private int matchpkt_gegen;
    private int satzpkt;
    private int satzpkt_gegen;
    private long vereinId;
    private String vereinName;



    /**
     * Constructor
     */
    public LigatabelleBE() {
        // empty
    }

    @Override
    public String toString() {
        return "LigatabelleBE{" +
                "veranstaltungId=" + veranstaltungId +
                ", wettkampfTag='" + wettkampfTag +
                ", mannschaftId='" + mannschaftId +
                ", mannschaftNummer='" + mannschaftNummer +
                ", tabellenplatz='" + tabellenplatz +
                ", matchpkt='" + matchpkt + '\'' +
                ", matchpkt_gegen='" + matchpkt_gegen +
                ", satzpkt='" + satzpkt +
                ", satzpkt_gegen='" + satzpkt_gegen +
                ", vereinId='" + vereinId +
                ", vereinName='" + vereinName + '\'' +
                '}';
    }


    public long getVeranstaltungId() {
        return veranstaltungId;
    }

    public void setVeranstaltungId(final long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }


    public long getMannschaftId() { return mannschaftId; }

    public void setMannschaftId(final long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }


    public long getVereinId() {
        return vereinId;
    }

    public void setVereinId(final long vereinId) {
        this.vereinId = vereinId;
    }


}
