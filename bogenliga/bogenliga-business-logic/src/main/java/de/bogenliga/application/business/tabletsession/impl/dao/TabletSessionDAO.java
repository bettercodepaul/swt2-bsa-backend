package de.bogenliga.application.business.tabletsession.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.tabletsession.impl.entity.TabletSessionBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 * @author Kay Scheerer
 */
@Repository
public class TabletSessionDAO {

    //define logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(TabletSessionDAO.class);

    //table name in the DB
    private static final String TABLE = "tablet_session";


    private static final String TABLET_SESSION_BE_WETTKAMPF_ID = "wettkampfId";
    private static final String TABLET_SESSION_BE_SCHEIBENNUMMER = "scheibennummer";
    private static final String TABLET_SESSION_BE_MATCH_ID = "matchId";
    private static final String TABLET_SESSION_BE_SATZNUMMER = "satznummer";
    private static final String TABLET_SESSION_BE_IS_ACTIVE = "active";


    private static final String TABLET_SESSION_TABLE_WETTKAMPF_ID = "tablet_session_wettkampf_id";
    private static final String TABLET_SESSION_TABLE_MATCH_ID = "tablet_session_match_id";
    private static final String TABLET_SESSION_TABLE_SCHEIBENNUMMER = "tablet_session_scheibennummer";
    private static final String TABLET_SESSION_TABLE_SATZNUMMER = "tablet_session_satznr";
    private static final String TABLET_SESSION_TABLE_IS_ACTIVE = "is_active";

    private static final BusinessEntityConfiguration<TabletSessionBE> TABLET_SESSION = new BusinessEntityConfiguration<>(
            TabletSessionBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    private static final String FIND_ALL = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .orderBy(TABLET_SESSION_TABLE_WETTKAMPF_ID)
            .compose().toString();

    private static final String FIND_BY_WETTKAMPF_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(TABLET_SESSION_TABLE_WETTKAMPF_ID)
            .orderBy(TABLET_SESSION_TABLE_SCHEIBENNUMMER)
            .compose().toString();

    private static final String FIND_BY_PK = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(TABLET_SESSION_TABLE_WETTKAMPF_ID)
            .andEquals(TABLET_SESSION_TABLE_SCHEIBENNUMMER)
            .compose().toString();

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public TabletSessionDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(TABLET_SESSION_TABLE_WETTKAMPF_ID, TABLET_SESSION_BE_WETTKAMPF_ID);
        columnsToFieldsMap.put(TABLET_SESSION_TABLE_SCHEIBENNUMMER, TABLET_SESSION_BE_SCHEIBENNUMMER);
        columnsToFieldsMap.put(TABLET_SESSION_TABLE_MATCH_ID, TABLET_SESSION_BE_MATCH_ID);
        columnsToFieldsMap.put(TABLET_SESSION_TABLE_SATZNUMMER, TABLET_SESSION_BE_SATZNUMMER);
        columnsToFieldsMap.put(TABLET_SESSION_TABLE_IS_ACTIVE, TABLET_SESSION_BE_IS_ACTIVE);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    public List<TabletSessionBE> findAll() {
        return basicDao.selectEntityList(TABLET_SESSION, FIND_ALL);
    }


    public List<TabletSessionBE> findByWettkampfId(Long wettkampfid) {
        return basicDao.selectEntityList(TABLET_SESSION, FIND_BY_WETTKAMPF_ID, wettkampfid);
    }


    public TabletSessionBE findByIdScheinebnummer(Long wettkampfid, Long scheibenNr) {
        return basicDao.selectSingleEntity(TABLET_SESSION, FIND_BY_PK, wettkampfid, scheibenNr);
    }


    public TabletSessionBE create(TabletSessionBE tabletSessionBE, Long currentUserId) {
        basicDao.setCreationAttributes(tabletSessionBE, currentUserId);

        return basicDao.insertEntity(TABLET_SESSION, tabletSessionBE);
    }


    public TabletSessionBE update(TabletSessionBE tabletSessionBE, Long currentUserId) {
        basicDao.setModificationAttributes(tabletSessionBE, currentUserId);

        return basicDao.updateEntity(TABLET_SESSION, tabletSessionBE, TABLET_SESSION_BE_WETTKAMPF_ID,
                TABLET_SESSION_BE_SCHEIBENNUMMER
        );
    }


    public void delete(TabletSessionBE tabletSessionBE, Long currentUserId) {
        basicDao.setModificationAttributes(tabletSessionBE, currentUserId);

        basicDao.deleteEntity(TABLET_SESSION, tabletSessionBE, TABLET_SESSION_BE_SCHEIBENNUMMER,
                TABLET_SESSION_BE_WETTKAMPF_ID);
    }
}
