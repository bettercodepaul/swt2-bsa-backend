package de.bogenliga.application.services.v1.sync.mapper;


import java.util.function.Function;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import de.bogenliga.application.services.v1.sync.model.LigaSyncMannschaftsmitgliedDTO;

public class LigaSyncMannschaftsmitgliedDTOMapper implements DataTransferObjectMapper {

    public static final Function<MannschaftsmitgliedDO, LigaSyncMannschaftsmitgliedDTO> toDTO = LigaSyncMannschaftsmitgliedDTOMapper::apply;
    public static final Function<LigaSyncMannschaftsmitgliedDTO, MannschaftsMitgliedDTO> toMannschaftsmitgliedDTO = LigaSyncMannschaftsmitgliedDTOMapper::apply;

    private LigaSyncMannschaftsmitgliedDTOMapper() {
        // empty private constructor
    }

    private static LigaSyncMannschaftsmitgliedDTO apply(MannschaftsmitgliedDO mannschaftmitgliedDO) {

        final Long id = mannschaftmitgliedDO.getId();
        final Long version = mannschaftmitgliedDO.getVersion();
        final Long mannschaftId = mannschaftmitgliedDO.getMannschaftId();
        final Long dsbMitgliedId = mannschaftmitgliedDO.getDsbMitgliedId();
        final Long rueckennummer = mannschaftmitgliedDO.getRueckennummer();


        return new LigaSyncMannschaftsmitgliedDTO(id, version, mannschaftId, dsbMitgliedId, rueckennummer);
    }


    private static MannschaftsMitgliedDTO apply(LigaSyncMannschaftsmitgliedDTO ligaSyncMannschaftsmitgliedDTO){
        final Long id = ligaSyncMannschaftsmitgliedDTO.getId();
        final Long mannschaftId = ligaSyncMannschaftsmitgliedDTO.getMannschaftId();
        final Long dsbMitgliedId = ligaSyncMannschaftsmitgliedDTO.getDsbMitgliedId();
        final Long rueckennummer = ligaSyncMannschaftsmitgliedDTO.getRueckennummer();

        return new MannschaftsMitgliedDTO(id, mannschaftId, dsbMitgliedId, 0, rueckennummer);
    }
}
