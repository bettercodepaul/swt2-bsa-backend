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
     * @return list of all vereine in the database.
     * empty list if no verein was found in the database.
     */
    List<VereinDO> findAll();

    //TODO create method
    //TODO update method
    //TODO delete method
    //TODO getById method
}
