package de.bogenliga.application.business.altsystem.verein.entity;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.verein.mapper.AltsystemVereinMapper;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemVerein implements AltsystemEntity<AltsystemMannschaftDO> {


    private final AltsystemVereinMapper altsystemVereinMapper;
    private final VereinComponent vereinComponent;
    private final AltsystemUebersetzung altsystemUebersetzung;


    @Autowired
    public AltsystemVerein (final AltsystemVereinMapper altsystemVereinMapper, final VereinComponent vereinComponent,
                            AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemVereinMapper = altsystemVereinMapper;
        this.vereinComponent = vereinComponent;
        this.altsystemUebersetzung = altsystemUebersetzung;
    }


    @Override
    public void create(AltsystemMannschaftDO altsystemDataObject, long currentUserId) throws SQLException {
        VereinDO vereinDO = new VereinDO();
        vereinDO = altsystemVereinMapper.toDO(vereinDO, altsystemDataObject);
    }


    @Override
    public void update(AltsystemMannschaftDO altsystemDataObject, long currentUserId) {

    }
}
