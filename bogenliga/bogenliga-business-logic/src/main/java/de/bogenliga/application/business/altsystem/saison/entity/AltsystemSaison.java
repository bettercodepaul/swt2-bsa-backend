package de.bogenliga.application.business.altsystem.saison.entity;

import org.springframework.stereotype.Component;
import de.bogenliga.application.business.altsystem.saison.dataobject.AltsystemSaisonDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzung;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungDO;
import de.bogenliga.application.business.altsystem.uebersetzung.AltsystemUebersetzungKategorie;
import de.bogenliga.application.common.altsystem.AltsystemEntity;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;

/**
 * Component to handle the import of a "Saison" entity
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class AltsystemSaison implements AltsystemEntity<AltsystemSaisonDO>{

    private final AltsystemUebersetzung altsystemUebersetzung;


    public AltsystemSaison(AltsystemUebersetzung altsystemUebersetzung) {
        this.altsystemUebersetzung = altsystemUebersetzung;
    }

    /**
     * Creates new entry in altsystemUebersetzung for sportjahr
     *
     * @param altsystemSaisonDO    altsystemSaison DO containing saison name
     * @param currentUserId        id of the currently logged in user that sent the create request
     *
     */
    @Override
    public void create(AltsystemSaisonDO altsystemSaisonDO, long currentUserId) {

    }
    public void update(AltsystemSaisonDO altsystemSaisonDO, long currentUserId) {

    }


}
