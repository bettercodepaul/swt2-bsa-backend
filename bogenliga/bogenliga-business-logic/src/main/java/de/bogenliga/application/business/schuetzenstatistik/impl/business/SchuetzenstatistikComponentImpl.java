package de.bogenliga.application.business.schuetzenstatistik.impl.business;

import de.bogenliga.application.business.schuetzenstatistik.api.SchuetzenstatistikComponent;
import de.bogenliga.application.business.schuetzenstatistik.api.types.SchuetzenstatistikDO;
import de.bogenliga.application.business.schuetzenstatistik.impl.dao.SchuetzenstatistikDAO;
import de.bogenliga.application.business.schuetzenstatistik.impl.entity.LigatabelleBE;
import de.bogenliga.application.business.schuetzenstatistik.impl.mapper.LigatabelleMapper;
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

    private static final String PRECONDITION_MSG_ROLE = "RoleDO must not be null";
    private static final String PRECONDITION_MSG_ROLE_ID = "RoleDO ID must not be negative";
    private static final String PRECONDITION_MSG_ROLE_NAME = "RoleDO name must not be null or empty";
    private static final String USER_ROLE_DEFAULT = "USER";
    private final SchuetzenstatistikDAO schuetzenstatistikDAO;

    private static final String PRECONDITION_VERANSTALTUNGID = "veranstaltungID cannot be null or negative";
    private static final String PRECONDITION_WETTKAMPFID = "wettkampfID cannot be null or negative";


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
    public List<SchuetzenstatistikDO> getLigatabelleVeranstaltung(Long veranstaltungId) {
        Preconditions.checkArgument(veranstaltungId >= 0, PRECONDITION_VERANSTALTUNGID);

        final ArrayList<SchuetzenstatistikDO> returnList = new ArrayList<>();
        final List<LigatabelleBE> ligatabelleBEList = schuetzenstatistikDAO.getSchuetzenstatistikVeranstaltung(veranstaltungId);

        if (ligatabelleBEList == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for Veranstaltungs-ID '%s'", veranstaltungId));
        }

        for (int i = 0; i < ligatabelleBEList.size(); i++) {
            returnList.add(i, LigatabelleMapper.toSchuetzenstatistikDO.apply(ligatabelleBEList.get(i)));
        }
        return returnList;
    }

    @Override
    public List<SchuetzenstatistikDO> getLigatabelleWettkampf(Long wettkampfId) {
        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_WETTKAMPFID);

        final ArrayList<SchuetzenstatistikDO> returnList = new ArrayList<>();
        final List<LigatabelleBE> ligatabelleBEList = schuetzenstatistikDAO.getSchuetzenstatistikWettkampf(wettkampfId);

        if (ligatabelleBEList == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for Wettkampf-ID '%s'", wettkampfId));
        }

        for (int i = 0; i < ligatabelleBEList.size(); i++) {
            returnList.add(i, LigatabelleMapper.toSchuetzenstatistikDO.apply(ligatabelleBEList.get(i)));
        }
        return returnList;
    }


}
