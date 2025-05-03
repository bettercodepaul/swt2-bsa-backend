package de.bogenliga.application.business.schusszettel.impl.mapper;

import de.bogenliga.application.business.schusszettel.impl.entity.TabletSchusszettelEntity;
import de.bogenliga.application.services.v1.schusszettel.model.SatzEingabeDTO;
import de.bogenliga.application.services.v1.schusszettel.model.SchuetzenSatzDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper-Klasse zum Konvertieren zwischen Entity und DTO.
 */
public class TabletSchusszettelMapper {

    public static List<TabletSchusszettelEntity> fromDTO(Long wettkampfId, Long teamId, SatzEingabeDTO dto) {
        List<TabletSchusszettelEntity> result = new ArrayList<>();
        for (SchuetzenSatzDTO satz : dto.getSatzeingabe()) {
            TabletSchusszettelEntity entity = new TabletSchusszettelEntity();
            entity.setWettkampfId(wettkampfId);
            entity.setTeamId(teamId);
            entity.setSchuetzenId(satz.getSchuetzenId());
            entity.setSchuss1(satz.getSchuss1());
            entity.setSchuss2(satz.getSchuss2());
            entity.setSchuss3(satz.getSchuss3());
            result.add(entity);
        }
        return result;
    }
}