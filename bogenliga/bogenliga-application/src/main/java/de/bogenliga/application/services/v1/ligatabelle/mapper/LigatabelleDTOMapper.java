package de.bogenliga.application.services.v1.ligatabelle.mapper;

import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.ligatabelle.model.LigatabelleDTO;

import java.util.function.Function;

/**
 * I map the {@link LigatabelleDO} and {@link LigatabelleDTO} objects
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class LigatabelleDTOMapper implements DataTransferObjectMapper {
    /**
     * I map the {@link LigatabelleDO} object to the {@link LigatabelleDTO} object
     */
    public static final Function<LigatabelleDO, LigatabelleDTO> toDTO = ligatabelleDO -> {

        final Long veranstaltungId = ligatabelleDO.getveranstaltungId();
        final String veranstaltungName = ligatabelleDO.getveranstaltungName();
        final Long wettkampfId = ligatabelleDO.getwettkampfId();
        final int wettkampfTag = ligatabelleDO.getwettkampfTag();
        final Long mannschaftId = ligatabelleDO.getmannschaftId();
        final int mannschaftNummer = ligatabelleDO.getmannschaftNummer();
        final Long vereinId = ligatabelleDO.getvereinId();
        final String vereinName = ligatabelleDO.getvereinName();
        final int matchpkt = ligatabelleDO.getmatchpkt();
        final int matchpkt_gegen = ligatabelleDO.getmatchpktGegen();
        final int satzpkt = ligatabelleDO.getsatzpkt();
        final int satzpkt_gegen = ligatabelleDO.getsatzpktGegen();
        final int satzpkt_differenz = ligatabelleDO.getsatzpktDifferenz();
        final int sortierung = ligatabelleDO.getsortierung();
        final int tabellenplatz = ligatabelleDO.gettabellenplatz();



        return new LigatabelleDTO(veranstaltungId, veranstaltungName, wettkampfId, wettkampfTag, mannschaftId,
                mannschaftNummer, vereinId, vereinName, matchpkt, matchpkt_gegen, satzpkt,
                satzpkt_gegen, satzpkt_differenz, sortierung, tabellenplatz);
    };
    /**
     * I map the {@link LigatabelleDTO} object to the {@link LigatabelleDO} object
     */
    public static final Function<LigatabelleDTO, LigatabelleDO> toDO = dto -> {

        LigatabelleDO ligatabelleDO = new LigatabelleDO();

        ligatabelleDO.setveranstaltungId(dto.getVeranstaltungId());
        ligatabelleDO.setveranstaltungName(dto.getVeranstaltungName());
        ligatabelleDO.setwettkampfId(dto.getWettkampfId());
        ligatabelleDO.setwettkampfTag(dto.getWettkampfTag());
        ligatabelleDO.setmannschaftId(dto.getMannschaftId());
        ligatabelleDO.setmannschaftNummer(dto.getMannschaftNummer());
        ligatabelleDO.setvereinId(dto.getVereinId());
        ligatabelleDO.setvereinName(dto.getVereinName());
        ligatabelleDO.setmatchpkt(dto.getMatchpkt());
        ligatabelleDO.setmatchpktGegen(dto.getMatchpktGegen());
        ligatabelleDO.setsatzpkt(dto.getSatzpkt());
        ligatabelleDO.setsatzpktGegen(dto.getSatzpktGegen());
        ligatabelleDO.setsatzpktDifferenz(dto.getSatzpktDifferenz());
        ligatabelleDO.setsortierung(dto.getSortierung());
        ligatabelleDO.settabellenplatz(dto.getTabellenplatz());


        return ligatabelleDO;
    };


    /**
     * Constructor
     */
    private LigatabelleDTOMapper() {
        // empty private constructor
    }
}
