package de.bogenliga.application.services.v1.schuetzenstatistik.mapper;

import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.schuetzenstatistik.model.SchuetzenstatistikDTO;

import java.util.function.Function;

/**
 * I map the {@link SchuetzenstatistikDO} and {@link SchuetzenstatistikDTO} objects
 */
public class SchuetzenstatistikDTOMapper implements DataTransferObjectMapper {
    /**
     * I map the {@link SchuetzenstatistikDO} object to the {@link SchuetzenstatistikDTO} object
     */
    public static final Function<SchuetzenstatistikDO, SchuetzenstatistikDTO> toDTO = schuetzenstatistikDO -> {

        final Long veranstaltungId = schuetzenstatistikDO.getveranstaltungId();
        final String veranstaltungName = schuetzenstatistikDO.getveranstaltungName();
        final Long wettkampfId = schuetzenstatistikDO.getwettkampfId();
        final int wettkampfTag = schuetzenstatistikDO.getwettkampfTag();
        final Long mannschaftId = schuetzenstatistikDO.getmannschaftId();
        final int mannschaftNummer = schuetzenstatistikDO.getmannschaftNummer();
        final Long vereinId = schuetzenstatistikDO.getvereinId();
        final String vereinName = schuetzenstatistikDO.getvereinName();
        final Long matchId = schuetzenstatistikDO.getMatchId();
        final int matchNr = schuetzenstatistikDO.getMatchNr();
        final Long dsbMitgliedId = schuetzenstatistikDO.getDsbMitgliedId();
        final String dsbMitgliedName = schuetzenstatistikDO.getDsbMitgliedName();
        final int rueckenNummer = schuetzenstatistikDO.getRueckenNummer();
        final float pfeilPunkteSchnitt = schuetzenstatistikDO.getPfeilpunkteSchnitt();
        final String schuetzeSatz1 = removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz1());
        final String schuetzeSatz2 = removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz2());
        final String schuetzeSatz3 = removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz3());
        final String schuetzeSatz4 = removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz4());
        final String schuetzeSatz5 = removeCurlyBracketsFromSchuetzeSatz(schuetzenstatistikDO.getschuetzeSatz5());



        return new SchuetzenstatistikDTO(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag, mannschaftId,
                mannschaftNummer, vereinId, vereinName, matchId, matchNr, dsbMitgliedId, dsbMitgliedName, rueckenNummer,
                pfeilPunkteSchnitt, schuetzeSatz1, schuetzeSatz2, schuetzeSatz3, schuetzeSatz4, schuetzeSatz5);
    };
    /**
     * I map the {@link SchuetzenstatistikDTO} object to the {@link SchuetzenstatistikDO} object
     */
    public static final Function<SchuetzenstatistikDTO, SchuetzenstatistikDO> toDO = dto -> {

        SchuetzenstatistikDO schuetzenstatistikDO = new SchuetzenstatistikDO();

        schuetzenstatistikDO.setveranstaltungId(dto.getVeranstaltungId());
        schuetzenstatistikDO.setveranstaltungName(dto.getVeranstaltungName());
        schuetzenstatistikDO.setwettkampfId(dto.getWettkampfId());
        schuetzenstatistikDO.setwettkampfTag(dto.getWettkampfTag());
        schuetzenstatistikDO.setmannschaftId(dto.getMannschaftId());
        schuetzenstatistikDO.setmannschaftNummer(dto.getMannschaftNummer());
        schuetzenstatistikDO.setvereinId(dto.getVereinId());
        schuetzenstatistikDO.setvereinName(dto.getVereinName());
        schuetzenstatistikDO.setMatchId(dto.getMatchId());
        schuetzenstatistikDO.setMatchNr(dto.getMatchNr());
        schuetzenstatistikDO.setDsbMitgliedId(dto.getDsbMitgliedId());
        schuetzenstatistikDO.setDsbMitgliedName(dto.getDsbMitgliedName());
        schuetzenstatistikDO.setRueckenNummer(dto.getRueckenNummer());
        schuetzenstatistikDO.setPfeilpunkteSchnitt(dto.getPfeilpunkteSchnitt());
        schuetzenstatistikDO.setSchuetzeSaetze( new String[]{
                                                "{"+ dto.getSchuetzeSatz1() + "}", "{"+ dto.getSchuetzeSatz2() + "}",
                                                "{"+ dto.getSchuetzeSatz3() + "}", "{"+ dto.getSchuetzeSatz4() + "}",
                                                "{"+ dto.getSchuetzeSatz5() + "}"});
        return schuetzenstatistikDO;
    };
    // Method for removing the braces from Sätze
    private static String removeCurlyBracketsFromSchuetzeSatz(String schuetzeSatz) {
        if (schuetzeSatz != null && schuetzeSatz.length() > 1) {
            return schuetzeSatz.substring(1, schuetzeSatz.length() - 1);
        } else {
            return "";
        }
    }

    /**
     * Constructor
     */
    private SchuetzenstatistikDTOMapper() {
        // empty private constructor
    }
}
