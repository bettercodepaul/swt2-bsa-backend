/**
 *
 */
package online.bogenliga.application.common.config;

import java.util.Iterator;
import java.util.ServiceLoader;
import online.bogenliga.application.common.utils.ArgumentChecker;


/**
 * A configuration manager, that is uses {@link ServiceLoader} to locate configurations.
 *
 * @author Thomas Wolf
 */
public class ServiceLoaderConfigurationManager extends DefaultConfigurationManager {

    /**
     * Create a new {@link ServiceLoaderConfigurationManager}.
     */
    public ServiceLoaderConfigurationManager() {
    }


    /* (non-Javadoc)
     * @see de.exxcellent.exxar.config.ConfigurationProvider#hasConfiguration(java.lang.Class)
     */
    @Override
    public <C extends Configuration> boolean hasConfiguration(final Class<C> configuration) {
        ArgumentChecker.checkNotNull(configuration, "configuration");
        if (super.hasConfiguration(configuration)) {
            return true;
        } else {
            final Iterator<C> iterator = ServiceLoader.load(configuration).iterator();
            if (iterator.hasNext()) {
                final C impl = iterator.next();
                // Register implementation found
                super.addConfiguration(configuration, impl);
                return true;
            } else {
                return false;
            }
        }
    }


    /* (non-Javadoc)
     * @see de.exxcellent.exxar.config.DefaultConfigurationManager#getConfigurationInt(java.lang.Class)
     */
    @Override
    protected <C extends Configuration> C getConfigurationInt(final Class<C> configuration) {
        ArgumentChecker.checkNotNull(configuration, "configuration");
        C impl = super.getConfigurationInt(configuration);
        if (impl == null) {
            // Load configuration implementation using ServiceLoader (once)
            final Iterator<C> iterator = ServiceLoader.load(configuration).iterator();
            if (iterator.hasNext()) {
                impl = iterator.next();
                // Register implementation found
                super.addConfiguration(configuration, impl);
            }
        }
        return impl;
    }
}
