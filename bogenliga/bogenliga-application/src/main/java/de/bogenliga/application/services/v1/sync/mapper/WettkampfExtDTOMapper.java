package de.bogenliga.application.services.v1.sync.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.sync.model.WettkampfExtDTO;

/**
 * I map the {@link WettkampfExtDTO} to the {@link WettkampfDO} object
 *
 * @author Jonas Sigloch, SWT SoSe 2022
 */
public class WettkampfExtDTOMapper implements DataTransferObjectMapper {

    public static final Function<WettkampfDO, WettkampfExtDTO> toDTO = WettkampfExtDTOMapper::apply;

    private static WettkampfExtDTO apply(WettkampfDO wettkampfDO) {

        return new WettkampfExtDTO();
    }
}
