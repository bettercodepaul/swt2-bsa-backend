package app.bogenliga.application.business.configuration.impl.business;

import java.util.HashSet;
import java.util.Set;
import app.bogenliga.application.business.configuration.api.ConfigurationComponent;
import app.bogenliga.application.business.configuration.api.types.ConfigurationVO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class ConfigurationComponentImpl implements ConfigurationComponent {

    @Override
    public Set<ConfigurationVO> findAll() {
        return new HashSet<>();
    }
}
