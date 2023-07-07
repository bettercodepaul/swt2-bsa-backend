package de.bogenliga.application.business.ligamatch.impl.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.match.api.types.LigamatchDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

/**
 * @author Adrian Kempf, HSRT MKI SS22 - SWT2
 */
public class LigamatchMapper implements ValueObjectMapper {

    /**
     * Empty hidden constructor to prevent instantiation
     */
    private LigamatchMapper() {}

    public static final Function<LigamatchBE, LigamatchDO> toLigamatchDO = ligamatchBE -> new LigamatchDO(
            ligamatchBE.getMatchId(),
            ligamatchBE.getWettkampfId(),
            ligamatchBE.getMatchNr(),
            ligamatchBE.getMatchScheibennummer(),
            ligamatchBE.getMatchpunkte(),
            ligamatchBE.getSatzpunkte(),
            ligamatchBE.getMannschaftId(),
            ligamatchBE.getMannschaftName(),
            ligamatchBE.getMannschaftNameGegner(),
            ligamatchBE.getScheibennummerGegner(),
            ligamatchBE.getMatchIdGegner(),
            ligamatchBE.getNaechsteMatchId(),
            ligamatchBE.getNaechsteNaechsteMatchId(),
            ligamatchBE.getStrafpunkteSatz1(),
            ligamatchBE.getStrafpunkteSatz2(),
            ligamatchBE.getStrafpunkteSatz3(),
            ligamatchBE.getStrafpunkteSatz4(),
            ligamatchBE.getStrafpunkteSatz5()
    );
}
