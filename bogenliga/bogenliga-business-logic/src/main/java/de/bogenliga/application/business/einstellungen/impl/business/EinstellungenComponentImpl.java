package de.bogenliga.application.business.einstellungen.impl.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.bogenliga.application.business.einstellungen.api.EinstellungenComponent;
import de.bogenliga.application.business.einstellungen.api.types.EinstellungenDO;
import de.bogenliga.application.business.einstellungen.impl.dao.EinstellungenDAO;
import de.bogenliga.application.business.einstellungen.impl.entity.EinstellungenBE;
import de.bogenliga.application.business.einstellungen.impl.mapper.EinstellungenMapper;
import de.bogenliga.application.business.lizenz.impl.entity.LizenzBE;
import de.bogenliga.application.business.lizenz.impl.mapper.LizenzMapper;
import de.bogenliga.application.business.passe.impl.entity.PasseBE;
import de.bogenliga.application.business.passe.impl.mapper.PasseMapper;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class EinstellungenComponentImpl implements EinstellungenComponent {


    private static final String PREDICTION_EINSTELLUNG = "EinstzellungenDO must not be null";
    private static final String PRECONDITION_CURRENT_EEINSTELLUNGEN_ID = "Current Einstellungen ID must not be null";
    private static final String PRECONDITION_CURRENT_EINSTELLUNGEN_ID_NEGATIVE = "Current User ID must not be negativ";

    private final EinstellungenDAO einstellungenDAO;



    private static final Logger LOGGER= LoggerFactory.getLogger(EinstellungenComponentImpl.class);


    public EinstellungenComponentImpl(
            EinstellungenDAO einstellungenDAO) {
        this.einstellungenDAO = einstellungenDAO;
    }


    public EinstellungenDO create(EinstellungenDO einstellungenDO,final Long currentUserId){

        final EinstellungenBE einstellungenBE = EinstellungenMapper.toEinstellungenBE.apply(einstellungenDO);

        final EinstellungenBE persistedEinstellungenBE = einstellungenDAO.create(einstellungenBE, currentUserId);
        return PasseMapper.toEinstellungenDO.apply(persistedEinstellungenBE);

    }

    public EinstellungenDO update(EinstellungenDO einstellungenDO,final Long currentUserId){


        final EinstellungenBE einstellungenBE = EinstellungenMapper.toEinstellungenBE.apply(einstellungenDO);

        final EinstellungenBE persistedEinstellungenBE = einstellungenDAO.update(einstellungenBE, currentUserId);
        return EinstellungenMapper.toEinstellungenDO.apply(persistedEinstellungenBE);





    }

    public EinstellungenDO delete(EinstellungenDO einstellungenDO,final Long currentMemberId){


        final EinstellungenBE einstellungenBE = EinstellungenMapper.toEinstellungenBE.apply(einstellungenDO);
        einstellungenDAO.delete(einstellungenBE, currentMemberId);



        return null;
    }



}
