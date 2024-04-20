package de.bogenliga.application.business.schuetzenstatistik.impl.dao;

import de.bogenliga.application.business.schuetzenstatistik.impl.entity.SchuetzenstatistikBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataAccessObject for the schuetzenstatistik
 * entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
@Repository
public class SchuetzenstatistikDAO implements DataAccessObject {
    // define the logger context
    private static final Logger LOG = LoggerFactory.getLogger(SchuetzenstatistikDAO.class);

    // table name in the database
    private static final String TABLE = "schuetzenstatistik";
    // business entity parameter names
    private static final String VERANSTALTUNGID_BE = "veranstaltungId";
    private static final String VERANSTALTUNGNAME_BE = "veranstaltungName";
    private static final String WETTKAMPFID_BE = "wettkampfId";
    private static final String WETTKAMPFTAG_BE = "wettkampfTag";
    private static final String MANNSCHAFTID_BE = "mannschaftId";
    private static final String MANNSCHAFTNUMMER_BE = "mannschaftNummer";
    private static final String VEREINID_BE = "vereinId";
    private static final String VEREINNAME_BE = "vereinName";
    private static final String MATCHID_BE = "matchId";
    private static final String MATCHNR_BE = "matchNr";
    private static final String DSBMITGLIEDID_BE = "dsbMitgliedId";
    private static final String DSBMITGLIEDNAME_BE = "dsbMitgliedName";
    private static final String RUECKENNUMMER_BE = "rueckenNummer";
    private static final String PFEILPUNKTESCHNITT_BE = "pfeilpunkteSchnitt";
    private static final String SCHUETZESATZ1_BE = "schuetzeSatz1";
    private static final String SCHUETZESATZ2_BE = "schuetzeSatz2";
    private static final String SCHUETZESATZ3_BE = "schuetzeSatz3";
    private static final String SCHUETZESATZ4_BE = "schuetzeSatz4";
    private static final String SCHUETZESATZ5_BE = "schuetzeSatz5";


    private static final String VERANSTALTUNGID_TABLE = "schuetzenstatistik_veranstaltung_id";
    private static final String VERANSTALTUNGNAME_TABLE = "schuetzenstatistik_veranstaltung_name";
    private static final String WETTKAMPFID_TABLE = "schuetzenstatistik_wettkampf_id";
    private static final String WETTKAMPFTAG_TABLE = "schuetzenstatistik_wettkampf_tag";
    private static final String MANNSCHAFTID_TABLE = "schuetzenstatistik_mannschaft_id";
    private static final String MANNSCHAFTNUMMER_TABLE = "schuetzenstatistik_mannschaft_nummer";
    private static final String VEREINID_TABLE = "schuetzenstatistik_verein_id";
    private static final String VEREINNAME_TABLE = "schuetzenstatistik_verein_name";
    private static final String MATCHID_TABLE = "schuetzenstatistik_match_id";
    private static final String MATCHNR_TABLE = "schuetzenstatistik_match_nr";
    private static final String DSBMITGLIEDID_TABLE = "schuetzenstatistik_dsb_mitglied_id";
    private static final String DSBMITGLIEDNAME_TABLE = "schuetzenstatistik_dsb_mitglied_name";
    private static final String RUECKENNUMMER_TABLE = "schuetzenstatistik_rueckennummer";
    private static final String PFEILPUNKTESCHNITT_TABLE = "schuetzenstatistik_pfeilpunkte_schnitt";
    private static final String SCHUETZESATZ1_TABLE = "schuetzenstatistik_schuetze_satz1";
    private static final String SCHUETZESATZ2_TABLE = "schuetzenstatistik_schuetze_satz2";
    private static final String SCHUETZESATZ3_TABLE = "schuetzenstatistik_schuetze_satz3";
    private static final String SCHUETZESATZ4_TABLE = "schuetzenstatistik_schuetze_satz4";
    private static final String SCHUETZESATZ5_TABLE = "schuetzenstatistik_schuetze_satz5";

    /*
     * SQL queries
     */

    /* der Select liefert die aktuelle Schuetzenstatistik zur Veranstaltung
    hierbei wird über die Wettkämpfe hinweg gemittelt - eigentlich falsch, da jeder Wettkampf abweiochende
    Pfeilanzahlne hat --> Änderung an DB-View notwendig.-
    Hier wird der Group by jetzt ohne WettkampfID und WettkampfTag gemacht -> werden zu 0 gesetzt
     */
    private static final String GET_SCHUETZENSTATISTIK = new QueryBuilder().selectFields(
            VERANSTALTUNGID_TABLE,
            VERANSTALTUNGNAME_TABLE,
            MANNSCHAFTID_TABLE,
            MANNSCHAFTNUMMER_TABLE,
            VEREINID_TABLE,
            VEREINNAME_TABLE,
            DSBMITGLIEDID_TABLE,
            DSBMITGLIEDNAME_TABLE,
            RUECKENNUMMER_TABLE,
            "SUM("+PFEILPUNKTESCHNITT_TABLE+")/COUNT("+PFEILPUNKTESCHNITT_TABLE+") as "+PFEILPUNKTESCHNITT_BE
    )
            .from(TABLE)
            .whereEquals(VERANSTALTUNGID_TABLE)
            .andEquals(VEREINID_TABLE)
            .groupBy(
                    VERANSTALTUNGID_TABLE,
                    VERANSTALTUNGNAME_TABLE,
                    MANNSCHAFTID_TABLE,
                    MANNSCHAFTNUMMER_TABLE,
                    VEREINID_TABLE,
                    VEREINNAME_TABLE,
                    DSBMITGLIEDID_TABLE,
                    DSBMITGLIEDNAME_TABLE,
                    RUECKENNUMMER_TABLE
            )
            .orderBy("SUM("+PFEILPUNKTESCHNITT_TABLE+")/COUNT("+PFEILPUNKTESCHNITT_TABLE+")")
            .compose().toString();

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<SchuetzenstatistikBE> SCHUETZENSTATISTIK = new BusinessEntityConfiguration<>(
            SchuetzenstatistikBE.class, TABLE, getColumnsToFieldsMap(), LOG);


    /* der Select liefert die aktuelle Schuetzenstatistik zur Wettkampf-ID
     */
    private static final String GET_SCHUETZENSTATISTIK_WETTKAMPF = new QueryBuilder().selectFields(
                VERANSTALTUNGID_TABLE,
                VERANSTALTUNGNAME_TABLE,
                WETTKAMPFID_TABLE,
                WETTKAMPFTAG_TABLE,
                MANNSCHAFTID_TABLE,
                MANNSCHAFTNUMMER_TABLE,
                VEREINID_TABLE,
                VEREINNAME_TABLE,
                MATCHID_TABLE,
                MATCHNR_TABLE,
                DSBMITGLIEDID_TABLE,
                DSBMITGLIEDNAME_TABLE,
                RUECKENNUMMER_TABLE,
                MATCHNR_TABLE,
                "ARRAY_remove("+SCHUETZESATZ1_TABLE+", NULL) as "+SCHUETZESATZ1_BE,
                "ARRAY_remove("+SCHUETZESATZ2_TABLE+", NULL) as "+SCHUETZESATZ2_BE,
                "ARRAY_remove("+SCHUETZESATZ3_TABLE+", NULL) as "+SCHUETZESATZ3_BE,
                "ARRAY_remove("+SCHUETZESATZ4_TABLE+", NULL) as "+SCHUETZESATZ4_BE,
                "ARRAY_remove("+SCHUETZESATZ5_TABLE+", NULL) as "+SCHUETZESATZ5_BE,
                "SUM("+PFEILPUNKTESCHNITT_TABLE+")/COUNT("+PFEILPUNKTESCHNITT_TABLE+") as "+PFEILPUNKTESCHNITT_BE
            )
            .from(TABLE)
            .whereEquals(WETTKAMPFID_TABLE)
            .andEquals(VEREINID_TABLE)
            .groupBy(
                    VERANSTALTUNGID_TABLE,
                    VERANSTALTUNGNAME_TABLE,
                    WETTKAMPFID_TABLE,
                    WETTKAMPFTAG_TABLE,
                    MANNSCHAFTID_TABLE,
                    MANNSCHAFTNUMMER_TABLE,
                    VEREINID_TABLE,
                    VEREINNAME_TABLE,
                    MATCHID_TABLE,
                    MATCHNR_TABLE,
                    DSBMITGLIEDID_TABLE,
                    DSBMITGLIEDNAME_TABLE,
                    RUECKENNUMMER_TABLE,
                    SCHUETZESATZ1_TABLE,
                    SCHUETZESATZ2_TABLE,
                    SCHUETZESATZ3_TABLE,
                    SCHUETZESATZ4_TABLE,
                    SCHUETZESATZ5_TABLE,
                    PFEILPUNKTESCHNITT_TABLE
            )
            .orderBy("SUM("+PFEILPUNKTESCHNITT_TABLE+")/COUNT("+PFEILPUNKTESCHNITT_TABLE+")")
            .compose().toString();


    private final BasicDAO basicDao;
    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public SchuetzenstatistikDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(VERANSTALTUNGID_TABLE, VERANSTALTUNGID_BE);
        columnsToFieldsMap.put(VERANSTALTUNGNAME_TABLE, VERANSTALTUNGNAME_BE);
        columnsToFieldsMap.put(WETTKAMPFID_TABLE, WETTKAMPFID_BE);
        columnsToFieldsMap.put(WETTKAMPFTAG_TABLE, WETTKAMPFTAG_BE);
        columnsToFieldsMap.put(MANNSCHAFTID_TABLE, MANNSCHAFTID_BE);
        columnsToFieldsMap.put(MANNSCHAFTNUMMER_TABLE, MANNSCHAFTNUMMER_BE);
        columnsToFieldsMap.put(VEREINID_TABLE, VEREINID_BE);
        columnsToFieldsMap.put(VEREINNAME_TABLE, VEREINNAME_BE);
        columnsToFieldsMap.put(MATCHID_TABLE, MATCHID_BE);
        columnsToFieldsMap.put(MATCHNR_TABLE, MATCHNR_BE);
        columnsToFieldsMap.put(DSBMITGLIEDID_TABLE, DSBMITGLIEDID_BE);
        columnsToFieldsMap.put(DSBMITGLIEDNAME_TABLE, DSBMITGLIEDNAME_BE);
        columnsToFieldsMap.put(RUECKENNUMMER_TABLE, RUECKENNUMMER_BE);
        columnsToFieldsMap.put(PFEILPUNKTESCHNITT_TABLE, PFEILPUNKTESCHNITT_BE);
        columnsToFieldsMap.put(SCHUETZESATZ1_TABLE, SCHUETZESATZ1_BE);
        columnsToFieldsMap.put(SCHUETZESATZ2_TABLE, SCHUETZESATZ2_BE);
        columnsToFieldsMap.put(SCHUETZESATZ3_TABLE, SCHUETZESATZ3_BE);
        columnsToFieldsMap.put(SCHUETZESATZ4_TABLE, SCHUETZESATZ4_BE);
        columnsToFieldsMap.put(SCHUETZESATZ5_TABLE, SCHUETZESATZ5_BE);

        return columnsToFieldsMap;
    }

    /**
     * Lesen der aktuellen Liga-Tabelle zur Veranstaltung
     */
    public List<SchuetzenstatistikBE> getSchuetzenstatistikVeranstaltung(final long veranstaltungId, final long vereinId) {
        return basicDao.selectEntityList(SCHUETZENSTATISTIK, GET_SCHUETZENSTATISTIK, veranstaltungId, vereinId);
    }

    /**
     * Lesen der aktuellen Schuetzenstatistik zum Wettkampf (ID)
     */
    public List<SchuetzenstatistikBE> getSchuetzenstatistikWettkampf(final long wettkampfId, final long vereinId) {
        return basicDao.selectEntityList(SCHUETZENSTATISTIK, GET_SCHUETZENSTATISTIK_WETTKAMPF, wettkampfId, vereinId);
    }
}
