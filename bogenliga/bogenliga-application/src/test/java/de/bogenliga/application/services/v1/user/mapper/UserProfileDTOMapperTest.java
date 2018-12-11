package de.bogenliga.application.services.v1.user.mapper;

import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.services.v1.user.model.UserProfileDTO;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserProfileDTOMapperTest {
    private static final long ID = 1337;
    private static final String VORNAME = "Sorscha";
    private static final String NACHNAME = "Kratikoff";
    private static final String GEBURTSDATUM = "1.9.1991";
    private static final String NATIONALITAET = "DE";
    private static final String MITGLIEDSNUMMER = "223344uu";
    private static final long VEREINSID = 2;
    private static final long USERID = 4242;
    private static final String EMAIL = "Sorscha.Kratikoff@test.de";

    private UserProfileDO getDO() {
        UserProfileDO userProfileDO = new UserProfileDO();
        userProfileDO.setVorname(VORNAME);
        userProfileDO.setEmail(EMAIL);
        return userProfileDO;
    }

    private UserProfileDTO getDTO() {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setVorname(VORNAME);
        userProfileDTO.setEmail(EMAIL);
        return userProfileDTO;
    }

    @Test
    public void toDO() {
        UserProfileDTO userProfileDTO = getDTO();
        final UserProfileDO actual = UserProfileDTOMapper.toDO.apply(userProfileDTO);


        assertThat(actual.getVorname()).isEqualTo(VORNAME);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    public void toDTO() {
        UserProfileDO userProfileDO = getDO();
        final UserProfileDTO actual = UserProfileDTOMapper.toDTO.apply(userProfileDO);


        assertThat(actual.getVorname()).isEqualTo(VORNAME);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
    }


}
