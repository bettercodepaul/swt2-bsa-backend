package de.bogenliga.application.services.v1.vereine.mapper;

import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.vereine.model.VereineDTO;

/**
 * I map the {@link VereinDO} and {@link VereineDTO} objects
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
public class VereineDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link VereinDO} to the {@link VereineDTO} object
     */

    public static final Function<VereinDO, VereineDTO> toDTO = vereinDO -> {
        final Long vereinId = vereinDO.getId();
        final String vereinName = vereinDO.getName();
        final String vereinIdentifier = vereinDO.getDsbIdentifier();
        final Long regionId = vereinDO.getRegionId();
        final String regionName = vereinDO.getRegionName();
        final Long createdByUserId = vereinDO.getCreatedByUserId();
        final OffsetDateTime createdAtUtc = vereinDO.getCreatedAtUtc();
        final Long version = vereinDO.getVersion();


        return new VereineDTO(vereinId, vereinName, vereinIdentifier, regionId, regionName, createdAtUtc,createdByUserId, version);
    };

    /**
     I map the {@link VereineDTO} to the {@link VereinDO} object
     */

    public static final Function<VereineDTO, VereinDO> toDO = dto -> {
        final Long vereinId  = dto.getId();
        final String vereinName = dto.getName();
        final String vereinIdentifier = dto.getIdentifier();
        final Long regionId = dto.getRegionId();
        final Long createdByUserId = dto.getCreatedByUserId();
        final OffsetDateTime createdAtUtc = dto.getCreatedAtUtc();
        final Long version = dto.getVersion();

        return new VereinDO(vereinId, vereinName, vereinIdentifier, regionId,
                createdAtUtc, createdByUserId, version);
    };

    /**
     * Constructor
     */
    private VereineDTOMapper(){
        //empty
    }
}
