package de.bogenliga.application.business.schuetzenstatistikmatch.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.schuetzenstatistikmatch.impl.entity.SchuetzenstatistikMatchBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class SchuetzenstatistikMatchDAO implements DataAccessObject {
    // define the logger context
    private static final Logger LOG = LoggerFactory.getLogger(SchuetzenstatistikMatchDAO.class);

    // table name in the database
    private static final String TABLE = "schuetzenstatistik";
    // business entity parameter names

    private static final String DSBMITGLIEDNAME_BE = "dsbMitgliedName";
    private static final String RUECKENNUMMER_BE = "rueckenNummer";
    private static final String MATCHNR_1_BE = "matchnr1";
    private static final String MATCHNR_2_BE = "matchnr2";
    private static final String MATCHNR_3_BE = "matchnr3";
    private static final String MATCHNR_4_BE = "matchnr4";
    private static final String MATCHNR_5_BE = "matchnr5";
    private static final String MATCHNR_6_BE = "matchnr6";
    private static final String MATCHNR_7_BE = "matchnr7";
    private static final String PFEILPUNKTESCHNITT_BE = "pfeilpunkteSchnitt";


    // table column names
    private static final String VERANSTALTUNGID_TABLE = "schuetzenstatistik_veranstaltung_id";
    private static final String WETTKAMPFID_TABLE = "schuetzenstatistik_wettkampf_id";
    private static final String VEREINID_TABLE = "schuetzenstatistik_verein_id";
    private static final String MATCHNR_TABLE = "schuetzenstatistik_match_nr";
    private static final String DSBMITGLIEDID_TABLE = "schuetzenstatistik_dsb_mitglied_id";
    private static final String DSBMITGLIEDNAME_TABLE = "schuetzenstatistik_dsb_mitglied_name";
    private static final String RUECKENNUMMER_TABLE = "schuetzenstatistik_rueckennummer";
    private static final String PFEILPUNKTESCHNITT_TABLE = "schuetzenstatistik_pfeilpunkte_schnitt";

    private static final String GET_SCHUETZENSTATISTIK_MATCH = new QueryBuilder().selectFields(
            DSBMITGLIEDNAME_TABLE,
            RUECKENNUMMER_TABLE,
            "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 1 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_1_BE,
            "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 2 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_2_BE,
            "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 3 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_3_BE,
            "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 4 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_4_BE,
            "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 5 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_5_BE,
            "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 6 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_6_BE,
            "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 7 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_7_BE,
            "ROUND(AVG(CASE WHEN " + PFEILPUNKTESCHNITT_TABLE + " <> 0 THEN " + PFEILPUNKTESCHNITT_TABLE + " END), 2 ) AS " + PFEILPUNKTESCHNITT_BE
            )
            .from(TABLE)
            .whereEquals(VERANSTALTUNGID_TABLE)
            .andEquals(VEREINID_TABLE)
            .groupBy(
                    WETTKAMPFID_TABLE,
                    DSBMITGLIEDID_TABLE,
                    DSBMITGLIEDNAME_TABLE,
                    RUECKENNUMMER_TABLE
            )
            .orderBy(DSBMITGLIEDID_TABLE)
            .compose().toString();

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<SchuetzenstatistikMatchBE> SCHUETZENSTATISTIK_MATCH = new BusinessEntityConfiguration<>(
            SchuetzenstatistikMatchBE.class, TABLE, getColumnsToFieldsMap(), LOG);


    /* der Select liefert die aktuelle Schuetzenstatistik zur Wettkampf-ID
     */
    private static final String GET_SCHUETZENSTATISTIK_MATCH_WETTKAMPF = new QueryBuilder().selectFields(
                    DSBMITGLIEDNAME_TABLE,
                    RUECKENNUMMER_TABLE,
                    "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 1 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_1_BE,
                    "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 2 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_2_BE,
                    "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 3 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_3_BE,
                    "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 4 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_4_BE,
                    "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 5 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_5_BE,
                    "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 6 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_6_BE,
                    "MAX(CASE WHEN  " + MATCHNR_TABLE + " = 7 THEN " + PFEILPUNKTESCHNITT_TABLE + " ELSE 0 END) AS " + MATCHNR_7_BE,
                    "ROUND(AVG(CASE WHEN " + PFEILPUNKTESCHNITT_TABLE + " <> 0 THEN " + PFEILPUNKTESCHNITT_TABLE + " END), 2 ) AS " + PFEILPUNKTESCHNITT_BE
            )
            .from(TABLE)
            .whereEquals(WETTKAMPFID_TABLE)
            .andEquals(VEREINID_TABLE)
            .groupBy(
                    WETTKAMPFID_TABLE,
                    DSBMITGLIEDID_TABLE,
                    DSBMITGLIEDNAME_TABLE,
                    RUECKENNUMMER_TABLE
            )
            .orderBy(DSBMITGLIEDID_TABLE)
            .compose().toString();


    private final BasicDAO basicDao;
    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public SchuetzenstatistikMatchDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(DSBMITGLIEDNAME_TABLE, DSBMITGLIEDNAME_BE);
        columnsToFieldsMap.put(RUECKENNUMMER_TABLE, RUECKENNUMMER_BE);
        columnsToFieldsMap.put(PFEILPUNKTESCHNITT_TABLE, PFEILPUNKTESCHNITT_BE);
        columnsToFieldsMap.put(MATCHNR_1_BE, MATCHNR_1_BE);
        columnsToFieldsMap.put(MATCHNR_2_BE, MATCHNR_2_BE);
        columnsToFieldsMap.put(MATCHNR_3_BE, MATCHNR_3_BE);
        columnsToFieldsMap.put(MATCHNR_4_BE, MATCHNR_4_BE);
        columnsToFieldsMap.put(MATCHNR_5_BE, MATCHNR_7_BE);
        columnsToFieldsMap.put(MATCHNR_6_BE, MATCHNR_6_BE);
        columnsToFieldsMap.put(MATCHNR_7_BE, MATCHNR_7_BE);

        return columnsToFieldsMap;
    }

    /**
     * Lesen der aktuellen Liga-Tabelle zur Veranstaltung
     */
    public List<SchuetzenstatistikMatchBE> getSchuetzenstatistikMatchVeranstaltung(final long veranstaltungId, final long vereinId) {
        return basicDao.selectEntityList(SCHUETZENSTATISTIK_MATCH, GET_SCHUETZENSTATISTIK_MATCH, veranstaltungId, vereinId);
    }

    /**
     * Lesen der aktuellen Schuetzenstatistik zum Wettkampf (ID)
     */
    public List<SchuetzenstatistikMatchBE> getSchuetzenstatistikMatchWettkampf(final long wettkampfId, final long vereinId) {
        return basicDao.selectEntityList(SCHUETZENSTATISTIK_MATCH, GET_SCHUETZENSTATISTIK_MATCH_WETTKAMPF, wettkampfId, vereinId);
    }




}
