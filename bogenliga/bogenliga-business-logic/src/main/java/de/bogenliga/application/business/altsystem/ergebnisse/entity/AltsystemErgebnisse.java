package de.bogenliga.application.business.altsystem.ergebnisse.entity;


import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.ergebnisse.mapper.AltsystemPasseMapper;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.altsystem.ergebnisse.dataobject.AltsystemErgebnisseDO;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;


/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemErgebnisse implements AltsystemEntity<AltsystemErgebnisseDO> {

    private final AltsystemPasseMapper altsystemPasseMapper;

    // Components
    private final PasseComponent passeComponent;



    @Autowired
    public AltsystemErgebnisse(final AltsystemPasseMapper altsystemPasseMapper, final PasseComponent passeComponent) {
        this.altsystemPasseMapper = altsystemPasseMapper;
        this.passeComponent = passeComponent;
    }

    @Override
    public void create(AltsystemErgebnisseDO altsystemDataObject, long currentUserId){
        List<PasseDO> passen = new ArrayList<>();
        passen = altsystemPasseMapper.toDO(passen, altsystemDataObject);

        // Add data to table
        for(PasseDO passe : passen){
            passeComponent.create(passe, currentUserId);
        }
    }
    @Override
    public void update(AltsystemErgebnisseDO altsystemDataObject, long currentUserId){
        // yet to be implemented
    }

}