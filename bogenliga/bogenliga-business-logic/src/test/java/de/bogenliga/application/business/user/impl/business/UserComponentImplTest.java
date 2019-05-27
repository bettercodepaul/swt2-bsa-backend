package de.bogenliga.application.business.user.impl.business;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.impl.businessactivity.SignInBA;
import de.bogenliga.application.business.user.impl.businessactivity.TechnicalUserBA;
import de.bogenliga.application.business.user.impl.businessactivity.PasswordHashingBA;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class UserComponentImplTest {
    private static final Long ID = 777L;
    private static final Long VERSION = 2L;
    private static final Long NEWVERSION = 3L;
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String NEWPASSWORD = "newpassword";
    private static final String SALT = "salt";
    private static final String PWDHASH = "pwdhash";
    private static final String NEWPWDHASH = "newpwdhash";
    private static final Long USER = 1L;
    private static final UserDO SYSTEM_USER = new UserDO(0L, "SYSTEM", false, null, null, 0L, null, 0L, 0L);


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UserDAO userDAO;
    @Mock
    private SignInBA signInBA;
    @Mock
    private TechnicalUserBA technicalUserBA;
    @Mock
    private PasswordHashingBA passwordHashingBA;

    @InjectMocks
    private UserComponentImpl underTest;

    @Captor
    private ArgumentCaptor<UserBE> userBEArgumentCaptor;


    @Test
    public void findAll() {
        // prepare test data
        final UserBE expectedBE = new UserBE();
        expectedBE.setUserId(ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setUsing2FA(false);

        expectedBE.setVersion(VERSION);

        // configure mocks
        when(userDAO.findAll()).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<UserDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.get(0).getEmail())
                .isEqualTo(expectedBE.getUserEmail());
        assertThat(actual.get(0).getVersion())
                .isEqualTo(expectedBE.getVersion());


        // verify invocations
        verify(userDAO).findAll();


    }


    @Test
    public void findById() {
        // prepare test data
        final UserBE expectedBE = new UserBE();
        expectedBE.setUserId(ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setUsing2FA(false);
        expectedBE.setVersion(VERSION);


        // configure mocks
        when(userDAO.findById(anyLong())).thenReturn(expectedBE);

        // call test method
        final UserDO actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getEmail())
                .isEqualTo(expectedBE.getUserEmail());
        assertThat(actual.getVersion())
                .isEqualTo(expectedBE.getVersion());

        // verify invocations
        verify(userDAO).findById(ID);
    }


    @Test
    public void findById_withoutId_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findById(null))
                .withMessageContaining("must not be negative")
                .withNoCause();

        // assert result

        // verify invocations
        verify(userDAO, never()).findById(anyLong());
    }


    @Test
    public void findById_withInvalidId_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findById(-1L))
                .withMessageContaining("must not be negative")
                .withNoCause();

        // assert result

        // verify invocations
        verify(userDAO, never()).findById(anyLong());
    }


    @Test
    public void findById_withTechnicalUserId() {
        // prepare test data
        final UserDO userDO = new UserDO();
        userDO.setEmail(EMAIL);
        userDO.setId(ID);
        userDO.setUsing2FA(false);

        // configure mocks
        when(technicalUserBA.getSystemUser()).thenReturn(userDO);

        // call test method
        final UserDO actual = underTest.findById(0L);

        // assert result
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);

        // verify invocations
        verify(userDAO, never()).findById(anyLong());
        verify(technicalUserBA).getSystemUser();
    }


    @Test
    public void findById_notResult() {
        // prepare test data

        // configure mocks
        when(userDAO.findById(anyLong())).thenReturn(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findById(ID))
                .withMessageContaining("NOT_FOUND")
                .withNoCause();

        // assert result


        // verify invocations
        verify(userDAO).findById(ID);
    }


    @Test
    public void findByEmail() {
        // prepare test data
        final UserBE expectedBE = new UserBE();
        expectedBE.setUserId(ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setUsing2FA(false);
        expectedBE.setVersion(VERSION);


        // configure mocks
        when(userDAO.findByEmail(anyString())).thenReturn(expectedBE);

        // call test method
        final UserDO actual = underTest.findByEmail(EMAIL);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getEmail())
                .isEqualTo(expectedBE.getUserEmail());
        assertThat(actual.getVersion())
                .isEqualTo(expectedBE.getVersion());

        // verify invocations
        verify(userDAO).findByEmail(EMAIL);
    }


    @Test
    public void signIn() {
        // prepare test data

        // configure mocks

        // call test method

        // assert result

        // verify invocations
    }


    @Test
    public void signInUser_withoutEmail_shouldThrowException() {
        // prepare test data
        // configure mocks
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.signIn(null, PASSWORD))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verify(signInBA, never()).signInUser(any(), any());
    }


    @Test
    public void signInUser_withEmptyEmail_shouldThrowException() {
        // prepare test data
        // configure mocks
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.signIn("", PASSWORD))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verify(signInBA, never()).signInUser(any(), any());
    }


    @Test
    public void signInUser_withoutPassword_shouldThrowException() {
        // prepare test data
        // configure mocks
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.signIn(EMAIL, null))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verify(signInBA, never()).signInUser(any(), any());
    }


    @Test
    public void signInUser_withEmptyPassword_shouldThrowException() {
        // prepare test data
        // configure mocks
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.signIn(EMAIL, ""))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verify(signInBA, never()).signInUser(any(), any());
    }


    @Test
    public void create_UserEmailnotNull() {

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create("", PASSWORD, ID, false))
                .withMessageContaining("must not be null")
                .withNoCause();

    }


    @Test
    public void create_UserPasswordnotNull() {

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(EMAIL, "", ID, false))
                .withMessageContaining("must not be null")
                .withNoCause();

    }


    @Test
    public void create_UserIDnotNull() {

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(EMAIL, PASSWORD, null, false))
                .withMessageContaining("must not be null")
                .withNoCause();

    }


    @Test
    public void create_sucessful() {

        final OffsetDateTime dateTime = OffsetDateTime.now();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final UserBE expectedBE = new UserBE();
        expectedBE.setUserId(ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setUsing2FA(false);
        expectedBE.setVersion(VERSION);
        expectedBE.setCreatedAtUtc(timestamp);
        expectedBE.setLastModifiedAtUtc(timestamp);
        expectedBE.setCreatedByUserId(USER);
        expectedBE.setLastModifiedByUserId(USER);

        // configure mocks
        when(passwordHashingBA.generateSalt()).thenReturn(SALT);
        when(passwordHashingBA.calculateHash(anyString(), anyString())).thenReturn(PWDHASH);
        when(userDAO.create(any(UserBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final UserDO actual = underTest.create(EMAIL, PASSWORD, USER, false);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getEmail())
                .isEqualTo(expectedBE.getUserEmail());
        assertThat(actual.getVersion())
                .isEqualTo(expectedBE.getVersion());

        // verify invocations
        verify(userDAO).create(userBEArgumentCaptor.capture(), anyLong());

    }


    @Test
    public void update_UserDO_notNull() {

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(null, PASSWORD, NEWPASSWORD, ID))
                .withMessageContaining("must not be null")
                .withNoCause();

    }


    @Test
    public void update_UserDO_ID_notNull() {


        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(SYSTEM_USER, PASSWORD, NEWPASSWORD, ID))
                .withMessageContaining("must not be null")
                .withNoCause();

    }


    @Test
    public void update_Password_notNull() {

        final UserDO inUserDO = new UserDO();
        inUserDO.setId(ID);
        inUserDO.setEmail(EMAIL);


        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(inUserDO, "", NEWPASSWORD, ID))
                .withMessageContaining("must not be null")
                .withNoCause();

    }


    @Test
    public void update_NewPassword_notNull() {

        final UserDO inUserDO = new UserDO();
        inUserDO.setId(ID);
        inUserDO.setEmail(EMAIL);


        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(inUserDO, PASSWORD, "", ID))
                .withMessageContaining("must not be null")
                .withNoCause();

    }


    @Test
    public void update_CurrentUserId_notNull() {

        final UserDO inUserDO = new UserDO();
        inUserDO.setId(ID);
        inUserDO.setEmail(EMAIL);


        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(inUserDO, PASSWORD, NEWPASSWORD, 0L))
                .withMessageContaining("must not be null")
                .withNoCause();

    }


    @Test
    public void update_wrong_password() {

        final OffsetDateTime dateTime = OffsetDateTime.now();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final UserBE inputBE = new UserBE();
        inputBE.setUserId(ID);
        inputBE.setUserPassword(PWDHASH);
        inputBE.setUserSalt(SALT);
        inputBE.setUserEmail(EMAIL);
        inputBE.setUsing2FA(false);
        inputBE.setVersion(VERSION);
        inputBE.setCreatedAtUtc(timestamp);
        inputBE.setLastModifiedAtUtc(timestamp);
        inputBE.setCreatedByUserId(USER);
        inputBE.setLastModifiedByUserId(USER);

        final UserBE expectedBE = new UserBE();
        expectedBE.setUserId(ID);
        expectedBE.setUserPassword(PWDHASH);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setUsing2FA(false);
        expectedBE.setVersion(NEWVERSION);
        expectedBE.setCreatedAtUtc(timestamp);
        expectedBE.setLastModifiedAtUtc(timestamp);
        expectedBE.setCreatedByUserId(USER);
        expectedBE.setLastModifiedByUserId(USER);

        final UserDO inUserDO = new UserDO();
        inUserDO.setId(ID);
        inUserDO.setEmail(EMAIL);


        // configure mocks
        when(passwordHashingBA.generateSalt()).thenReturn(SALT);
        when(passwordHashingBA.calculateHash(anyString(), anyString())).thenReturn(NEWPWDHASH);
        when(userDAO.findById(anyLong())).thenReturn(inputBE);
        when(userDAO.update(any(UserBE.class), anyLong())).thenReturn(expectedBE);


        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(inUserDO, PASSWORD, NEWPASSWORD, USER))
                .withMessageContaining("password incorrect")
                .withNoCause();

    }


    @Test
    public void update_sucessful() {

        final OffsetDateTime dateTime = OffsetDateTime.now();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final UserBE inputBE = new UserBE();
        inputBE.setUserId(ID);
        inputBE.setUserPassword(PWDHASH);
        inputBE.setUserSalt(SALT);
        inputBE.setUserEmail(EMAIL);
        inputBE.setVersion(VERSION);
        inputBE.setCreatedAtUtc(timestamp);
        inputBE.setLastModifiedAtUtc(timestamp);
        inputBE.setCreatedByUserId(USER);
        inputBE.setLastModifiedByUserId(USER);

        final UserBE expectedBE = new UserBE();
        expectedBE.setUserId(ID);
        expectedBE.setUserPassword(PWDHASH);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setVersion(NEWVERSION);
        expectedBE.setCreatedAtUtc(timestamp);
        expectedBE.setLastModifiedAtUtc(timestamp);
        expectedBE.setCreatedByUserId(USER);
        expectedBE.setLastModifiedByUserId(USER);

        final UserDO inUserDO = new UserDO();
        inUserDO.setId(ID);
        inUserDO.setEmail(EMAIL);


        // configure mocks
        when(passwordHashingBA.generateSalt()).thenReturn(SALT);
        when(passwordHashingBA.calculateHash(anyString(), anyString())).thenReturn(PWDHASH);
        when(userDAO.findById(anyLong())).thenReturn(inputBE);
        when(userDAO.update(any(UserBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final UserDO actual = underTest.update(inUserDO, PASSWORD, NEWPASSWORD, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getEmail())
                .isEqualTo(expectedBE.getUserEmail());
        assertThat(actual.getVersion())
                .isEqualTo(expectedBE.getVersion());

    }


    @Test
    public void isTechnicalUser() {
        // prepare test data
        // configure mocks

        // call test method
        final boolean actual = underTest.isTechnicalUser(SYSTEM_USER);

        // assert result
        assertThat(actual).isFalse();

        // verify invocations
    }
}