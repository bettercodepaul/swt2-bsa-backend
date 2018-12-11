package de.bogenliga.application.services.v1.user.mapper;

import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.services.v1.user.model.UserProfileDTO;
import org.junit.Test;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class UserProfileDTOMapperTest {
    private static final long ID = 1337;
    private static final String EMAIL = "Sorscha.Kratikoff@test.de";
    private static final String VORNAME = "Sorscha";
    private static final String NACHNAME = "Kratikoff";
    private static final Date GEBURTSDATUM = Date.valueOf("1990-01-02");
    private static final String NATIONALITAET = "DE";
    private static final String MITGLIEDSNUMMER = "223344uu";
    private static final long VEREINSID = 2;

    private UserProfileDO getDO() {
        UserProfileDO userProfileDO = new UserProfileDO();
        userProfileDO.setId(ID);
        userProfileDO.setEmail(EMAIL);
        userProfileDO.setVorname(VORNAME);
        userProfileDO.setNachname(NACHNAME);
        userProfileDO.setGeburtsdatum(GEBURTSDATUM);
        userProfileDO.setNationalitaet(NATIONALITAET);
        userProfileDO.setMitgliedsnummer(MITGLIEDSNUMMER);
        userProfileDO.setVereinsId(VEREINSID);
        return userProfileDO;
    }

    private UserProfileDTO getDTO() {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(ID);
        userProfileDTO.setEmail(EMAIL);
        userProfileDTO.setVorname(VORNAME);
        userProfileDTO.setNachname(NACHNAME);
        userProfileDTO.setGeburtsdatum(GEBURTSDATUM);
        userProfileDTO.setNationalitaet(NATIONALITAET);
        userProfileDTO.setMitgliedsnummer(MITGLIEDSNUMMER);
        userProfileDTO.setVereinsId(VEREINSID);
        return userProfileDTO;
    }

    @Test
    public void toDO() {
        UserProfileDTO userProfileDTO = getDTO();
        final UserProfileDO actual = UserProfileDTOMapper.toDO.apply(userProfileDTO);
        assertThat(actual.getVorname()).isEqualTo(VORNAME);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
        assertThat(actual.equals(getDO()));
    }

    @Test
    public void toDTO() {
        UserProfileDO userProfileDO = getDO();
        final UserProfileDTO actual = UserProfileDTOMapper.toDTO.apply(userProfileDO);
        assertThat(actual.getVorname()).isEqualTo(VORNAME);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
        assertThat(actual.equals(getDTO()));
    }
}
