/**
 *
 */
package app.bogenliga.application.common.config;


/**
 * Holds the global instance of {@link ConfigurationManager} used.
 *
 * @author Thomas Wolf
 */
public class GlobalConfigurationManager {

    /**
     * Static instance.
     */
    private static final ServiceLoaderConfigurationManager configurationManager = new ServiceLoaderConfigurationManager();


    /**
     * Create a new {@link GlobalConfigurationManager}.
     */
    private GlobalConfigurationManager() {
    }


    /**
     * Returns the global {@link ConfigurationManager} instance.
     */
    public static ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }


    /**
     * Returns the configuration implementation of the given configuration interface.
     *
     * @param configuration the configuration interface {@link Class} (not <code>null</code>)
     * @return the {@link Configuration} implementation (guaranteed to be not <code>null</code>)
     * @throws IllegalArgumentException if no configuration implementation is
     *                                  registered under the given key class or if the configuration class given is
     *                                  <code>null</code>
     */
    public static <C extends Configuration> C getGlobalConfiguration(final Class<C> configuration) {
        return getConfigurationManager().getConfiguration(configuration);
    }
}
