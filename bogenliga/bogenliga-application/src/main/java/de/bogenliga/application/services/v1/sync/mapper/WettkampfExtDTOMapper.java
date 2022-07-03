package de.bogenliga.application.services.v1.sync.mapper;

import java.sql.Date;
import java.util.function.Function;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.sync.model.WettkampfExtDTO;

/**
 *  I map the {@link WettkampfDO} to the {@link WettkampfExtDTO} object
 *  one way because current implementation is read only
 * @author Jonas Sigloch, SWT SoSe 2022
 */
public class WettkampfExtDTOMapper implements DataTransferObjectMapper {

    private WettkampfExtDTOMapper() {
        // private empty constructor
    }

    public static final Function<WettkampfDO, WettkampfExtDTO> toDTO = WettkampfExtDTOMapper::apply;

    private static WettkampfExtDTO apply(WettkampfDO wettkampfDO) {
        final Long id = wettkampfDO.getId();
        final Long veranstaltungsId = wettkampfDO.getWettkampfVeranstaltungsId();
        final Date datum = wettkampfDO.getWettkampfDatum();
        final String strasse = wettkampfDO.getWettkampfStrasse();
        final String plz = wettkampfDO.getWettkampfPlz();
        final String ortsname = wettkampfDO.getWettkampfOrtsname();
        final String ortsinfo = wettkampfDO.getWettkampfOrtsinfo();
        final String beginn = wettkampfDO.getWettkampfBeginn();
        final Long tag = wettkampfDO.getWettkampfTag();
        final Long disziplinId = wettkampfDO.getWettkampfDisziplinId();
        final Long wettkampfTypId = wettkampfDO.getWettkampfTypId();
        final Long version = wettkampfDO.getVersion();
        final Long ausrichter = wettkampfDO.getWettkampfAusrichter();
        final String offlineToken = wettkampfDO.getOfflineToken();

        return new WettkampfExtDTO(id, veranstaltungsId, datum, strasse, plz, ortsname, ortsinfo, beginn, tag,
                disziplinId, wettkampfTypId, version, ausrichter, offlineToken);
    }
}
