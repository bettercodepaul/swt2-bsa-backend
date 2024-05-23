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
 */
@Repository
public class SchuetzenstatistikWettkampfDAO implements DataAccessObject {

    private static final Logger LOG = LoggerFactory.getLogger(SchuetzenstatistikWettkampfDAO.class);

    // table name in the database
    private static final String TABLE = "schuetzenstatistik";
    // business entity parameter names
    private static final String VERANSTALTUNGID_BE = "veranstaltungId";
    private static final String WETTKAMPFID_BE = "wettkampfId";
    private static final String WETTKAMPFTAG_BE = "wettkampfTag";
    private static final String VEREINID_BE = "vereinId";
    private static final String DSBMITGLIEDID_BE = "dsbMitgliedId";
    private static final String DSBMITGLIEDNAME_BE = "dsbMitgliedName";
    private static final String RUECKENNUMMER_BE = "rueckenNummer";
    private static final String WETTKAMPFTAG1_BE = "wettkampftag1";
    private static final String WETTKAMPFTAG2_BE = "wettkampftag2";
    private static final String WETTKAMPFTAG3_BE = "wettkampftag3";
    private static final String WETTKAMPFTAG4_BE = "wettkampftag4";
    private static final String WETTKAMPFTAGESCHNITT_BE = "wettkampftageSchnitt";


    private static final String VERANSTALTUNGID_TABLE = "schuetzenstatistik_veranstaltung_id";
    private static final String WETTKAMPFID_TABLE = "schuetzenstatistik_wettkampf_id";
    private static final String WETTKAMPFTAG_TABLE = "schuetzenstatistik_wettkampf_tag";
    private static final String VEREINID_TABLE = "schuetzenstatistik_verein_id";
    private static final String DSBMITGLIEDID_TABLE = "schuetzenstatistik_dsb_mitglied_id";
    private static final String DSBMITGLIEDNAME_TABLE = "schuetzenstatistik_dsb_mitglied_name";
    private static final String RUECKENNUMMER_TABLE = "schuetzenstatistik_rueckennummer";
    private static final String WETTKAMPFTAG1_TABLE = "wettkampftag_1";
    private static final String WETTKAMPFTAG2_TABLE = "wettkampftag_2";
    private static final String WETTKAMPFTAG3_TABLE = "wettkampftag_3";
    private static final String WETTKAMPFTAG4_TABLE = "wettkampftag_4";
    private static final String WETTKAMPFTAGESCHNITT_TABLE = "wettkampftage_schnitt";


    private static final String GET_SCHUETZENSTATISTIK_WETTKAMPF =
            "WITH wettkampfschnitte AS (" +
                    "SELECT" +
                    "    schuetzenstatistik_dsb_mitglied_id as dsb_mitglied_id," +
                    "    schuetzenstatistik_wettkampf_id as wettkampf_id," +
                    "    schuetzenstatistik_wettkampf_tag as wettkampf_tag," +
                    "    schuetzenstatistik_veranstaltung_id as veranstaltung_id," +
                    "    schuetzenstatistik_dsb_mitglied_name as dsb_mitglied_name," +
                    "    schuetzenstatistik_verein_id as verein_id," +
                    "    ROUND(AVG(CASE WHEN schuetzenstatistik_pfeilpunkte_schnitt <> 0 THEN schuetzenstatistik_pfeilpunkte_schnitt END), 2) AS wettkampf_schnitt" +
                    "FROM schuetzenstatistik" +
                    "GROUP BY schuetzenstatistik_dsb_mitglied_id, schuetzenstatistik_veranstaltung_id," +
                    "         schuetzenstatistik_wettkampf_id, schuetzenstatistik_wettkampf_tag," +
                    "         schuetzenstatistik_verein_id, schuetzenstatistik_dsb_mitglied_name)" +
                    "SELECT" +
                    "    MAX(wettkampfschnitte.dsb_mitglied_name)," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 1 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_1," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 2 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_2," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 3 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_3," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 4 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_4," +
                    "    ROUND(AVG(wettkampfschnitte.wettkampf_schnitt), 2) as veranstaltung_schnitt" +
                    "FROM wettkampfschnitte" +
                    "WHERE wettkampfschnitte_veranstaltung_id =? AND wettkampfschnitte_verein_id =?"+
                    "GROUP BY wettkampfschnitte.dsb_mitglied_id, wettkampfschnitte.veranstaltung_id;";


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
                    "FROM schuetzenstatistik" +
                    "GROUP BY schuetzenstatistik_dsb_mitglied_id, schuetzenstatistik_veranstaltung_id," +
                    "         schuetzenstatistik_wettkampf_id, schuetzenstatistik_wettkampf_tag," +
                    "         schuetzenstatistik_verein_id, schuetzenstatistik_dsb_mitglied_name)" +
                    "SELECT" +
                    "    MAX(wettkampfschnitte.dsb_mitglied_name)," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 1 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_1," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 2 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_2," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 3 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_3," +
                    "    MAX(CASE WHEN  wettkampfschnitte.wettkampf_tag = 4 THEN wettkampfschnitte.wettkampf_schnitt ELSE 0 END) as wettkampftag_4," +
                    "    ROUND(AVG(wettkampfschnitte.wettkampf_schnitt), 2) as veranstaltung_schnitt" +
                    "FROM wettkampfschnitte" +
                    "WHERE wettkampfschnitte_wettkampf_id =? AND wettkampfschnitte_verein_id =?"+
                    "GROUP BY wettkampfschnitte.dsb_mitglied_id, wettkampfschnitte.veranstaltung_id;";



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

        columnsToFieldsMap.put(VERANSTALTUNGID_TABLE, VERANSTALTUNGID_BE);
        columnsToFieldsMap.put(WETTKAMPFID_TABLE, WETTKAMPFID_BE);
        columnsToFieldsMap.put(WETTKAMPFTAG_TABLE, WETTKAMPFTAG_BE);
        columnsToFieldsMap.put(VEREINID_TABLE, VEREINID_BE);
        columnsToFieldsMap.put(DSBMITGLIEDID_TABLE, DSBMITGLIEDID_BE);
        columnsToFieldsMap.put(DSBMITGLIEDNAME_TABLE, DSBMITGLIEDNAME_BE);
        columnsToFieldsMap.put(RUECKENNUMMER_TABLE, RUECKENNUMMER_BE);
        columnsToFieldsMap.put(WETTKAMPFTAG1_TABLE, WETTKAMPFTAG1_BE);
        columnsToFieldsMap.put(WETTKAMPFTAG2_TABLE, WETTKAMPFTAG2_BE);
        columnsToFieldsMap.put(WETTKAMPFTAG3_TABLE, WETTKAMPFTAG3_BE);
        columnsToFieldsMap.put(WETTKAMPFTAG4_TABLE,WETTKAMPFTAG4_BE);
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
}
