package de.bogenliga.application.business.Passe.api;

import java.util.List;
import de.bogenliga.application.business.Passe.api.types.PasseDO;
import de.bogenliga.application.business.Passe.impl.dao.PasseDAO;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public interface PasseComponent {

    /**
     * Return all passe entries.
     *
     * @return list of all passe in the database;
     * empty list, if no passe is found
     */
    List<PasseDO> findAll();


    /**
     * Return all passe from one Wettkampf
     * @return list of all passe from one Wettkampf in the database;
     * empty list, if no passe are found
     */

    List<PasseDO> findByWettkampfId(long wettkampfId);



    /**
     * Return all passe entries from one team.
     *
     * @return list of all passe from one team in the database;
     * empty list, if no passe are found
     */

    List<PasseDO> findByTeamId(long teamId);



    /**
     * Return a passe entry with the given ids.
     *
     * @param dsbMitgliedId of the mannschaftsmitglied,
     * @param mannschaftId of the mannschaft
     * @return  list of passe from one mitglied in one team;
     * empty list, if no passe are found
     */
    List<PasseDO> findByMemberAndTeamId(long dsbMitgliedId, long mannschaftId);


    /**
     * Return a passe entry with the given id.
     *
     * @param dsbMitgliedId of the mannschaftsmitglied,
     * @param mannschaftId of the mannschaft
     * @return  list of passe from one mitglied in one team;
     * empty list, if no passe are found
     */
    List<PasseDO> findByMemberAndTeamId(long dsbMitgliedId, long mannschaftId, long lfdnr,long matchNr,long wettkampfId);


    /**
     * Return a passe entry with the given id.
     *
     * @param lfdNr counting number,
     * @return  passe by counting number;
     * empty list, if no passe are found
     */
    PasseDO findByLfdNr(long lfdNr);


    /**
     * Create a new passe in the database.
     *
     * @param passeDO new passeDO
     * @param currentMemberId id of the member currently updating the passe
     * @param mannschaftId id of the mannschaft
     * @param wettkampfId id of the wettkampf
     * @param matchNr number of the match
     * @param lfdNr counting number
     * @return persisted version of the passe
     */
    PasseDO create(PasseDO passeDO, long currentMemberId,long mannschaftId, long wettkampfId,long matchNr, long lfdNr);



    /**
     * Update an existing passe. The passe is identified by the id's set in passeDO.
     *
     * @param passeDO existing passeDO to update
     * @param currentMemberId id of the member currently updating the passe
     * @return persisted version of the passe
     */
    PasseDO update(PasseDO passeDO, long currentMemberId);


    /**
     * Delete an existing passe. The passe is identified by the id's set in passeDO.
     *
     * @param passeDO passe to delete
     * @param currentMemberId id of the member currently updating the passe
     */
    void delete(PasseDO passeDO, long currentMemberId);

    boolean checkExistingSchuetze(long teamId, long memberId);

}
