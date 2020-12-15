package de.bogenliga.application.business.einstellungen.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.einstellungen.impl.entity.EinstellungenBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * DataAccessObject for the einstellungen entity in the database.
 *
 * @author Fabio Care, fabio_silas.care@student.reutlingen-university.de
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods.
 */
@Repository
public class EinstellungenDAO implements DataAccessObject {
    // define logger context
    private static final Logger LOG = LoggerFactory.getLogger(EinstellungenDAO.class);

    // table name in the DB
    private static final String TABLE = "configuration";

    // business entity parameters
    private static final String EINSTELLUNGEN_BE_ID = "einstellungenId";
    private static final String EINSTELLUNGEN_BE_KEY = "einstellungenKey";
    private static final String EINSTELLUNGEN_BE_VALUE = "einstellungenValue";

    // table columns
    private static final String EINSTELLUNGEN_TABLE_ID = "configuration_id";
    private static final String EINSTELLUNGEN_TABLE_KEY = "configuration_key";
    private static final String EINSTELLUNGEN_TABLE_VALUE = "configuration_value";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<EinstellungenBE> EINSTELLUNGEN = new BusinessEntityConfiguration<>(
            EinstellungenBE.class, TABLE, getColumnsToFieldsMap(), LOG);

    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM configuration";

    private static final String FIND_BY_ID =
            "SELECT * "
                    + " FROM configuration"
                    + " WHERE configuration_id = ?";



    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public EinstellungenDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();
        columnsToFieldsMap.put(EINSTELLUNGEN_TABLE_ID, EINSTELLUNGEN_BE_ID);
        columnsToFieldsMap.put(EINSTELLUNGEN_TABLE_KEY, EINSTELLUNGEN_BE_KEY);
        columnsToFieldsMap.put(EINSTELLUNGEN_TABLE_VALUE, EINSTELLUNGEN_BE_VALUE);

        //kann theoretisch weg
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * Return all einstellungen entries
     *
     * @return list of {@link EinstellungenBE} or an empty list if no result found
     */
    public List<EinstellungenBE> findAll() {
        return basicDao.selectEntityList(EINSTELLUNGEN, FIND_ALL);
    }


    /**
     * Create a new einstellungen entry
     *
     * @param einstellungenBE einstellung die angelegt werden soll
     * @param currentUserId   user der die Änderung vornimmt
     *
     * @return Business Entity corresponding to the created einstellungen entry
     */
    public EinstellungenBE create(final EinstellungenBE einstellungenBE, final Long currentUserId) {
        basicDao.setCreationAttributes(einstellungenBE, currentUserId);
        return basicDao.insertEntity(EINSTELLUNGEN, einstellungenBE);
    }

    public EinstellungenBE findById(final long id) {
        return basicDao.selectSingleEntity(EINSTELLUNGEN, FIND_BY_ID, id);
    }



    /**
     * Update an existing einstellungen entry
     *
     * @param einstellungenBE einstellung die geändert werden soll
     * @param currentUserId   user der die Änderung vornimmt
     *
     * @return Business Entity corresponding to the updated einstellungen entry
     */
    public EinstellungenBE update(final EinstellungenBE einstellungenBE, final Long currentUserId) {
        basicDao.setModificationAttributes(einstellungenBE, currentUserId);
        return basicDao.updateEntity(EINSTELLUNGEN, einstellungenBE, EINSTELLUNGEN_BE_ID);
    }


    /**
     * Delete existing einstellungen
     *
     * @param einstellungenBE einstellung die gelöscht werden soll
     * @param currentUserId   user der die Änderung vornimmt
     */
    public void delete(final EinstellungenBE einstellungenBE, final Long currentUserId) {
        basicDao.setModificationAttributes(einstellungenBE, currentUserId);
        basicDao.deleteEntity(EINSTELLUNGEN, einstellungenBE, EINSTELLUNGEN_BE_ID);
    }

}
