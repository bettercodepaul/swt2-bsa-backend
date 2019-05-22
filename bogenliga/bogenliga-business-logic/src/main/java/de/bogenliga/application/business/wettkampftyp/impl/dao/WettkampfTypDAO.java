package de.bogenliga.application.business.wettkampftyp.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampfTypBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class WettkampfTypDAO implements DataAccessObject {
    //define logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(WettkampfTypDAO.class);

    //table name in the DB
    private static final String TABLE = "wettkampftyp";


    //business entity parameters
    private static final String WETTKAMPFTYP_BE_ID = "wettkampftypId";
    private static final String WETTKAMPFTYP_BE_NAME = "wettkampftypId";

    private static final String WETTKAMPFTYP_TABLE_ID = "wettkampftyp_id";
    private static final String WETTKAMPFTYP_TABLE_NAME = "wettkampftyp_name";

    private static final BusinessEntityConfiguration<WettkampfTypBE> WETTKAMPFTYP = new BusinessEntityConfiguration<>(
            WettkampfTypBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    private final BasicDAO basicDao;

    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(WETTKAMPFTYP_TABLE_ID, WETTKAMPFTYP_BE_ID);
        columnsToFieldsMap.put(WETTKAMPFTYP_TABLE_NAME, WETTKAMPFTYP_BE_NAME);
        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }

    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public WettkampfTypDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }

    private static final String FIND_BY_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(WETTKAMPFTYP_TABLE_ID)
            .compose().toString();

    /**
     * Return all passe from one Wettkampf
     *
     * @param wettkampfTypId
     *
     * @return list of all passe from one Wettkampf in the database; empty list, if no passe are found
     */
    public WettkampfTypBE findByWettkampfId(Long wettkampfTypId) {
        return basicDao.selectSingleEntity(WETTKAMPFTYP, FIND_BY_ID, wettkampfTypId);
    }


}
