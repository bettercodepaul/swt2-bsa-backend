package de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.SchuetzenstatistikWettkampfComponent;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.api.types.SchuetzenstatistikWettkampftageDO;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.dao.SchuetzenstatistikWettkampfDAO;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.entity.SchuetzenstatistikWettkampfBE;
import de.bogenliga.application.business.schuetzenstatistikwettkampf.impl.mapper.SchuetzenstatistikWettkampfMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import org.springframework.stereotype.Component;

/**
 *  Implementation of {@link SchuetzenstatistikWettkampfComponent}
 * @author Anna Baur
 */
@Component
public class SchuetzenstatistikWettkampfComponentImpl implements SchuetzenstatistikWettkampfComponent {
    private final SchuetzenstatistikWettkampfDAO schuetzenstatistikWettkampfDAO;

    private static final String PRECONDITION_VERANSTALTUNGID = "veranstaltungID cannot be null or negative";
    private static final String PRECONDITION_WETTKAMPFID = "wettkampfID cannot be null or negative";
    private static final String PRECONDITION_VEREINID = "vereinID cannot be null or negative";

    /**
     * Constructor
     * dependency injection with {@link Autowired}
     * @param schuetzenstatistikWettkampfDAO to access the database and return user representations
     */
    @Autowired
    public SchuetzenstatistikWettkampfComponentImpl(final SchuetzenstatistikWettkampfDAO schuetzenstatistikWettkampfDAO) {
        this.schuetzenstatistikWettkampfDAO = schuetzenstatistikWettkampfDAO;
    }


    @Override
    public List<SchuetzenstatistikWettkampftageDO> getSchuetzenstatistikWettkampfVeranstaltung(Long veranstaltungId, Long vereinId) {
        Preconditions.checkArgument(veranstaltungId >= 0, PRECONDITION_VERANSTALTUNGID);
        Preconditions.checkArgument(vereinId >= 0, PRECONDITION_VEREINID);

        final ArrayList<SchuetzenstatistikWettkampftageDO> returnList = new ArrayList<>();
        final List<SchuetzenstatistikWettkampfBE> schuetzenstatistikWettkampfBEList = schuetzenstatistikWettkampfDAO.getSchuetzenstatistikWettkampfVeranstaltung(veranstaltungId, vereinId);

        if (schuetzenstatistikWettkampfBEList == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, ("No result found for Veranstaltungs-ID "+ veranstaltungId+ " and Verein-ID "+ vereinId));
        }

        for (int i = 0; i < schuetzenstatistikWettkampfBEList.size(); i++) {
            returnList.add(i, SchuetzenstatistikWettkampfMapper.toSchuetzenstatistikWettkampfDO.apply(schuetzenstatistikWettkampfBEList.get(i)));
        }
        return returnList;
    }

    @Override
    public List<SchuetzenstatistikWettkampftageDO> getSchuetzenstatistikWettkampf(Long wettkampfId, Long vereinId) {
        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_WETTKAMPFID);
        Preconditions.checkArgument(vereinId >= 0, PRECONDITION_VEREINID);

        final ArrayList<SchuetzenstatistikWettkampftageDO> returnList = new ArrayList<>();
        final List<SchuetzenstatistikWettkampfBE> schuetzenstatistikWettkampfBEList = schuetzenstatistikWettkampfDAO.getSchuetzenstatistikWettkampf(wettkampfId, vereinId);

        if (schuetzenstatistikWettkampfBEList == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, ("No result found for Wettkampf-ID " + wettkampfId) + " and Verein-ID " + vereinId);
        }

        for (int i = 0; i < schuetzenstatistikWettkampfBEList.size(); i++) {
            returnList.add(i, SchuetzenstatistikWettkampfMapper.toSchuetzenstatistikWettkampfDO.apply(schuetzenstatistikWettkampfBEList.get(i)));
        }
        return returnList;
    }
}
