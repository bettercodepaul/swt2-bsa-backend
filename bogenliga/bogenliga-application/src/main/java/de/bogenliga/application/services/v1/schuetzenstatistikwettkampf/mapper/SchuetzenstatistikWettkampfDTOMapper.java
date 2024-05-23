package de.bogenliga.application.services.v1.schuetzenstatistikwettkampf.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.types.SchuetzenstatistikWettkampftageDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.schuetzenstatistikwettkampf.model.SchuetzenstatistikWettkampfDTO;

/**
 * @author Anna Baur
 *
 *  Mapping of {@link SchuetzenstatistikWettkampftageDO} and {@link SchuetzenstatistikWettkampfDTO} objects
 *
 */
public class SchuetzenstatistikWettkampfDTOMapper implements DataTransferObjectMapper {

    public static final Function<SchuetzenstatistikWettkampftageDO, SchuetzenstatistikWettkampfDTO> toDTO = schuetzenstatistikWettkampfDO -> {

        final Long veranstaltungId = schuetzenstatistikWettkampfDO.getveranstaltungId();
        final Long wettkampfId = schuetzenstatistikWettkampfDO.getwettkampfId();
        final int wettkampfTag = schuetzenstatistikWettkampfDO.getwettkampfTag();
        final Long vereinId = schuetzenstatistikWettkampfDO.getvereinId();
        final Long dsbMitgliedId = schuetzenstatistikWettkampfDO.getDsbMitgliedId();
        final String dsbMitgliedName = schuetzenstatistikWettkampfDO.getDsbMitgliedName();
        final int rueckenNummer = schuetzenstatistikWettkampfDO.getRueckenNummer();
        final float wettkampftag1 = schuetzenstatistikWettkampfDO.getWettkampftag1();
        final float wettkampftag2 = schuetzenstatistikWettkampfDO.getWettkampftag2();
        final float wettkampftag3 = schuetzenstatistikWettkampfDO.getWettkampftag3();
        final float wettkampftag4 = schuetzenstatistikWettkampfDO.getWettkampftag4();
        final float wettkampftageSchnitt = schuetzenstatistikWettkampfDO.getWettkampftageSchnitt();




        return new SchuetzenstatistikWettkampfDTO(veranstaltungId, wettkampfId, wettkampfTag, vereinId,
                dsbMitgliedId, dsbMitgliedName, rueckenNummer, wettkampftag1,wettkampftag2,wettkampftag3,wettkampftag4
                ,wettkampftageSchnitt);
    };
    /**
     * I map the {@link SchuetzenstatistikWettkampfDTO} object to the {@link SchuetzenstatistikWettkampftageDO} object
     */
    public static final Function<SchuetzenstatistikWettkampfDTO, SchuetzenstatistikWettkampftageDO> toDO = dto -> {

        SchuetzenstatistikWettkampftageDO schuetzenstatistikWettkampfDO = new SchuetzenstatistikWettkampftageDO();

        schuetzenstatistikWettkampfDO.setveranstaltungId(dto.getVeranstaltungId());
        schuetzenstatistikWettkampfDO.setwettkampfId(dto.getWettkampfId());
        schuetzenstatistikWettkampfDO.setwettkampfTag(dto.getWettkampfTag());
        schuetzenstatistikWettkampfDO.setvereinId(dto.getVereinId());
        schuetzenstatistikWettkampfDO.setDsbMitgliedId(dto.getDsbMitgliedId());
        schuetzenstatistikWettkampfDO.setDsbMitgliedName(dto.getDsbMitgliedName());
        schuetzenstatistikWettkampfDO.setRueckenNummer(dto.getRueckenNummer());
        schuetzenstatistikWettkampfDO.setWettkampftag1(dto.getWettkampftag1());
        schuetzenstatistikWettkampfDO.setWettkampftag2(dto.getWettkampftag2());
        schuetzenstatistikWettkampfDO.setWettkampftag3(dto.getWettkampftag3());
        schuetzenstatistikWettkampfDO.setWettkampftag4(dto.getWettkampftag4());
        schuetzenstatistikWettkampfDO.setWettkampftageSchnitt(dto.getWettkampftageSchnitt());

        return schuetzenstatistikWettkampfDO;
    };

    /**
     * Constructor
     */
    private SchuetzenstatistikWettkampfDTOMapper() {
        // empty private constructor
    }
}
