package online.bogenliga.application.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import online.bogenliga.application.common.validation.Preconditions;


/**
 * An abstract implementation of {@link ConfigurationManager}.
 *
 * @author Thomas Wolf
 * @version 1.$Revision$
 */
public class DefaultConfigurationManager implements ConfigurationManager {

    private static final String CONFIGURATION_CLASS = "configuration class must not be null";

    /**
     * Map of key ({@link Configuration}) to {@link Configuration} implementation.
     */
    private final Map<Class<Configuration>, Configuration> configImplementations = new HashMap<>();


    /**
     * Creates a new {@link DefaultConfigurationManager}.
     */
    DefaultConfigurationManager() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C extends Configuration> void addConfiguration(final Class<C> configuration, final C implementation) {
        Preconditions.checkNotNull(configuration, CONFIGURATION_CLASS);
        Preconditions.checkNotNull(implementation, "implementation");
        configImplementations.put((Class<Configuration>) configuration, implementation);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <C extends Configuration> boolean hasConfiguration(final Class<C> configuration) {
        Preconditions.checkNotNull(configuration, CONFIGURATION_CLASS);
        return (getConfigurationInt(configuration) != null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <C extends Configuration> C getConfiguration(final Class<C> configuration) {
        Preconditions.checkNotNull(configuration, CONFIGURATION_CLASS);
        final C configurationInstance = getConfigurationInt(configuration);
        if (configurationInstance != null) {
            return configurationInstance;
        } else {
            // not found
            throw new NoSuchElementException(
                    "No ConfigurationDescriptor defined for interface \"" + configuration.getName() + "\".");
        }
    }


    /**
     * Returns the {@link Configuration} instance for the key class.
     *
     * @param configuration the configuration interface {@link Class} (not <code>null</code>)
     * @return the {@link Configuration} instance or <code>null</code>, if
     * no {@link Configuration} was found
     */
    @SuppressWarnings("unchecked")
    protected <C extends Configuration> C getConfigurationInt(final Class<C> configuration) {
        Preconditions.checkNotNull(configuration, CONFIGURATION_CLASS);
        if (this.configImplementations.containsKey(configuration)) {
            return (C) this.configImplementations.get(configuration);
        } else {
            return null;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConfiguration(final Class<? extends Configuration> configurationClass) {
        Preconditions.checkNotNull(configurationClass, "The key class must not be null");
        this.configImplementations.remove(configurationClass);
    }
}
