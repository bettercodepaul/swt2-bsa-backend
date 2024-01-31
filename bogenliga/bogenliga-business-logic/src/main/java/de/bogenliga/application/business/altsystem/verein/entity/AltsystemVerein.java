package de.bogenliga.application.business.altsystem.verein.entity;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.mannschaft.dataobject.AltsystemMannschaftDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.business.altsystem.verein.mapper.AltsystemVereinMapper;
import de.bogenliga.application.business.vereine.api.VereinComponent;
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

        String parsedIdentifier = altsystemVereinMapper.parseIdentifier(altsystemDataObject);
        VereinDO vorhanden = altsystemVereinMapper.getVereinDO(parsedIdentifier);

        if (vorhanden == null){
            vereinDO = altsystemVereinMapper.toDO(vereinDO, altsystemDataObject);
            vereinDO = altsystemVereinMapper.addDefaultFields(vereinDO);
            vereinComponent.create(vereinDO, currentUserId);
            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Mannschaft_Verein, (int) altsystemDataObject.getId(), vereinDO.getId().longValue(), "");

        }else {
            altsystemUebersetzung.updateOrInsertUebersetzung(AltsystemUebersetzungKategorie.Mannschaft_Verein, (int) altsystemDataObject.getId(), vorhanden.getId().longValue(), "");
        }

    }


    @Override
    public void update(AltsystemMannschaftDO altsystemDataObject, long currentUserId) throws SQLException {

        AltsystemUebersetzungDO vereinUebersetzung = altsystemUebersetzung.findByAltsystemID(
                AltsystemUebersetzungKategorie.Mannschaft_Verein, altsystemDataObject.getId());


        if(vereinUebersetzung == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", altsystemDataObject.getId()));
        }

        VereinDO vereinDO = vereinComponent.findById(vereinUebersetzung.getBogenligaId());

        altsystemVereinMapper.toDO(vereinDO, altsystemDataObject);

        vereinComponent.update(vereinDO, currentUserId);

    }
}
