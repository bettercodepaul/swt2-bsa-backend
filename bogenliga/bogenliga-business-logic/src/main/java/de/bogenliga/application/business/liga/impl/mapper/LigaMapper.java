package de.bogenliga.application.business.liga.impl.mapper;

import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

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
        public static final Function<LigaBE, LigaDO> toLigaDO = be-> {
            final long id = be.getLigaId();
            final String ligaName = be.getLigaName();
            final long regionID = be.getLigaRegionId();
            final String regionName = be.getLigaRegionName();
            final long ligaUebergeordnetId = be.getLigaUebergeordnetId();
            final String ligaUebergeordnetName = be.getLigaUebergeordnetName();
            final long ligaVerantwortlichId = be.getLigaVerantwortlichId();
            final String ligaVerantwortlichMail = be.getLigaVerantwortlichMail();
            final long userId = be.getLigaUserId();

            //technical parameter
          return null;
        };
}
