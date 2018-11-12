package de.bogenliga.application.business.mannschaftsmitglied.api;

import java.util.List;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.common.component.ComponentFacade;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public interface MannschaftsmitgliedComponent extends ComponentFacade {

    /**
     * Return all mannschaftsmitglied entries.
     *
     * @return list of all mannschaftsmitglied mannschaftsmitglied in the database;
     * empty list, if no mannschaftsmitglied is found
     */
    List<MannschaftsmitgliedDO> findAll();

    /**
     * Return all mannschaftsmitglied with the attribut mitglied_eingesetzt = true entries.
     *
     * @return list of all mannschaftsmitglieder mannschaftsmitglieder
     *         mit mitglied_eingesetzt = true in the database;
     *         empty list, if no mannschaftsmitglied schuetze is found
     */

    List<MannschaftsmitgliedDO> findAllSchuetze();


    /**
     * Return a mannschaftsmitglied entry with the given id.
     *
     * @param mannschaftId of the mannschaftsmitglied, mitgliedId of the mannschaftsmitglied
     * @return single mannschaftsmitglied entry with the given id;
     * null, if no mannschaftsmitglied is found
     */
    MannschaftsmitgliedDO findById(long mannschaftId, long mitgliedId);



    /**
     * Create a new mannschaftsmitglied in the database.
     *
     * @param mannschaftsmitgliedDO new mannschaftsmitglied
     * @return persisted version of the mannschaftsmitglied
     */
    DsbMitgliedDO create(MannschaftsmitgliedDO mannschaftsmitgliedDO, long currentMannschaftsmitgliedId);



    /**
     * Update an existing mannschaftsmitglied. The mannschaftsmitglied is identified by the id.
     *
     * @param mannschaftsmitgliedDO existing mannschaftsmitgliedDO to update
     * @return persisted version of the mannschaftsmitglied
     */
    DsbMitgliedDO update(MannschaftsmitgliedDO mannschaftsmitgliedDO, long currentMannschaftsmitgliedId);


    /**
     * Delete an existing mannschaftsmitglied. The mannschaftsmitglied is identified by the id.
     *
     * @param mannschaftsmitgliedDO mannschaftsmitglied to delete
     */
    void delete(MannschaftsmitgliedDO mannschaftsmitgliedDO, long currentMannschaftsmitgliedId);

}
