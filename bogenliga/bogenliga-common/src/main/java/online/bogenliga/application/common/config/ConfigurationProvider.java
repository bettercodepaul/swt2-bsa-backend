package online.bogenliga.application.common.config;

/**
 * Provides access to stored {@link Configuration Configurations}.
 *
 * @author Thomas Wolf
 * @version 1.$Revision$
 */
public interface ConfigurationProvider {

    /**
     * Returns <code>true</code>, if the there is a configuration
     * implementation registered under the given configuration interface.
     *
     * @param configuration the configuration interface {@link Class} (not <code>null</code>)
     * @return the {@link Configuration} implementation
     * @throws IllegalArgumentException if the configuration class given is
     *                                  <code>null</code>
     */
    public <C extends Configuration> boolean hasConfiguration(Class<C> configuration);

    /**
     * Returns the configuration implementation of the given configuration interface.
     *
     * @param configuration the configuration interface {@link Class} (not <code>null</code>)
     * @return the {@link Configuration} implementation (guaranteed to be not <code>null</code>)
     * @throws IllegalArgumentException if no configuration implementation is
     *                                  registered under the given key class or if the configuration class given is
     *                                  <code>null</code>
     */
    public <C extends Configuration> C getConfiguration(Class<C> configuration);

}