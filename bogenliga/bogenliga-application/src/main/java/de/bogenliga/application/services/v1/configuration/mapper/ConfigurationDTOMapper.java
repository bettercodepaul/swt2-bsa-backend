package de.bogenliga.application.services.v1.configuration.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.configuration.model.ConfigurationDTO;

/**
 * I map the {@link ConfigurationDO} and {@link ConfigurationDTO} objects
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public final class ConfigurationDTOMapper implements DataTransferObjectMapper {

    /**
     * Constructor
     */
    private ConfigurationDTOMapper() {
        // empty private constructor
    }


    /**
     * I map the {@link ConfigurationDO} object to the {@link ConfigurationDTO} object
     */
    public static final Function<ConfigurationDO, ConfigurationDTO> toDTO = vo -> {
        final Long id = vo.getId();
        final String key = vo.getKey();
        final String value = vo.getValue();
        final String regex = vo.getRegex();

        return new ConfigurationDTO(id, key, value, regex);
    };

    /**
     * I map the {@link ConfigurationDTO} object to the {@link ConfigurationDO} object
     */
    public static final Function<ConfigurationDTO, ConfigurationDO> toDO = dto -> {
        final Long id = dto.getId();
        final String key = dto.getKey();
        final String value = dto.getValue();
        final String regex = dto.getRegex();

        return new ConfigurationDO(id, key, value, regex);
    };
}
