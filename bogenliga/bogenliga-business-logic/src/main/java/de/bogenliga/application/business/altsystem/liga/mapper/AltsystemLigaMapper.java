package de.bogenliga.application.business.altsystem.liga.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.liga.dataobject.AltsystemLigaDO;
import de.bogenliga.application.business.disziplin.api.DisziplinComponent;
import de.bogenliga.application.business.disziplin.api.types.DisziplinDO;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
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
    private final DisziplinComponent disziplinComponent;
    private final RegionenComponent regionenComponent;
    private final UserComponent userComponent;

    @Autowired
    public AltsystemLigaMapper(final DisziplinComponent disziplinComponent, final RegionenComponent regionenComponent, final UserComponent userComponent){
        this.disziplinComponent = disziplinComponent;
        this.regionenComponent = regionenComponent;
        this.userComponent = userComponent;
    }
    public LigaDO toDO(LigaDO ligaDO, AltsystemLigaDO altsystemLigaDO){
        String ligaName = altsystemLigaDO.getName();
        long ligaUebergeordnetId = altsystemLigaDO.getIdNextLiga();

        ligaDO.setName(ligaName);

        // Id muss noch übersetzt werden
        ligaDO.setLigaUebergeordnetId(ligaUebergeordnetId);

        // Checken ob es eine Recurve oder Compound Liga ist
        Map<String, DisziplinDO> disziplinen = getDisziplinen();
        DisziplinDO disziplinDO;

        if(ligaName.toLowerCase().contains("comp")){
            disziplinDO = disziplinen.get(COMPOUND_NAME);
        }
        else{
            disziplinDO = disziplinen.get(RECURVE_NAME);
        }
        ligaDO.setDisziplinId(disziplinDO.getDisziplinId());

        return ligaDO;
    }

    public LigaDO addDefaultFields(LigaDO ligaDO) {
        // ID vom Admin User herausfinden
        UserDO adminDO = userComponent.findByEmail("admin@bogenliga.de");
        ligaDO.setLigaVerantwortlichId(adminDO.getId());
        // ID von DSB Region herausfinden
        RegionenDO dsbDO = getDsbDO();
        ligaDO.setRegionId(dsbDO.getId());
        return ligaDO;
    }

    public Map<String, DisziplinDO> getDisziplinen(){
        HashMap<String, DisziplinDO> hashDisziplinen = new HashMap<>();
        List<DisziplinDO> disziplinen = disziplinComponent.findAll();
        for (DisziplinDO disziplinDO : disziplinen){
            String disziplinName = disziplinDO.getDisziplinName().toLowerCase();
            if (disziplinName.equals(COMPOUND_NAME)){
                hashDisziplinen.put(COMPOUND_NAME, disziplinDO);
            }else if (disziplinName.equals(RECURVE_NAME)){
                hashDisziplinen.put(RECURVE_NAME, disziplinDO);
            }
        }
        return hashDisziplinen;
    }

    public RegionenDO getDsbDO(){
        RegionenDO dsbDO = null;
        List<RegionenDO> regionenDOS = regionenComponent.findBySearch("dsb");
        for (RegionenDO regionenDO : regionenDOS){
            String regionName = regionenDO.getRegionName().toLowerCase();
            if(regionName.equals("dsb")){
                dsbDO = regionenDO;
                break;
            }
        }
        return dsbDO;
    }
}
