package de.bogenliga.application.business.altsystem.verein.mapper;


import java.util.List;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.liga.mapper.AltsystemLigaMapper;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemVereinMapper implements ValueObjectMapper {




    public AltsystemVereinMapper(VereinComponent vereinComponent, AltsystemVereinMapper altsystemVereinMapper,
                                 AltsystemUebersetzung altsystemUebersetzung) {

    }



}
