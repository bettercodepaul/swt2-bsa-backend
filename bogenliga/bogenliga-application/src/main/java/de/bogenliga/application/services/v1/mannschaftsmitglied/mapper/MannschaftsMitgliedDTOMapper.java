package de.bogenliga.application.services.v1.mannschaftsmitglied.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.common.service.types.DataTransferObject;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;

public class MannschaftsMitgliedDTOMapper implements DataTransferObject {
    private static final long serialVersionUID = -6617029287670580334L;

    private MannschaftsMitgliedDTOMapper() {
    }

    public static final Function<MannschaftsmitgliedDO, MannschaftsMitgliedDTO> toDTO = mannschaftsMitgliedDO -> {
        final Long id = mannschaftsMitgliedDO.getId();
        final Long mannschaftId = mannschaftsMitgliedDO.getMannschaftId();
        final Long dsbMitgliedId = mannschaftsMitgliedDO.getDsbMitgliedId();
        final Integer dsbMitgliedEingesetzt = mannschaftsMitgliedDO.getDsbMitgliedEingesetzt();
        final Long rueckennummer = mannschaftsMitgliedDO.getRueckennummer();

        return new MannschaftsMitgliedDTO(id, mannschaftId,
                dsbMitgliedId, dsbMitgliedEingesetzt, rueckennummer);
    };

    public static final Function<MannschaftsMitgliedDTO, MannschaftsmitgliedDO> toDO = dto -> {
        final Long id = dto.getId();
        final Long mannschaftId = dto.getMannschaftsId();
        final Long dsbMitgliedId = dto.getDsbMitgliedId();
        final Integer dsbMitgliedEingesetzt = dto.getDsbMitgliedEingesetzt();
        final Long rueckennummer = dto.getRueckennummer();

        return new MannschaftsmitgliedDO(id, mannschaftId,
                dsbMitgliedId, dsbMitgliedEingesetzt, "","", rueckennummer);
    };

}
