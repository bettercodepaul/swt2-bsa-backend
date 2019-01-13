package de.bogenliga.application.business.Setzliste.impl.dao;

import de.bogenliga.application.business.Setzliste.impl.entity.SetzlisteBE;
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
 * DataAccessObject for the Setzliste entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Yann Philippczyk, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class SetzlisteDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(SetzlisteDAO.class);

    // table name in the database
    private static final String TABLE = "veranstaltung";
    // business entity parameter names

    private static final String LIGATABELLE_BE_TABELLENPLATZ = "ligatabelleTabellenplatz";
    private static final String VEREIN_BE_NAME = "vereinName";
    private static final String MANNSCHAFT_BE_NR = "mannschaftNummer";
    private static final String VERANSTALTUNG_BE_NAME = "veranstaltungName";
    private static final String WETTKAMPF_BE_TAG = "wettkampfTag";
    private static final String WETTKAMPF_BE_DATUM = "wettkampfDatum";
    private static final String WETTKAMPF_BE_BEGINN = "wettkampfBeginn";
    private static final String WETTKAMPF_BE_ORT = "wettkampfOrt";

    private static final String LIGATABELLE_TABLE_TABELLENPLATZ = "ligatabelle_tabellenplatz";
    private static final String VEREIN_TABLE_NAME = "verein_name";
    private static final String MANNSCHAFT_TABLE_NR = "mannschaft_nummer";
    private static final String VERANSTALTUNG_TABLE_NAME = "veranstaltung_name";
    private static final String WETTKAMPF_TABLE_TAG = "wettkampf_tag";
    private static final String WETTKAMPF_TABLE_DATUM = "wettkampf_datum";
    private static final String WETTKAMPF_TABLE_BEGINN = "wettkampf_beginn";
    private static final String WETTKAMPF_TABLE_ORT = "wettkampf_ort";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<SetzlisteBE> SETZLISTE = new BusinessEntityConfiguration<>(
            SetzlisteBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String GET_TABLE =
            "SELECT lt.ligatabelle_tabellenplatz, v.verein_name, ms.mannschaft_nummer, vs.veranstaltung_name, wk.wettkampf_tag,"
                   + " wk.wettkampf_datum, wk.wettkampf_beginn, wk.wettkampf_ort"
                   + " FROM veranstaltung as vs"
                   + " INNER JOIN mannschaft AS ms ON vs.veranstaltung_id = ms.mannschaft_veranstaltung_id"
                   + " INNER JOIN verein AS v ON v.verein_id = ms.mannschaft_verein_id"
                   + " INNER JOIN ligatabelle lt ON ms.mannschaft_id = lt.ligatabelle_mannschaft_id"
                   + " INNER JOIN wettkampf AS wk ON vs.veranstaltung_id = wk.wettkampf_veranstaltung_id"
                   + " WHERE wk.wettkampf_tag = lt.ligatabelle_wettkampf_tag"
                   + " AND wk.wettkampf_id = ?"
                   + " AND lt.ligatabelle_wettkampf_tag = ?"
                   + " ORDER BY lt.ligatabelle_tabellenplatz";



    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public SetzlisteDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(LIGATABELLE_TABLE_TABELLENPLATZ, LIGATABELLE_BE_TABELLENPLATZ);
        columnsToFieldsMap.put(VEREIN_TABLE_NAME, VEREIN_BE_NAME);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_NR, MANNSCHAFT_BE_NR);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_NAME, VERANSTALTUNG_BE_NAME);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_TAG, WETTKAMPF_BE_TAG);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_DATUM, WETTKAMPF_BE_DATUM);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_BEGINN, WETTKAMPF_BE_BEGINN);
        columnsToFieldsMap.put(WETTKAMPF_TABLE_ORT, WETTKAMPF_BE_ORT);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return all setzliste entries
     */
    public List<SetzlisteBE> getTable(int wettkampfid, int wettkampftag) {
        return basicDao.selectEntityList(SETZLISTE, GET_TABLE, wettkampfid, wettkampftag);
    }
}
