package de.bogenliga.application.business.trigger.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterBE;
import de.bogenliga.application.business.trigger.impl.entity.MigrationTimestampBE;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;


/**
 * TODO [AL] class documentation
 *
 * @author Benedikt Holst
 */
@Repository
public class MigrationTimestampDAO implements DataAccessObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            MigrationTimestampDAO.class);


    private static final String TABLE = "Migrationtimestamp";

    private static final String SYNCDATATRIGGER_BE_TIMESTAMP = "Migrationtimestamp";
    private static final String SYNCDATATRIGGER_BE_ID = "id";

    private static final String SYNCDATATRIGGER_TABLE_TIMESTAMP = "Migrationtimestamp";
    private static final String SYNCDATATRIGGER_TABLE_ID = "id";

    private static final BusinessEntityConfiguration<MigrationTimestampBE> SYNCDATATRIGGER = new BusinessEntityConfiguration<>(
            MigrationTimestampBE.class, TABLE, getColumsToFieldsMap(), LOGGER);

    /**private final BasicDAO basicDao;

     public MigrationTimestampBE create(final MigrationTimestampBE migrationTimestampBE) {
     basicDao.setCreationAttributes(migrationTimestampBE);

     return basicDao.insertEntity(SYNCDATATRIGGER, migrationTimestampBE);
     }
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM Migrationtimestamp";
    private final BasicDAO basicDAO;


    @Autowired
    public MigrationTimestampDAO(final BasicDAO basicDAO) {
        this.basicDAO = basicDAO;
    }


    private static Map<String, String> getColumsToFieldsMap() {
        final Map<String, String> columnsToFieldMap = new HashMap<>();

        columnsToFieldMap.put(SYNCDATATRIGGER_TABLE_TIMESTAMP, SYNCDATATRIGGER_BE_TIMESTAMP);
        columnsToFieldMap.put(SYNCDATATRIGGER_TABLE_ID, SYNCDATATRIGGER_BE_ID);

        columnsToFieldMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());
        return columnsToFieldMap;

    }
    public List<MigrationTimestampBE> findAll() {
        return basicDAO.selectEntityList(SYNCDATATRIGGER, FIND_ALL);
    }

    public MigrationTimestampBE create(final MigrationTimestampBE timestampBE) {
        return basicDAO.insertEntity(SYNCDATATRIGGER, timestampBE);
    }

    public MigrationTimestampBE update(final MigrationTimestampBE timestampBE) {
        return basicDAO.updateEntity(SYNCDATATRIGGER, timestampBE);
    }
}