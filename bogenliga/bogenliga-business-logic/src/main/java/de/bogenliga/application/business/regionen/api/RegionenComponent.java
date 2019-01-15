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

}
