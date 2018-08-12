package app.bogenliga.application.business.configuration.api;

import java.util.Set;
import app.bogenliga.application.business.configuration.api.types.ConfigurationVO;
import app.bogenliga.application.common.component.ComponentFacade;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public interface ConfigurationComponent extends ComponentFacade {

    Set<ConfigurationVO> findAll();
}
