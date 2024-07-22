package de.bogenliga.application.business.dsbmannschaft.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

@Repository
public class DsbMannschaftDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(DsbMannschaftDAO.class);

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
    private static final String MANNSCHAFT_BE_MANNSCHAFTSNUMMER = "mannschaftNummer";

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
    private static final String MANNSCHAFT_TABLE_MANNSCHAFTSNUMMER = "mannschaft_nummer";



    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<DsbMannschaftBE> MANNSCHAFT = new BusinessEntityConfiguration<>(
            DsbMannschaftBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM mannschaft"
                    + " ORDER BY mannschaft_id";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM mannschaft"
                    + " WHERE mannschaft_id = ?";

    private static final String FIND_ALL_BY_VEREINS_ID =
            "SELECT * "
                    + " FROM mannschaft"
                    + " WHERE mannschaft_verein_id = ?"
                    + " ORDER BY mannschaft_nummer";

    private static final String FIND_ALL_BY_VERANSTALTUNGS_ID =
            "SELECT * "
                    + " FROM mannschaft"
                    + " WHERE " + MANNSCHAFT_TABLE_EVENTID +" = ?"
                    + " ORDER BY "+MANNSCHAFT_TABLE_SORTIERUNG;

    private static final String FIND_ALL_BY_WETTKAMPF_ID =
            "select DISTINCT m.* FROM mannschaft m, wettkampf w, veranstaltung v\n" +
                    "WHERE w.wettkampf_veranstaltung_id = v.veranstaltung_id\n" +
                    "AND m.mannschaft_veranstaltung_id = v.veranstaltung_id\n" +
                    "AND w.wettkampf_id = ?\n" +
                    "group by m.mannschaft_id";

    private static final String FIND_ALL_BY_WARTSCHLANGE =
            "SELECT * "
                    + " FROM mannschaft"
                    + " WHERE mannschaft_veranstaltung_id IS NULL";
    private static final String FIND_ALL_BY_NAME =
            "SELECT a.* "
                    + "FROM mannschaft a, verein b "
                    + "WHERE a.mannschaft_verein_id = b.verein_id "
                    + "AND CONCAT(LOWER(b.verein_name), ' ' , "
                    + "LOWER(CAST(a.mannschaft_nummer AS TEXT))) LIKE LOWER(?) "
                    + "AND mannschaft_veranstaltung_id IS NULL";
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
    public DsbMannschaftDAO(final BasicDAO basicDao) {
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
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_MANNSCHAFTSNUMMER,MANNSCHAFT_BE_MANNSCHAFTSNUMMER);


        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return all dsbmitglied entries
     */
    public List<DsbMannschaftBE> findAll() {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL);
    }


    /**
     * Return all dsbmannschaft entries with the given vereinsId
     *
     * @param id from the verein
     * @return all dbsmannschaft entries with the given vereinsId
     */

    public List<DsbMannschaftBE> findAllByVereinsId(final long id) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_VEREINS_ID, id);}


    /**
     * Return all dsbmannschaft entries with the given Veranstaltungs-Id
     *
     * @param id from the Veranstaltung
     * @return all dbsmannschaft entries with the given Veranstaltungs-Id
     */

    public List<DsbMannschaftBE> findAllByVeranstaltungsId(final long id) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_VERANSTALTUNGS_ID, id);}

    /**
     * Return all dsbmannschaft entries with the given Wettkampf-Id
     *
     * @param id from the Wettkampf
     * @return all dbsmannschaft entries with the given Wettkampf-Id
     */

    public List<DsbMannschaftBE> findAllByWettkampfId(final long id) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_WETTKAMPF_ID, id);}
    public List<DsbMannschaftBE> findVeranstaltungAndWettkampfById(final long id) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_VERSANSTALTUNGEN_BY_VEREIN, id);}
    /**
     * Return all dsbmannschaft entries that are currently in the waiting queue
     *
     * @return all dsbmannschaft entries in the waiting queue
     */

    public List<DsbMannschaftBE> findAllByWarteschlange() {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_WARTSCHLANGE);}

    /**
     * Return all dsbmannschaft entries with the given name
     *
     * @param name of the dsbmannschaft
     * @return all dbsmannschaft entries with the given name
     */

    public List<DsbMannschaftBE> findAllByName(final String name) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_NAME, new StringBuilder()
                                                                            .append("%")
                                                                            .append(name)
                                                                            .append("%")
                                                                            .toString());}

    /**
     * Return dsbmitglied entry with specific id
     *
     * @param id
     */
    public DsbMannschaftBE findById(final long id) {
        return basicDao.selectSingleEntity(MANNSCHAFT, FIND_BY_ID, id);
    }


    /**
     * Create a new dsbmitglied entry
     *
     * @param dsbMannschaftBE
     * @param currentDsbMannschaftId
     *
     * @return Business Entity corresponding to the created dsbmitglied entry
     */
    public DsbMannschaftBE create(final DsbMannschaftBE dsbMannschaftBE, final long currentUserId) {
        basicDao.setCreationAttributes(dsbMannschaftBE, currentUserId);

        return basicDao.insertEntity(MANNSCHAFT, dsbMannschaftBE);
    }


    /**
     * Update an existing dsbmitglied entry
     *
     * @param dsbMannschaftBE
     * @param currentDsbMannschaftId
     *
     * @return Business Entity corresponding to the updated dsbmitglied entry
     */
    public DsbMannschaftBE update(final DsbMannschaftBE dsbMannschaftBE, final long currentUserId) {
        basicDao.setModificationAttributes(dsbMannschaftBE, currentUserId);

        return basicDao.updateEntity(MANNSCHAFT, dsbMannschaftBE, MANNSCHAFT_BE_ID);
    }


    /**
     * Delete existing mannschaft entry
     *
     * @param dsbMannschaftBE
     * @param currentDsbMannschaftId
     */
    public void delete(final DsbMannschaftBE dsbMannschaftBE, final long currentUserId) {
        basicDao.setModificationAttributes(dsbMannschaftBE, currentUserId);

        basicDao.deleteEntity(MANNSCHAFT, dsbMannschaftBE, MANNSCHAFT_BE_ID);
    }

}
