package de.bogenliga.application.business.schuetzenstatistik.impl.dao;

import de.bogenliga.application.business.schuetzenstatistik.impl.entity.SchuetzenstatistikBE;
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

/**
 * DataAccessObject for the schuetzenstatistik
 * entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
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


    private static final String VERANSTALTUNGID_TABLE = "schuetzenstatistik_veranstaltung_id";
    private static final String VERANSTALTUNGNAME_TABLE = "schuetzenstatistik_veranstaltung_name";
    private static final String WETTKAMPFID_TABLE = "schuetzenstatistik_wettkampf_id";
    private static final String WETTKAMPFTAG_TABLE = "schuetzenstatistik_wettkampf_tag";
    private static final String MANNSCHAFTID_TABLE = "schuetzenstatistik_mannschaft_id";
    private static final String MANNSCHAFTNUMMER_TABLE = "schuetzenstatistik_mannschaft_nummer";
    private static final String VEREINID_TABLE = "schuetzenstatistik_verein_id";
    private static final String VEREINNAME_TABLE = "schuetzenstatistik_verein_name";

    /*
     * SQL queries
     */

    /* der Select liefert die aktuelle Schuetzenstatistik zur Veranstaltung -
    * es wird immer der "höchste Wettkampftag ermittelt un die Tabellenreihenfolge
    * automatisch erzeugt durch die Sortierkriterien beim generieren der Row-Number
    * ggf. mpüssen wir für die verschiedenen Liga-Formen andere Selects hinterlegen
    * hier jetzt erst mal der Select für Match-Punkte vor Satzpunkt-Differenz
    * sollte für Liga-Satzsystem passen
     */
    private static final String GET_SCHUETZENSTATISTIK =
        "SELECT lt.schuetzenstatistik" +
                "_veranstaltung_id, lt.schuetzenstatistik" +
                "_veranstaltung_name," +
           "lt.schuetzenstatistik" +
                "_wettkampf_id, lt.schuetzenstatistik" +
                "_wettkampf_tag, lt.schuetzenstatistik" +
                "_mannschaft_id, lt.schuetzenstatistik" +
                "_mannschaft_nummer," +
                "lt.schuetzenstatistik" +
                "_verein_id, lt.schuetzenstatistik" +
                "_verein_name, lt.schuetzenstatistik" +
                "_matchpkt, lt.schuetzenstatistik" +
                "_matchpkt_gegen," +
           "lt.schuetzenstatistik" +
                "_satzpkt, lt.schuetzenstatistik" +
                "_satzpkt_gegen, lt.schuetzenstatistik" +
                "_satzpkt_differenz, lt.schuetzenstatistik" +
                "_sortierung," +
           "row_number()  over (" +
             "order by lt.schuetzenstatistik" +
                "_matchpkt desc, lt.schuetzenstatistik" +
                "_matchpkt_gegen," +
               "lt.schuetzenstatistik" +
                "_satzpkt_differenz desc, lt.schuetzenstatistik" +
                "_satzpkt desc," +
               "lt.schuetzenstatistik" +
                "_satzpkt_gegen, lt.schuetzenstatistik" +
                "_sortierung," +
               "lt.schuetzenstatistik" +
                "_veranstaltung_id, lt.schuetzenstatistik" +
                "_veranstaltung_name," +
               "lt.schuetzenstatistik" +
                "_wettkampf_id, lt.schuetzenstatistik" +
                "_wettkampf_tag," +
               "lt.schuetzenstatistik" +
                "_mannschaft_id, lt.schuetzenstatistik" +
                "_mannschaft_nummer," +
               "lt.schuetzenstatistik" +
                "_verein_id, lt.schuetzenstatistik" +
                "_verein_name" +
             ")as tabellenplatz from schuetzenstatistik" +
                " as lt where lt.schuetzenstatistik" +
                "_veranstaltung_id = ?" +
        "and lt.schuetzenstatistik" +
                "_wettkampf_tag = (" +
            "select max(ligat.schuetzenstatistik" +
                "_wettkampf_tag) " +
            "from schuetzenstatistik" +
                " as ligat where ligat.schuetzenstatistik" +
                "_veranstaltung_id = lt.schuetzenstatistik" +
                "_veranstaltung_id)";

    /* der Select liefert die aktuelle Schuetzenstatistik zur Wettkampf-ID
     * ggf. mpüssen wir für die verschiedenen Liga-Formen andere Selects hinterlegen
     * hier jetzt erst mal der Select für Match-Punkte vor Satzpunkt-Differenz
     * sollte für Liga-Satzsystem passen
     */
    private static final String GET_SCHUETZENSTATISTIK_WETTKAMPF =
            "SELECT schuetzenstatistik" +
                    "_veranstaltung_id, "+
                    "schuetzenstatistik" +
                    "_veranstaltung_name," +
                    "schuetzenstatistik" +
                    "_wettkampf_id," +
                    "schuetzenstatistik" +
                    "_wettkampf_tag," +
                    "schuetzenstatistik" +
                    "_mannschaft_id," +
                    "schuetzenstatistik" +
                    "_mannschaft_nummer," +
                    "schuetzenstatistik" +
                    "_verein_id," +
                    "schuetzenstatistik" +
                    "_verein_name," +
                    "schuetzenstatistik" +
                    "_matchpkt," +
                    "schuetzenstatistik" +
                    "_matchpkt_gegen," +
                    "schuetzenstatistik" +
                    "_satzpkt," +
                    "schuetzenstatistik" +
                    "_satzpkt_gegen," +
                    "schuetzenstatistik" +
                    "_satzpkt_differenz," +
                    "schuetzenstatistik" +
                    "_sortierung," +
                    "row_number()  over (" +
                        "order by schuetzenstatistik" +
                    "_matchpkt desc ," +
                        "schuetzenstatistik" +
                    "_matchpkt_gegen," +
                        "schuetzenstatistik" +
                    "_satzpkt_differenz desc," +
                        "schuetzenstatistik" +
                    "_satzpkt desc," +
                        "schuetzenstatistik" +
                    "_satzpkt_gegen," +
                        "schuetzenstatistik" +
                    "_sortierung," +
                        "schuetzenstatistik" +
                    "_veranstaltung_id," +
                        "schuetzenstatistik" +
                    "_veranstaltung_name," +
                        "schuetzenstatistik" +
                    "_wettkampf_id," +
                        "schuetzenstatistik" +
                    "_wettkampf_tag," +
                        "schuetzenstatistik" +
                    "_mannschaft_id," +
                        "schuetzenstatistik" +
                    "_mannschaft_nummer," +
                        "schuetzenstatistik" +
                    "_verein_id," +
                        "schuetzenstatistik" +
                    "_verein_name" +
                        ")as tabellenplatz" +
                    "from schuetzenstatistik" +
                    "" +
                    "where schuetzenstatistik" +
                    "_wettkampf_id = ?" ;


    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<SchuetzenstatistikBE> SCHUETZENSTATISTIK = new BusinessEntityConfiguration<>(
            SchuetzenstatistikBE.class, TABLE, getColumnsToFieldsMap(), LOG);



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
        columnsToFieldsMap.put(MATCHPKT_TABLE, MATCHPKT_BE);
        columnsToFieldsMap.put(MATCHPKTGEGEN_TABLE, MATCHPKTGEGEN_BE);
        columnsToFieldsMap.put(SATZPKT_TABLE, SATZPKT_BE);
        columnsToFieldsMap.put(SATZPKTGEGEN_TABLE, SATZPKTGEGEN_BE);
        columnsToFieldsMap.put(SATZPKTDIFFERENZ_TABLE, SATZPKTDIFFERENZ_BE);
        columnsToFieldsMap.put(SORTIERUNG_TABLE, SORTIERUNG_BE);
        columnsToFieldsMap.put(TABELLENPLATZ_TABLE, TABELLENPLATZ_BE);

        return columnsToFieldsMap;
    }

    /**
     * Lesen der aktuellen Liga-Tabelle zur Veranstaltung
     */
    public List<SchuetzenstatistikBE> getSchuetzenstatistikVeranstaltung(final long id) {
        return basicDao.selectEntityList(SCHUETZENSTATISTIK, GET_SCHUETZENSTATISTIK, id);
    }

    /**
     * Lesen der aktuellen Schuetzenstatistik zum Wettkampf (ID)
     */
    public List<SchuetzenstatistikBE> getSchuetzenstatistikWettkampf(final long id) {
        return basicDao.selectEntityList(SCHUETZENSTATISTIK, GET_SCHUETZENSTATISTIK_WETTKAMPF, id);
    }




}
