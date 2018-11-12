package de.bogenliga.application.services.v1.mannschaftsmitglied.mapper;

import de.bogenliga.application.common.service.types.DataTransferObject;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import org.apache.tomcat.util.buf.StringUtils;

public class MannschaftsMitgliedDTOMapper implements DataTransferObject {

    public static final Function<MannschaftsMitgliedDO, MannschaftsMitgliedDTO> toDTO = mannschaftsMitgliedDO -> {
        final long mannschaftId = mannschaftsMitgliedDO.getId();
        final long dsbMitgliedId = mannschaftsMitgliedDO.getDsbMitgliedId();
        final boolean dsbMitgliedEingesetzt = mannschaftsMitgliedDO.getDsbMitgliedEingesetzt();

        return new MannschaftsMitgliedDTO(mannschaftId,
                dsbMitgliedId, dsbMitgliedEingesetzt);
    }

    public static final Function<MannschaftsMitgliedDTO, MannschaftsMitgliedDO> toDO = dto -> {
        final long mannschaftId = dto.getId();
        final long dsbMitgliedId = dto.getDsbMitgliedId();
        final boolean dsbMitgliedEingesetzt = dto.getDsbMitgliedEingesetzt();

        return new MannschaftsMitgliedDO(mannschaftId,
                dsbMitgliedId, dsbMitgliedEingesetzt);
    }

    private MannschaftsMitgliedDTOMapper(){};

}
