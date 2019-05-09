package de.bogenliga.application.business.regionen.api;

import java.util.List;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 *Responsible for the Class DB requests
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */

public interface RegionenComponent extends ComponentFacade {
    /**
     * Return all class entries
     * @return list of all Regionen classes in the DB
     */
    List<RegionenDO> findAll();

    /**
     * Return all class entries
     * @return list of all Region classes of type = kreis in the DB
     */
    List<RegionenDO> findAllByType(final String type);

    /**
     * Returns a "Region" with the given id
     *
     * @param regionID ID of the region to be queried from the database.
     *
     * @return returns the queried verein
     */
    RegionenDO findById(final long regionID);

    /**
     * Create an entry of region
     *
     * @param regionenDO           Region DataObject containing propertires of a region
     * @param currentDsbMitglied Id of the currently logged in user that sent create request
     *
     * @return returns that created region
     */
    RegionenDO create(RegionenDO regionenDO, long currentDsbMitglied);


    /**
     * Updates a database entry with the given values
     *
     * @param regionenDO           Region DO containing properties of a region
     * @param currentDsbMitglied id of the currently logged in user that sent the update request
     *
     * @return returns the updated region entry
     */
    RegionenDO update(RegionenDO regionenDO, long currentDsbMitglied);

    /**
     * Deletes a database entry depending on the id of the regionenDO property
     *
     * @param regionenDO           Region DataObject containing atleast an id
     * @param currentDsbMitglied id of the currently logged in user that sent the delete request
     */
    void delete(RegionenDO regionenDO, long currentDsbMitglied);

}
