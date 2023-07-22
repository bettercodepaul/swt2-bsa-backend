package de.bogenliga.application.business.liga.impl.mapper;

import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.function.Function;

/**
 * I convert the liga DataObjects and BusinessEntities.
 *
 */
public class LigaMapper implements ValueObjectMapper {
    /**
     * Converts a {@link LigaBE} to a {@link LigaDO}
     *
     */
    public static final LigaDO toLigaDO(LigaBE ligaBE, LigaBE uebergeordnetLiga, RegionenDO regionenDO, UserDO userDO, DisziplinDO disziplinDO){

        OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(ligaBE.getCreatedAtUtc());
        OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(ligaBE.getLastModifiedAtUtc());

        String disziplinName = "";


        if (disziplinDO.getDisziplinId() != 0) {
            disziplinName = " " + disziplinDO.getDisziplinName();
        }

        LigaDO ligaDO = new LigaDO(
                ligaBE.getLigaId(),
                ligaBE.getLigaName() + disziplinName,
                regionenDO.getId(),
                regionenDO.getRegionName(),
                ligaBE.getLigaUebergeordnetId(),
                uebergeordnetLiga.getLigaName(),
                userDO.getId(),
                userDO.getEmail(),
                disziplinDO.getDisziplinId(),
                ligaBE.getLigaDetail(),
                ligaBE.getLigaFileBase64(),
                ligaBE.getLigaFileName(),
                ligaBE.getLigaFileType()


        );
        ligaDO.setCreatedAtUtc(createdAtUtc);
        ligaDO.setLastModifiedAtUtc(lastModifiedAtUtc);
        return ligaDO;
    }

    /**
     * Converts a {@link LigaDO} to a {@link LigaBE}
     *
     */
    public static final Function <LigaDO,LigaBE> toLigaBE = ligaDO -> {

        Timestamp createdAtUtcTimestamp = DateProvider.convertOffsetDateTime(ligaDO.getCreatedAtUtc());
        Timestamp lastModifiedAtUtcTimestamp = DateProvider.convertOffsetDateTime(ligaDO.getLastModifiedAtUtc());



        LigaBE ligaBE = new LigaBE();
        ligaBE.setLigaId(ligaDO.getId());
        ligaBE.setLigaDisziplinId(ligaDO.getDisziplinId());
        ligaBE.setLigaName(ligaDO.getName());
        ligaBE.setLigaRegionId(ligaDO.getRegionId());
        ligaBE.setLigaUebergeordnetId(ligaDO.getLigaUebergeordnetId());
        ligaBE.setLigaVerantwortlichId(ligaDO.getLigaVerantwortlichId());
        ligaBE.setCreatedAtUtc(createdAtUtcTimestamp);
        ligaBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);
        ligaBE.setLigaDetail(ligaDO.getLigaDetail());
        ligaBE.setLigaFileBase64(ligaDO.getLigaFileBase64());
        ligaBE.setLigaFileName(ligaDO.getLigaFileName());
        ligaBE.setLigaFileType(ligaDO.getLigaFileType());

        return ligaBE;
    };

    /**
     * Private Constructor
     */
    private LigaMapper(){}
}