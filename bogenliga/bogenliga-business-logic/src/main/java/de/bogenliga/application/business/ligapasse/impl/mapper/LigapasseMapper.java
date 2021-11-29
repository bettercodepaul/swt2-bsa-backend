package de.bogenliga.application.business.ligapasse.impl.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.ligapasse.api.types.LigapasseDO;
import de.bogenliga.application.business.ligapasse.impl.entity.LigapasseBE;

/**
 * TODO [AL] class documentation
 *
 * @author Paul Zeller, Paul_Johann.Zeller@Student.Reutlingen-University.de
 */
public class LigapasseMapper {

    private LigapasseMapper(){};

    public static final Function<LigapasseBE,LigapasseDO> toMatchDO = LigapasseBE ->{
        return new LigapasseDO(
                LigapasseBE.getWettkampfId(),
                LigapasseBE.getMatchId(),
                LigapasseBE.getPasseId(),
                LigapasseBE.getPasseLfdnr(),
                LigapasseBE.getPasseMannschaftId(),
                LigapasseBE.getDsbMitgliedId(),
                LigapasseBE.getDsbMitgliedName(),
                LigapasseBE.getMannschaftsmitgliedRueckennummer(),
                LigapasseBE.getPasseRingzahlPfeil1(),
                LigapasseBE.getPasseRingzahlPfeil2()
        );
    };
}
