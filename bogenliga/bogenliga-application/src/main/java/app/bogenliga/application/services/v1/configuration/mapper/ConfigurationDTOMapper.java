package app.bogenliga.application.services.v1.configuration.mapper;

import java.util.function.Function;
import app.bogenliga.application.business.configuration.api.types.ConfigurationVO;
import app.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import app.bogenliga.application.services.v1.configuration.model.ConfigurationDTO;

/**
 * I map the {@link ConfigurationVO} and {@link ConfigurationDTO} objects
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public interface ConfigurationDTOMapper extends DataTransferObjectMapper {

    /**
     * I map the {@link ConfigurationVO} object to the {@link ConfigurationDTO} object
     */
    public static Function<ConfigurationVO, ConfigurationDTO> toDTO = (vo) -> {
        String key = vo.getKey();
        String value = vo.getValue();

        return new ConfigurationDTO(key, value);
    };

    /**
     * I map the {@link ConfigurationDTO} object to the {@link ConfigurationVO} object
     */
    public static Function<ConfigurationDTO, ConfigurationVO> toVO = (dto) -> {
        String key = dto.getKey();
        String value = dto.getValue();

        return new ConfigurationVO(key, value);
    };
}
