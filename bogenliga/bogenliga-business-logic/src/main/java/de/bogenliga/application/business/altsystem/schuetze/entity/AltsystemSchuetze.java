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

    private final AltsystemSchuetzeMapper altsystemSchuetzeMapper;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;


    @Autowired
    public AltsystemSchuetze(final AltsystemSchuetzeMapper altsystemSchuetzeMapper, final DsbMitgliedComponent dsbMitgliedComponent,
                             AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemSchuetzeMapper = altsystemSchuetzeMapper;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }


    @Override
    public void create(AltsystemSchuetzeDO altsystemSchuetzeDO, long currentUserId) {
        // Map data to new object, add default fields
        DsbMitgliedDO dsbMitgliedDO = new DsbMitgliedDO();
        /*
        try {
            dsbMitgliedDO = altsystemSchuetzeMapper.toDO(dsbMitgliedDO, altsystemSchuetzeDO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
        //dsbMitgliedDO = altsystemSchuetzeMapper.addDefaultFields(dsbMitgliedDO);
    }

    @Override
    public void update(AltsystemSchuetzeDO altsystemSchuetzeDO, long currentUserId){
        // yet to be implemented
    }
}
