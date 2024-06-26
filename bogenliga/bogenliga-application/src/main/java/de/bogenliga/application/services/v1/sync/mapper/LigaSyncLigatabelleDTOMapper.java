package de.bogenliga.application.services.v1.sync.mapper;

import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.sync.model.LigaSyncLigatabelleDTO;
import java.util.function.Function;

/**
 * I map the {@link LigatabelleDO} to {@link LigaSyncLigatabelleDTO} objects
 * // only One-Way as Ligatabelle is read-only
 */
public class LigaSyncLigatabelleDTOMapper implements DataTransferObjectMapper {
    /**
     * I map the {@link LigatabelleDO} object to the {@link LigaSyncLigatabelleDTO} object
     */
    public static final Function<LigatabelleDO, LigaSyncLigatabelleDTO> toDTO = LigaSyncLigatabelleDTOMapper::apply;


    /**
     * Constructor
     */
    private LigaSyncLigatabelleDTOMapper() {
        // empty private constructor
    }

    private static LigaSyncLigatabelleDTO apply(LigatabelleDO ligatabelleDO) {

        final Long veranstaltungId = ligatabelleDO.getveranstaltungId();
        final String veranstaltungName = ligatabelleDO.getveranstaltungName();
        final Long wettkampfId = ligatabelleDO.getwettkampfId();
        final Integer wettkampfTag = ligatabelleDO.getwettkampfTag();
        final Long mannschaftId = ligatabelleDO.getmannschaftId();
        final String mannschaftName = ligatabelleDO.getvereinName()+' '+ ligatabelleDO.getmannschaftNummer();
        final Integer matchpkt = ligatabelleDO.getmatchpkt();
        final Integer matchpktGegen = ligatabelleDO.getMatchpktGegen();
        final Integer satzpkt = ligatabelleDO.getsatzpkt();
        final Integer satzpktGegen = ligatabelleDO.getSatzpktGegen();
        final Integer satzpktDifferenz = ligatabelleDO.getSatzpktDifferenz();
        final Integer sortierung = ligatabelleDO.getsortierung();
        final Integer tabellenplatz = ligatabelleDO.gettabellenplatz();
        final Integer matchCount = ligatabelleDO.getMatchCount();


        return new LigaSyncLigatabelleDTO(veranstaltungId, veranstaltungName,
                wettkampfId, wettkampfTag, mannschaftId, mannschaftName,
                matchpkt, matchpktGegen, satzpkt,
                satzpktGegen, satzpktDifferenz, sortierung, tabellenplatz, matchCount);
    }
}
