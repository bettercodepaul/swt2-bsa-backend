package de.bogenliga.application.services.v1.wettkampftyp.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.configuration.model.ConfigurationDTO;
import de.bogenliga.application.services.v1.wettkampftyp.model.WettkampfTypDTO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class WettkampfTypDTOMapper implements DataTransferObjectMapper {

    /**
     * Empty hidden constructor to prevent instantiation
     */
    private WettkampfTypDTOMapper () {}

    /**
     * I map the {@link ConfigurationDO} object to the {@link ConfigurationDTO} object
     */
    public static final Function<WettkampfTypDO, WettkampfTypDTO> toDTO = wettkampfTypDO -> new WettkampfTypDTO(
            wettkampfTypDO.getId(),wettkampfTypDO.getName());


    /**
     * I map the {@link ConfigurationDO} object to the {@link ConfigurationDTO} object
     */
    public static final Function<WettkampfTypDTO, WettkampfTypDO> toDO = wettkampfTypDTO -> new WettkampfTypDO(wettkampfTypDTO.getId(),
            wettkampfTypDTO.getName());
}
