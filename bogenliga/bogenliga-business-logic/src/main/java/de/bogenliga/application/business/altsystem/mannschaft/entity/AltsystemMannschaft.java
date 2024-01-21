package de.bogenliga.application.business.altsystem.mannschaft.entity;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemMannschaftMapper;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class AltsystemMannschaft implements AltsystemEntity<AltsystemMannschaftDO> {


    private final AltsystemMannschaftMapper altsystemMannschaftMapper;
    private final DsbMannschaftComponent mannschaftComponent;


    @Autowired
    public AltsystemMannschaft(final AltsystemMannschaftMapper altsystemMannschaftMapper, final DsbMannschaftComponent mannschaftComponent){
        this.altsystemMannschaftMapper = altsystemMannschaftMapper;
        this.mannschaftComponent = mannschaftComponent;
    }

    @Override
    public void create(AltsystemMannschaftDO altsystemDataObject, long currentUserId) throws SQLException {
        DsbMannschaftDO dsbMannschaftDO = new DsbMannschaftDO();
        dsbMannschaftDO = AltsystemMannschaftMapper.toDO(dsbMannschaftDO, altsystemDataObject);
    }


    @Override
    public void update(AltsystemMannschaftDO altsystemDataObject, long currentUserId) {

    }
}
