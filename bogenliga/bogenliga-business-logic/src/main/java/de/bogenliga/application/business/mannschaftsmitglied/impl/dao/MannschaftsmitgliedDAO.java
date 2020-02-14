package de.bogenliga.application.business.mannschaftsmitglied.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.database.queries.QueryBuilder;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
@Repository
public class MannschaftsmitgliedDAO implements DataAccessObject {


    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(MannschaftsmitgliedDAO.class);

    // table name in the database
    private static final String TABLE = "mannschaftsmitglied";
    private static final String TABLE_ALIAS = "m";

    private static final String DSB_MITGLIED_TABLE = "dsb_mitglied";
    private static final String DSB_MITGLIED_TABLE_ALIAS = "d";
    private static final String DSB_MITGLIED_TABLE_MITGLIED_ID = "dsb_mitglied_id";
    // business entity parameter names

    private static final String MANNSCHAFTSMITGLIED_BE_ID = "id";
    private static final String MANNSCHAFTSMITGLIED_BE_TEAM_ID = "mannschaftId";
    private static final String MANNSCHAFTSMITGLIED_BE_DSB_MITGLIED_ID = "dsbMitgliedId";
    private static final String MANNSCHAFTSMITGLIED_BE_INSERT = "dsbMitgliedEingesetzt";

    // new: important for the join with dsb_mitglied
    private static final String DSBMITGLIED_BE_FORENAME = "dsbMitgliedVorname";
    private static final String DSBMITGLIED_BE_SURNAME = "dsbMitgliedNachname";

    private static final String MANNSCHAFTSMITGLIED_TABLE_ID = "mannschaftsmitglied_id";
    private static final String MANNSCHAFTSMITGLIED_TABLE_TEAM_ID = "mannschaftsmitglied_mannschaft_id";
    private static final String MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID = "mannschaftsmitglied_dsb_mitglied_id";
    private static final String MANNSCHAFTSMITGLIED_TABLE_EMPLOYED = "mannschaftsmitglied_dsb_mitglied_eingesetzt";

    // new: important for the join with dsb_mitglied
    private static final String DSBMITGLIED_TABLE_FORENAME = "dsb_mitglied_vorname";
    private static final String DSBMITGLIED_TABLE_SURNAME = "dsb_mitglied_nachname";

    private static final String[] selectedFields = {
            MANNSCHAFTSMITGLIED_TABLE_ID, MANNSCHAFTSMITGLIED_TABLE_TEAM_ID, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID,
            MANNSCHAFTSMITGLIED_TABLE_EMPLOYED, DSBMITGLIED_TABLE_FORENAME, DSBMITGLIED_TABLE_SURNAME
    };

    private static final String FIND_ALL = new QueryBuilder()
            .selectFields(selectedFields)
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .orderBy(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID)
            .compose().toString();

    private static final String FIND_BY_TEAM_ID = new QueryBuilder()
            .selectFields(selectedFields)
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .whereEquals(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID)
            .compose().toString();

    private static final String FIND_BY_MEMBER_AND_TEAM_ID = new QueryBuilder()
            .selectFields(selectedFields)
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .whereEquals(MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID)
            .andEquals(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID)
            .compose().toString();

    private static final String FIND_BY_MEMBER_ID = new QueryBuilder()
            .selectFields(selectedFields)
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .whereEquals(MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID)
            .compose().toString();

    private static final String FIND_BY_DSBMITGLIED_ID = new QueryBuilder()
            .selectAll()
            .from(TABLE)
            .whereEquals(MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID)
            .compose().toString();

    //hier suchen wir  alle Teammtiglieder, die eingesetzt wurden
    // d.h. nicht nur gemeldet, sondern sie haben auch Pfeilwerte erfasst
    private static final String FIND_ALL_SCHUETZE_TEAM = new QueryBuilder()
            .selectFields(selectedFields)
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .whereGteRaw(MANNSCHAFTSMITGLIED_TABLE_EMPLOYED, "1")
            .andEquals(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID)
            .orderBy(MANNSCHAFTSMITGLIED_TABLE_ID)
            .compose().toString();

    //hier suchen wir  alle Teammtiglieder, die potentiell eingesetzt werden könnten
    // d.h. sie wurden für die Mannschaft gemeldet.
    private static final String FIND_ALL_SCHUETZE_TEAM_GEMELDET = new QueryBuilder()
            .selectFields(selectedFields)
            .from(TABLE, TABLE_ALIAS)
            .join(DSB_MITGLIED_TABLE, DSB_MITGLIED_TABLE_ALIAS)
            .on(TABLE_ALIAS, MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, DSB_MITGLIED_TABLE_ALIAS,
                    DSB_MITGLIED_TABLE_MITGLIED_ID)
            .whereEquals(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID)
            .orderBy(MANNSCHAFTSMITGLIED_TABLE_ID)
            .compose().toString();

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<MannschaftsmitgliedBE> MANNSCHAFTSMITGLIED = new BusinessEntityConfiguration<>(
            MannschaftsmitgliedBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);


    private final BasicDAO basicDao;


    @Autowired
    public MannschaftsmitgliedDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(MANNSCHAFTSMITGLIED_TABLE_ID, MANNSCHAFTSMITGLIED_BE_ID);
        columnsToFieldsMap.put(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID, MANNSCHAFTSMITGLIED_BE_TEAM_ID);
        columnsToFieldsMap.put(MANNSCHAFTSMITGLIED_TABLE_DSB_MITGLIED_ID, MANNSCHAFTSMITGLIED_BE_DSB_MITGLIED_ID);
        columnsToFieldsMap.put(MANNSCHAFTSMITGLIED_TABLE_EMPLOYED, MANNSCHAFTSMITGLIED_BE_INSERT);

        // new: important for the join with dsb_mitglied
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_FORENAME, DSBMITGLIED_BE_FORENAME);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_SURNAME, DSBMITGLIED_BE_SURNAME);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());
        return columnsToFieldsMap;
    }


    /**
     * Return all mannschaftsmitglied entries
     */
    public List<MannschaftsmitgliedBE> findAll() {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED, FIND_ALL);
    }


    /**
     * return all members in the team
     *
     * @param id
     *
     * @return
     */
    public List<MannschaftsmitgliedBE> findByTeamId(final long id) {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED, FIND_BY_TEAM_ID, id);
    }


    public List<MannschaftsmitgliedBE> findAllSchuetzeInTeam(final long id) {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED, FIND_ALL_SCHUETZE_TEAM, id);
    }

    public List<MannschaftsmitgliedBE> findAllSchuetzeInTeamGemeldet(final long id) {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED, FIND_ALL_SCHUETZE_TEAM_GEMELDET, id);
    }


    public MannschaftsmitgliedBE findByMemberAndTeamId(long teamId, final long memberId) {
        MannschaftsmitgliedBE test;
        test = basicDao.selectSingleEntity(MANNSCHAFTSMITGLIED, FIND_BY_MEMBER_AND_TEAM_ID, memberId, teamId);
        return test;
    }

    public List<MannschaftsmitgliedBE> findByMemberId(final long memberId) {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED, FIND_BY_DSBMITGLIED_ID, memberId);
    }


    public MannschaftsmitgliedBE create(final MannschaftsmitgliedBE mannschaftsmitgliedBE, final long currentMemberId) {
        basicDao.setCreationAttributes(mannschaftsmitgliedBE, currentMemberId);

        return basicDao.insertEntity(MANNSCHAFTSMITGLIED, mannschaftsmitgliedBE);
    }


    public MannschaftsmitgliedBE update(final MannschaftsmitgliedBE mannschaftsmitgliedBE, final long currentMemberId) {
        basicDao.setModificationAttributes(mannschaftsmitgliedBE, currentMemberId);

        return basicDao.updateEntity(MANNSCHAFTSMITGLIED, mannschaftsmitgliedBE, MANNSCHAFTSMITGLIED_BE_DSB_MITGLIED_ID);
    }


    public void delete(final MannschaftsmitgliedBE mannschaftsmitgliedBE, final long currentMemberId) {
        basicDao.setModificationAttributes(mannschaftsmitgliedBE, currentMemberId);

        basicDao.deleteEntity(MANNSCHAFTSMITGLIED, mannschaftsmitgliedBE, MANNSCHAFTSMITGLIED_BE_ID);
    }


    public boolean checkExistingSchuetze(long teamId, final long memberId) {
        return findByMemberAndTeamId(teamId, memberId).getDsbMitgliedEingesetzt() > 0;
    }
}