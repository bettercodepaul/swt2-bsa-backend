package de.bogenliga.application.business.ligamatch.impl.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.ligamatch.api.types.LigamatchDO;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;

/**
 * TODO [AL] class documentation
 *
 * @author Paul Zeller, Paul_Johann.Zeller@Student.Reutlingen-University.de
 */
public class LigamatchMapper {
    private LigamatchMapper(){};

    public static final Function<LigamatchBE, LigamatchDO> toMatchDO = LigamatchBE ->{
        return new LigamatchDO(
                LigamatchBE.getWettkampfId(),
                LigamatchBE.getMatchId(),
                LigamatchBE.getMatchNr(),
                LigamatchBE.getScheibennummer(),
                LigamatchBE.getMannschaftId(),
                LigamatchBE.getMannschaftName(),
                LigamatchBE.getMannschaftNameGegner(),
                LigamatchBE.getScheibennummerGegner(),
                LigamatchBE.getIdGegner(),
                LigamatchBE.getNaechsteMatchId(),
                LigamatchBE.getNaechsteNaechsteMatchNrMatchId(),
                LigamatchBE.getStrafpunkteSatz1(),
                LigamatchBE.getStrafpunkteSatz2(),
                LigamatchBE.getStrafpunkteSatz3(),
                LigamatchBE.getStrafpunkteSatz4(),
                LigamatchBE.getStrafpunkteSatz5()

        );
    };
}
