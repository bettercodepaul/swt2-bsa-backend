package de.bogenliga.application.services.v1.mannschaftsmitglied.mapper;

import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.common.service.types.DataTransferObject;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import org.apache.tomcat.util.buf.StringUtils;

import java.util.function.Function;

public class MannschaftsMitgliedDTOMapper implements DataTransferObject {

    public static final Function<MannschaftsmitgliedDO, MannschaftsMitgliedDTO> toDTO = mannschaftsMitgliedDO -> {
        final long mannschaftId = mannschaftsMitgliedDO.getMannschaftId();
        final long dsbMitgliedId = mannschaftsMitgliedDO.getDsbMitgliedId();
        final boolean dsbMitgliedEingesetzt = mannschaftsMitgliedDO.isDsbMitgliedEingesetzt();

        return new MannschaftsMitgliedDTO(mannschaftId,
                dsbMitgliedId, dsbMitgliedEingesetzt);
    };

    public static final Function<MannschaftsMitgliedDTO, MannschaftsmitgliedDO> toDO = dto -> {
        final long mannschaftId = dto.getMannschaftsId();
        final long dsbMitgliedId = dto.getDsbMitgliedId();
        final boolean dsbMitgliedEingesetzt = dto.isDsbMitgliedEingesetzt();

        return new MannschaftsmitgliedDO(mannschaftId,
                dsbMitgliedId, dsbMitgliedEingesetzt);
    };

    private MannschaftsMitgliedDTOMapper(){};

}
