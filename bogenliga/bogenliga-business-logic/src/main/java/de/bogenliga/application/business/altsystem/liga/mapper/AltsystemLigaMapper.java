package de.bogenliga.application.business.altsystem.liga.mapper;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.disziplin.api.DisziplinComponent;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

/**
 * Provides functionality to map the bean of Bogenliga.de to a
 * Business Entity (BE) of the new application
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemLigaMapper implements ValueObjectMapper {

    private static final String COMPOUND_NAME = "compound";
    private static final String RECURVE_NAME = "recurve";
    private static final String LIGA_UEBERGEORDNET_NAME = "Bundesliga";
    private final LigaComponent ligaComponent;
    private final DisziplinComponent disziplinComponent;
    private final RegionenComponent regionenComponent;

    @Autowired
    public AltsystemLigaMapper(final LigaComponent ligaComponent, final DisziplinComponent disziplinComponent, final RegionenComponent regionenComponent){
        this.ligaComponent = ligaComponent;
        this.disziplinComponent = disziplinComponent;
        this.regionenComponent = regionenComponent;
    }

    /**
     Converts the legacy data into a LigaDO*
     @param ligaDO DataObject in which the new data should be stored
     @param altsystemLigaDO data from the legacy system
     @return LigaDO with values from altsystemLigaDO
     */
    public LigaDO toDO(LigaDO ligaDO, AltsystemLigaDO altsystemLigaDO){
        String ligaName = altsystemLigaDO.getName();
        ligaDO.setName(ligaName);

        String key;
        // Checken ob es eine Recurve oder Compound Liga ist
        if(ligaName.toLowerCase().contains("comp")){
            key = COMPOUND_NAME;
        }
        else{
            key = RECURVE_NAME;
        }
        DisziplinDO disziplinDO = getDisziplinByKey(key);
        ligaDO.setDisziplinId(disziplinDO.getDisziplinId());

        return ligaDO;
    }

    /**
     Adds fields to the new LigaDO which cannot be extracted from the legacy data and therefore must be set to a default value*
     @param ligaDO DataObject in which the new data should be stored
     @param currentDSBMitglied id of the member that should be responsible for the Liga (field LigaVerantwortlich)
     @return LigaDO with default values
     */
    public LigaDO addDefaultFields(LigaDO ligaDO, long currentDSBMitglied) {
        // Importierender Benutzer wird als Verantwortlicher gesetzt
        ligaDO.setLigaVerantwortlichId(currentDSBMitglied);
        // ID von DSB Region herausfinden
        RegionenDO dsbDO = getDsbDO();
        ligaDO.setRegionId(dsbDO.getId());
        // Übergeordnete Liga defaultmäßig auf Bundesliga setzen
        LigaDO ligaUebergeordnetDO = ligaComponent.checkExistsLigaName(LIGA_UEBERGEORDNET_NAME);
        ligaDO.setLigaUebergeordnetId(ligaUebergeordnetDO.getId());
        return ligaDO;
    }

    /**
     Helper function to get either the discipline Compound or Recurve in order to store them in the LigaDO*
     @param key either "compound" or "recurve"
     @return DisziplinDO for the specified discipline
     */
    public DisziplinDO getDisziplinByKey(String key){
        DisziplinDO resultDO = null;
        // Auslesen aller vorhandenen Disziplinen
        List<DisziplinDO> disziplinen = disziplinComponent.findAll();

        for (DisziplinDO disziplinDO : disziplinen){
            String disziplinName = disziplinDO.getDisziplinName().toLowerCase();
            // Testen ob die aktuelle Disziplin dem Key entspricht
            if (disziplinName.equals(key)) {
                resultDO = disziplinDO;
                break;
            }
        }
        return resultDO;
    }

    /**
     Helper function to get the RegionenDO for DSB in order to store its ID in the LigaDO*
     @return RegionenDO for the DSB
     */
    public RegionenDO getDsbDO(){
        // Funktion gibt den DSB als konkrete Region zurück
        // Dieser wird defaultmäßig für alle Ligen als Region gesetzt
        RegionenDO dsbDO = null;
        List<RegionenDO> regionenDOS = regionenComponent.findAll();
        for (RegionenDO regionenDO : regionenDOS){
            String regionKuerzel = regionenDO.getRegionKuerzel().toLowerCase();
            if(regionKuerzel.equals("dsb")){
                dsbDO = regionenDO;
                break;
            }
        }
        return dsbDO;
    }
}
