package de.bogenliga.application.business.einstellungen.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.einstellungen.api.EinstellungenComponent;
import de.bogenliga.application.business.einstellungen.api.types.EinstellungenDO;
import de.bogenliga.application.business.einstellungen.impl.dao.EinstellungenDAO;
import de.bogenliga.application.business.einstellungen.impl.entity.EinstellungenBE;
import de.bogenliga.application.business.einstellungen.impl.mapper.EinstellungenMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;


/**
 * Implementation of {@link EinstellungenComponent}
 *
 * @author Fabio Care fabio_silas.care@student.reutlingen-university.de
 */
@Component
public class EinstellungenComponentImpl implements EinstellungenComponent {


    private final EinstellungenDAO einstellungenDAO;


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param einstellungenDAO to access the database and return configuration representations
     */

    @Autowired
    public EinstellungenComponentImpl(final EinstellungenDAO einstellungenDAO) {

        this.einstellungenDAO = einstellungenDAO;
    }


    @Override
    public List<EinstellungenDO> findAll() {
        final List<EinstellungenBE> einstellungenBEList = einstellungenDAO.findAll();

        return einstellungenBEList.stream().map(EinstellungenMapper.toDO).collect(Collectors.toList());
    }

    @Override
    public EinstellungenDO findById(final long id) {

        final EinstellungenBE result = einstellungenDAO.findById(id);
        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, "no match found for id ");
        }
        return EinstellungenMapper.toDO.apply(result);


    }

    @Override
    public EinstellungenDO create(EinstellungenDO einstellungenDO, long currentUserId) {

        final EinstellungenBE einstellungenBE = EinstellungenMapper.toBE.apply(einstellungenDO);

        final EinstellungenBE persistedEinstellungenBE = einstellungenDAO.create(einstellungenBE, currentUserId);
        return EinstellungenMapper.toDO.apply(persistedEinstellungenBE);

    }


    @Override
    public EinstellungenDO update(EinstellungenDO einstellungenDO, long currentUserId) {


        final EinstellungenBE einstellungenBE = EinstellungenMapper.toBE.apply(einstellungenDO);

        final EinstellungenBE persistedEinstellungenBE = einstellungenDAO.update(einstellungenBE, currentUserId);
        return EinstellungenMapper.toDO.apply(persistedEinstellungenBE);

    }


    @Override
    public void delete(EinstellungenDO einstellungenDO, long currentUserId) {

        final EinstellungenBE einstellungenBE = EinstellungenMapper.toBE.apply(einstellungenDO);
        einstellungenDAO.delete(einstellungenBE, currentUserId);

    }


}
