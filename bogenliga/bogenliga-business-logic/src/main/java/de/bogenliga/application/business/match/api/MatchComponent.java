package de.bogenliga.application.business.match.api;

import java.util.List;
import de.bogenliga.application.business.match.api.types.MatchDO;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public interface MatchComponent {

    /**
     * Return all matches entries.
     *
     * @return list of all match in the database; empty list, if no match is found
     */
    List<MatchDO> findAll();


    /**
     * Return a single match by unique id
     *
     * @return single matchDO
     */

    MatchDO findById(Long id);

      /**
     * Return a single match by combined primary key attributes
     *
     * @return single matchDO
     */

    MatchDO findByPk(Long nr, Long wettkampfId, Long mannschaftId, Long begegnung, Long scheibenNummer);

    /**
     * Return a single match by combined attributes
     * @param wettkampfId ID from Wettkampf
     * @param matchNr Number of the match
     * @param scheibenNummer number of the target board
     * @return singleMatchDO
     */

    MatchDO findByWettkampfIDMatchNrScheibenNr(Long wettkampfId, Long matchNr, Long scheibenNummer);


    /**
     * Return all matches from one Wettkampf
     *
     * @return list of all match from one Wettkampf in the database; empty list, if no match are found
     */

    List<MatchDO> findByWettkampfId(Long wettkampfId);


    /**
     * Return all matches entries from one mannschaft.
     *
     * @return list of all match from one mannschaft in the database; empty list, if no match are found
     */

    List<MatchDO> findByMannschaftId(Long mannschaftId);

    /**
     * Create a new match in the database.
     *
     * @param matchDO       the new matchDO
     * @param currentUserId the id of the creating user
     *
     * @return persisted version of the match
     */
    public MatchDO create(MatchDO matchDO, final Long currentUserId);

    /**
     * Create the initial Matches for the Wettkampftag 0 of the Veranstaltung.
     * It's used to show the Ligatabelle.
     *
     * @param veranstaltungsId       the Id of the corresponding Veranstaltung
     * @param currentUserId the id of the creating user
     *
     */
    public void createInitialMatchesWT0(final Long veranstaltungsId, final Long currentUserId);

    /**
     * Update an existing match. The match is identified by the id's set in matchDO.
     *
     * @param matchDO         existing matchDO to update
     * @param currentMemberId id of the member currently updating the match
     *
     * @return persisted version of the match
     */
    MatchDO update(MatchDO matchDO, Long currentMemberId);


    /**
     * Delete an existing match. The match is identified by the id's set in matchDO.
     *
     * @param matchDO         match to delete
     * @param currentMemberId id of the member currently updating the match
     */
    void delete(MatchDO matchDO, Long currentMemberId);

    /**
     * Erzeuge den Namen einer Mannschaft aus der ID
     *
     * @param mannschaftID         Mannschaft_ID
     *
     */
    String getMannschaftsNameByID(long mannschaftID);
}
