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

    private Long veranstaltungId;
    private String veranstaltungName;
    private Long wettkampfId;
    private int wettkampfTag;
    private Long mannschaftId;
    private int mannschaftNummer;
    private Long vereinId;
    private String vereinName;
    private int matchpkt;
    private int matchpkt_gegen;
    private int satzpkt;
    private int satzpkt_gegen;
    private int satzpkt_differenz;
    private int sortierung;
    private int tabellenplatz;



    /**
     * Constructor
     */
    public LigatabelleBE() {
        // empty
    }

    /** hier der select um alle übergeordneten Ligen zu einer Liga zu finden:
     * with recursive cte AS (
     * 	select liga_id, liga_uebergeordnet, 1 as level
     * 	from liga
     *
     * 	union all
     * 	select t.liga_id, c.liga_uebergeordnet, c.level +1
     * 	from cte  c
     * 	join liga t on t.liga_uebergeordnet = c.liga_id
     * 	)select * from cte where liga_uebergeordnet notnull;
     *
     * 	beim select am Ende noch einschränken auf die gesuchte liga_id
     *
     *
     */

    /*
     *
     *
     * @return
     */


    @Override
    public String toString() {
        return "LigatabelleBE{" +
                "veranstaltungId=" + veranstaltungId +
                ", veranstaltungName=" + veranstaltungId +
                ", wettkampfId='" + wettkampfId +
                ", wettkampfTag='" + wettkampfTag +
                ", mannschaftId='" + mannschaftId +
                ", mannschaftNummer='" + mannschaftNummer +
                ", vereinId='" + vereinId +
                ", vereinName='" + vereinName +
                ", matchpkt='" + matchpkt + '\'' +
                ", matchpkt_gegen='" + matchpkt_gegen +
                ", satzpkt='" + satzpkt +
                ", satzpkt_gegen='" + satzpkt_gegen +
                ", satzpkt_differenz='" + satzpkt_differenz +
                ", sortierung='" + sortierung +
                ", tabellenplatz='" + tabellenplatz +
                '}';
    }


    public long getVeranstaltungId() {
        return veranstaltungId;
    }
    public void setVeranstaltungId(final long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }

    public String getVeranstaltungName() {
        return veranstaltungName;
    }
    public void setVeranstaltungName(final String veranstaltungName) {
        this.veranstaltungName = veranstaltungName;
    }

    public long getWettkampfId() {
        return wettkampfId;
    }
    public void setWettkampfId(final long wettkampfId) {
        this.wettkampfId = wettkampfId;
    }

    public int getWettkampfTag() {
        return wettkampfTag;
    }
    public void setWettkampfTag(final int wettkampfTag) {
        this.wettkampfTag = wettkampfTag;
    }

    public long getMannschaftId() {
        return mannschaftId;
    }
    public void setMannschaftId(final long mannschaftId) {
        this.mannschaftId = mannschaftId;
    }

    public int getMannschaftNummer() {
        return mannschaftNummer;
    }
    public void setMannschaftNummer(final int mannschaftNummer) {
        this.mannschaftNummer = mannschaftNummer;
    }

    public long getVereinId() {
        return vereinId;
    }
    public void setVereinId(final long vereinId) {
        this.vereinId = vereinId;
    }

    public String getVereinName() {
        return vereinName;
    }
    public void setVereinName(final String vereinName) {
        this.vereinName = vereinName;
    }

    public int getMatchpkt() {
        return matchpkt;
    }
    public void setMatchpkt(final int matchpkt) {
        this.matchpkt = matchpkt;
    }

    public int getMatchpkt_gegen() {
        return matchpkt_gegen;
    }
    public void setMatchpkt_gegen(final int matchpkt_gegen) {
        this.matchpkt_gegen = matchpkt_gegen;
    }

    public int getSatzpkt() {
        return satzpkt;
    }
    public void setSatzpkt(final int satzpkt) {
        this.satzpkt = satzpkt;
    }

    public int getSatzpkt_gegen() {
        return satzpkt_gegen;
    }
    public void setSatzpkt_gegen(final int satzpkt_gegen) {
        this.satzpkt_gegen = satzpkt_gegen;
    }

    public int getSatzpkt_differenz() {
        return satzpkt_differenz;
    }
    public void setSatzpkt_differenz(final int satzpkt_differenz) {
        this.satzpkt_differenz = satzpkt_differenz;
    }

    public int getSortierung() {
        return sortierung;
    }
    public void setSortierung(final int sortierung) {
        this.sortierung = sortierung;
    }

    public int getTabellenplatz() {
        return tabellenplatz;
    }
    public void setTabellenplatz(final int tabellenplatz) {
        this.tabellenplatz = tabellenplatz;
    }







}
