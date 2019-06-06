package de.bogenliga.application.business.sportjahr.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.sportjahr.api.SportjahrComponent;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.business.sportjahr.impl.dao.SportjahrDAO;
import de.bogenliga.application.business.sportjahr.impl.entity.SportjahrBE;
import de.bogenliga.application.business.sportjahr.impl.mapper.SportjahrMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link SportjahrComponent}
 *
 * @author Marcel Schneider
 */
@Component
public class SportjahrComponentImpl implements SportjahrComponent {

    private static final String PRECONDITION_MSG_SPORTJAHR_ID = "sportjahrID must not be null and must not be negative";
    private static final String PRECONDITION_MSG_SPORTJAHR_NAME = "sportjahrName must not be null";


    private final SportjahrDAO sportjahrDAO;


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param sportjahrDAO to access the database and return dsbmitglied representations
     */
    @Autowired
    public SportjahrComponentImpl(final SportjahrDAO sportjahrDAO) {
        this.sportjahrDAO = sportjahrDAO;
        System.out.println("created DAO object");
    }


    @Override
    public List<SportjahrDO> findAll() {
        final List<SportjahrBE> sportjahrBEList = sportjahrDAO.findAll();
        return sportjahrBEList.stream().map(SportjahrMapper.toSportjahrDO).collect(Collectors.toList());
    }


    @Override
    public SportjahrDO findById(final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_SPORTJAHR_ID);

        final SportjahrBE result = sportjahrDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return SportjahrMapper.toSportjahrDO.apply(result);
    }


    @Override
    public SportjahrDO create(final SportjahrDO sportjahrDO, final long currentSportjahrID) {
        checkDsbMitgliedDO(sportjahrDO, currentSportjahrID);

        final SportjahrBE sportjahrBE = SportjahrMapper.toSportjahrBE.apply(sportjahrDO);
        final SportjahrBE persistedSportjahrBE = sportjahrDAO.create(sportjahrBE, currentSportjahrID);

        return SportjahrMapper.toSportjahrDO.apply(persistedSportjahrBE);
    }


    @Override
    public SportjahrDO update(final SportjahrDO sportjahrDO, final long currentSportjahrID) {
        checkDsbMitgliedDO(sportjahrDO, currentSportjahrID);
        Preconditions.checkArgument(sportjahrDO.getId() >= 0, PRECONDITION_MSG_SPORTJAHR_ID);

        final SportjahrBE sportjahrBE = SportjahrMapper.toSportjahrBE.apply(sportjahrDO);
        final SportjahrBE persistedSportjahrBE = sportjahrDAO.update(sportjahrBE, currentSportjahrID);

        return SportjahrMapper.toSportjahrDO.apply(persistedSportjahrBE);
    }


    @Override
    public void delete(final SportjahrDO sportjahrDO, final long currentSportjahrID) {
        Preconditions.checkNotNull(sportjahrDO, PRECONDITION_MSG_SPORTJAHR_ID);
        Preconditions.checkArgument(sportjahrDO.getId() >= 0, PRECONDITION_MSG_SPORTJAHR_ID);
        Preconditions.checkArgument(currentSportjahrID >= 0, PRECONDITION_MSG_SPORTJAHR_ID);

        final SportjahrBE sportjahrBE = SportjahrMapper.toSportjahrBE.apply(sportjahrDO);

        sportjahrDAO.delete(sportjahrBE, currentSportjahrID);

    }


    private void checkDsbMitgliedDO(final SportjahrDO sportjahrDO, final long currentSportjahrID) {
        Preconditions.checkNotNull(sportjahrDO, PRECONDITION_MSG_SPORTJAHR_ID);
        Preconditions.checkArgument(currentSportjahrID >= 0, PRECONDITION_MSG_SPORTJAHR_ID);
        Preconditions.checkNotNull(sportjahrDO.getName(), PRECONDITION_MSG_SPORTJAHR_NAME);
    }

}
