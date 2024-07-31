package de.bogenliga.application.business.ligatabelle.impl.dao;

import de.bogenliga.application.business.ligatabelle.impl.entity.LigatabelleBE;
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
 * DataAccessObject for the ligatabelle entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
@Repository
public class LigatabelleDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOG = LoggerFactory.getLogger(LigatabelleDAO.class);

    // table name in the database
    private static final String TABLE = "ligatabelle";
    // business entity parameter names

    private static final String VERANSTALTUNGID_BE = "veranstaltungId";
    private static final String VERANSTALTUNGNAME_BE = "veranstaltungName";
    private static final String WETTKAMPFID_BE = "wettkampfId";
    private static final String WETTKAMPFTAG_BE = "wettkampfTag";
    private static final String MANNSCHAFTID_BE = "mannschaftId";
    private static final String MANNSCHAFTNUMMER_BE = "mannschaftNummer";
    private static final String VEREINID_BE = "vereinId";
    private static final String VEREINNAME_BE = "vereinName";
    private static final String MATCHPKT_BE = "matchpkt";
    private static final String MATCHPKTGEGEN_BE = "matchpktGegen";
    private static final String SATZPKT_BE = "satzpkt";
    private static final String SATZPKTGEGEN_BE = "satzpktGegen";
    private static final String SATZPKTDIFFERENZ_BE = "satzpktDifferenz";
    private static final String SORTIERUNG_BE = "sortierung";
    private static final String TABELLENPLATZ_BE = "tabellenplatz";
    private static final String MATCH_COUNT_BE = "matchCount";

    private static final String VERANSTALTUNGID_TABLE = "ligatabelle_veranstaltung_id";
    private static final String VERANSTALTUNGNAME_TABLE = "ligatabelle_veranstaltung_name";
    private static final String WETTKAMPFID_TABLE = "ligatabelle_wettkampf_id";
    private static final String WETTKAMPFTAG_TABLE = "ligatabelle_wettkampf_tag";
    private static final String MANNSCHAFTID_TABLE = "ligatabelle_mannschaft_id";
    private static final String MANNSCHAFTNUMMER_TABLE = "ligatabelle_mannschaft_nummer";
    private static final String VEREINID_TABLE = "ligatabelle_verein_id";
    private static final String VEREINNAME_TABLE = "ligatabelle_verein_name";
    private static final String MATCHPKT_TABLE = "ligatabelle_matchpkt";
    private static final String MATCHPKTGEGEN_TABLE = "ligatabelle_matchpkt_gegen";
    private static final String SATZPKT_TABLE = "ligatabelle_satzpkt";
    private static final String SATZPKTGEGEN_TABLE = "ligatabelle_satzpkt_gegen";
    private static final String SATZPKTDIFFERENZ_TABLE = "ligatabelle_satzpkt_differenz";
    private static final String SORTIERUNG_TABLE = "ligatabelle_sortierung";
    private static final String TABELLENPLATZ_TABLE = "tabellenplatz";
    private static final String MATCH_COUNT_TABLE = "ligatabelle_match_count";

    /*
     * SQL queries
     */

    /* der Select liefert die aktuelle Ligatabelle zur Veranstaltung -
    * es wird immer der "höchste Wettkampftag ermittelt und die Tabellenreihenfolge
    * automatisch erzeugt durch die Sortierkriterien beim generieren der Row-Number
    * ggf. mpüssen wir für die verschiedenen Liga-Formen andere Selects hinterlegen
    * hier jetzt erst mal der Select für Match-Punkte vor Satzpunkt-Differenz
    * sollte für Liga-Satzsystem passen
     */
    private static final String GET_LIGATABELLE =
        "SELECT MAX(ligatabelle_veranstaltung_id) AS ligatabelle_veranstaltung_id," +
            "MAX(ligatabelle_veranstaltung_id) AS ligatabelle_veranstaltung_id," +
            "MAX(ligatabelle_veranstaltung_name) AS ligatabelle_veranstaltung_name," +
            "MAX(ligatabelle_wettkampf_id) AS ligatabelle_wettkampf_id," +
            "MAX(ligatabelle_wettkampf_tag) AS ligatabelle_wettkampf_tag," +
            "MAX(ligatabelle_mannschaft_id) AS ligatabelle_mannschaft_id," +
            "MAX(ligatabelle_mannschaft_nummer) AS ligatabelle_mannschaft_nummer," +
            "MAX(ligatabelle_verein_id) AS ligatabelle_verein_id," +
            "MAX(ligatabelle_verein_name) AS ligatabelle_verein_name," +
            "SUM(ligatabelle_matchpkt) AS ligatabelle_matchpkt," +
            "SUM(ligatabelle_matchpkt_gegen) AS ligatabelle_matchpkt_gegen," +
            "SUM(ligatabelle_satzpkt) AS ligatabelle_satzpkt," +
            "SUM(ligatabelle_satzpkt_gegen) AS ligatabelle_satzpkt_gegen," +
            "SUM(ligatabelle_satzpkt_differenz) AS ligatabelle_satzpkt_differenz," +
            "MAX(ligatabelle_sortierung) AS ligatabelle_sortierung," +
            "SUM(ligatabelle_match_count) AS ligatabelle_match_count," +
            "row_number()  over (" +
                "order by SUM(COALESCE(ligatabelle_matchpkt, 0)) desc ,"+
                "SUM(COALESCE(ligatabelle_matchpkt_gegen, 0)),"+
                "SUM(COALESCE(ligatabelle_satzpkt_differenz, 0)) desc,"+
                "SUM(COALESCE(ligatabelle_satzpkt, 0)) desc,"+
                "SUM(COALESCE(ligatabelle_satzpkt_gegen, 0)),"+
                "MAX(COALESCE(ligatabelle_sortierung, 0)),"+
                "MAX(ligatabelle_veranstaltung_id),"+
                "MAX(ligatabelle_veranstaltung_name),"+
                "MAX(ligatabelle_wettkampf_id),"+
                "MAX(ligatabelle_wettkampf_tag),"+
                "MAX(ligatabelle_mannschaft_id),"+
                "MAX(ligatabelle_mannschaft_nummer),"+
                "MAX(ligatabelle_verein_id),"+
                "MAX(ligatabelle_verein_name)"+
            ") as tabellenplatz " +
            "from ligatabelle "+
            "where ligatabelle_wettkampf_id = ANY ( "+
                "select w.wettkampf_id "+
                "from wettkampf w "+
                "where w.wettkampf_veranstaltung_id = "+
                    "(select v.veranstaltung_id "+
                    "from veranstaltung v "+
                    "where v.veranstaltung_id = ?) "+
            ") "+
            "group by ligatabelle_mannschaft_id ";

    /* der Select liefert die aktuelle Ligatabelle zur Wettkampf-ID
     * ggf. müssen wir für die verschiedenen Liga-Formen andere Selects hinterlegen
     * hier jetzt erst mal der Select für Match-Punkte vor Satzpunkt-Differenz
     * sollte für Liga-Satzsystem passen
     */
    private static final String GET_LIGATABELLE_WETTKAMPF =
        "SELECT MAX(ligatabelle_veranstaltung_id) AS ligatabelle_veranstaltung_id," +
            "MAX(ligatabelle_veranstaltung_id) AS ligatabelle_veranstaltung_id," +
            "MAX(ligatabelle_veranstaltung_name) AS ligatabelle_veranstaltung_name," +
            "MAX(ligatabelle_wettkampf_id) AS ligatabelle_wettkampf_id," +
            "MAX(ligatabelle_wettkampf_tag) AS ligatabelle_wettkampf_tag," +
            "MAX(ligatabelle_mannschaft_id) AS ligatabelle_mannschaft_id," +
            "MAX(ligatabelle_mannschaft_nummer) AS ligatabelle_mannschaft_nummer," +
            "MAX(ligatabelle_verein_id) AS ligatabelle_verein_id," +
            "MAX(ligatabelle_verein_name) AS ligatabelle_verein_name," +
            "SUM(ligatabelle_matchpkt) AS ligatabelle_matchpkt," +
            "SUM(ligatabelle_matchpkt_gegen) AS ligatabelle_matchpkt_gegen," +
            "SUM(ligatabelle_satzpkt) AS ligatabelle_satzpkt," +
            "SUM(ligatabelle_satzpkt_gegen) AS ligatabelle_satzpkt_gegen," +
            "SUM(ligatabelle_satzpkt_differenz) AS ligatabelle_satzpkt_differenz," +
            "MAX(ligatabelle_sortierung) AS ligatabelle_sortierung," +
            "SUM(ligatabelle_match_count) AS ligatabelle_match_count," +
            "row_number()  over (" +
                "order by SUM(COALESCE(ligatabelle_matchpkt, 0)) desc ," +
                "SUM(COALESCE(ligatabelle_matchpkt_gegen, 0))," +
                "SUM(COALESCE(ligatabelle_satzpkt_differenz, 0)) desc," +
                "SUM(COALESCE(ligatabelle_satzpkt, 0)) desc," +
                "SUM(COALESCE(ligatabelle_satzpkt_gegen, 0))," +
                "MAX(COALESCE(ligatabelle_sortierung, 0))," +
                "MAX(ligatabelle_veranstaltung_id)," +
                "MAX(ligatabelle_veranstaltung_name)," +
                "MAX(ligatabelle_wettkampf_id)," +
                "MAX(ligatabelle_wettkampf_tag)," +
                "MAX(ligatabelle_mannschaft_id)," +
                "MAX(ligatabelle_mannschaft_nummer)," +
                "MAX(ligatabelle_verein_id)," +
                "MAX(ligatabelle_verein_name)" +
            ") as tabellenplatz " +
            "from ligatabelle " +
            "where ligatabelle_wettkampf_id = ANY ( " +
                "select w.wettkampf_id " +
                "from wettkampf w " +
                "where w.wettkampf_veranstaltung_id =" +
                    "(select v.veranstaltung_id " +
                    "from wettkampf wk " +
                    "join veranstaltung v on wk.wettkampf_veranstaltung_id = v.veranstaltung_id " +
                    "where wk.wettkampf_id = ?) " +
                "and w.wettkampf_tag <= ( " +
                    "select wk.wettkampf_tag " +
                    "from wettkampf wk " +
                    "where wk.wettkampf_id = ?) " +
            ") " +
            "group by ligatabelle_mannschaft_id";


    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<LigatabelleBE> LIGATABELLE = new BusinessEntityConfiguration<>(
            LigatabelleBE.class, TABLE, getColumnsToFieldsMap(), LOG);



    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public LigatabelleDAO(final BasicDAO basicDao) {
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
        columnsToFieldsMap.put(MATCH_COUNT_TABLE, MATCH_COUNT_BE);

        return columnsToFieldsMap;
    }

    /**
     * Lesen der aktuellen Liga-Tabelle zur Veranstaltung
     */
    public List<LigatabelleBE> getLigatabelleVeranstaltung(final long id) {
        return basicDao.selectEntityList(LIGATABELLE, GET_LIGATABELLE, id);
    }

    /**
     * Lesen der aktuellen Liga-Tabelle zum Wettkampf (ID)
     */
    public List<LigatabelleBE> getLigatabelleWettkampf(final long id) {
        return basicDao.selectEntityList(LIGATABELLE, GET_LIGATABELLE_WETTKAMPF, id, id);
    }
}
