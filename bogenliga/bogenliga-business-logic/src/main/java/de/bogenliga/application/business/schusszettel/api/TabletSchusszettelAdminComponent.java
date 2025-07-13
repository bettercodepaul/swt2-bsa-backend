package de.bogenliga.application.business.schusszettel.api;

import de.bogenliga.application.business.schusszettel.api.types.TabletSessionInfoDO;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * Administrative component interface for Tablet Schusszettel session management.
 * 
 * <h2>FUNCTIONALITY</h2>
 * Provides administrative operations for tablet session lifecycle management.
 * Handles session initialization, deletion, token management, and status monitoring
 * for competition administrators.
 * 
 * <h2>SESSION LIFECYCLE</h2>
 * <ul>
 *   <li>Initialize: Creates sessions for all teams in competition with state analysis</li>
 *   <li>Monitor: Validates and corrects session states against database</li>
 *   <li>Delete: Removes all sessions for competition</li>
 *   <li>Re-tokenize: Generates new access tokens while preserving state</li>
 * </ul>
 * 
 * <h2>STATE DETECTION</h2>
 * Initialization analyzes existing competition data to determine appropriate
 * initial states based on pass completion, match progression, and LigamatchBE data.
 * 
 * @see TabletSchusszettelComponent Score entry operations
 * @see TabletSessionInfoDO Session status data structure
 */
public interface TabletSchusszettelAdminComponent {

    /**
     * Initializes tablet sessions for all teams in competition.
     * 
     * Deletes existing sessions and creates new sessions for each team.
     * Uses LigamatchBE data to determine current match position and analyzes
     * pass data to set appropriate initial state (SCHUETZENMELDUNG, SATZEINGABE,
     * WARTE, or WETTKAMPF_ENDE). Generates secure access tokens for each session.
     * 
     * @param wettkampfId Competition identifier
     * @throws BusinessException if no matches found for competition
     */
    void initializeForWettkampf(long wettkampfId);

    /**
     * Deletes all tablet sessions for competition.
     * 
     * Removes all session data from database for specified competition.
     * Used for cleanup or re-initialization scenarios.
     * 
     * @param wettkampfId Competition identifier
     */
    void deleteForWettkampf(long wettkampfId);

    /**
     * Checks if tablet sessions exist for competition.
     * 
     * @param wettkampfId Competition identifier
     * @return true if sessions exist, false otherwise
     */
    boolean existsForWettkampf(long wettkampfId);

    /**
     * Generates new access token for team session.
     * 
     * Creates new cryptographically secure token while preserving all
     * session state data. Used for security or access control purposes.
     * 
     * @param wettkampfId Competition identifier
     * @param teamId Team identifier
     * @throws BusinessException if session not found
     */
    void reTokenize(long wettkampfId, long teamId);

    /**
     * Retrieves session overview for all teams in competition.
     * 
     * Returns current session data including team names, states, tokens,
     * and opponent information. Validates sessions against current database
     * state and corrects inconsistencies. Evaluates WARTE states for
     * potential progression.
     * 
     * @param wettkampfId Competition identifier
     * @return TabletSessionInfoDO containing session overview with team details
     */
    TabletSessionInfoDO generateSchusszettelSessions(long wettkampfId);
}
