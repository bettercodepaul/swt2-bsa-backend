package de.bogenliga.application.business.altsystem.wettkampfdaten.entity;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.wettkampfdaten.dataobject.AltsystemWettkampfdatenDO;
import de.bogenliga.application.business.altsystem.wettkampfdaten.mapper.AltsystemMatchMapper;
import de.bogenliga.application.business.altsystem.wettkampfdaten.mapper.AltsystemWettkampftagMapper;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.altsystem.AltsystemEntity;

/**
 * Component to handle the import of a "Wettkampfdaten" entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemWettkampfdaten implements AltsystemEntity<AltsystemWettkampfdatenDO> {

    private final AltsystemMatchMapper altsystemWettkampfdatenMapper;
    private final AltsystemWettkampftagMapper altsystemWettkampftagMapper;
    private final MatchComponent matchComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final LigaComponent ligaComponent;

    @Autowired
    public AltsystemWettkampfdaten(final AltsystemMatchMapper altsystemWettkampfdatenMapper,
                                   AltsystemWettkampftagMapper altsystemWettkampftagMapper, final MatchComponent matchComponent,
                                   VeranstaltungComponent veranstaltungComponent, LigaComponent ligaComponent) {
        this.altsystemWettkampfdatenMapper = altsystemWettkampfdatenMapper;
        this.altsystemWettkampftagMapper = altsystemWettkampftagMapper;
        this.matchComponent = matchComponent;
        this.veranstaltungComponent = veranstaltungComponent;
        this.ligaComponent = ligaComponent;
    }

    @Override
    public void create(AltsystemWettkampfdatenDO altsystemDataObject, long currentUserId){
        // Daten sind im Altsystem redundant
        // Bearbeitung nur, falls Sec = 0 ist
        if (altsystemDataObject.getSec() == 0){
            List<WettkampfDO> wettkampfTage = altsystemWettkampftagMapper.getOrCreateWettkampftage(altsystemDataObject, currentUserId);

            MatchDO[] match = new MatchDO[2];
            match[0] = new MatchDO();
            match[1] = new MatchDO();
            match = altsystemWettkampfdatenMapper.toDO(match, altsystemDataObject);
            match = altsystemWettkampfdatenMapper.addDefaultFields(match, wettkampfTage);
        }


        // Add data to table
        // ligaComponent.create(ligaDO, <UserID>);

        // Add to translation table

    }

    @Override
    public void update(AltsystemWettkampfdatenDO altsystemDataObject, long currentUserId){
        // Get primary key from translation table

        // Find data in table with corresponding id
        MatchDO matchDO = matchComponent.findById(1L);

        // Map data to new object, don't add default fields
        // altsystemWettkampfdatenMapper.toDO(match, altsystemDataObject);

        // Update data in table with given primary key
        // ligaComponent.update(ligaDO, <UserID>);
    }

}
