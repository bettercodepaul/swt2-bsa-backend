package de.bogenliga.application.business.einstellungen.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.einstellungen.api.EinstellungenComponent;
import de.bogenliga.application.business.einstellungen.api.types.EinstellungenDO;
import de.bogenliga.application.business.einstellungen.impl.dao.EinstellungenDAO;
import de.bogenliga.application.business.einstellungen.impl.entity.EinstellungenBE;
import de.bogenliga.application.business.einstellungen.impl.mapper.EinstellungenMapper;


/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class EinstellungenComponentImpl implements EinstellungenComponent {


    private static final String PREDICTION_EINSTELLUNG = "EinstzellungenDO must not be null";
    private static final String PRECONDITION_CURRENT_EEINSTELLUNGEN_ID = "Current Einstellungen ID must not be null";
    private static final String PRECONDITION_CURRENT_EINSTELLUNGEN_ID_NEGATIVE = "Current User ID must not be negativ";

    private final EinstellungenDAO einstellungenDAO;



    private static final Logger LOGGER= LoggerFactory.getLogger(EinstellungenComponentImpl.class);

    @Autowired
    public EinstellungenComponentImpl(EinstellungenDAO einstellungenDAO) {
        this.einstellungenDAO = einstellungenDAO;
    }

    @Override
    public List<EinstellungenDO> findAll() {
        final List<EinstellungenBE> einstellungenList = einstellungenDAO.findAll();

        return einstellungenList.stream().map(EinstellungenMapper.toEinstellungenDO).collect(Collectors.toList());
    }



    @Override
    public EinstellungenDO create(EinstellungenDO einstellungenDO, long currentEinstellungenId) {

        final EinstellungenBE einstellungenBE = EinstellungenMapper.toEinstellungenBE.apply(einstellungenDO);

        final EinstellungenBE persistedEinstellungenBE = einstellungenDAO.create(einstellungenBE, currentEinstellungenId);
        return EinstellungenMapper.toEinstellungenDO.apply(persistedEinstellungenBE);

    }

    @Override
    public EinstellungenDO update(EinstellungenDO einstellungenDO, long currentEinstellungenId) {


        final EinstellungenBE einstellungenBE = EinstellungenMapper.toEinstellungenBE.apply(einstellungenDO);

        final EinstellungenBE persistedEinstellungenBE = einstellungenDAO.update(einstellungenBE, currentEinstellungenId);
        return EinstellungenMapper.toEinstellungenDO.apply(persistedEinstellungenBE);

    }

    @Override
    public void delete(EinstellungenDO einstellungenDO, long currentEinstellungenId) {

        final EinstellungenBE einstellungenBE = EinstellungenMapper.toEinstellungenBE.apply(einstellungenDO);
        einstellungenDAO.delete(einstellungenBE, currentEinstellungenId);

    }



}
