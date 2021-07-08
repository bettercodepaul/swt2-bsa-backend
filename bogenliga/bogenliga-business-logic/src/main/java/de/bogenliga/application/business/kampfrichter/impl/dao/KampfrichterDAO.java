package de.bogenliga.application.business.kampfrichter.impl.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterExtendedBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;


/**
 * DataAccessObject for the kampfrichter entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 * Functionality to find all Kampfrichter who are not in a Wettkampftag (wettkampfid) but have the Kampfrichter-Lizenz is implemented here
 * @author Rahul Pöse
 */
@Repository
public class KampfrichterDAO implements DataAccessObject {

    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(KampfrichterDAO.class);

    // table name in the database
    private static final String TABLE = "kampfrichter";
    // business entity parameter names

    private static final String KAMPFRICHTER_BE_ID = "kampfrichterUserId";
    private static final String KAMPFRICHTER_BE_COMPETITION_ID = "kampfrichterWettkampfId";
    private static final String KAMPFRICHTER_BE_LEADING = "kampfrichterLeitend";

    private static final String KAMPFRICHTER_TABLE_ID = "kampfrichter_benutzer_id";
    private static final String KAMPFRICHTER_TABLE_COMPETITION_ID = "kampfrichter_wettkampf_id";
    private static final String KAMPFRICHTER_TABLE_LEADING = "kampfrichter_leitend";

    //For KampfrichterExtendedBE
    private static final String KAMPFRICHTER_BE_WETTKAMPFID = "kampfrichterExtendedWettkampfID";
    private static final String KAMPFRICHTER_BE_LEITEND = "kampfrichterExtendedLeitend";
    private static final String KAMPFRICHTER_BE_USERID = "kampfrichterExtendedUserID";
    private static final String KAMPFRICHTER_BE_VORNAME = "kampfrichterExtendedVorname";
    private static final String KAMPFRICHTER_BE_NACHNAME = "kampfrichterExtendedNachname";
    private static final String KAMPFRICHTER_BE_EMAIL = "kampfrichterExtendedEmail";

    private static final String KAMPFRICHTER_TABLE_USERID = "benutzer_id";
    private static final String KAMPFRICHTER_TABLE_VORNAME = "dsb_mitglied_vorname";
    private static final String KAMPFRICHTER_TABLE_NACHNAME = "dsb_mitglied_nachname";
    private static final String KAMPFRICHTER_TABLE_EMAIL = "benutzer_email";



    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<KampfrichterBE> KAMPFRICHTER = new BusinessEntityConfiguration<>(
            KampfrichterBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);
    private static final BusinessEntityConfiguration<KampfrichterExtendedBE> KAMPFRICHTEREXTENDED = new BusinessEntityConfiguration<>(
            KampfrichterExtendedBE.class, TABLE, getColumnsToFieldsMapExtended(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM kampfrichter"
                    + " ORDER BY kampfrichter_benutzer_id";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM kampfrichter "
                    + " WHERE kampfrichter_benutzer_id = ?";

    private static final String FIND_KAMPFRICHTER =
            "SELECT * "
                    + " FROM lizenz "
                    + " WHERE lizenz_typ = Kampfrichter AND lizenz_dsb_mitglied_id = ?";

    private static final String FIND_KAMPFRICHTER_NOT_WETTKAMPID =
            "Select benutzer.benutzer_id, dsb_mitglied.dsb_mitglied_vorname, dsb_mitglied.dsb_mitglied_nachname, benutzer.benutzer_email " +
                    "from lizenz, benutzer, dsb_mitglied " +
                    "where lizenz.lizenz_typ = 'Kampfrichter' " +
                    "and lizenz.lizenz_dsb_mitglied_id = benutzer.benutzer_dsb_mitglied_id " +
                    "and dsb_mitglied.dsb_mitglied_id = lizenz.lizenz_dsb_mitglied_id " +
                    "and benutzer.benutzer_id not in (" +
                    "select kampfrichter.kampfrichter_benutzer_id from kampfrichter, wettkampf " +
                    "where wettkampf.wettkampf_id= kampfrichter.kampfrichter_wettkampf_id " +
                    "and wettkampf.wettkampf_id=?)";

    private static final String FIND_KAMPFRICHTER_WETTKAMPID =
            "Select benutzer.benutzer_id, kampfrichter.kampfrichter_wettkampf_id, kampfrichter.kampfrichter_leitend, dsb_mitglied.dsb_mitglied_vorname, " +
                    "dsb_mitglied.dsb_mitglied_nachname, benutzer.benutzer_email " +
                    "from benutzer, dsb_mitglied, kampfrichter, wettkampf " +
                    "where kampfrichter.kampfrichter_wettkampf_id=wettkampf.wettkampf_id " +
                    "and kampfrichter.kampfrichter_wettkampf_id=? " +
                    "and kampfrichter.kampfrichter_benutzer_id= benutzer.benutzer_id " +
                    "and benutzer.benutzer_dsb_mitglied_id = dsb_mitglied.dsb_mitglied_id";

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public KampfrichterDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(KAMPFRICHTER_TABLE_ID, KAMPFRICHTER_BE_ID);
        columnsToFieldsMap.put(KAMPFRICHTER_TABLE_COMPETITION_ID, KAMPFRICHTER_BE_COMPETITION_ID);
        columnsToFieldsMap.put(KAMPFRICHTER_TABLE_LEADING, KAMPFRICHTER_BE_LEADING);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    private static Map<String, String> getColumnsToFieldsMapExtended() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(KAMPFRICHTER_TABLE_USERID, KAMPFRICHTER_BE_USERID);
        columnsToFieldsMap.put(KAMPFRICHTER_TABLE_EMAIL, KAMPFRICHTER_BE_EMAIL);
        columnsToFieldsMap.put(KAMPFRICHTER_TABLE_COMPETITION_ID, KAMPFRICHTER_BE_WETTKAMPFID);
        columnsToFieldsMap.put(KAMPFRICHTER_TABLE_LEADING, KAMPFRICHTER_BE_LEITEND);
        columnsToFieldsMap.put(KAMPFRICHTER_TABLE_VORNAME, KAMPFRICHTER_BE_VORNAME);
        columnsToFieldsMap.put(KAMPFRICHTER_TABLE_NACHNAME, KAMPFRICHTER_BE_NACHNAME);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return all kampfrichter entries
     */
    public List<KampfrichterBE> findAll() {
        return basicDao.selectEntityList(KAMPFRICHTER, FIND_ALL);
    }


    /**
     * Return kampfrichter entry with specific id
     *
     * @param userId
     */
    public KampfrichterBE findById(final long userId) {
        return basicDao.selectSingleEntity(KAMPFRICHTER, FIND_BY_ID, userId);
    }


    /**
     * Return kampfrichterExtended not in wettkampfid
     *
     * @param wettkampfId
     */
    public List<KampfrichterExtendedBE> findByWettkampfidNotInWettkampftag(final long wettkampfId){

        return basicDao.selectEntityList(KAMPFRICHTEREXTENDED, FIND_KAMPFRICHTER_NOT_WETTKAMPID, wettkampfId);
    }

    /**
     * Return kampfrichterExtended in wettkampfid
     *
     * @param wettkampfId
     */
    public List<KampfrichterExtendedBE> findByWettkampfidInWettkampftag(final long wettkampfId){

        return basicDao.selectEntityList(KAMPFRICHTEREXTENDED, FIND_KAMPFRICHTER_WETTKAMPID, wettkampfId);
    }


    /**
     * Create a new kampfrichter entry
     *
     * @param kampfrichterBE
     * @param currentKampfrichterUserId
     *
     * @return Business Entity corresponding to the created kampfrichter entry
     */
    public KampfrichterBE create(final KampfrichterBE kampfrichterBE, final long currentKampfrichterUserId) {
        basicDao.setCreationAttributes(kampfrichterBE, currentKampfrichterUserId);

        return basicDao.insertEntity(KAMPFRICHTER, kampfrichterBE);
    }


    /**
     * Update an existing dsbmitglied entry
     *
     * @param kampfrichterBE
     * @param currentKampfrichterUserId
     *
     * @return Business Entity corresponding to the updated kampfrichter entry
     */
    public KampfrichterBE update(final KampfrichterBE kampfrichterBE, final long currentKampfrichterUserId) {
        basicDao.setModificationAttributes(kampfrichterBE, currentKampfrichterUserId);

        return basicDao.updateEntity(KAMPFRICHTER, kampfrichterBE, KAMPFRICHTER_BE_ID);
    }


    /**
     * Delete existing kampfrichter entry
     *
     * @param kampfrichterBE
     * @param currentKampfrichterUserId
     */
    public void delete(final KampfrichterBE kampfrichterBE, final long currentKampfrichterUserId) {
        basicDao.setModificationAttributes(kampfrichterBE, currentKampfrichterUserId);

        basicDao.deleteEntity(KAMPFRICHTER, kampfrichterBE, KAMPFRICHTER_BE_ID, KAMPFRICHTER_BE_COMPETITION_ID);
    }
}