package online.bogenliga.application.services.v1.configuration.mapper;

import java.util.function.Function;
import online.bogenliga.application.business.configuration.api.types.ConfigurationVO;
import online.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import online.bogenliga.application.services.v1.configuration.model.ConfigurationDTO;

/**
 * I map the {@link ConfigurationVO} and {@link ConfigurationDTO} objects
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
     * I map the {@link ConfigurationVO} object to the {@link ConfigurationDTO} object
     */
    public static final Function<ConfigurationVO, ConfigurationDTO> toDTO = vo -> {
        final String key = vo.getKey();
        final String value = vo.getValue();

        return new ConfigurationDTO(key, value);
    };

    /**
     * I map the {@link ConfigurationDTO} object to the {@link ConfigurationVO} object
     */
    public static final Function<ConfigurationDTO, ConfigurationVO> toVO = dto -> {
        final String key = dto.getKey();
        final String value = dto.getValue();

        return new ConfigurationVO(key, value);
    };
}
