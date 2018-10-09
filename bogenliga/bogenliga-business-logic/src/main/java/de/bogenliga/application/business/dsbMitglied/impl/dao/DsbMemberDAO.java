package de.bogenliga.application.business.dsbMember.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.dsbMember.impl.entity.DsbMemberBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * DataAccessObject for the dsbMember entity in the database.
 *
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class DsbMemberDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(DsbMemberDAO.class);

    // table name in the database
    private static final String TABLE = "dsb_mitglied";
    // business entity parameter names

    private static final String DSBMITGLIED_BE_ID = "dsbMemberId";
    private static final String DSBMITGLIED_BE_FORENAME = "dsbMemberForename";
    private static final String DSBMITGLIED_BE_SURNAME = "dsbMemberSurname";
    private static final String DSBMITGLIED_BE_BIRTHDATE = "dsbMemberBirthdate";
    private static final String DSBMITGLIED_BE_NATIONALITY = "dsbMemberNationality";
    private static final String DSBMITGLIED_BE_MEMBERNUMBER = "dsbMemberMemberNumber";
    private static final String DSBMITGLIED_BE_CLUB_ID = "dsbMemberClubId";
    private static final String DSBMITGLIED_BE_USER_ID = "dsbMemberUserId";

    private static final String DSBMITGLIED_TABLE_ID = "dsb_mitglied_id";
    private static final String DSBMITGLIED_TABLE_FORENAME = "dsb_mitglied_vorname";
    private static final String DSBMITGLIED_TABLE_SURNAME = "dsb_mitglied_nachname";
    private static final String DSBMITGLIED_TABLE_BIRTHDATE = "dsb_mitglied_geburtsdatum";
    private static final String DSBMITGLIED_TABLE_NATIONALITY  = "dsb_mitglied_nationalitaet";
    private static final String DSBMITGLIED_TABLE_MEMBERNUMBER = "dsb_mitglied_mitgliedsnummer";
    private static final String DSBMITGLIED_TABLE_CLUB_ID = "dsb_mitglied_verein_id";
    private static final String DSBMITGLIED_TABLE_USER_ID = "dsb_mitglied_benutzer_id";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<DsbMemberBE> DSBMITGLIED = new BusinessEntityConfiguration<>(
            DsbMemberBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM benutzer";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM benutzer "
                    + " WHERE benutzer_id = ?";

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public DsbMemberDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(DSBMITGLIED_TABLE_ID, DSBMITGLIED_BE_ID);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_FORENAME, DSBMITGLIED_BE_FORENAME);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_SURNAME, DSBMITGLIED_BE_SURNAME);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_BIRTHDATE, DSBMITGLIED_BE_BIRTHDATE);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_NATIONALITY, DSBMITGLIED_BE_NATIONALITY);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_MEMBERNUMBER, DSBMITGLIED_BE_MEMBERNUMBER);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_CLUB_ID, DSBMITGLIED_BE_CLUB_ID);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_USER_ID, DSBMITGLIED_BE_USER_ID);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return all dsbMember entries
     */
    public List<DsbMemberBE> findAll() {
        return basicDao.selectEntityList(DSBMITGLIED, FIND_ALL);
    }


    /**
     * Return dsbMember entry with specific id
     *
     * @param id
     */
    public DsbMemberBE findById(final long id) {
        return basicDao.selectSingleEntity(DSBMITGLIED, FIND_BY_ID, id);
    }


    /**
     * Create a new dsbMember entry
     *
     * @param dsbMemberBE
     * @param currentDsbMemberId
     * @return Business Entity corresponding to the created dsbMember entry
     */
    public DsbMemberBE create(final DsbMemberBE dsbMemberBE, final long currentDsbMemberId) {
        basicDao.setCreationAttributes(dsbMemberBE, currentDsbMemberId);

        return basicDao.insertEntity(DSBMITGLIED, dsbMemberBE);
    }


    /**
     * Update an existing dsbMember entry
     *
     * @param dsbMemberBE
     * @param currentDsbMemberId
     * @return Business Entity corresponding to the updated dsbMember entry
     */
    public DsbMemberBE update(final DsbMemberBE dsbMemberBE, final long currentDsbMemberId) {
        basicDao.setModificationAttributes(dsbMemberBE, currentDsbMemberId);

        return basicDao.updateEntity(DSBMITGLIED, dsbMemberBE, DSBMITGLIED_BE_ID);
    }


    /**
     * Delete existing dsbMember entry
     *
     * @param dsbMemberBE
     * @param currentDsbMemberId
     */
    public void delete(final DsbMemberBE dsbMemberBE, final long currentDsbMemberId) {
        basicDao.setModificationAttributes(dsbMemberBE, currentDsbMemberId);

        basicDao.deleteEntity(DSBMITGLIED, dsbMemberBE, DSBMITGLIED_BE_ID);
    }
}
