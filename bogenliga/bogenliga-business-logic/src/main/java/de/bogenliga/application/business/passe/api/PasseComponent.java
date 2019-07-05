package de.bogenliga.application.business.passe.api;

import java.util.List;
import de.bogenliga.application.business.passe.api.types.PasseDO;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public interface PasseComponent {

    /**
     * Return all passe entries.
     *
     * @return list of all passe in the database; empty list, if no passe is found
     */
    List<PasseDO> findAll();

    PasseDO findById(Long id);

    /**
     * Return all passe from one Wettkampf
     *
     * @return list of all passe from one Wettkampf in the database; empty list, if no passe are found
     */

    List<PasseDO> findByWettkampfId(Long wettkampfId);

    /**
     * Return all passe from one Wettkampf
     *
     * @param memberId of the member
     *
     * @return list of all passe from one Wettkampf in the database; empty list, if no passe are found
     */

    List<PasseDO> findByMemberId(Long memberId);

    /**
     * Return a passe entry with the given ids.
     *
     * @param dsbMemberId  of the mannschaftsmitglied,
     * @param mannschaftId of the mannschaft
     *
     * @return list of passe from one mitglied in a mannschaft ; empty list, if no passe are found
     */
    List<PasseDO> findByMemberMannschaftId(Long dsbMemberId, Long mannschaftId);

    /**
     * Return all passe entries from one team.
     *
     * @return list of all passe from one team in the database; empty list, if no passe are found
     */

    List<PasseDO> findByTeamId(Long teamId);


    /**
     * Return a passe entry with the given ids.
     *
     * @param mannschaftId of the mannschaftsmitglied,
     * @param matchId      of the match
     *
     * @return list of passe from one mannschaft in a match; empty list, if no passe are found
     */
    List<PasseDO> findByMannschaftMatchId(Long mannschaftId, Long matchId);

    /**
     * Return a passe entry with the given ids.
     *
     * @param dsbMemberId of the mannschaftsmitglied,
     * @param matchId     of the mannschaft
     *
     * @return list of passe from one mitglied in a mannschaft ; empty list, if no passe are found
     */
    List<PasseDO> findByMitgliedMatchId(Long dsbMemberId, Long matchId);

    /**
     * Return a passe entry with the given ids.
     *
     * @param matchId of the match
     *
     * @return list of passe from one mannschaft in a match; empty list, if no passe are found
     */
    List<PasseDO> findByMatchId(Long matchId);

    /**
     * Return passe entries with the given wettkmapfId and dsbMitgliedId
     *
     * @param wettkampfId id of the wettkampf
     * @param mitgliedId  id of the dsbmitglied
     *
     * @return list of passe from a dsbmitglied in the given wettkampf, empty list if no passe are found
     */
    List<PasseDO> findByWettkampfIdAndMitgliedId(Long wettkampfId, Long mitgliedId);

    /**
     * Return a passe entry by the combined pk attributes.
     *
     * @return list of passe from one mannschaft in a match; empty list, if no passe are found
     */
    PasseDO findByPk(Long wettkampfId, Long matchNr, Long mannschaftId, Long passeLfdNr, Long dsbMitgliedId);

    /**
     * Create a new passe in the database.
     *
     * @param passeDO       the new passeDO
     * @param currentUserId the id of the creating user
     *
     * @return persisted version of the passe
     */
    public PasseDO create(PasseDO passeDO, final Long currentUserId);

    /**
     * Update an existing passe. The passe is identified by the id's set in passeDO.
     *
     * @param passeDO         existing passeDO to update
     * @param currentMemberId id of the member currently updating the passe
     *
     * @return persisted version of the passe
     */
    PasseDO update(PasseDO passeDO, Long currentMemberId);


    /**
     * Delete an existing passe. The passe is identified by the id's set in passeDO.
     *
     * @param passeDO         passe to delete
     * @param currentMemberId id of the member currently updating the passe
     */
    void delete(PasseDO passeDO, Long currentMemberId);
}
