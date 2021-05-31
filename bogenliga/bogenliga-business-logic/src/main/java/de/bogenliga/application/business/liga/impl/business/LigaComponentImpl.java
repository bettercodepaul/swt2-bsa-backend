package de.bogenliga.application.business.liga.impl.business;

import java.util.ArrayList;
import java.util.List;

import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.dao.LigaDAO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.liga.impl.mapper.LigaMapper;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.regionen.api.types.RegionenDO;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
public class LigaComponentImpl implements LigaComponent {

    private static final String PRECONDITION_MSG_LIGA = "ligaDO must not be Null";
    private static final String PRECONDITION_MSG_LIGA_ID = "ligaId must not be Null";
    private static final String PRECONDITION_MSG_LIGA_NAME = "ligaName must not be Null";
    private static final String PRECONDITION_MSG_REGION_ID = "ligaRegionId must not be Null";
    private static final String PRECONDITION_MSG_LIGA_UEBERGEORDNET_ID = "ligaUebergeordnetId must not be Null";
    private static final String PRECONDITION_MSG_LIGA_VERANTWORTLICH_ID = "ligaVerantwortlichId must not be Null";
    private static final String PRECONDITION_MSG_CURRENT_LIGA_ID = "Current liga id must not be Null";

    private final LigaDAO ligaDAO;
    private final RegionenComponent regionenComp;
    private final UserComponent userComp;


    @Autowired
    public LigaComponentImpl(final LigaDAO ligaDAO, @Lazy final RegionenComponent regionenComp, @Lazy final UserComponent userComp) {
        this.ligaDAO = ligaDAO;
        this.regionenComp = regionenComp;
        this.userComp = userComp;
    }


    @Override
    public List<LigaDO> findAll() {
        final ArrayList<LigaDO> returnList = new ArrayList<>();
        final List<LigaBE> ligaBEList = ligaDAO.findAll();

        for (int i = 0; i < ligaBEList.size(); i++) {

            returnList.add(i, completeLiga(ligaBEList.get(i)));

        }
        return returnList;
    }


    @Override
    public LigaDO findById(long id) {

        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_LIGA_ID);

        final LigaBE result = ligaDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return completeLiga(result);
    }


    @Override
    public LigaDO create(LigaDO ligaDO, long currentDsbMitgliedId) {

        checkLigaDO(ligaDO, currentDsbMitgliedId);
        final LigaBE ligaBE = LigaMapper.toLigaBE.apply(ligaDO);
        final LigaBE persistedLigaBE = ligaDAO.create(ligaBE, currentDsbMitgliedId);
        return completeLiga(persistedLigaBE);
    }


    @Override
    public LigaDO update(LigaDO ligaDO, long currentDsbMitgliedId) {
        checkLigaDO(ligaDO, currentDsbMitgliedId);
        Preconditions.checkArgument(ligaDO.getId() >= 0, PRECONDITION_MSG_LIGA_ID);

        final LigaBE ligaBE = LigaMapper.toLigaBE.apply(ligaDO);
        final LigaBE persistedLigaBE = ligaDAO.update(ligaBE, currentDsbMitgliedId);

        return completeLiga(persistedLigaBE);
    }


    @Override
    public void delete(LigaDO ligaDO, long currentDsbMitgliedId) {
        Preconditions.checkNotNull(ligaDO, PRECONDITION_MSG_LIGA);
        Preconditions.checkArgument(ligaDO.getId() >= 0, PRECONDITION_MSG_LIGA_ID);
        Preconditions.checkArgument(currentDsbMitgliedId >= 0, PRECONDITION_MSG_CURRENT_LIGA_ID);

        final LigaBE ligaBE = LigaMapper.toLigaBE.apply(ligaDO);

        ligaDAO.delete(ligaBE, currentDsbMitgliedId);
    }


    private void checkLigaDO(final LigaDO ligaDO, final long currentDsbMitgliedId) {
        Preconditions.checkNotNull(ligaDO, PRECONDITION_MSG_LIGA);
        Preconditions.checkArgument(currentDsbMitgliedId >= 0, PRECONDITION_MSG_LIGA_ID);
        Preconditions.checkNotNull(ligaDO.getName(), PRECONDITION_MSG_LIGA_NAME);
        Preconditions.checkArgument(ligaDO.getRegionId() >= 0, PRECONDITION_MSG_REGION_ID);

        if (ligaDO.getLigaUebergeordnetId() != null) {
            Preconditions.checkArgument(ligaDO.getLigaUebergeordnetId() >= 0, PRECONDITION_MSG_LIGA_UEBERGEORDNET_ID);
        }

        if (ligaDO.getLigaVerantwortlichId() != null) {
            Preconditions.checkArgument(ligaDO.getLigaVerantwortlichId() >= 0, PRECONDITION_MSG_LIGA_VERANTWORTLICH_ID);
        }
    }


    private LigaDO completeLiga(LigaBE ligaBE) {
        LigaBE tempLigaBE = new LigaBE();
        RegionenDO tempRegionenDO = new RegionenDO(0L);
        UserDO tempUserDO = new UserDO();

        if (ligaBE.getLigaUebergeordnetId() != null) {
            tempLigaBE = ligaDAO.findById(ligaBE.getLigaUebergeordnetId());
        }
        if (ligaBE.getLigaRegionId() != null) {
            tempRegionenDO = regionenComp.findById(ligaBE.getLigaRegionId());
        }
        if(ligaBE.getLigaVerantwortlichId() != null) {
            tempUserDO = userComp.findById(ligaBE.getLigaVerantwortlichId());
        }

        return LigaMapper.toLigaDO(ligaBE, tempLigaBE, tempRegionenDO, tempUserDO);

    }
}