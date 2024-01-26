package de.bogenliga.application.business.altsystem.liga.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.altsystem.liga.mapper.AltsystemLigaMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * Component to handle the import of a "Liga" entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemLiga implements AltsystemEntity<AltsystemLigaDO> {



    @Autowired
    public AltsystemLiga(final AltsystemLigaMapper altsystemLigaMapper, final LigaComponent ligaComponent,
                         AltsystemUebersetzung altsystemUebersetzung) {

    }

    @Override
    public void create(AltsystemLigaDO altsystemDataObject, long currentUserId){

    }

    @Override
    public void update(AltsystemLigaDO altsystemDataObject, long currentUserId){

    }

}
