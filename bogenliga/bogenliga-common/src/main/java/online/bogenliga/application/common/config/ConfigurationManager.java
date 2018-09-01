package online.bogenliga.application.common.config;


/*
 * Copyright (c) 2005
 * software design & management AG
 * All rights reserved.
 * This file is made available under the terms of the license
 * agreement that accompanies this distribution.
 *
 * $Revision:$, last modified $Date: $ by $Author: $
 */

/**
 * Adds modification capabilities to the {@link ConfigurationProvider}.
 * <P>
 * Note: Implementations of this interface must be thread safe.
 *
 * @author Thomas Wolf
 * @version 1.0
 */
public interface ConfigurationManager extends ConfigurationProvider {

    /**
     * Adds a configuration implementation for the given configuration interface.
     *
     * @param configuration  the {@link Configuration} interface
     *                       (not <code>null</code>, must be a sub-interface of {@link Configuration})
     * @param implementation the {@link Configuration} implementation
     *                       (not <code>null</code>, must implement <code>configuration</code>)
     */
    public <C extends Configuration> void addConfiguration(Class<C> configuration, C implementation);

    /**
     * Removes a previously added configuration implementation
     *
     * @param configuration the {@link Configuration} interface
     *                      (not <code>null</code>, must be a sub-interface of {@link Configuration})
     */
    public void removeConfiguration(Class<? extends Configuration> configuration);

}