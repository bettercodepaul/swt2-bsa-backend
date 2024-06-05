package de.bogenliga.application.business.schuetzenstatistikmatch.impl.business;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.schuetzenstatistikmatch.impl.dao.SchuetzenstatistikMatchDAO;
import de.bogenliga.application.business.schuetzenstatistikmatch.api.SchuetzenstatistikMatchComponent;
import de.bogenliga.application.business.schuetzenstatistikmatch.api.types.SchuetzenstatistikMatchDO;
import de.bogenliga.application.business.schuetzenstatistikmatch.impl.entity.SchuetzenstatistikMatchBE;
import de.bogenliga.application.business.schuetzenstatistikmatch.impl.mapper.SchuetzenstatistikMatchMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Component
public class SchuetzenstatistikMatchComponentImpl implements SchuetzenstatistikMatchComponent {

    private final SchuetzenstatistikMatchDAO schuetzenstatistikMatchDAO;

    private static final String PRECONDITION_VERANSTALTUNGID = "veranstaltungID cannot be null or negative";
    private static final String PRECONDITION_WETTKAMPFID = "wettkampfID cannot be null or negative";
    private static final String PRECONDITION_VEREINID = "vereinID cannot be null or negative";

    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param schuetzenstatistikMatchDAO to access the database and return user representations
     */
    @Autowired
    public SchuetzenstatistikMatchComponentImpl(final SchuetzenstatistikMatchDAO schuetzenstatistikMatchDAO) {
        this.schuetzenstatistikMatchDAO = schuetzenstatistikMatchDAO;
    }



    @Override
    public List<SchuetzenstatistikMatchDO> getSchuetzenstatistikMatchVeranstaltung(Long veranstaltungId, Long vereinId) {
        Preconditions.checkArgument(veranstaltungId >= 0, PRECONDITION_VERANSTALTUNGID);
        Preconditions.checkArgument(vereinId >= 0, PRECONDITION_VEREINID);

        final ArrayList<SchuetzenstatistikMatchDO> returnList = new ArrayList<>();
        final List<SchuetzenstatistikMatchBE> schuetzenstatistikMatchBEList = schuetzenstatistikMatchDAO.getSchuetzenstatistikMatchVeranstaltung(veranstaltungId, vereinId);

        if (schuetzenstatistikMatchBEList == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, ("No result found for Veranstaltungs-ID "+ veranstaltungId+ " and Verein-ID "+ vereinId));
        }

        for (int i = 0; i < schuetzenstatistikMatchBEList.size(); i++) {
            returnList.add(i, SchuetzenstatistikMatchMapper.toSchuetzenstatistikMatchDO.apply(schuetzenstatistikMatchBEList.get(i)));
        }
        return returnList;
    }


    @Override
    public List<SchuetzenstatistikMatchDO> getSchuetzenstatistikMatchWettkampf(Long wettkampfId, Long vereinId) {
        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_WETTKAMPFID);
        Preconditions.checkArgument(vereinId >= 0, PRECONDITION_VEREINID);

        final ArrayList<SchuetzenstatistikMatchDO> returnList = new ArrayList<>();
        final List<SchuetzenstatistikMatchBE> schuetzenstatistikMatchBEList = schuetzenstatistikMatchDAO.getSchuetzenstatistikMatchWettkampf(wettkampfId, vereinId);
        if (schuetzenstatistikMatchBEList == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, ("No result found for Wettkampf-ID " + wettkampfId) + " and Verein-ID " + vereinId);
        }

        for (int i = 0; i < schuetzenstatistikMatchBEList.size(); i++) {
            returnList.add(i, SchuetzenstatistikMatchMapper.toSchuetzenstatistikMatchDO.apply(schuetzenstatistikMatchBEList.get(i)));
        }
        return returnList;
    }
}
