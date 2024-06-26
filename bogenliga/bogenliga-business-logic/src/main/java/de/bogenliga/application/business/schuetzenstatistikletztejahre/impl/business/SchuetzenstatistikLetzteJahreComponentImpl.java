package de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.business;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.SchuetzenstatistikLetzteJahreComponent;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.api.types.SchuetzenstatistikLetzteJahreDO;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.dao.SchuetzenstatistikLetzteJahreDAO;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.entity.SchuetzenstatistikLetzteJahreBE;
import de.bogenliga.application.business.schuetzenstatistikletztejahre.impl.mapper.SchuetzenstatistikLetzteJahreMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * @author Alessa Hackh
 */

@Component
public class SchuetzenstatistikLetzteJahreComponentImpl implements SchuetzenstatistikLetzteJahreComponent {

    private final SchuetzenstatistikLetzteJahreDAO schuetzenstatistikLetzteJahreDAO;

    private static final String PRECONDITION_VERANSTALTUNGID = "veranstaltungID cannot be null or negative";
    private static final String PRECONDITION_VEREINID = "vereinID cannot be null or negative";
    private static final String PRECONDITION_SPORTJAHR = "sportjahr cannot be null or negative";

    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param schuetzenstatistikLetzteJahreDAO to access the database and return user representations
     */
    @Autowired
    public SchuetzenstatistikLetzteJahreComponentImpl(final SchuetzenstatistikLetzteJahreDAO schuetzenstatistikLetzteJahreDAO) {
        this.schuetzenstatistikLetzteJahreDAO = schuetzenstatistikLetzteJahreDAO;
    }

    @Override
    public List<SchuetzenstatistikLetzteJahreDO> getSchuetzenstatistikLetzteJahre(long sportjahr, long veranstaltungId, long vereinId) {
        Preconditions.checkArgument(veranstaltungId >= 0, PRECONDITION_VERANSTALTUNGID);
        Preconditions.checkArgument(vereinId >= 0, PRECONDITION_VEREINID);
        Preconditions.checkArgument(sportjahr >= 0, PRECONDITION_SPORTJAHR);

        final ArrayList<SchuetzenstatistikLetzteJahreDO> returnList = new ArrayList<>();
        final List<SchuetzenstatistikLetzteJahreBE> schuetzenstatistikLetzteJahreBEList = schuetzenstatistikLetzteJahreDAO.getSchuetzenstatistikLetzteJahre(sportjahr, veranstaltungId, vereinId);

        if (schuetzenstatistikLetzteJahreBEList == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, ("No result found for Sportjahr " + sportjahr + " and Veranstaltungs-ID " + veranstaltungId + " and Verein-ID " + vereinId));
        }

        for (int i = 0; i < schuetzenstatistikLetzteJahreBEList.size(); i++) {
            returnList.add(i, SchuetzenstatistikLetzteJahreMapper.toSchuetzenstatistikLetzteJahreDO.apply(schuetzenstatistikLetzteJahreBEList.get(i)));
        }
        return returnList;
    }
}
