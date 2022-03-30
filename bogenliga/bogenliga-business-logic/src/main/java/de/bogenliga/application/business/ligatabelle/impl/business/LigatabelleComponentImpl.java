package de.bogenliga.application.business.ligatabelle.impl.business;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftSortierungComponent;
import de.bogenliga.application.business.ligatabelle.api.LigatabelleComponent;
import de.bogenliga.application.business.ligatabelle.api.types.LigatabelleDO;
import de.bogenliga.application.business.ligatabelle.impl.dao.LigatabelleDAO;
import de.bogenliga.application.business.ligatabelle.impl.entity.LigatabelleBE;
import de.bogenliga.application.business.ligatabelle.impl.mapper.LigatabelleMapper;

import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link LigatabelleComponent}
 */
@Component
public class LigatabelleComponentImpl implements LigatabelleComponent {

    private final LigatabelleDAO ligatabelleDAO;
    private final DsbMannschaftSortierungComponent mannschaftSortierungComp;

    private static final String PRECONDITION_VERANSTALTUNGID = "veranstaltungID cannot be null or negative";
    private static final String PRECONDITION_WETTKAMPFID = "wettkampfID cannot be null or negative";


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param ligatabelleDAO to access the database and return user representations
     */
    @Autowired
    public LigatabelleComponentImpl(final LigatabelleDAO ligatabelleDAO, final DsbMannschaftSortierungComponent maSortierungComp) {
        this.ligatabelleDAO = ligatabelleDAO;
        this.mannschaftSortierungComp = maSortierungComp;
    }


    @Override
    public List<LigatabelleDO> getLigatabelleVeranstaltung(Long veranstaltungId) {
        Preconditions.checkArgument(veranstaltungId >= 0, PRECONDITION_VERANSTALTUNGID);

        final ArrayList<LigatabelleDO> returnList = new ArrayList<>();
        final List<LigatabelleBE> ligatabelleBEList = ligatabelleDAO.getLigatabelleVeranstaltung(veranstaltungId);

        if (ligatabelleBEList == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for Veranstaltungs-ID '%s'", veranstaltungId));
        }

        for (int i = 0; i < ligatabelleBEList.size(); i++) {
            returnList.add(i, LigatabelleMapper.toLigatabelleDO.apply(ligatabelleBEList.get(i)));
        }
        return returnList;
    }

    @Override
    public List<LigatabelleDO> getLigatabelleWettkampf(Long wettkampfId) {
        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_WETTKAMPFID);

        final ArrayList<LigatabelleDO> returnList = new ArrayList<>();
        final List<LigatabelleBE> ligatabelleBEList = ligatabelleDAO.getLigatabelleWettkampf(wettkampfId);

        if (ligatabelleBEList == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for Wettkampf-ID '%s'", wettkampfId));
        }

        for (int i = 0; i < ligatabelleBEList.size(); i++) {
            returnList.add(i, LigatabelleMapper.toLigatabelleDO.apply(ligatabelleBEList.get(i)));
        }
        return returnList;
    }


}
