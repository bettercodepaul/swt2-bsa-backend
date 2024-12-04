package de.bogenliga.application.business.liga.impl.business;


import java.util.List;

import de.bogenliga.application.business.disziplin.api.DisziplinComponent;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.dao.LigaDAO;
import de.bogenliga.application.business.liga.impl.dao.LigaDAOext;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.liga.impl.entity.LigaBEext;
import de.bogenliga.application.business.liga.impl.mapper.LigaMapper;
import de.bogenliga.application.business.regionen.api.RegionenComponent;
import de.bogenliga.application.business.user.api.UserComponent;
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
    private final LigaDAOext ligaExtendedDAO;


    @Autowired
    public LigaComponentImpl(final LigaDAO ligaDAO, @Lazy final RegionenComponent regionenComp, @Lazy final UserComponent userComp, @Lazy final DisziplinComponent disziplinComp,
                             LigaDAOext ligaExtendedDAO) {
        this.ligaDAO = ligaDAO;
        this.ligaExtendedDAO = ligaExtendedDAO;
    }

    @Override
    public List<LigaDO> findAll() {
        final List<LigaBEext> ligaExtendedBEList = ligaExtendedDAO.findEverything();

        return ligaExtendedBEList.stream()
                .map(LigaMapper::mapToLigaDO)
                .toList();
    }

    @Override
    public List<LigaDO> findBySearch(final String searchTerm){
        final List<LigaBEext> ligaExtendedBEList = ligaExtendedDAO.findBySearch(searchTerm);

        return ligaExtendedBEList.stream()
                .map(LigaMapper::mapToLigaDO)
                .toList();
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
    public LigaDO findByLowest(long id){

        Preconditions.checkArgument(id>=0,PRECONDITION_MSG_LIGA_ID);
        final LigaBE lowestLeague = ligaDAO.findByLowest(id);
        LigaDO emptyLiga = new LigaDO();
        if(lowestLeague == null){
            return emptyLiga;
        }
        return completeLiga(lowestLeague);
    }

    @Override
    public LigaDO checkExist(long id) {

        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_LIGA_ID);

        final LigaBE result = ligaDAO.findById(id);
        LigaDO emptyLiga = new LigaDO();

        if (result == null) {
            return emptyLiga;
        }

        return completeLiga(result);
    }

    @Override
    public LigaDO checkExistsLigaName(String ligaName) {

        final LigaBE result = ligaDAO.findByLigaName(ligaName);
        LigaDO emptyLiga = new LigaDO();

        if (result == null) {
            return emptyLiga;
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
        LigaBEext additionalData = ligaExtendedDAO.findAdditionalDataByLigaId(ligaBE.getLigaId());

        if (additionalData == null) {
            additionalData = new LigaBEext();
        }

        return LigaMapper.toLigaDO(ligaBE, additionalData);
    }
}