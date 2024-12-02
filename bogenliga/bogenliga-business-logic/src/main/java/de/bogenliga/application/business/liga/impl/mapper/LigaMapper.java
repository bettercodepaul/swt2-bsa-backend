package de.bogenliga.application.business.liga.impl.mapper;

import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.liga.impl.entity.LigaExtendedBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.time.DateProvider;

import java.sql.Timestamp;
import java.util.function.Function;

/**
 * I convert the liga DataObjects and BusinessEntities.
 *
 */
public class LigaMapper implements ValueObjectMapper {

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
        ligaBE.setLigaFileBase64(ligaDO.getLigaDoFileBase64());
        ligaBE.setLigaFileName(ligaDO.getLigaDoFileName());
        ligaBE.setLigaFileType(ligaDO.getLigaDoFileType());

        return ligaBE;
    };

    /**
     * Maps a {@link LigaExtendedBE} to a {@link LigaDO}.
     *
     * @param be the business entity
     * @return the domain object
     */
    public static LigaDO mapToLigaDO(LigaExtendedBE be) {
        final Long ligaId = be.getLigaId();
        final String ligaName = be.getLigaName();
        final Long ligaRegionId = be.getLigaRegionId();
        final String regionName = be.getRegionName();
        final Long ligaUebergeordnetId = be.getLigaUebergeordnetId();
        final String uebergeordneteLigaName = be.getUebergeordneteLigaName();
        final Long ligaVerantwortlichId = be.getLigaVerantwortlichId();
        final String verantwortlicherName = be.getVerantwortlicherName();
        final Long disziplinId = be.getLigaDisziplinId();
        final String ligaDetail = be.getLigaDetail();
        final String ligaFileBase64 = be.getLigaFileBase64();
        final String ligaFileName = be.getLigaFileName();
        final String ligaFileType = be.getLigaFileType();

        return new LigaDO(
                ligaId,
                ligaName,
                ligaRegionId,
                regionName,
                ligaUebergeordnetId,
                uebergeordneteLigaName,
                ligaVerantwortlichId,
                verantwortlicherName,
                disziplinId,
                ligaDetail,
                ligaFileBase64,
                ligaFileName,
                ligaFileType
        );

    }

    public static LigaDO toLigaDO(LigaBE ligaBE, LigaExtendedBE additionalData) {
        if (ligaBE == null) {
            return null;
        }

        // Basisdaten aus LigaBE
        Long ligaId = ligaBE.getLigaId();
        String ligaName = ligaBE.getLigaName();
        Long ligaRegionId = ligaBE.getLigaRegionId();
        Long ligaUebergeordnetId = ligaBE.getLigaUebergeordnetId();
        Long ligaVerantwortlichId = ligaBE.getLigaVerantwortlichId();
        Long disziplinId = ligaBE.getLigaDisziplinId();
        String ligaDetail = ligaBE.getLigaDetail();
        String ligaFileBase64 = ligaBE.getLigaFileBase64();
        String ligaFileName = ligaBE.getLigaFileName();
        String ligaFileType = ligaBE.getLigaFileType();

        // Zusätzliche Daten aus LigaExtendedBE
        String regionName = additionalData != null ? additionalData.getRegionName() : null;
        String uebergeordneteLigaName = additionalData != null ? additionalData.getUebergeordneteLigaName() : null;
        String verantwortlicherName = additionalData != null ? additionalData.getVerantwortlicherName() : null;

        // Zusammenführen und Rückgabe des LigaDO
        return new LigaDO(
                ligaId,
                ligaName,
                ligaRegionId,
                regionName,
                ligaUebergeordnetId,
                uebergeordneteLigaName,
                ligaVerantwortlichId,
                verantwortlicherName,
                disziplinId,
                ligaDetail,
                ligaFileBase64,
                ligaFileName,
                ligaFileType
        );
    }


    /**
     * Private Constructor
     */
    private LigaMapper(){}
}