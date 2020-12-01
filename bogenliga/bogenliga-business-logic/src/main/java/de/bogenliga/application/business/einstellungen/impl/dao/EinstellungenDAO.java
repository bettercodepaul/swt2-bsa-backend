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
 * TODO [AL] class documentation
 *
 * @author Fabio Care, fabio_silas.care@student.reutlingen-university.de
 */
@Repository
public class EinstellungenDAO implements DataAccessObject {
    // define logger context
    private static final Logger LOG = LoggerFactory.getLogger(EinstellungenDAO.class);

    // table name in the DB
    private static final String TABLE = "einstellungen";

    // business entity parameters
    private static final String EINSTELLUNGEN_ID = "einstellungenId";
    private static final String EINSTELLUNGEN_KEY = "einstellungenKey";
    private static final String EINSTELLUNGEN_VALUE = "einstellungenValue";

    // table columns
    private static final String EINSTELLUNGEN_TABLE_ID = "einstellungen_id";
    private static final String EINSTELLUNGEN_TABLE_KEY = "einstellungen_key";
    private static final String EINSTELLUNGEN_TABLE_VALUE = "einstellungen_value";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<EinstellungenBE> EINSTELLUNGEN = new BusinessEntityConfiguration<>(
            EinstellungenBE.class, TABLE, getColumnsToFieldsMap(), LOG);

    private static final String FIND_ALL =
            "SELEC * "
                    + "FROM einstellungen";


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

        columnsToFieldsMap.put(EINSTELLUNGEN_TABLE_ID, EINSTELLUNGEN_ID);
        columnsToFieldsMap.put(EINSTELLUNGEN_TABLE_KEY, EINSTELLUNGEN_KEY);
        columnsToFieldsMap.put(EINSTELLUNGEN_TABLE_VALUE, EINSTELLUNGEN_VALUE);

        return columnsToFieldsMap;
    }

    /**
     * Return all einstellungen entries
     */
    public List<EinstellungenBE> findAll() {
        return basicDao.selectEntityList(EINSTELLUNGEN, FIND_ALL);
    }


    /**
     * Create a new einstellungen entry
     *
     * @param einstellungenBE
     * @param currentUserId
     *
     * @return Business Entity corresponding to the created einstellungen entry
     */
    public EinstellungenBE create(final EinstellungenBE einstellungenBE, final Long currentUserId) {
        basicDao.setCreationAttributes(einstellungenBE, currentUserId);
        return basicDao.insertEntity(EINSTELLUNGEN, einstellungenBE);
    }


    /**
     * Update an existing einstellungen entry
     *
     * @param einstellungenBE
     * @param currentUserId
     *
     * @return Business Entity corresponding to the updated einstellungen entry
     */
    public EinstellungenBE update(final EinstellungenBE einstellungenBE, final Long currentUserId) {
        basicDao.setModificationAttributes(einstellungenBE, currentUserId);
        return basicDao.updateEntity(EINSTELLUNGEN, einstellungenBE, EINSTELLUNGEN_ID);
    }


    /**
     * Delete existing einstellungen
     *
     * @param einstellungenBE
     * @param currentUserId
     */
    public void delete(final EinstellungenBE einstellungenBE, final Long currentUserId) {
        basicDao.setModificationAttributes(einstellungenBE, currentUserId);
        basicDao.deleteEntity(EINSTELLUNGEN, einstellungenBE, EINSTELLUNGEN_ID);
    }

}
