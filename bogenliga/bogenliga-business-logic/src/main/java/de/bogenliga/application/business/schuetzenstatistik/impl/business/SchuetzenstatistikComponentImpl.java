package de.bogenliga.application.business.schuetzenstatistik.impl.business;

import de.bogenliga.application.business.schuetzenstatistik.api.SchuetzenstatistikComponent;
import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.business.schuetzenstatistik.impl.dao.SchuetzenstatistikDAO;
import de.bogenliga.application.business.schuetzenstatistik.impl.entity.SchuetzenstatistikBE;
import de.bogenliga.application.business.schuetzenstatistik.impl.mapper.SchuetzenstatistikMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link SchuetzenstatistikComponent}
 */
@Component
public class SchuetzenstatistikComponentImpl implements SchuetzenstatistikComponent {

    private final SchuetzenstatistikDAO schuetzenstatistikDAO;

    private static final String PRECONDITION_VERANSTALTUNGID = "veranstaltungID cannot be null or negative";
    private static final String PRECONDITION_WETTKAMPFID = "wettkampfID cannot be null or negative";
    private static final String PRECONDITION_VEREINID = "vereinID cannot be null or negative";

    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param schuetzenstatistikDAO to access the database and return user representations
     */
    @Autowired
    public SchuetzenstatistikComponentImpl(final SchuetzenstatistikDAO schuetzenstatistikDAO) {
        this.schuetzenstatistikDAO = schuetzenstatistikDAO;
    }


    @Override
    public List<SchuetzenstatistikDO> getSchuetzenstatistikVeranstaltung(Long veranstaltungId, Long vereinId) {
        Preconditions.checkArgument(veranstaltungId >= 0, PRECONDITION_VERANSTALTUNGID);
        Preconditions.checkArgument(vereinId >= 0, PRECONDITION_VEREINID);

        final ArrayList<SchuetzenstatistikDO> returnList = new ArrayList<>();
        final List<SchuetzenstatistikBE> schuetzenstatistikBEList = schuetzenstatistikDAO.getSchuetzenstatistikVeranstaltung(veranstaltungId, vereinId);

        if (schuetzenstatistikBEList == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, ("No result found for Veranstaltungs-ID "+ veranstaltungId+ " and Verein-ID "+ vereinId));
        }

        for (int i = 0; i < schuetzenstatistikBEList.size(); i++) {
            returnList.add(i, SchuetzenstatistikMapper.toSchuetzenstatistikDO.apply(schuetzenstatistikBEList.get(i)));
        }
        return returnList;
    }

    @Override
    public List<SchuetzenstatistikDO> getSchuetzenstatistikWettkampf(Long wettkampfId, Long vereinId) {
        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_WETTKAMPFID);
        Preconditions.checkArgument(vereinId >= 0, PRECONDITION_VEREINID);

        final ArrayList<SchuetzenstatistikDO> returnList = new ArrayList<>();
        final List<SchuetzenstatistikBE> schuetzenstatistikBEList = schuetzenstatistikDAO.getSchuetzenstatistikWettkampf(wettkampfId, vereinId);

        if (schuetzenstatistikBEList == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, ("No result found for Wettkampf-ID " + wettkampfId) + " and Verein-ID " + vereinId);
        }

        for (int i = 0; i < schuetzenstatistikBEList.size(); i++) {
            returnList.add(i, SchuetzenstatistikMapper.toSchuetzenstatistikDO.apply(schuetzenstatistikBEList.get(i)));
        }
        return returnList;
    }


}
