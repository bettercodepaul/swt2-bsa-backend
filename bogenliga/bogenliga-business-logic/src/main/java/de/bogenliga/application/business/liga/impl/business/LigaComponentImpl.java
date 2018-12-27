package de.bogenliga.application.business.liga.impl.business;

import java.util.List;
import java.util.stream.Collectors;

import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.dao.LigaDAO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.liga.impl.mapper.LigaMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;

public class LigaComponentImpl implements LigaComponent {

    private static final String PRECONDITION_MSG_LIGA_ID="ligaId must not be Null";
    private static final String PRECONDITION_MSG_LIGA_NAME="ligaName must not be Null";
    private static final String PRECONDITION_MSG_REGION_ID="ligaRegionId must not be Null";
    private static final String PRECONDITION_MSG_REGION_NAME="ligaRegionName must not be Null";
    private static final String PRECONDITION_MSG_LIGA_UEBERGEORDNET_ID="ligaUebergeordnetId must not be Null";
    private static final String PRECONDITION_MSG_LIGA_UEBERGEORDNET_NAME="ligaUebergeordnetName must not be Null";
    private static final String PRECONDITION_MSG_LIGA_VERANTWORTLICH_ID="ligaVerantwortlichId must not be Null";
    private static final String PRECONDITION_MSG_LIGA_VERANTWORTLICH_MAIL="ligaVerantwortlichMail must not be Null";
    private static final String PRECONDITION_MSG_LIGA_USER_ID="ligaUserId must not be Null";

    private final LigaDAO ligaDAO;

    @Autowired
    public LigaComponentImpl(final LigaDAO ligaDAO){this.ligaDAO= ligaDAO;}

    @Override
    public List<LigaDO> findAll() {
        final List<LigaBE> ligaBEList = ligaDAO.findAll();
        return ligaBEList.stream().map(LigaMapper.toLigaDO).collect(Collectors.toList());
    }


    @Override
    public LigaDO findById(long id) {

        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_LIGA_ID);

        final LigaBE result = ligaDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return LigaMapper.toLigaDO.apply(result);
    }


    @Override
    public LigaDO create(LigaDO ligaDO, long currentLigaId) {
        return null;
    }


    @Override
    public LigaDO update(LigaDO ligaDO, long currentLigaId) {
        return null;
    }


    @Override
    public void delete(LigaDO ligaDO, long currentLigaId) {

    }
}
