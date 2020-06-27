package de.bogenliga.application.business.user.impl.business;

import de.bogenliga.application.business.dsbmitglied.impl.dao.DsbMitgliedDAO;
import de.bogenliga.application.business.dsbmitglied.impl.entity.DsbMitgliedBE;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.UserProfileComponent;
import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.user.impl.mapper.UserMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link UserComponent}
 */
@Component
public class UserProfileComponentImpl implements UserProfileComponent {

    private static final String PRECONDITION_MSG_USER_ID = "UserDO ID must not be negative";
    private final UserDAO userDAO;
    private final DsbMitgliedDAO dsbMitgliedDAO;


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param userDAO         to access the database and return user representations
     * @param dsbMitgliedDAO  to access the database and return DSB Mitglied representations
     */
    @Autowired
    public UserProfileComponentImpl(final UserDAO userDAO,
                                    final DsbMitgliedDAO dsbMitgliedDAO) {
        this.userDAO = userDAO;
        this.dsbMitgliedDAO = dsbMitgliedDAO;
    }


    @Override
    public UserProfileDO findById(final Long id) {
        Preconditions.checkNotNull(id, PRECONDITION_MSG_USER_ID);
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_USER_ID);
        UserProfileDO userProfileDO = new UserProfileDO();

        final UserBE userBE = userDAO.findById(id); // required for email adress
        if (userBE == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }
        final DsbMitgliedBE dsbMitgliedBE = dsbMitgliedDAO.findById(userBE.getDsb_mitglied_id());    // required for remaining profile data
        if (dsbMitgliedBE == null) {
            userProfileDO.setEmail(userBE.getUserEmail());
        } else {
            userProfileDO = UserMapper.toUserProfileDO.apply(userBE, dsbMitgliedBE);
        }


        return userProfileDO;
    }

}
