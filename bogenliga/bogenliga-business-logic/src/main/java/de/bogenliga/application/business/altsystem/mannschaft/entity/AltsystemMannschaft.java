package de.bogenliga.application.business.altsystem.mannschaft.entity;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemMannschaftMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
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


    @Autowired
    public AltsystemMannschaft(final AltsystemMannschaftMapper altsystemMannschaftMapper, final DsbMannschaftComponent mannschaftComponent,
                               AltsystemUebersetzung altsystemUebersetzung){
        this.altsystemMannschaftMapper = altsystemMannschaftMapper;
        this.dsbMannschaftComponent = mannschaftComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }

    @Override
    public void create(AltsystemMannschaftDO altsystemDataObject, long currentUserId) throws SQLException {
        DsbMannschaftDO dsbMannschaftDO = new DsbMannschaftDO();
        dsbMannschaftDO = altsystemMannschaftMapper.toDO(dsbMannschaftDO, altsystemDataObject);
        dsbMannschaftDO = altsystemMannschaftMapper.addDefaultFields(dsbMannschaftDO, currentUserId, altsystemDataObject);

        dsbMannschaftComponent.create(dsbMannschaftDO, currentUserId);

        altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, (int) altsystemDataObject.getId(), dsbMannschaftDO.getId().longValue(), "");
    }


    @Override
    public void update(AltsystemMannschaftDO altsystemDataObject, long currentUserId) {

        AltsystemUebersetzungDO mannschaftUebersetzung = altsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Mannschaft_Mannschaft, altsystemDataObject.getId());


        if(mannschaftUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", altsystemDataObject.getId()));
        }

        DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(mannschaftUebersetzung.getBogenliga_id());

        altsystemMannschaftMapper.toDO(dsbMannschaftDO, altsystemDataObject);

        dsbMannschaftComponent.update(dsbMannschaftDO, currentUserId);

    }
}
