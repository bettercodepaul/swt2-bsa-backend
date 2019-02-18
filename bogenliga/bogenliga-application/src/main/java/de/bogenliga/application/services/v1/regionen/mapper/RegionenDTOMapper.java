package de.bogenliga.application.services.v1.regionen.mapper;

import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.regionen.model.RegionenDTO;
import de.bogenliga.application.services.v1.vereine.model.VereineDTO;

/**
 * I map the {@link RegionenDO} and {@link RegionenDTO} objects
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class RegionenDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link VereinDO} to the {@link VereineDTO} object
     */
    public static final Function<RegionenDO, RegionenDTO> toDTO = regionenDO -> {
        final Long id = regionenDO.getId();
        final String name = regionenDO.getRegionName();
        final String kuerzel = regionenDO.getRegionKuerzel();
        final String typ = regionenDO.getRegionType();
        final Long uebergeordnet = regionenDO.getRegionUebergeordnet();
        final Long createdByUserId = regionenDO.getCreatedByUserId();
        final OffsetDateTime createdAtUtc = regionenDO.getCreatedAtUtc();
        final Long version = regionenDO.getVersion();


        return new RegionenDTO(id, name, kuerzel, typ, uebergeordnet, createdAtUtc,createdByUserId, version);
    };

    /**
     * Constructor
     */
    private RegionenDTOMapper(){
        //empty
    }
}