package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.services.v1.schusszettel.model.SatzEingabeDTO;
import de.bogenliga.application.services.v1.schusszettel.model.SchuetzenSatzDTO;
import de.bogenliga.application.business.schusszettel.api.types.SatzEingabeDO;
import de.bogenliga.application.business.schusszettel.api.types.SchuetzenSatzDO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.ArrayList;

/**
 * Mapper zur Konvertierung zwischen SatzEingabeDTO und SatzEingabeDO.
 *
 * @author Marty Lauterbach
 */
public class TabletSatzEingabeMapper {

    public static SatzEingabeDO toDO(SatzEingabeDTO dto) {
        SatzEingabeDO doObj = new SatzEingabeDO();
        doObj.setSatzeingabe(
                dto.getSatzeingabe().stream()
                        .map(TabletSatzEingabeMapper::toDO)
                        .collect(Collectors.toList())
        );
        return doObj;
    }

    public static SatzEingabeDTO toDTO(SatzEingabeDO doObj) {
        SatzEingabeDTO dto = new SatzEingabeDTO();
        dto.setSatzeingabe(
                doObj.getSatzeingabe().stream()
                        .map(TabletSatzEingabeMapper::toDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    public static SchuetzenSatzDO toDO(SchuetzenSatzDTO dto) {
        return new SchuetzenSatzDO(
                dto.getSchuetzenId(),
                dto.getSchuss1(),
                dto.getSchuss2(),
                dto.getSchuss3()
        );
    }

    public static SchuetzenSatzDTO toDTO(SchuetzenSatzDO doObj) {
        SchuetzenSatzDTO dto = new SchuetzenSatzDTO();
        dto.setSchuetzenId(doObj.getSchuetzenId());
        dto.setSchuss1(doObj.getSchuss1());
        dto.setSchuss2(doObj.getSchuss2());
        dto.setSchuss3(doObj.getSchuss3());
        return dto;
    }

    @SuppressWarnings("unchecked")
    public static SatzEingabeDTO fromMap(Map<String, Object> payload) {
        SatzEingabeDTO dto = new SatzEingabeDTO();

        Object rawList = payload.get("satzeingabe");
        if (rawList instanceof List<?>) {
            List<Map<String, Object>> eingaben = (List<Map<String, Object>>) rawList;
            List<SchuetzenSatzDTO> result = new ArrayList<>();

            for (Map<String, Object> entry : eingaben) {
                SchuetzenSatzDTO satz = new SchuetzenSatzDTO();
                satz.setSchuetzenId(getLong(entry.get("schuetzen_id")));
                satz.setSchuss1(getInt(entry.get("schuss1")));
                satz.setSchuss2(getInt(entry.get("schuss2")));
                satz.setSchuss3(getInt(entry.get("schuss3")));
                result.add(satz);
            }

            dto.setSatzeingabe(result);
        }

        return dto;
    }

    private static Long getLong(Object o) {
        if (o instanceof Number) return ((Number) o).longValue();
        if (o instanceof String) return Long.valueOf((String) o);
        return null;
    }

    private static Integer getInt(Object o) {
        if (o instanceof Number) return ((Number) o).intValue();
        if (o instanceof String) return Integer.valueOf((String) o);
        return null;
    }

}
