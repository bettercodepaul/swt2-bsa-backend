package de.bogenliga.application.services.v1.wettkampf.mapper;

import de.bogenliga.application.business.wettkampf.api.types.AnzeigenDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.wettkampf.model.AnzeigenDTO;

import java.util.function.Function;

public final class AnzeigenDTOMapper implements DataTransferObjectMapper {
    public static final Function<AnzeigenDO, AnzeigenDTO> toDTO = anzeigenDO -> {
        final Long id = anzeigenDO.getId();
        final String physischeBildschirmId = anzeigenDO.getPhysischeBildschirmId();
        final String tableTyp = anzeigenDO.getTableTyp();
        final Long wettkampfId = anzeigenDO.getWettkampfId();
        final int aktuellesMatch = anzeigenDO.getAktuellesMatch();

        return new AnzeigenDTO(id, physischeBildschirmId, tableTyp, wettkampfId, aktuellesMatch);
    };

    public static final Function<AnzeigenDTO, AnzeigenDO> toDO = anzeigenDTO -> {
        final Long id = anzeigenDTO.getId();
        final String physischeBildschirmId = anzeigenDTO.getPhysischeBildschirmId();
        final String tableTyp = anzeigenDTO.getTableTyp();
        final Long wettkampfId = anzeigenDTO.getWettkampfId();
        final int aktuellesMatch = anzeigenDTO.getAktuellesMatch();

        return new AnzeigenDO(id, physischeBildschirmId, tableTyp, wettkampfId, aktuellesMatch);
    };

    private AnzeigenDTOMapper() {
        //Leerer privater Konstruktor zum verstecken des impliziten public Konstruktors
    }
}
