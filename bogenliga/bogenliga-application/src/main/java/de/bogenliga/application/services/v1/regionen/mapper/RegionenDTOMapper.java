package de.bogenliga.application.services.v1.regionen.mapper;

import java.time.OffsetDateTime;
import java.util.function.Function;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.regionen.model.RegionenDTO;

/**
 * I map the {@link RegionenDO} and {@link RegionenDTO} objects
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class RegionenDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link RegionenDO} to the {@link RegionenDTO} object
     */
    public static final Function<RegionenDO, RegionenDTO> toDTO = regionenDO -> {
        final Long id = regionenDO.getId();
        final String regionName = regionenDO.getRegionName();
        final String kuerzel = regionenDO.getRegionKuerzel();
        final String typ = regionenDO.getRegionTyp();
        final Long uebergeordnet = regionenDO.getRegionUebergeordnet();
        final String regionUebergeordnetAsName = regionenDO.getRegionUebergeordnetAsName();
        final Long createdByUserId = regionenDO.getCreatedByUserId();
        final OffsetDateTime createdAtUtc = regionenDO.getCreatedAtUtc();
        final Long version = regionenDO.getVersion();


        return new RegionenDTO(id, regionName, kuerzel, typ, uebergeordnet, regionUebergeordnetAsName, createdAtUtc,
                createdByUserId, version);
    };


    /**
     I map the {@link RegionenDTO} to the {@link RegionenDO} object
     */

    public static final Function<RegionenDTO, RegionenDO> toDO = dto -> {
        final Long id = dto.getId();
        final String name = dto.getRegionName();
        final String kuerzel = dto.getRegionKuerzel();
        final String typ = dto.getRegionTyp();
        final Long uebergeordnet = dto.getRegionUebergeordnet();
        final String regionUebergeordnetAsName = dto.getRegionUebergeordnetAsName();
        final Long createdByUserId = dto.getCreatedByUserId();
        final OffsetDateTime createdAtUtc = dto.getCreatedAtUtc();
        final Long version = dto.getVersion();


        return new RegionenDO(id, name, kuerzel, typ, uebergeordnet, regionUebergeordnetAsName,
                createdAtUtc, createdByUserId, version);
    };


    /**
     * Constructor
     */
    private RegionenDTOMapper(){
        //empty
    }

}