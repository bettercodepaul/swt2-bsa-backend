package de.bogenliga.application.business.mannschaftsmitglied.impl.dao;

import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.mannschaftsmitglied.impl.entity.MannschaftsmitgliedBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//
/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Repository
public class MannschaftsmitgliedDAO implements DataAccessObject {


    // define the logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(MannschaftsmitgliedDAO.class);

    // table name in the database
    private static final String TABLE = "mannschaftsmitglied";
    // business entity parameter names

    private static final String MANNSCHAFTSMITGLIED_BE_TEAM_ID = "mannschaftId";
    private static final String MANNSCHAFTSMITGLIED_BE_USER_ID = "dsbMitgliedId";
    private static final String MANNSCHAFTSMITGLIED_BE_INSERT = "dsbMitgliedEingesetzt";

    // new: important for the join with dsb_mitglied
    private static final String DSBMITGLIED_BE_FORENAME = "dsbMitgliedVorname";
    private static final String DSBMITGLIED_BE_SURNAME = "dsbMitgliedNachname";


    private static final String MANNSCHAFTSMITGLIED_TABLE_TEAM_ID = "mannschaftsmitglied_mannschaft_id";
    private static final String MANNSCHAFTSMITGLIED_TABLE_USER_ID = "mannschaftsmitglied_dsb_mitglied_id";
    private static final String MANNSCHAFTSMITGLIED_TABLE_INSERT = "mannschaftsmitglied_dsb_mitglied_eingesetzt";

    // new: important for the join with dsb_mitglied
    private static final String DSBMITGLIED_TABLE_FORENAME = "dsb_mitglied_vorname";
    private static final String DSBMITGLIED_TABLE_SURNAME = "dsb_mitglied_nachname";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<MannschaftsmitgliedBE> MANNSCHAFTSMITGLIED = new BusinessEntityConfiguration<>(
            MannschaftsmitgliedBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);


    private static final String FIND_ALL =
            "SELECT mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, dsb_mitglied_vorname, dsb_mitglied_nachname"
                    + " FROM mannschaftsmitglied m join dsb_mitglied d on m.mannschaftsmitglied_dsb_mitglied_id = d.dsb_mitglied_id"
                    + " ORDER BY mannschaftsmitglied_mannschaft_id";

    private static final String FIND_BY_TEAM_ID =
            "SELECT mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, dsb_mitglied_vorname, dsb_mitglied_nachname"
                    + " FROM mannschaftsmitglied m join dsb_mitglied d on m.mannschaftsmitglied_dsb_mitglied_id = d.dsb_mitglied_id"
                    + " WHERE mannschaftsmitglied_mannschaft_id = ?";

    private static final String FIND_BY_MEMBER_AND_TEAM_ID =
            "SELECT mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, dsb_mitglied_vorname, dsb_mitglied_nachname "
                    + "FROM mannschaftsmitglied m join dsb_mitglied d on m.mannschaftsmitglied_dsb_mitglied_id = d.dsb_mitglied_id"
                    +" WHERE mannschaftsmitglied_mannschaft_id =? AND mannschaftsmitglied_dsb_mitglied_id=?";

    private static final String FIND_ALL_SCHUETZE_TEAM =
            "SELECT mannschaftsmitglied_mannschaft_id, mannschaftsmitglied_dsb_mitglied_id, mannschaftsmitglied_dsb_mitglied_eingesetzt, dsb_mitglied_vorname, dsb_mitglied_nachname"
                    + "FROM mannschaftsmitglied m join dsb_mitglied d on m.mannschaftsmitglied_dsb_mitglied_id = d.dsb_mitglied_id"
                    +" WHERE   mannschaftsmitglied_mannschaft_id =? AND mannschaftsmitglied_dsb_mitglied_eingesetzt= true";




    private final BasicDAO basicDao;

    @Autowired
    public MannschaftsmitgliedDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(MANNSCHAFTSMITGLIED_TABLE_TEAM_ID, MANNSCHAFTSMITGLIED_BE_TEAM_ID);
        columnsToFieldsMap.put(MANNSCHAFTSMITGLIED_TABLE_USER_ID,MANNSCHAFTSMITGLIED_BE_USER_ID);
        columnsToFieldsMap.put(MANNSCHAFTSMITGLIED_TABLE_INSERT, MANNSCHAFTSMITGLIED_BE_INSERT);

        System.out.println("test");
        //new: important for the join with dsb_mitglied
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_FORENAME,DSBMITGLIED_BE_FORENAME);
        columnsToFieldsMap.put(DSBMITGLIED_TABLE_SURNAME,DSBMITGLIED_BE_SURNAME);


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
     * @param id
     * @return
     */
    public List<MannschaftsmitgliedBE> findByTeamId(final long id) {
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED, FIND_BY_TEAM_ID, id);
    }


    public List<MannschaftsmitgliedBE> findAllSchuetzeInTeam(final long id){
        return basicDao.selectEntityList(MANNSCHAFTSMITGLIED, FIND_ALL_SCHUETZE_TEAM,id);
    }


    public MannschaftsmitgliedBE findByMemberAndTeamId(long teamId, final long memberId){
        MannschaftsmitgliedBE test;
        test = basicDao.selectSingleEntity(MANNSCHAFTSMITGLIED, FIND_BY_MEMBER_AND_TEAM_ID, teamId,memberId);
        return test;

    }

    public MannschaftsmitgliedBE create(final MannschaftsmitgliedBE mannschaftsmitgliedBE, final long currentMemberId) {

        basicDao.setCreationAttributes(mannschaftsmitgliedBE, currentMemberId);

        return basicDao.insertEntity(MANNSCHAFTSMITGLIED, mannschaftsmitgliedBE);
    }


    public MannschaftsmitgliedBE update(final MannschaftsmitgliedBE mannschaftsmitgliedBE,final long currentMemberId) {
        basicDao.setModificationAttributes(mannschaftsmitgliedBE, currentMemberId);


     return basicDao.updateEntity(MANNSCHAFTSMITGLIED, mannschaftsmitgliedBE, MANNSCHAFTSMITGLIED_BE_USER_ID);

    }

    public void delete(final MannschaftsmitgliedBE mannschaftsmitgliedBE, final long currentMemberId) {
        basicDao.setModificationAttributes(mannschaftsmitgliedBE, currentMemberId);

        basicDao.deleteEntity(MANNSCHAFTSMITGLIED, mannschaftsmitgliedBE,MANNSCHAFTSMITGLIED_BE_USER_ID);

    }

    public boolean checkExistingSchuetze(long teamId, final long memberId){
        return findByMemberAndTeamId(teamId,memberId).isDsbMitgliedEingesetzt();
    }


}