package de.bogenliga.application.business.user.impl.mapper;

import de.bogenliga.application.business.user.api.types.UserVO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.common.component.mapping.ValueObjectMapper;

import java.util.function.Function;

public class UserMapper implements ValueObjectMapper {
    public static final Function<UserBE, UserVO> toVO = be -> {

        final int id = be.getUserId();
        final String email = be.getUserEmail();
        final String salt = be.getUserSalt();
        final String password = be.getUserPassword();

        return new UserVO(id, email, salt, password);
    };

    public static final Function<UserVO, UserBE> toBE = vo -> {

        final int id = vo.getId();
        final String email = vo.getEmail();
        final String salt = vo.getSalt();
        final String password = vo.getPassword();

        UserBE userBE = new UserBE();
        userBE.setUserId(id);
        userBE.setUserEmail(email);
        userBE.setUserSalt(salt);
        userBE.setUserPassword(password);

        return userBE;
    };


    /**
     * Private constructor
     */
    private UserMapper() {
        // empty private constructor
    }
}
