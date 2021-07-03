package de.bogenliga.application.services.v1.schuetzenstatistik.mapper;

import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.schuetzenstatistik.model.SchuetzenstatistikDTO;

import java.util.function.Function;

/**
 * I map the {@link SchuetzenstatistikDO} and {@link SchuetzenstatistikDTO} objects
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
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
        final int matchpkt = schuetzenstatistikDO.getmatchpkt();
        final int matchpkt_gegen = schuetzenstatistikDO.getmatchpktGegen();
        final int satzpkt = schuetzenstatistikDO.getsatzpkt();
        final int satzpkt_gegen = schuetzenstatistikDO.getsatzpktGegen();
        final int satzpkt_differenz = schuetzenstatistikDO.getsatzpktDifferenz();
        final int sortierung = schuetzenstatistikDO.getsortierung();
        final int tabellenplatz = schuetzenstatistikDO.gettabellenplatz();



        return new SchuetzenstatistikDTO(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag, mannschaftId,
                mannschaftNummer, vereinId, vereinName, matchpkt, matchpkt_gegen, satzpkt,
                satzpkt_gegen, satzpkt_differenz, sortierung, tabellenplatz);
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
        schuetzenstatistikDO.setmatchpkt(dto.getMatchpkt());
        schuetzenstatistikDO.setmatchpktGegen(dto.getMatchpkt_gegen());
        schuetzenstatistikDO.setsatzpkt(dto.getSatzpkt());
        schuetzenstatistikDO.setsatzpktGegen(dto.getSatzpkt_gegen());
        schuetzenstatistikDO.setsatzpktDifferenz(dto.getSatzpkt_differenz());
        schuetzenstatistikDO.setsortierung(dto.getSortierung());
        schuetzenstatistikDO.settabellenplatz(dto.getTabellenplatz());


        return schuetzenstatistikDO;
    };


    /**
     * Constructor
     */
    private Schuetzenstatistik() {
        // empty private constructor
    }
}
