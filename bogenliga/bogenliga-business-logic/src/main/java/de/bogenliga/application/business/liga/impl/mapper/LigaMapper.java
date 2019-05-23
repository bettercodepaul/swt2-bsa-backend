package de.bogenliga.application.business.liga.impl.mapper;

import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import de.bogenliga.application.business.user.impl.entity.UserBE;
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
        public static final LigaDO toLigaDO(LigaBE ligaBE, LigaBE uebergeordnetLiga, RegionenBE regionenBE, UserBE userBE){

            OffsetDateTime createdAtUtc = DateProvider.convertTimestamp(ligaBE.getCreatedAtUtc());
            OffsetDateTime lastModifiedAtUtc = DateProvider.convertTimestamp(ligaBE.getLastModifiedAtUtc());

           LigaDO ligaDO = new LigaDO(
                   ligaBE.getLigaId(),
                   ligaBE.getLigaName(),
                   regionenBE.getRegionId(),
                   regionenBE.getRegionName(),
                   ligaBE.getLigaUebergeordnetId(),
                   uebergeordnetLiga.getLigaName(),
                   userBE.getUserId(),
                   userBE.getUserEmail()
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
        ligaBE.setLigaName(ligaDO.getName());
        ligaBE.setLigaRegionId(ligaDO.getRegionId());
        ligaBE.setLigaUebergeordnetId(ligaDO.getLigaUebergeordnetId());
        ligaBE.setLigaVerantwortlichId(ligaDO.getLigaVerantwortlichId());
        ligaBE.setCreatedAtUtc(createdAtUtcTimestamp);
        ligaBE.setLastModifiedAtUtc(lastModifiedAtUtcTimestamp);

        return ligaBE;
    };

    /**
     * Private Constructor
     */
    private LigaMapper(){}
}
