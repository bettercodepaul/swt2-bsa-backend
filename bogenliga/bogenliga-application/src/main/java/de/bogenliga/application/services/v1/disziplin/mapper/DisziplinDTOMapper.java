package de.bogenliga.application.services.v1.disziplin.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.disziplin.model.DisziplinDTO;

/**
 * I map the {@link DisziplinDO} and {@link DisziplinDTO} objects
 *
 * @author Nico Keilig
 */
public class DisziplinDTOMapper implements DataTransferObjectMapper {


    private DisziplinDTOMapper(){}

    /**
     * I map the {@link DisziplinDO} object to the {@link DisziplinDTO} object
     */
    public static final Function<DisziplinDO, DisziplinDTO> toDTO = vo -> {
        final Long disziplinID = vo.getDisziplinID();
        final String disziplinName = vo.getDisziplinName();

        return new DisziplinDTO(disziplinID,disziplinName);
    };

    /**
     * I map the {@link DisziplinDTO} object to the {@link DisziplinDO} object
     */
    public static final Function<DisziplinDTO, DisziplinDO> toDO = dto -> {
        final Long disziplinId = dto.getDisziplinId();
        final String disziplinName = dto.getDisziplinName();


        return new DisziplinDO(disziplinId,disziplinName);
    };
}
