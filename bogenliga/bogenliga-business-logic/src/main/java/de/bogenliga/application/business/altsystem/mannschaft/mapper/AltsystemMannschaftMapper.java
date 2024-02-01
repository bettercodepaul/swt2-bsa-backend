package de.bogenliga.application.business.altsystem.mannschaft.mapper;

import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.liga.mapper.AltsystemLigaMapper;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemMannschaftMapper implements ValueObjectMapper {

    private final AltsystemUebersetzung altsystemUebersetzung;

    private final DsbMannschaftComponent dsbMannschaftComponent;



    public AltsystemMannschaftMapper(AltsystemUebersetzung altsystemUebersetzung,
                                     AltsystemLigaMapper altsystemLigaMapper,
                                     DsbMannschaftComponent dsbMannschaftComponent) {
        this.altsystemUebersetzung = altsystemUebersetzung;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
    }


    public DsbMannschaftDO toDO(DsbMannschaftDO dsbMannschaftDO, AltsystemMannschaftDO altsystemMannschaftDO){

        String currentName = altsystemMannschaftDO.getName().toString();
        int nameLong = currentName.length() - 1;
        char lastChar = altsystemMannschaftDO.getName().charAt(nameLong);
        String lastCharAsString = String.valueOf(lastChar);
        int mannNr;
        String num = "1234567890";


        if (currentName.equals("fehlender Verein") || currentName.equals("<leer>") || currentName == null) {
            return null;
        }
        if (num.contains(lastCharAsString)){
            mannNr = Integer.parseInt(lastCharAsString);
        } else {
            mannNr = 1;
        }

        dsbMannschaftDO.setNummer(mannNr);

        return dsbMannschaftDO;
    }


    public DsbMannschaftDO addDefaultFields ( DsbMannschaftDO dsbMannschaftDO, long currentDSBMitglied,  AltsystemMannschaftDO altsystemDataObject){

        dsbMannschaftDO.setBenutzerId(currentDSBMitglied);


        AltsystemUebersetzungDO vereinUebersetzung = altsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Mannschaft_Verein, altsystemDataObject.getId());

        if(vereinUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", altsystemDataObject.getId()));
        }

        long vereinId = Long.parseLong(String.valueOf(vereinUebersetzung));
        dsbMannschaftDO.setVereinId(vereinId);


        AltsystemUebersetzungDO veranstaltungUebersetzung = altsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Mannschaft_Veranstaltung, altsystemDataObject.getId());

        if(veranstaltungUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", altsystemDataObject.getId()));
        }

        long veranstaltungId = Long.parseLong(String.valueOf(veranstaltungUebersetzung));
        dsbMannschaftDO.setVeranstaltungId(veranstaltungId);

        return dsbMannschaftDO;
    }





}
