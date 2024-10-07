package de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.dao;

import de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.entity.SchuetzenstatistikWettkampfBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * DataAccessObject for the schuetzenstatistik entity in the database.
 * @author Anna Baur
 * & Alessa Hackh
 */
@Repository
public class SchuetzenstatistikWettkampfDAO implements DataAccessObject {

    private static final Logger LOG = LoggerFactory.getLogger(SchuetzenstatistikWettkampfDAO.class);

    // table name in the database
    private static final String TABLE = "schuetzenstatistik";
    // business entity parameter names


    private static final String DSBMITGLIEDNAME_BE = "dsbMitgliedName";
    private static final String RUECKENNUMMER_BE = "rueckenNummer";
    private static final String WETTKAMPFTAG1_BE = "wettkampftag1";
    private static final String WETTKAMPFTAG2_BE = "wettkampftag2";
    private static final String WETTKAMPFTAG3_BE = "wettkampftag3";
    private static final String WETTKAMPFTAG4_BE = "wettkampftag4";
    private static final String WETTKAMPFTAGESCHNITT_BE = "wettkampftageSchnitt";

    // for statistics for one archer in a season across ALL leagues
    private static final String SCHNITT_WKT1_BE = "schnitt_wettkampftage1";
    private static final String SCHNITT_WKT2_BE = "schnitt_wettkampftage2";
    private static final String SCHNITT_WKT3_BE = "schnitt_wettkampftage3";
    private static final String SCHNITT_WKT4_BE = "schnitt_wettkampftage4";
    private static final String SCHNITT_SPORTJAHR_BE = "schnitt_sportjahr";


    private static final String DSBMITGLIEDNAME_TABLE = "dsb_mitglied_name";
    private static final String RUECKENNUMMER_TABLE = "rueckennummer";
    private static final String WETTKAMPFTAG1_TABLE = "wettkampftag_1";
    private static final String WETTKAMPFTAG2_TABLE = "wettkampftag_2";
    private static final String WETTKAMPFTAG3_TABLE = "wettkampftag_3";
    private static final String WETTKAMPFTAG4_TABLE = "wettkampftag_4";
    private static final String WETTKAMPFTAGESCHNITT_TABLE = "veranstaltung_schnitt";
    private static final String GET_SCHUETZENSTATISTIK_WETTKAMPF =
            "SELECT " +
    "m.mannschaftsmitglied_rueckennummer AS rueckennummer, " +
    "pfeilschnitte_schuetze_veranstaltung.pfeilschnitte_schuetze_veranstaltung_schuetzenname AS dsb_mitglied_name, " +
    "pfeilschnitte_schuetze_veranstaltung.pfeilschnitte_schuetze_veranstaltung_wettkampftag_1_schnitt AS wettkampftag_1, " +
    "pfeilschnitte_schuetze_veranstaltung.pfeilschnitte_schuetze_veranstaltung_wettkampftag_2_schnitt AS wettkampftag_2, " +
    "pfeilschnitte_schuetze_veranstaltung.pfeilschnitte_schuetze_veranstaltung_wettkampftag_3_schnitt AS wettkampftag_3, " +
    "pfeilschnitte_schuetze_veranstaltung.pfeilschnitte_schuetze_veranstaltung_wettkampftag_4_schnitt AS wettkampftag_4, " +
    "pfeilschnitte_schuetze_veranstaltung_veranstaltung_schnitt AS veranstaltung_schnitt " +

    "FROM pfeilschnitte_schuetze_veranstaltung JOIN mannschaftsmitglied m ON pfeilschnitte_schuetze_veranstaltung_dsb_mitglied_id = m.mannschaftsmitglied_dsb_mitglied_id " +
            "WHERE " +
    "m.mannschaftsmitglied_mannschaft_id = pfeilschnitte_schuetze_veranstaltung_mannschaft_id " +
    "AND pfeilschnitte_schuetze_veranstaltung.pfeilschnitte_schuetze_veranstaltung_veranstaltung_schnitt > 0 " +
    "AND pfeilschnitte_schuetze_veranstaltung_veranstaltung_id = ? " +
    "AND pfeilschnitte_schuetze_veranstaltung.pfeilschnitte_schuetze_veranstaltung_verein_id = ? ;";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<SchuetzenstatistikWettkampfBE> SCHUETZENSTATISTIKWETTKAMPF = new BusinessEntityConfiguration<>(
            SchuetzenstatistikWettkampfBE.class, TABLE, getColumnsToFieldsMap(), LOG);
    private static final String GET_SCHUETZENSTATISTIK_WETTKAMPF_WETTKAMPF =
            "WITH wettkampfschnitte AS (" +
                    "SELECT" +
                    "    schuetzenstatistik_dsb_mitglied_id as dsb_mitglied_id," +
                    "    schuetzenstatistik_wettkampf_id as wettkampf_id," +
                    "    schuetzenstatistik_wettkampf_tag as wettkampf_tag," +
                    "    schuetzenstatistik_veranstaltung_id as veranstaltung_id," +
                    "    schuetzenstatistik_dsb_mitglied_name as dsb_mitglied_name," +
                    "    schuetzenstatistik_verein_id as verein_id," +
                    "    ROUND(AVG(CASE WHEN schuetzenstatistik_pfeilpunkte_schnitt <> 0 THEN schuetzenstatistik_pfeilpunkte_schnitt END), 2) AS wettkampf_schnitt" +
                    " FROM schuetzenstatistik" +
                    " GROUP BY schuetzenstatistik_dsb_mitglied_id, schuetzenstatistik_veranstaltung_id," +
                    "         schuetzenstatistik_wettkampf_id, schuetzenstatistik_wettkampf_tag," +
                    "         schuetzenstatistik_verein_id, schuetzenstatistik_dsb_mitglied_name)" +
                    " SELECT" +
                    "    MAX(wettkampfschnitte.dsb_mitglied_name)," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 1 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_1," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 2 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_2," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 3 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_3," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 4 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_4," +
                    "    ROUND(AVG(wettkampfschnitte.wettkampf_schnitt), 2) as veranstaltung_schnitt" +
                    " FROM wettkampfschnitte" +
                    " WHERE wettkampfschnitte.wettkampf_id =? AND wettkampfschnitte.verein_id =?"+
                    " GROUP BY wettkampfschnitte.dsb_mitglied_id, wettkampfschnitte.veranstaltung_id;" ;

    // for statistics for one archer in a season across ALL leagues
    private static final String GET_SCHUETZENSTATISTIK_ALLELIGEN =
            "WITH wettkampfschnitte AS (" +
                    "SELECT" +
                    "    schuetzenstatistik_dsb_mitglied_id as dsb_mitglied_id," +
                    "    schuetzenstatistik_wettkampf_tag as wettkampf_tag," +
                    "    schuetzenstatistik_dsb_mitglied_name as dsb_mitglied_name," +
                    "    schuetzenstatistik_verein_id as verein_id," +
                    "    veranstaltung_sportjahr as sportjahr," +
                    "    ROUND(AVG(CASE WHEN schuetzenstatistik_pfeilpunkte_schnitt <> 0 THEN schuetzenstatistik_pfeilpunkte_schnitt END), 2) AS wettkampf_schnitt" +
                    " FROM schuetzenstatistik JOIN veranstaltung on schuetzenstatistik.schuetzenstatistik_veranstaltung_id = veranstaltung.veranstaltung_id" +
                    " GROUP BY schuetzenstatistik_dsb_mitglied_id, veranstaltung_sportjahr, schuetzenstatistik.schuetzenstatistik_wettkampf_tag, dsb_mitglied_name, schuetzenstatistik_verein_id)" +
                    " SELECT" +
                    "    MAX(wettkampfschnitte.dsb_mitglied_name) as dsb_mitglied_name," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 1 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_1," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 2 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_2," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 3 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_3," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 4 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_4," +
                    "    ROUND(AVG(wettkampfschnitte.wettkampf_schnitt), 2) as wettkampftageSchnitt" +
                    " FROM wettkampfschnitte" +
                    " WHERE wettkampfschnitte.sportjahr = ? AND wettkampfschnitte.verein_id =?" +
                    " GROUP BY wettkampfschnitte.dsb_mitglied_id, wettkampfschnitte.sportjahr" +
                    " HAVING ROUND(AVG(wettkampfschnitte.wettkampf_schnitt), 2)  > 0;";

    private final BasicDAO basicDao;
    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public SchuetzenstatistikWettkampfDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(DSBMITGLIEDNAME_TABLE, DSBMITGLIEDNAME_BE);
        columnsToFieldsMap.put(RUECKENNUMMER_TABLE, RUECKENNUMMER_BE);
        columnsToFieldsMap.put(WETTKAMPFTAG1_TABLE, WETTKAMPFTAG1_BE);
        columnsToFieldsMap.put(WETTKAMPFTAG2_TABLE, WETTKAMPFTAG2_BE);
        columnsToFieldsMap.put(WETTKAMPFTAG3_TABLE, WETTKAMPFTAG3_BE);
        columnsToFieldsMap.put(WETTKAMPFTAG4_TABLE, WETTKAMPFTAG4_BE);
        columnsToFieldsMap.put(WETTKAMPFTAGESCHNITT_TABLE, WETTKAMPFTAGESCHNITT_BE);

        return columnsToFieldsMap;
    }

    /**
     * Lesen der aktuellen Liga-Tabelle zur Veranstaltung
     */
    public List<SchuetzenstatistikWettkampfBE> getSchuetzenstatistikWettkampfVeranstaltung(final long veranstaltungId, final long vereinId) {
        return basicDao.selectEntityList(SCHUETZENSTATISTIKWETTKAMPF, GET_SCHUETZENSTATISTIK_WETTKAMPF, veranstaltungId, vereinId);
    }

    /**
     * Lesen der aktuellen Schuetzenstatistik zum Wettkampf (ID)
     */
    public List<SchuetzenstatistikWettkampfBE> getSchuetzenstatistikWettkampf(final long wettkampfId, final long vereinId) {
        return basicDao.selectEntityList(SCHUETZENSTATISTIKWETTKAMPF, GET_SCHUETZENSTATISTIK_WETTKAMPF_WETTKAMPF, wettkampfId, vereinId);
    }

    /**
     * Statistik des gewählten Sportjahres erhalten
     */
    public List<SchuetzenstatistikWettkampfBE> getSchuetzenstatistikAlleLigen(final long sportjahr, final long vereinId) {
        return basicDao.selectEntityList(SCHUETZENSTATISTIKWETTKAMPF, GET_SCHUETZENSTATISTIK_ALLELIGEN, sportjahr, vereinId);
    }
}
