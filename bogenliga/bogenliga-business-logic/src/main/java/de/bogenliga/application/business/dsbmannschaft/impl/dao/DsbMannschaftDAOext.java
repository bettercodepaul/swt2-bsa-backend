package de.bogenliga.application.business.dsbmannschaft.impl.dao;

import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBEext;
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

@Repository
public class DsbMannschaftDAOext implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(DsbMannschaftDAOext.class);

    // table name in the database
    private static final String TABLE = "mannschaft";
    // business entity parameter names


    private static final String MANNSCHAFT_BE_ID = "id";
    private static final String MANNSCHAFT_BE_TEAMID = "vereinId";
    private static final String MANNSCHAFT_BE_NUMBER = "nummer";
    private static final String MANNSCHAFT_BE_EVENTID = "veranstaltungId";
    private static final String MANNSCHAFT_BE_USER_ID = "benutzerId";
    private static final String MANNSCHAFT_BE_SORTIERUNG = "sortierung";
    private static final String MANNSCHAFT_BE_VEREINNAME = "vereinName";
    private static final String MANNSCHAFT_BE_WETTKAMPFORT = "wettkampfOrtsname";
    private static final String MANNSCHAFT_BE_WETTKAMPTAG = "wettkampfTag";
    private static final String MANNSCHAFT_BE_VERANSTALTUNGSNAME = "veranstaltungName";

    private static final String MANNSCHAFT_TABLE_ID = "mannschaft_id";
    private static final String MANNSCHAFT_TABLE_TEAMID = "mannschaft_verein_id";
    private static final String MANNSCHAFT_TABLE_NUMBER = "mannschaft_nummer";
    private static final String MANNSCHAFT_TABLE_EVENTID = "mannschaft_veranstaltung_id";
    private static final String MANNSCHAFT_TABLE_USER_ID = "mannschaft_benutzer_id";
    private static final String MANNSCHAFT_TABLE_SORTIERUNG = "mannschaft_sortierung";
    private static final String MANNSCHAFT_TABLE_VEREINNAME = "verein_name";
    private static final String MANNSCHAFT_TABLE_WETTKAMPFORT = "wettkampf_ortsname";
    private static final String MANNSCHAFT_TABLE_WETTKAMPTAG = "wettkampf_tag";
    private static final String MANNSCHAFT_TABLE_VERANSTALTUNGSNAME = "veranstaltung_name";



    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<DsbMannschaftBEext> MANNSCHAFT = new BusinessEntityConfiguration<>(
            DsbMannschaftBEext.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */

    private static final String FIND_ALL_BY_WETTKAMPF_ID =
            "select DISTINCT m.* FROM mannschaft m, wettkampf w, veranstaltung v\n" +
                    "WHERE w.wettkampf_veranstaltung_id = v.veranstaltung_id\n" +
                    "AND m.mannschaft_veranstaltung_id = v.veranstaltung_id\n" +
                    "AND w.wettkampf_id = ?\n" +
                    "group by m.mannschaft_id";

    private static final String FIND_VERSANSTALTUNGEN_BY_VEREIN =
            "SELECT veranstaltung_name, wettkampf_tag, wettkampf_ortsname, verein_name, mannschaft_nummer "
                    + "FROM veranstaltung ver "
                    + "JOIN mannschaft m ON ver.veranstaltung_id = m.mannschaft_veranstaltung_id "
                    + "JOIN verein v ON m.mannschaft_verein_id = v.verein_id "
                    + "JOIN wettkampf ON ver.veranstaltung_id = wettkampf.wettkampf_veranstaltung_id "
                    + "WHERE v.verein_id = ? "
                    + "AND ver.veranstaltung_phase = 2 "
                    + "GROUP BY mannschaft_nummer, veranstaltung_name, verein_name, wettkampf_ortsname, wettkampf_tag; ";

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public DsbMannschaftDAOext(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(MANNSCHAFT_TABLE_ID, MANNSCHAFT_BE_ID);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_TEAMID, MANNSCHAFT_BE_TEAMID);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_NUMBER, MANNSCHAFT_BE_NUMBER);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_SORTIERUNG, MANNSCHAFT_BE_SORTIERUNG);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_USER_ID, MANNSCHAFT_BE_USER_ID);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_EVENTID, MANNSCHAFT_BE_EVENTID);
       columnsToFieldsMap.put(MANNSCHAFT_TABLE_WETTKAMPFORT, MANNSCHAFT_BE_WETTKAMPFORT);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_WETTKAMPTAG, MANNSCHAFT_BE_WETTKAMPTAG);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_VEREINNAME, MANNSCHAFT_BE_VEREINNAME);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_VERANSTALTUNGSNAME, MANNSCHAFT_BE_VERANSTALTUNGSNAME);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }



    public List<DsbMannschaftBEext> findAllByWettkampfId(final long id) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_WETTKAMPF_ID, id);}
    public List<DsbMannschaftBEext> findVeranstaltungAndWettkampfById(final long id) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_VERSANSTALTUNGEN_BY_VEREIN, id);}
    /**
     * Return all dsbmannschaft entries that are currently in the waiting queue
     *
     * @return all dsbmannschaft entries in the waiting queue
     */

}
