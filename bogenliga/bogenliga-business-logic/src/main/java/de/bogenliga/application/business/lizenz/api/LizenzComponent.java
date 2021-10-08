package de.bogenliga.application.business.lizenz.api;

import java.util.List;
import de.bogenliga.application.business.lizenz.api.types.LizenzDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * @author Manuel Baisch
 */
public interface LizenzComponent extends ComponentFacade {

    /**
     * Return all Lizenzen entries.
     *
     * @return list of all Lizenzen in the database;
     * empty list, if no Lizenzen are found.
     */
    List<LizenzDO> findAll();






    /**
     * Return a Lizenz by DSB-Mitglieds-Id.
     *
     * @return list of all LizenzDOs for a DSB-Mitglied.
     * empty list, if no Lizenzen are found.
     */
    List<LizenzDO> findByDsbMitgliedId(final long id);



    /**
     * Creates a a new Lizenz
     *
     * @return the created Lizenz
     */

    LizenzDO create(final LizenzDO lizenzDO ,final long currentUserId);



    /**
     * Updates a Lizenz. Finds a Lizenz by LizenzID
     *
     * @return the updated Lizenz.
     * null, if no Lizenz is found
     */
    LizenzDO update(final LizenzDO lizenzDO ,final long currentUserId);


    /**
     * delete a Lizenz by its LizenzID
     *
     * @return null
     */
    void delete(final LizenzDO lizenzDO,final long currentUserId);

    byte[] getLizenzPDFasByteArray(long dsbMitgliedID, long teamID);

    byte[] getMannschaftsLizenzenPDFasByteArray(long dsbMannschaftsId);
}
