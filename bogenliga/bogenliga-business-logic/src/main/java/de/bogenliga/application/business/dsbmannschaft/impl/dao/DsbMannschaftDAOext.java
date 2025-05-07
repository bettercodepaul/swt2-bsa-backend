package de.bogenliga.application.business.dsbmannschaft.impl.dao;

import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBEext;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.nio.channels.FileChannel;
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
    private static final String MANNSCHAFT_BE_VEREINID = "vereinId";
    private static final String MANNSCHAFT_BE_NUMBER = "nummer";
    private static final String MANNSCHAFT_BE_EVENTID = "veranstaltungId";
    private static final String MANNSCHAFT_BE_USER_ID = "benutzerId";
    private static final String MANNSCHAFT_BE_SORTIERUNG = "sortierung";
    private static final String MANNSCHAFT_BE_SPORTJAHR = "sportjahr";
    private static final String MANNSCHAFT_BE_VEREINNAME = "vereinName";
    private static final String MANNSCHAFT_BE_WETTKAMPFORT = "wettkampfOrtsname";
    private static final String MANNSCHAFT_BE_WETTKAMPTAG = "wettkampfTag";
    private static final String MANNSCHAFT_BE_VERANSTALTUNGSNAME = "veranstaltungName";

    private static final String MANNSCHAFT_TABLE_ID = "mannschaft_id";
    private static final String MANNSCHAFT_TABLE_VEREINID = "mannschaft_verein_id";
    private static final String MANNSCHAFT_TABLE_NUMBER = "mannschaft_nummer";
    private static final String MANNSCHAFT_TABLE_USER_ID = "mannschaft_benutzer_id";
    private static final String MANNSCHAFT_TABLE_EVENTID = "mannschaft_veranstaltung_id";
    private static final String MANNSCHAFT_TABLE_SORTIERUNG = "mannschaft_sortierung";
    private static final String MANNSCHAFT_TABLE_SPORTJAHR = "mannschaft_sportjahr";
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
            " SELECT DISTINCT "
                    + " m.*, "
                    + " MAX(v.verein_name) AS vereinName "
                    + " FROM "
                    + " mannschaft m "
                    + " JOIN "
                    + " wettkampf w ON w.wettkampf_veranstaltung_id = m.mannschaft_veranstaltung_id "
                    + " JOIN "
                    + " veranstaltung ve ON ve.veranstaltung_id = m.mannschaft_veranstaltung_id "
                    + " LEFT JOIN "
                    + " verein v ON m.mannschaft_verein_id = v.verein_id "
                    + " WHERE "
                    + " w.wettkampf_id = ? "
                    + " GROUP BY "
                    + " m.mannschaft_id ";
    private static final String FIND_VERSANSTALTUNGEN_BY_VEREIN =
            "SELECT veranstaltung_name, wettkampf_tag, wettkampf_ortsname, verein_name, mannschaft_nummer "
                    + "FROM veranstaltung ver "
                    + "JOIN mannschaft m ON ver.veranstaltung_id = m.mannschaft_veranstaltung_id "
                    + "JOIN verein v ON m.mannschaft_verein_id = v.verein_id "
                    + "JOIN wettkampf ON ver.veranstaltung_id = wettkampf.wettkampf_veranstaltung_id "
                    + "WHERE v.verein_id = ? "
                    + "AND ver.veranstaltung_phase = 2 "
                    + "GROUP BY mannschaft_nummer, veranstaltung_name, verein_name, wettkampf_ortsname, wettkampf_tag; ";

    private static final String FIND_ALL_BY_WETTKAMPF_ID_WITH_NAME =
            " SELECT "
                    + " m.*, "
                    + " v.verein_name as vereinName "
                    + " FROM mannschaft m "
                    + " LEFT JOIN verein v ON m.mannschaft_verein_id = v.verein_id "
                    + " WHERE m.mannschaft_veranstaltung_id = ? "
                    + " ORDER BY m.mannschaft_sortierung; ";

    private static final String FIND_BY_ID_WITH_NAME =
            " SELECT "
                    + "m.*, "
                    + " v.verein_name AS vereinName "
                    + " FROM "
                    + " mannschaft m "
                    + " LEFT JOIN "
                    + " verein v ON m.mannschaft_verein_id = v.verein_id "
                    + " WHERE "
                    + " m.mannschaft_id = ? ";

    private static final String FIND_ALL_BY_VEREINS_ID_WITH_NAME =
            " SELECT "
                    + " m.*, "
                    + " v.verein_name AS vereinName "
                    + " FROM "
                    + " mannschaft m "
                    + " LEFT JOIN "
                    + " verein v ON m.mannschaft_verein_id = v.verein_id "
                    + " WHERE mannschaft_verein_id = ? "
                    + " ORDER BY mannschaft_nummer ";

    private static final String FIND_ALL_BY_NAME_WITH_NAME =
            " SELECT "
                    + " a.*, "
                    + " b.verein_name AS vereinName "
                    + " FROM mannschaft a, verein b "
                    + " WHERE a.mannschaft_verein_id = b.verein_id "
                    + " AND CONCAT(LOWER(b.verein_name), ' ' , "
                    + " LOWER(CAST(a.mannschaft_nummer AS TEXT))) "
                    + " LIKE LOWER(?) "
                    + " AND mannschaft_veranstaltung_id IS NULL ";

    private static final String FIND_ALL_BY_WARTESCHLANGE_WITH_NAME =
            " SELECT "
                    + " m.*, "
                    + " v.verein_name AS vereinName "
                    + " FROM mannschaft m "
                    + " JOIN verein v ON m.mannschaft_verein_id = v.verein_id "
                    + " WHERE "
                    + " m.mannschaft_veranstaltung_id IS NULL "
                    + " AND m.mannschaft_sportjahr IS NULL ";

    private static final String FIND_ALL_WITH_NAME =
            " SELECT "
                    + " m.*, "
                    + " v.verein_name AS vereinName "
                    + " FROM "
                    + " mannschaft m "
                    + " JOIN verein v ON m.mannschaft_verein_id = v.verein_id "
                    + " ORDER BY "
                    + " mannschaft_id ";

    private static final String FIND_ALL_BY_VERANSTALTUNGS_ID_WITH_NAME =
            " SELECT "
                    + " m.*, "
                    + " v.verein_name AS vereinName "
                    + " FROM mannschaft m "
                    + " JOIN verein v ON m.mannschaft_verein_id = v.verein_id "
                    + " WHERE "
                    + " mannschaft_veranstaltung_id = ? "
                    + " ORDER BY mannschaft_sortierung ";

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
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_VEREINID, MANNSCHAFT_BE_VEREINID);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_NUMBER, MANNSCHAFT_BE_NUMBER);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_SORTIERUNG, MANNSCHAFT_BE_SORTIERUNG);
        columnsToFieldsMap.put(MANNSCHAFT_TABLE_SPORTJAHR, MANNSCHAFT_BE_SPORTJAHR);
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
    public List<DsbMannschaftBEext> findAllByWettkampfIdWithName(final long id) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_WETTKAMPF_ID_WITH_NAME, id);}
    public DsbMannschaftBEext findByIdwithName(final long id) {
        return basicDao.selectSingleEntity(MANNSCHAFT, FIND_BY_ID_WITH_NAME, id);}
    public List<DsbMannschaftBEext> findAllByVereinsIdwithName(final long id) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_VEREINS_ID_WITH_NAME, id);}
    public List<DsbMannschaftBEext> findAllByNameWithName(final String name) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_NAME_WITH_NAME, new StringBuilder().append("%").append(name).append("%").toString());}
    public List<DsbMannschaftBEext> findAllByWarteschlangewithName() {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_WARTESCHLANGE_WITH_NAME);}
    public List<DsbMannschaftBEext> findAllwithName() {
        return  basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_WITH_NAME);}
    public List<DsbMannschaftBEext> findAllByVeranstaltungsIdwithName(final long id) {
        return basicDao.selectEntityList(MANNSCHAFT, FIND_ALL_BY_VERANSTALTUNGS_ID_WITH_NAME, id);}


    public FileChannel findById(long teamId) {
        // TODO
        
        return null;
    }

    /**
     * Return all dsbmannschaft entries that are currently in the waiting queue
     *
     * @return all dsbmannschaft entries in the waiting queue
     */

}
