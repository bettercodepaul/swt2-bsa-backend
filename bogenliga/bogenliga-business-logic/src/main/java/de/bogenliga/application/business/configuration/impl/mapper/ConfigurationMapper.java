package de.bogenliga.application.business.configuration.impl.mapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.business.configuration.impl.entity.ConfigurationBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

/**
 * I convert the {@link ConfigurationBE} and the {@link ConfigurationDO}
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public final class ConfigurationMapper implements ValueObjectMapper {

    /**
     * Converts a {@link ConfigurationBE} to a {@link ConfigurationDO}
     */
    public static final Function<ConfigurationBE, ConfigurationDO> toDO = be -> {
        final Long id = be.getConfigurationId();
        final String key = be.getConfigurationKey();
        final String value = be.getConfigurationValue();
        final String regex = be.getConfigurationRegex();

        Long createdByUserId = be.getCreatedByUserId();
        Long lastModifiedByUserId = be.getLastModifiedByUserId();
        Long version = be.getVersion();

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(be.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(be.getLastModifiedAtUtc());

        return new ConfigurationDO(id, key, value, regex, createdAtUtc, createdByUserId, lastModifiedAtUtc, lastModifiedByUserId,
                version);
    };

    /**
     * Converts a {@link ConfigurationDO} to a {@link ConfigurationBE}
     */
    public static final Function<ConfigurationDO, ConfigurationBE> toBE = vo -> {
        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(vo.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(
                vo.getLastModifiedAtUtc());

        ConfigurationBE configurationBE = new ConfigurationBE();
        configurationBE.setConfigurationId(vo.getId());
        configurationBE.setConfigurationValue(vo.getValue());
        configurationBE.setConfigurationKey(vo.getKey());
        configurationBE.setConfigurationRegex(vo.getRegex());
        configurationBE.setCreatedAtUtc(createdAtUtcTimestamp);
        configurationBE.setCreatedByUserId(vo.getCreatedByUserId());
        configurationBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        configurationBE.setLastModifiedByUserId(vo.getLastModifiedByUserId());
        configurationBE.setVersion(vo.getVersion());

        return configurationBE;
    };


    /**
     * Private constructor
     */
    private ConfigurationMapper() {
        // empty private constructor
    }
}
