package de.bogenliga.application.services.v1.schusszettel.mapper;

import de.bogenliga.application.business.schusszettel.api.types.SchuetzenMeldungDO;
import de.bogenliga.application.services.v1.schusszettel.model.SchuetzenMeldungDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Mapper für Schützenmeldung DTO ↔ DO.
 * Trennt API-Modelle vom Business-Layer.
 *
 * @author Marty Lauterbach
 */
public class TabletSchuetzenMeldungMapper {

    public static SchuetzenMeldungDO toDO(SchuetzenMeldungDTO dto) {
        SchuetzenMeldungDO result = new SchuetzenMeldungDO();
        result.setGemeldeteSchuetzen(dto.getGemeldeteSchuetzen());
        return result;
    }

    public static SchuetzenMeldungDTO toDTO(SchuetzenMeldungDO doObj) {
        SchuetzenMeldungDTO result = new SchuetzenMeldungDTO();
        result.setGemeldeteSchuetzen(doObj.getGemeldeteSchuetzen());
        return result;
    }

    @SuppressWarnings("unchecked")
    public static SchuetzenMeldungDTO fromMap(Map<String, Object> payload) {
        SchuetzenMeldungDTO dto = new SchuetzenMeldungDTO();

        Object rawList = payload.get("gemeldete_schuetzen");
        if (rawList instanceof List<?>) {
            List<Object> inputList = (List<Object>) rawList;
            List<Long> resultList = new ArrayList<>();

            for (Object obj : inputList) {
                if (obj instanceof Number) {
                    resultList.add(((Number) obj).longValue());
                } else if (obj instanceof String) {
                    resultList.add(Long.valueOf((String) obj));
                }
            }

            dto.setGemeldeteSchuetzen(resultList);
        }

        return dto;
    }

}
