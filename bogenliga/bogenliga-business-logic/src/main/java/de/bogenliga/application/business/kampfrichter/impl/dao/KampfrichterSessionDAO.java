package de.bogenliga.application.business.kampfrichter.impl.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.kampfrichter.impl.entity.KampfrichterSessionEntity;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 * DAO for kampfrichter_session table.
 * One session per Wettkampf — stores the token used for QR-code based access.
 */
@Repository
public class KampfrichterSessionDAO implements DataAccessObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(KampfrichterSessionDAO.class);
    private static final String TABLE = "kampfrichter_session";

    private static final String COL_ID           = "id";
    private static final String COL_TOKEN        = "token";
    private static final String COL_WETTKAMPF_ID = "wettkampf_id";

    private static final BusinessEntityConfiguration<KampfrichterSessionEntity> TABLE_CONFIG =
            new BusinessEntityConfiguration<>(KampfrichterSessionEntity.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    @Autowired
    private BasicDAO basicDao;

    private static Map<String, String> getColumnsToFieldsMap() {
        Map<String, String> map = new HashMap<>();
        map.put(COL_ID, "id");
        map.put(COL_TOKEN, "token");
        map.put(COL_WETTKAMPF_ID, "wettkampfId");
        map.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());
        return map;
    }

    public Optional<KampfrichterSessionEntity> findByWettkampfId(long wettkampfId) {
        String sql = new QueryBuilder()
                .selectAll()
                .from(TABLE)
                .whereEquals(COL_WETTKAMPF_ID)
                .compose().toString();
        return basicDao.selectEntityList(TABLE_CONFIG, sql, wettkampfId).stream().findFirst();
    }

    public Optional<KampfrichterSessionEntity> findByWettkampfIdAndToken(long wettkampfId, String token) {
        String sql = new QueryBuilder()
                .selectAll()
                .from(TABLE)
                .whereEquals(COL_WETTKAMPF_ID)
                .andEquals(COL_TOKEN)
                .compose().toString();
        return basicDao.selectEntityList(TABLE_CONFIG, sql, wettkampfId, token).stream().findFirst();
    }

    public KampfrichterSessionEntity create(KampfrichterSessionEntity entity, long currentUserId) {
        basicDao.setCreationAttributes(entity, currentUserId);
        return basicDao.insertEntity(TABLE_CONFIG, entity);
    }

    public KampfrichterSessionEntity update(KampfrichterSessionEntity entity, long currentUserId) {
        basicDao.setModificationAttributes(entity, currentUserId);
        return basicDao.updateEntity(TABLE_CONFIG, entity, COL_ID);
    }
}
