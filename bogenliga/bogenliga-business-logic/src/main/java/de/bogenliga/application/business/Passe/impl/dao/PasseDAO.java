package de.bogenliga.application.business.Passe.impl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import de.bogenliga.application.business.Passe.impl.entity.PasseBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.BusinessEntityConfiguration;
import de.bogenliga.application.common.component.dao.DataAccessObject;

/**
 * @author Kay Scheerer
 */
@Repository
public class PasseDAO implements DataAccessObject {

    //define logger context
    private static final Logger LOGGER = LoggerFactory.getLogger(PasseDAO.class);

    //table name in the DB
    private static final String TABLE = "passe";


    //business entity parameters
    private static final String PASSE_BE_MANNSCHAFT_ID = "passeMannschaftId";
    private static final String PASSE_BE_WETTKAMPF_ID = "passeWettkampfId";
    private static final String PASSE_BE_MATCH_NR = "passeMatchNr";
    private static final String PASSE_BE_LFDNR = "passeLfdnr";
    private static final String PASSE_BE_DSB_MITGLIED_ID = "passeDsbMitgliedId";
    private static final String PASSE_BE_PFEIL_1 = "passeRingzahlPfeil1";
    private static final String PASSE_BE_PFEIL_2 = "passeRingzahlPfeil2";
    private static final String PASSE_BE_PFEIL_3 = "passeRingzahlPfeil3";
    private static final String PASSE_BE_PFEIL_4 = "passeRingzahlPfeil4";
    private static final String PASSE_BE_PFEIL_5 = "passeRingzahlPfeil5";
    private static final String PASSE_BE_PFEIL_6 = "passeRingzahlPfeil6";

    private static final String PASSE_TABLE_MANNSCHAFT_ID = "passe_mannschaft_id";
    private static final String PASSE_TABLE_WETTKAMPF_ID = "passe_wettkampf_id";
    private static final String PASSE_TABLE_MATCH_NR = "passe_match_nr";
    private static final String PASSE_TABLE_LFDNR = "passe_lfdnr";
    private static final String PASSE_TABLE_DSB_MITGLIED_ID = "passe_dsb_mitglied_id";

    private static final String PASSE_TABLE_PFEIL_1 = "passe_ringzahl_pfeil1";
    private static final String PASSE_TABLE_PFEIL_2 = "passe_ringzahl_pfeil2";
    private static final String PASSE_TABLE_PFEIL_3 = "passe_ringzahl_pfeil3";
    private static final String PASSE_TABLE_PFEIL_4 = "passe_ringzahl_pfeil4";
    private static final String PASSE_TABLE_PFEIL_5 = "passe_ringzahl_pfeil5";
    private static final String PASSE_TABLE_PFEIL_6 = "passe_ringzahl_pfeil6";
    private static final String PASSE_TABLE_ID = "passe_id";

    // wrap all specific config parameters
    private static final BusinessEntityConfiguration<PasseBE> PASSE = new BusinessEntityConfiguration<>(
            PasseBE.class, TABLE, getColumnsToFieldsMap(), LOGGER);

    private final BasicDAO basicDao;


    /**
     * Initialize the transaction manager to provide a database connection
     *
     * @param basicDao to handle the commonly used database operations
     */
    @Autowired
    public PasseDAO(final BasicDAO basicDao) {
        this.basicDao = basicDao;
    }


    // table column label mapping to the business entity parameter names
    private static Map<String, String> getColumnsToFieldsMap() {
        final Map<String, String> columnsToFieldsMap = new HashMap<>();

        columnsToFieldsMap.put(PASSE_TABLE_MANNSCHAFT_ID, PASSE_BE_MANNSCHAFT_ID);
        columnsToFieldsMap.put(PASSE_TABLE_WETTKAMPF_ID, PASSE_BE_WETTKAMPF_ID);
        columnsToFieldsMap.put(PASSE_TABLE_MATCH_NR, PASSE_BE_MATCH_NR);
        columnsToFieldsMap.put(PASSE_TABLE_LFDNR, PASSE_BE_LFDNR);
        columnsToFieldsMap.put(PASSE_TABLE_DSB_MITGLIED_ID, PASSE_BE_DSB_MITGLIED_ID);
        columnsToFieldsMap.put(PASSE_TABLE_PFEIL_1, PASSE_BE_PFEIL_1);
        columnsToFieldsMap.put(PASSE_TABLE_PFEIL_2, PASSE_BE_PFEIL_2);
        columnsToFieldsMap.put(PASSE_TABLE_PFEIL_3, PASSE_BE_PFEIL_3);
        columnsToFieldsMap.put(PASSE_TABLE_PFEIL_4, PASSE_BE_PFEIL_4);
        columnsToFieldsMap.put(PASSE_TABLE_PFEIL_5, PASSE_BE_PFEIL_5);
        columnsToFieldsMap.put(PASSE_TABLE_PFEIL_6, PASSE_BE_PFEIL_6);

        // add technical columns
        columnsToFieldsMap.putAll(BasicDAO.getTechnicalColumnsToFieldsMap());

        return columnsToFieldsMap;
    }


    /**
     * SQL queries, not sure which ones are needed at all
     */
    private static final String FIND_ALL =
            "SELECT * "
                    + " FROM " + TABLE
                    + " ORDER BY " + PASSE_TABLE_LFDNR
                    + "=?";

    private static final String FIND_BY_MATCH_ID =
            "SELECT * "
                    + " FROM " + TABLE
                    + " WHERE " + "" //MatchID ist noch nicht in der Passetabelle
                    + "=?";

    private static final String FIND_BY_MANNSCHAFT_ID =
            "SELECT * "
                    + " FROM " + TABLE
                    + " WHERE " + PASSE_TABLE_MANNSCHAFT_ID
                    + "=?";
    private static final String FIND_BY_MEMBER_ID =
            "SELECT * "
                    + " FROM " + TABLE
                    + " WHERE " + PASSE_TABLE_DSB_MITGLIED_ID
                    + "=?";

    private static final String FIND_BY_WETTKAMPF_ID =
            "SELECT * "
                    + " FROM " + TABLE
                    + " WHERE " + PASSE_TABLE_WETTKAMPF_ID
                    + "=?";

    private static final String FIND_BY_MEMBER_MANNSCHAFT_ID =
            "SELECT * "
                    + " FROM " + TABLE
                    + " WHERE " + PASSE_TABLE_DSB_MITGLIED_ID
                    + "=? "
                    + " AND WHERE " + PASSE_TABLE_MANNSCHAFT_ID
                    + "=?";

    private static final String FIND_BY_MANNSCHAFT_MATCH_ID =
            "SELECT * "
                    + " FROM " + TABLE
                    + " WHERE " + PASSE_TABLE_MANNSCHAFT_ID
                    + "=? "
                    + " AND WHERE " + "" //PASSE_TABLE_MATCH_ID
                    + "=?";

    private static final String FIND_BY_MITGLIED_MATCH_ID =
            "SELECT * "
                    + " FROM " + TABLE
                    + " WHERE " + PASSE_TABLE_DSB_MITGLIED_ID
                    + "=? "
                    + " AND WHERE " + "" //PASSE_TABLE_MATCH_ID
                    + "=?";



    /**
     * Create a new passe entry
     *
     * @param passeBE
     * @param currentKampfrichterUserId
     *
     * @return Business Entity corresponding to the created kampfrichter entry (should only be created by Veranstalter
     * and  entities
     */
    public PasseBE create(final PasseBE passeBE, final long currentKampfrichterUserId) {
        basicDao.setCreationAttributes(passeBE, currentKampfrichterUserId);

        return basicDao.insertEntity(PASSE, passeBE);
    }


    /**
     * Return all passe entries.
     *
     * @return list of all passe in the database; empty list, if no passe is found
     */
    public List<PasseBE> findAll() {
        return basicDao.selectEntityList(PASSE, FIND_ALL);
    }


    /**
     * Return all passe from one Wettkampf
     *
     * @param wettkampfId
     *
     * @return list of all passe from one Wettkampf in the database; empty list, if no passe are found
     */
    public List<PasseBE> findByWettkampfId(long wettkampfId) {
        return basicDao.selectEntityList(PASSE, FIND_BY_WETTKAMPF_ID, wettkampfId);
    }


    /**
     * Return all passe entries from one team.
     *
     * @param teamId
     *
     * @return list of all passe from one team in the database; empty list, if no passe are found
     */
    public List<PasseBE> findByTeamId(long teamId) {
        return basicDao.selectEntityList(PASSE, FIND_BY_MANNSCHAFT_ID,teamId);
    }


    /**
     * Return a passe entry with the given ids.
     *
     * @param dsbMitgliedId of the mannschaftsmitglied,
     * @param mannschaftId  of the mannschaft
     *
     * @return list of passe from one mitglied in one team; empty list, if no passe are found
     */
    public List<PasseBE> findByMemberMannschaftId(long dsbMitgliedId, long mannschaftId) {
        return basicDao.selectEntityList(PASSE, FIND_BY_MEMBER_MANNSCHAFT_ID,dsbMitgliedId,mannschaftId);
    }


    /**
     * Return a passe entry with the given ids.
     *
     * @param dsbMitgliedId of the mannschaftsmitglied
     *
     * @return list of passe from one mitglied in one team; empty list, if no passe are found
     */
    public List<PasseBE> findByMemberId(long dsbMitgliedId) {
        return basicDao.selectEntityList(PASSE, FIND_BY_MEMBER_ID);
    }


    /**
     * Return a passe entry with the given ids.
     *
     * @param matchId      of the match,
     * @param mannschaftId of the mannschaft
     *
     * @return list of passe from one mitglied in one team; empty list, if no passe are found
     */
    public List<PasseBE> findByMannschaftMatchId(long mannschaftId, long matchId) {
        return basicDao.selectEntityList(PASSE, FIND_BY_MANNSCHAFT_MATCH_ID,mannschaftId,matchId);
    }


    /**
     * Return a passe entry with the given ids.
     *
     * @param dsbMitgliedId of the mannschaftsmitglied,
     * @param matchId       of the match
     *
     * @return list of passe from one mitglied in one team; empty list, if no passe are found
     */
    public List<PasseBE> findByMitgliedMatchId(long dsbMitgliedId, long matchId) {
        return basicDao.selectEntityList(PASSE, FIND_BY_MITGLIED_MATCH_ID);
    }


    /**
     * Return a passe entry with the given ids.
     *
     * @param matchId of the match
     *
     * @return list of passe from one mitglied in one team; empty list, if no passe are found
     */
    public List<PasseBE> findByMatchId(long matchId) {
        return basicDao.selectEntityList(PASSE, FIND_BY_MATCH_ID);
    }


    /**
     * Update an existing passe. The passe is identified by the id's set in passeDO.
     *
     * @param passeBE         existing passeDO to update
     * @param currentMemberId id of the member currently updating the passe
     *
     * @return persisted version of the passe
     */
    public PasseBE update(final PasseBE passeBE, final long currentMemberId) {
        basicDao.setModificationAttributes(passeBE, currentMemberId);


        return basicDao.updateEntity(PASSE, passeBE, PASSE_TABLE_ID);
    }


    /**
     * Delete an existing passe. The passe is identified by the id's set in passeDO.
     *
     * @param passeBE         passe to delete
     * @param currentMemberId id of the member currently updating the passe
     */
    public void delete(PasseBE passeBE, long currentMemberId) {
        basicDao.setModificationAttributes(passeBE, currentMemberId);
        basicDao.deleteEntity(PASSE,passeBE,PASSE_TABLE_ID);
    }
}
