package de.bogenliga.application.business.veranstaltung.api;

import java.util.List;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
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
     * Returns all Veranstaltungen of one ligaleiter.
     *
     * @param ligaleiterId id of the selected ligaleiter
     * @return list of all Veranstaltungen with a given ligaleiter_id.
     * empty list if the ligaleiter_id is not found in the veranstaltung-table
     */
    List<VeranstaltungDO> findByLigaleiterId(final long ligaleiterId);



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
     * Returns the last "Veranstaltung" of the given current Veranstaltung id
     *
     * @param veranstaltungId ID of the current veranstaltung to query the last Veranstaltung.
     *
     * @return returns the queried Veranstaltung
     */
    VeranstaltungDO findLastVeranstaltungById(final long veranstaltungId);

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

    /**
     *
     * @param sportjahr
     * @return a list with Veranstaltungen with the same Sportjahr
     */
    List<VeranstaltungDO> findBySportjahr(long sportjahr);

    /**
     *
     * @return a list with all Sportjahre distinct
     */

    List<SportjahrDO> findAllSportjahreDestinct();

    List<VeranstaltungDO> findByLigaID(long ligaID);

    List<VeranstaltungDO> findBySportjahrDestinct(long sportjahr);
}
