package de.bogenliga.application.business.altsystem.schuetze.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.business.altsystem.schuetze.mapper.AltsystemSchuetzeMapper;


/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemSchuetze implements AltsystemEntity<AltsystemSchuetzeDO> {


    @Autowired
    public AltsystemSchuetze(final AltsystemSchuetzeMapper altsystemSchuetzeMapper, final DsbMitgliedComponent dsbMitgliedComponent,
                             AltsystemUebersetzung altsystemUebersetzung) {

    }


    @Override
    public void create(AltsystemSchuetzeDO altsystemSchuetzeDO, long currentUserId) {
    }

    @Override
    public void update(AltsystemSchuetzeDO altsystemSchuetzeDO, long currentUserId){
        // yet to be implemented
    }
}
