package de.bogenliga.application.services.v1.liga.mapper;

import java.util.function.Function;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.liga.model.LigaDTO;

/**
 * I map the {@link LigaDO} and {@link LigaDTO} objects
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class LigaDTOMapper implements DataTransferObjectMapper {
    /**
     * I map the {@link LigaDO} object to the {@link LigaDTO} object
     */
    public static final Function<LigaDO, LigaDTO> toDTO = ligaDO -> {

        final Long ligaId = ligaDO.getId();
        final String ligaName = ligaDO.getName();
        final Long regionId = ligaDO.getRegionId();
        final String regionName = ligaDO.getRegionName();
        final Long liga_uebergeordnet_id = ligaDO.getLigaUebergeordnetId();
        final String liga_uebergeordnet_name = ligaDO.getLigaUebergeordnetName();
        final Long liga_verantwortlich_id = ligaDO.getLigaVerantwortlichId();
        final String liga_verantwortlich_mail = ligaDO.getLigaVerantwortlichMail();
        final Long liga_disziplin_id = ligaDO.getDisziplinId();
        final String liga_detail = ligaDO.getLigaDetail();
        final String liga_file_base64 = ligaDO.getLigaDoFileBase64();
        final String liga_file_name = ligaDO.getLigaDoFileName();
        final String liga_file_type = ligaDO.getLigaDoFileType();



        return new LigaDTO(ligaId, ligaName, regionId, regionName, liga_uebergeordnet_id, liga_uebergeordnet_name,
                liga_verantwortlich_id, liga_verantwortlich_mail,liga_disziplin_id, liga_detail,
                liga_file_base64, liga_file_name, liga_file_type);
    };
    /**
     * I map the {@link LigaDTO} object to the {@link LigaDO} object
     */
    public static final Function<LigaDTO, LigaDO> toDO = dto -> {

        LigaDO ligaDO = new LigaDO();
        ligaDO.setId(dto.getId());
        ligaDO.setName(dto.getName());
        ligaDO.setRegionId(dto.getRegionId());
        ligaDO.setLigaUebergeordnetId(dto.getLigaUebergeordnetId());
        ligaDO.setLigaVerantwortlichId(dto.getLigaVerantwortlichId());
        ligaDO.setDisziplinId(dto.getDisziplinId());
        ligaDO.setLigaDetail(dto.getLigaDetail());
        ligaDO.setLigaDoFileBase64(dto.getLigaDetailFileBase64());
        ligaDO.setLigaDoFileName(dto.getLigaDetailFileName());
        ligaDO.setLigaDoFileType(dto.getLigaDetailFileType());

        return ligaDO;
    };


    /**
     * Constructor
     */
    private LigaDTOMapper() {
        // empty private constructor
    }
}
