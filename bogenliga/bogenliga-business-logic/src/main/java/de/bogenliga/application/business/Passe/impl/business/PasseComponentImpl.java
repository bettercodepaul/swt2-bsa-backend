package de.bogenliga.application.business.Passe.impl.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import de.bogenliga.application.business.Passe.api.PasseComponent;
import de.bogenliga.application.business.Passe.api.types.PasseDO;
import de.bogenliga.application.business.Passe.impl.dao.PasseDAO;
import de.bogenliga.application.business.Passe.impl.entity.PasseBE;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class PasseComponentImpl implements PasseComponent {


    private static final String PRECONDITION_PASSEDO = "PasseDO must not be null";
    private static final String PRECONDITION_PASSE_MANNSCHAFT_ID = "passe_mannschaft_id must not be null";
    private static final String PRECONDITION_DSBMITGLIED_MITGLIED_ID = "PasseDO_dsb_mitglied_id must not be null";

    private static final String PRECONDITION_PASSE_MANNSCHAFT_ID_NEGATIV = "MannschaftsmitgliedDO_Mannschaft_ID must not be negativ";
    private static final String PRECONDITION_PASSE_DSB_MITGLIED_ID_NEGATIV = "PasseDO_Dsb_Mitglied_ID must not be negativ";



    private final PasseDAO passeDAO;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param passeDAO to access the database and return passe representations
     */
    @Autowired
    public PasseComponentImpl(final PasseDAO passeDAO) { this.passeDAO = passeDAO; }

    /**
     * Return all passe entries.
     *
     * @return list of all passe in the database; empty list, if no passe is found
     */
    @Override
    public List<PasseDO> findAll() {
        final List<PasseBE> mannschaftsmitgliedBEList = passeDAO.findAll();
        return null;
    }


    /**
     * Return all passe from one Wettkampf
     *
     * @param wettkampfId
     *
     * @return list of all passe from one Wettkampf in the database; empty list, if no passe are found
     */
    @Override
    public List<PasseDO> findByWettkampfId(long wettkampfId) {
        return null;
    }


    /**
     * Return all passe entries from one team.
     *
     * @param teamId
     *
     * @return list of all passe from one team in the database; empty list, if no passe are found
     */
    @Override
    public List<PasseDO> findByTeamId(long teamId) {
        return null;
    }


    /**
     * Return a passe entry with the given ids.
     *
     * @param dsbMitgliedId of the mannschaftsmitglied,
     * @param mannschaftId  of the mannschaft
     *
     * @return list of passe from one mitglied in one team; empty list, if no passe are found
     */
    @Override
    public List<PasseDO> findByMemberAndTeamId(long dsbMitgliedId, long mannschaftId) {
        return null;
    }


    /**
     * Return a passe entry with the given id.
     *
     * @param dsbMitgliedId of the mannschaftsmitglied,
     * @param mannschaftId  of the mannschaft
     * @param lfdnr
     * @param matchNr
     * @param wettkampfId
     *
     * @return list of passe from one mitglied in one team; empty list, if no passe are found
     */
    @Override
    public List<PasseDO> findByMemberAndTeamId(long dsbMitgliedId, long mannschaftId, long lfdnr, long matchNr,
                                               long wettkampfId) {
        return null;
    }


    /**
     * Return a passe entry with the given id.
     *
     * @param lfdNr counting number,
     *
     * @return passe by counting number; empty list, if no passe are found
     */
    @Override
    public PasseDO findByLfdNr(long lfdNr) {
        return null;
    }


    /**
     * Create a new passe in the database.
     *
     * @param passeDO         new passeDO
     * @param currentMemberId id of the member currently updating the passe
     * @param mannschaftId    id of the mannschaft
     * @param wettkampfId     id of the wettkampf
     * @param matchNr         number of the match
     * @param lfdNr           counting number
     *
     * @return persisted version of the passe
     */
    @Override
    public PasseDO create(PasseDO passeDO, long currentMemberId, long mannschaftId, long wettkampfId, long matchNr,
                          long lfdNr) {
        return null;
    }


    /**
     * Update an existing passe. The passe is identified by the id's set in passeDO.
     *
     * @param passeDO         existing passeDO to update
     * @param currentMemberId id of the member currently updating the passe
     *
     * @return persisted version of the passe
     */
    @Override
    public PasseDO update(PasseDO passeDO, long currentMemberId) {
        return null;
    }


    /**
     * Delete an existing passe. The passe is identified by the id's set in passeDO.
     *
     * @param passeDO         passe to delete
     * @param currentMemberId id of the member currently updating the passe
     */
    @Override
    public void delete(PasseDO passeDO, long currentMemberId) {

    }


    @Override
    public boolean checkExistingSchuetze(long teamId, long memberId) {
        return false;
    }
}
