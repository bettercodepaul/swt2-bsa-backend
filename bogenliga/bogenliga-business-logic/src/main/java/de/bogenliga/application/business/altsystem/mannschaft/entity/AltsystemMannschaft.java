package de.bogenliga.application.business.altsystem.mannschaft.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemMannschaftMapper;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemVeranstaltungMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.verein.entity.AltsystemVerein;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemMannschaft implements AltsystemEntity<AltsystemMannschaftDO> {


    private final AltsystemMannschaftMapper altsystemMannschaftMapper;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;
    private final AltsystemVeranstaltungMapper altsystemVeranstaltungMapper;
    private final AltsystemVerein altsystemVerein;


    @Autowired
    public AltsystemMannschaft(final AltsystemMannschaftMapper altsystemMannschaftMapper, final DsbMannschaftComponent mannschaftComponent,
                               AltsystemUebersetzung altsystemUebersetzung,
                               AltsystemVeranstaltungMapper altsystemVeranstaltungMapper,
                               AltsystemVerein altsystemVerein){
        this.altsystemMannschaftMapper = altsystemMannschaftMapper;
        this.dsbMannschaftComponent = mannschaftComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
        this.altsystemVeranstaltungMapper = altsystemVeranstaltungMapper;
        this.altsystemVerein = altsystemVerein;
    }

    @Override
    public void create(AltsystemMannschaftDO altsystemDataObject, long currentUserId) {
        DsbMannschaftDO dsbMannschaftDO = new DsbMannschaftDO();
        altsystemVerein.create(altsystemDataObject, currentUserId);

        VeranstaltungDO veranstaltungDO = altsystemVeranstaltungMapper.getOrCreateVeranstaltung(altsystemDataObject, currentUserId);

        dsbMannschaftDO = altsystemMannschaftMapper.toDO(altsystemDataObject, dsbMannschaftDO);
        dsbMannschaftDO = altsystemMannschaftMapper.addDefaultFields(dsbMannschaftDO, currentUserId, altsystemDataObject, veranstaltungDO);

        dsbMannschaftComponent.create(dsbMannschaftDO, currentUserId);

        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft,
                (int) altsystemDataObject.getId(), dsbMannschaftDO.getId().longValue(), "");
    }


    @Override
    public void update(AltsystemMannschaftDO altsystemDataObject, long currentUserId) {

        altsystemVerein.update(altsystemDataObject, currentUserId);
        AltsystemUebersetzungDO mannschaftUebersetzung = altsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, altsystemDataObject.getId());


        if(mannschaftUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", altsystemDataObject.getId()));
        }

        DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(mannschaftUebersetzung.getBogenligaId());

        altsystemMannschaftMapper.toDO( altsystemDataObject, dsbMannschaftDO);

        dsbMannschaftComponent.update(dsbMannschaftDO, currentUserId);

    }
}
