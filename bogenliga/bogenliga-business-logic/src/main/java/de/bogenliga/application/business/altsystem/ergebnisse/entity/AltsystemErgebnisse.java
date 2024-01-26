package de.bogenliga.application.business.altsystem.ergebnisse.entity;


import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.mannschaft.mapper.AltsystemMannschaftMapper;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;

import de.bogenliga.application.business.altsystem.ergebnisse.mapper.AltsystemPasseMapper;
import de.bogenliga.application.business.altsystem.wettkampfdaten.mapper.AltsystemMatchMapper;
import de.bogenliga.application.business.altsystem.wettkampfdaten.mapper.AltsystemWettkampftagMapper;
import de.bogenliga.application.business.altsystem.schuetze.mapper.AltsystemSchuetzeMapper;

import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;

import de.bogenliga.application.business.altsystem.wettkampfdaten.dataobject.AltsystemWettkampfdatenDO;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.ergebnisse.dataobject.AltsystemErgebnisseDO;
import de.bogenliga.application.business.altsystem.schuetze.dataobject.AltsystemSchuetzeDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
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
    public AltsystemErgebnisse(final AltsystemPasseMapper altsystemPasseMapper,
                                   final PasseComponent passeComponent) {
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