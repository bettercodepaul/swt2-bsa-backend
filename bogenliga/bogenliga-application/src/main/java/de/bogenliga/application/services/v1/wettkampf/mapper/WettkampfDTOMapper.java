package de.bogenliga.application.services.v1.wettkampf.mapper;
import java.sql.Date;
import java.util.function.Function;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;


/**
 * I map the {@link WettkampfDO} and {@link WettkampfDTO} objects
 *
 * @author Marvin Holm, Daniel Schott
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public class WettkampfDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link WettkampfDO} to the {@link WettkampfDTO} object
     */

    public static final Function<WettkampfDO, WettkampfDTO> toDTO = wettkampfDO -> {
        final Long wettkampfId = wettkampfDO.getId();
        final Long wettkampfVeranstaltungsId = wettkampfDO.getWettkampfVeranstaltungsId();
        final Date wettkampfDatum = wettkampfDO.getWettkampfDatum();
        final String wettkampfStrasse = wettkampfDO.getWettkampfStrasse();
        final String wettkampfPlz = wettkampfDO.getWettkampfPlz();
        final String wettkampfOrtsname = wettkampfDO.getWettkampfOrtsname();
        final String wettkampfOrtsinfo = wettkampfDO.getWettkampfOrtsinfo();
        final String wettkampfBeginn = wettkampfDO.getWettkampfBeginn();
        final Long wettkampfTag = wettkampfDO.getWettkampfTag();
        final Long wettkampfDisziplinId = wettkampfDO.getWettkampfDisziplinId();
        final Long wettkampfTypId = wettkampfDO.getWettkampfTypId();
        final Long version = wettkampfDO.getVersion();
        final Long wettkampfAusrichter = wettkampfDO.getWettkampfAusrichter();

        return new WettkampfDTO(wettkampfId, wettkampfVeranstaltungsId, wettkampfDatum, wettkampfStrasse,
                wettkampfPlz, wettkampfOrtsname, wettkampfOrtsinfo, wettkampfBeginn, wettkampfTag,
                wettkampfDisziplinId, wettkampfTypId, version, wettkampfAusrichter);
    };


    /**
     * maps the {@link WettkampfDTO} to the {@link WettkampfDO} object
     */
    public static final Function<WettkampfDTO, WettkampfDO> toDO = wettkampfDTO -> {

        final Long wettkampfId = wettkampfDTO.getId();
        final Long wettkampfVeranstaltungsId = wettkampfDTO.getwettkampfVeranstaltungsId();
        final Date wettkampfDatum = wettkampfDTO.getDatum();
        final String wettkampfStrasse = wettkampfDTO.getWettkampfStrasse();
        final String wettkampfPlz = wettkampfDTO.getWettkampfPlz();
        final String wettkampfOrtsname = wettkampfDTO.getWettkampfOrtsname();
        final String wettkampfOrtsinfo = wettkampfDTO.getWettkampfOrtsinfo();
        final String wettkampfBeginn = wettkampfDTO.getWettkampfBeginn();
        final Long wettkampfTag = wettkampfDTO.getWettkampfTag();
        final Long wettkampfDisziplinId = wettkampfDTO.getWettkampfDisziplinId();
        final Long wettkampfTypId = wettkampfDTO.getWettkampfTypId();
        final Long version = wettkampfDTO.getVersion();
        final Long wettkampfAusrichter = wettkampfDTO.getWettkampfAusrichter();

        return new WettkampfDO(wettkampfId, wettkampfVeranstaltungsId, wettkampfDatum, wettkampfStrasse,
                wettkampfPlz, wettkampfOrtsname, wettkampfOrtsinfo, wettkampfBeginn, wettkampfTag,
                wettkampfDisziplinId, wettkampfTypId, version, wettkampfAusrichter);
    };


    /**
     * Constructor
     */
    private WettkampfDTOMapper(){
        //empty
    }
}
