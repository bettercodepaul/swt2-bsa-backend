package de.bogenliga.application.business.veranstaltung.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * TODO [AL] class documentation
 *
 *@author Daniel Schott, daniel.schott@student.reutlingen-university.de
 */
@Repository
public class VeranstaltungDAO implements DataAccessObject{


    private static final String VERANSTALTUNG_BE_ID = "veranstaltung_id";
    private static final String VERANSTALTUNG_BE_WETTKAMPFTYP_ID= "veranstaltung_wettkampftyp_id";
    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_NAME= "veranstaltung_name";
    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_SPORTJAHR = "veranstaltung_sportjahr";
    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_MELDEDEADLINE = "veranstaltung_meldedeadline";
    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_LIGALEITER_ID= "veranstaltung_ligaleiter_id";
    private static final String VERANSTALTUNG_BE_VERANSTALTUNG_LIGA_ID = "veranstaltung_liga_id";




    private static final String VERANSTALTUNG_TABLE_ID = "veranstaltung_id";
    private static final String VERANSTALTUNG_TABLE_WETTKAMPFTYP_ID= "veranstaltung_wettkampftyp_id";
    private static final String VERANSTALTUNG_TABLE_NAME= "veranstaltung_name";
    private static final String VERANSTALTUNG_TABLE_SPORTJAHR = "veranstaltung_sportjahr";
    private static final String VERANSTALTUNG_TABLE_MELDEDEADLINE = "veranstaltung_meldedeadline";
    private static final String VERANSTALTUNG_TABLE_LIGALEITER_ID= "veranstaltung_ligaleiter_id";
    private static final String VERANSTALTUNG_TABLE_LIGA_ID = "veranstaltung_liga_id";



    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(VeranstaltungDAO.class);

    // table name in the database
    private static final String TABLE = "veranstaltung";
    // business entity parameter names



    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<VeranstaltungBE> VERANSTALTUNG= new BusinessEntityConfiguration<>(
            VeranstaltungBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    /*
     * SQL queries
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM veranstaltung"
                    + " ORDER BY veranstaltung.veranstaltung_id";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM veranstaltung "
                    + " WHERE veranstaltung_id = ?";

    private final BasicDAO basicDao;

    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public VeranstaltungDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }



    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_ID, VERANSTALTUNG_BE_ID);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_WETTKAMPFTYP_ID, VERANSTALTUNG_BE_WETTKAMPFTYP_ID);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_NAME, VERANSTALTUNG_BE_VERANSTALTUNG_NAME);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_SPORTJAHR, VERANSTALTUNG_BE_VERANSTALTUNG_SPORTJAHR);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_MELDEDEADLINE, VERANSTALTUNG_BE_VERANSTALTUNG_MELDEDEADLINE);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_LIGALEITER_ID, VERANSTALTUNG_BE_VERANSTALTUNG_LIGALEITER_ID);
        columnsToFieldsMap.put(VERANSTALTUNG_TABLE_LIGA_ID, VERANSTALTUNG_BE_VERANSTALTUNG_LIGA_ID);



        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return all Veranstaltung entries from the database
     */
    public List<VeranstaltungBE> findAll()
    {
        return basicDao.selectEntityList(VERANSTALTUNG, FIND_ALL);
    }


    /**
     * Return Veranstaltung entry with specific id
     *
     * @param id - selected ID of Veranstaltung you want to recieve
     */
    public VeranstaltungBE findById(final long id) {
        return basicDao.selectSingleEntity(VERANSTALTUNG, FIND_BY_ID, id);
    }

    /**
     * Delete existing veranstaltung entrycreated_at_utc
     *
     * @param veranstaltungBE
     * @param currentDsbMitgliedId
     */
    public void delete(final VeranstaltungBE veranstaltungBE, final long currentDsbMitgliedId) {
        basicDao.setModificationAttributes(veranstaltungBE, currentDsbMitgliedId);

        basicDao.deleteEntity(VERANSTALTUNG, veranstaltungBE, VERANSTALTUNG_BE_ID);
    }

    /**
     * Update an existing veranstaltung entry
     *
     * @param veranstaltungBE
     * @param currentDsbMitgliedId
     * @return Business Entity corresponding to the updated dsbmitglied entry
     */
    public VeranstaltungBE update(final VeranstaltungBE veranstaltungBE, final long currentDsbMitgliedId) {
        basicDao.setModificationAttributes(veranstaltungBE, currentDsbMitgliedId);

        return basicDao.updateEntity(VERANSTALTUNG, veranstaltungBE, VERANSTALTUNG_BE_ID);
    }

    /**
     * Create a new veranstaltung entry
     *
     * @param veranstaltungBE
     * @param currentDsbMitgliedId
     * @return Business Entity corresponding to the created dsbmitglied entry
     */
    public VeranstaltungBE create(final VeranstaltungBE veranstaltungBE, final long currentDsbMitgliedId) {
        basicDao.setCreationAttributes(veranstaltungBE, currentDsbMitgliedId);

        return basicDao.insertEntity(VERANSTALTUNG, veranstaltungBE);
    }

}
