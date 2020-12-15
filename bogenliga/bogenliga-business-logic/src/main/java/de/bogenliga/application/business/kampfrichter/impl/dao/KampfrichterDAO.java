package de.bogenliga.application.business.kampfrichter.impl.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.SQL;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;

/**
 * DataAccessObject for the kampfrichter entity in the database.
 * <p>
 * Use a {@link BusinessEntityConfiguration} for each entity to configure the generic {@link BasicDAO} methods
 *
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

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<KampfrichterBE> KAMPFRICHTER = new BusinessEntityConfiguration<>(
            KampfrichterBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

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

        basicDao.deleteEntity(KAMPFRICHTER, kampfrichterBE, KAMPFRICHTER_BE_ID);
    }


    // TODO: Delete all this if not needed
    public void myDelete(final KampfrichterBE kampfrichterBE) {
        String string = String.format(
                "DELETE FROM kampfrichter WHERE kampfrichter_benutzer_id = %s AND kampfrichter_wettkampf_id = %s;",
                kampfrichterBE.getKampfrichterUserId().toString(),
                kampfrichterBE.getKampfrichterWettkampfId().toString());
//        String.format("The user with the username %s already exists.", user.getUsername())
    }


//    public static SQL.SQLWithParameter deleteSQL(final Object updateObj, final String tableName,
//                                                 final String[] fieldSelector,
//                                                 final Map<String, String> columnToFieldMapping) {
//        final SQL.SQLWithParameter sqlWithParameter = new SQL().new SQLWithParameter();
//        final StringBuilder sql = new StringBuilder();
//        final List<Object> para;
//
//        sql.append("DELETE FROM ");
//
//        try {
//            final String tName = updateObj.getClass().getSimpleName();
//
//            if (tableName != null) {
//                sql.append(tableName);
//            } else {
//                sql.append(tName);
//            }
//
//            final Field[] fields = updateObj.getClass().getDeclaredFields();
//
//            para = appendFieldsToDeleteStatement(updateObj, fieldSelector, fields);
//        } catch (final SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
//                | InvocationTargetException e) {
//            throw new TechnicalException(ErrorCode.DATABASE_ERROR, e);
//        }
//
//        sqlWithParameter.setParameter(para.toArray());
//        return appendWhereStatements(sql, fieldSelector, columnToFieldMapping, sqlWithParameter);
//    }
}
