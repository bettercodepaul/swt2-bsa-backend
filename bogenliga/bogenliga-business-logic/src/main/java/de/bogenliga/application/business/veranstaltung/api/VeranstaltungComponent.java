package de.bogenliga.application.business.veranstaltung.api;

import java.util.List;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * Responsible for the veranstaltung database requests.
 *
 * @author Daniel Schott, daniel.schott@student.reutlingen-university.de
 */
public interface VeranstaltungComponent extends ComponentFacade {

    /**
     * Return all Veranstaltung entries.
     *
     * @return list of all Veranstaltung in the database. empty list if no Veranstaltung was found in the database.
     */
    List<VeranstaltungDO> findAll();

    /**
     * Returns a "Veranstaltung" with the given id
     *
     * @param veranstaltungId ID of the verein to be queried from the database.
     *
     * @return returns the queried Veranstaltung
     */
    VeranstaltungDO findById(final long veranstaltungId);



    /**
     * Create an entry of Veranstaltung
     *
     * @param veranstaltungDO    Veranstaltungs DataObject containing propertires of a Veranstaltung
     * @param currentDsbMitglied Id of the currently logged in user that sent create request
     *
     * @return returns that created Verein
    */

    VeranstaltungDO create(VeranstaltungDO veranstaltungDO, long currentDsbMitglied);


    /**
     * Updates a database entry with the given values
     *
     * @param veranstaltungDO    Veranstaltung DO containing properties of a verein
     * @param currentDsbMitglied id of the currently logged in user that sent the update request
     *
     * @return returns the updated verein entry
    */

    VeranstaltungDO update(VeranstaltungDO veranstaltungDO, long currentDsbMitglied);

    /**
     * Deletes a database entry depending on the id of the VeranstaltungDO property
     *
     * @param veranstaltungDO    Veranstaltung DataObject containing atleast an id
     * @param currentDsbMitglied id of the currently logged in user that sent the delete request
    */

    void delete(VeranstaltungDO veranstaltungDO, long currentDsbMitglied);


    /**
     * Returns a "Veranstaltung" with the given id
     *
     * @param ligado ID of the liga to be queried from the database.
     *
     * @return returns the queried Veranstaltung
     */



}
