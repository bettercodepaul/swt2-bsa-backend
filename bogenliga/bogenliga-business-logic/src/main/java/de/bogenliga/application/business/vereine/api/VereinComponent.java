package de.bogenliga.application.business.vereine.api;

import java.util.List;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the vereine database requests.
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public interface VereinComponent extends ComponentFacade {

    /**
     * Return all vereine entries.
     *
     * @return list of all vereine in the database. empty list if no verein was found in the database.
     */
    List<VereinDO> findAll();

    /**
     * Returns a "Region" with the given id
     *
     * @param vereinId ID of the region to be queried from the database.
     *
     * @return returns the queried verein
     */
    VereinDO findById(final long vereinId);

    /**
     * Create an entry of verein
     *
     * @param vereinDO           Verein DataObject containing propertires of a verein
     * @param currentDsbMitglied Id of the currently logged in user that sent create request
     *
     * @return returns that created Verein
     */
    VereinDO create(VereinDO vereinDO, long currentDsbMitglied);


    /**
     * Updates a database entry with the given values
     *
     * @param vereinDO           Verein DO containing properties of a verein
     * @param currentDsbMitglied id of the currently logged in user that sent the update request
     *
     * @return returns the updated verein entry
     */
    VereinDO update(VereinDO vereinDO, long currentDsbMitglied);

    /**
     * Deletes a database entry depending on the id of the vereinDO property
     *
     * @param vereinDO           Verein DataObject containing atleast an id
     * @param currentDsbMitglied id of the currently logged in user that sent the delete request
     */
    void delete(VereinDO vereinDO, long currentDsbMitglied);
}
