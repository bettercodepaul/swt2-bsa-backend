package de.bogenliga.application.business.user.impl.businessactivity;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.dao.UserLoginHistoryDAO;
import de.bogenliga.application.business.user.impl.dao.UserPermissionDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.user.impl.entity.UserFailedSignInAttemptsBE;
import de.bogenliga.application.business.user.impl.entity.UserPermissionBE;
import de.bogenliga.application.business.user.impl.entity.UserSignInHistoryBE;
import de.bogenliga.application.business.user.impl.types.SignInResult;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.time.DateProvider;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class SignInBATest {

    private static final long USER_ID = 123;
    private static final long OTHER_USER_ID = 321;
    private static final long VERSION = 1;
    private static final String EMAIL = "email";
    private static final String SALT = "salt";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_HASH = "password_hashed";
    private static final Timestamp TIMESTAMP = DateProvider.currentTimestampUtc();
    private static final String PERMISSION_1 = "permission1";


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UserDAO userDAO;
    @Mock
    private UserPermissionDAO userPermissionDAO;
    @Mock
    private UserLoginHistoryDAO userLoginHistoryDAO;
    @Mock
    private PasswordHashingBA passwordHashingBA;

    @InjectMocks
    private SignInBA underTest;

    @Captor
    private ArgumentCaptor<UserSignInHistoryBE> userSignInHistoryBEArgumentCaptor;


    @Test
    public void signInUser() {
        // prepare test data
        final UserBE userBE = createUserBE();
        final UserFailedSignInAttemptsBE userFailedSignInAttemptsBE = createUserFailedSignInAttemptsBE(
                userBE.getUserId(), 0);
        final List<UserPermissionBE> userPermissionBEList = Collections.singletonList(
                createUserPermissionBE(userBE.getUserId(), PERMISSION_1));

        // configure mocks
        when(userDAO.findByEmail(any())).thenReturn(userBE);
        when(userLoginHistoryDAO.findSignInUserInformationByEmail(anyLong(), anyLong())).thenReturn(
                userFailedSignInAttemptsBE);
        when(passwordHashingBA.calculateHash(any(), any())).thenReturn(PASSWORD_HASH);
        when(userPermissionDAO.findByUserId(anyLong())).thenReturn(userPermissionBEList);

        // call test method
        final UserWithPermissionsDO actual = underTest.signInUser(EMAIL, PASSWORD);

        // assert result
        assertThat(actual.getId()).isEqualTo(userBE.getUserId());
        assertThat(actual.getEmail()).isEqualTo(userBE.getUserEmail());
        assertThat(actual.getVersion()).isEqualTo(userBE.getVersion());
        assertThat(actual.getCreatedAtUtc()).isEqualTo(DateProvider.convertTimestamp(userBE.getCreatedAtUtc()));
        assertThat(actual.getCreatedByUserId()).isEqualTo(userBE.getCreatedByUserId());
        assertThat(actual.getLastModifiedAtUtc()).isEqualTo(
                DateProvider.convertTimestamp(userBE.getLastModifiedAtUtc()));
        assertThat(actual.getLastModifiedByUserId()).isEqualTo(userBE.getLastModifiedByUserId());
        assertThat(actual.getPermissions()).contains(PERMISSION_1);

        // verify invocations
        verify(userDAO).findByEmail(EMAIL);
        verify(userLoginHistoryDAO).findSignInUserInformationByEmail(userBE.getUserId(), 5 * 60);
        verify(passwordHashingBA).calculateHash(PASSWORD, SALT);
        verify(userPermissionDAO).findByUserId(userBE.getUserId());
        verify(userLoginHistoryDAO).create(userSignInHistoryBEArgumentCaptor.capture());

        assertThat(userSignInHistoryBEArgumentCaptor.getValue().getSignInResult())
                .isEqualTo(SignInResult.LOGIN_SUCCESS);
    }


    @Test
    public void signInUser_withTooManyFailedLoginAttempts_shouldThrowException() {
        // prepare test data
        final UserBE userBE = createUserBE();
        final UserFailedSignInAttemptsBE userFailedSignInAttemptsBE = createUserFailedSignInAttemptsBE(
                userBE.getUserId(), 16);
        final List<UserPermissionBE> userPermissionBEList = Collections.singletonList(
                createUserPermissionBE(userBE.getUserId(), PERMISSION_1));

        // configure mocks
        when(userDAO.findByEmail(any())).thenReturn(userBE);
        when(userLoginHistoryDAO.findSignInUserInformationByEmail(anyLong(), anyLong())).thenReturn(
                userFailedSignInAttemptsBE);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.signInUser(EMAIL, PASSWORD))
                .withMessageContaining("blocked")
                .withNoCause();

        // assert result

        // verify invocations
        verify(userDAO).findByEmail(EMAIL);
        verify(userLoginHistoryDAO).findSignInUserInformationByEmail(userBE.getUserId(), 5 * 60);
        verify(passwordHashingBA, never()).calculateHash(any(), any());
        verify(userPermissionDAO, never()).findByUserId(anyLong());
        verify(userLoginHistoryDAO).create(userSignInHistoryBEArgumentCaptor.capture());

        assertThat(userSignInHistoryBEArgumentCaptor.getValue().getSignInResult())
                .isEqualTo(SignInResult.LOGIN_FAILED);
    }


    @Test
    public void signInUser_withWrongPassword_shouldThrowException() {
        // prepare test data
        final UserBE userBE = createUserBE();
        final UserFailedSignInAttemptsBE userFailedSignInAttemptsBE = createUserFailedSignInAttemptsBE(
                userBE.getUserId(), 0);
        final List<UserPermissionBE> userPermissionBEList = Collections.singletonList(
                createUserPermissionBE(userBE.getUserId(), PERMISSION_1));

        // configure mocks
        when(userDAO.findByEmail(any())).thenReturn(userBE);
        when(userLoginHistoryDAO.findSignInUserInformationByEmail(anyLong(), anyLong())).thenReturn(
                userFailedSignInAttemptsBE);
        when(passwordHashingBA.calculateHash(any(), any())).thenReturn("XXXX");

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.signInUser(EMAIL, PASSWORD))
                .withMessageContaining("credentials")
                .withNoCause();

        // assert result

        // verify invocations
        verify(userDAO).findByEmail(EMAIL);
        verify(userLoginHistoryDAO).findSignInUserInformationByEmail(userBE.getUserId(), 5 * 60);
        verify(passwordHashingBA).calculateHash(PASSWORD, SALT);
        verify(userPermissionDAO, never()).findByUserId(anyLong());
        verify(userLoginHistoryDAO).create(userSignInHistoryBEArgumentCaptor.capture());

        assertThat(userSignInHistoryBEArgumentCaptor.getValue().getSignInResult())
                .isEqualTo(SignInResult.LOGIN_FAILED);
    }


    @Test
    public void signInUser_withoutExistingUser_shouldThrowException() {
        // prepare test data

        // configure mocks
        when(userDAO.findByEmail(EMAIL)).thenReturn(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.signInUser(EMAIL, PASSWORD))
                .withMessageContaining("credentials")
                .withNoCause();

        // assert result

        // verify invocations
        verify(userDAO).findByEmail(EMAIL);
        verify(userLoginHistoryDAO, never()).findSignInUserInformationByEmail(anyLong(), anyLong());
        verify(passwordHashingBA, never()).calculateHash(any(), any());
        verify(userPermissionDAO, never()).findByUserId(anyLong());
        verify(userLoginHistoryDAO, never()).create(any());
    }


    private UserPermissionBE createUserPermissionBE(final long userId, final String permission) {
        final UserPermissionBE userPermissionBE = new UserPermissionBE();
        userPermissionBE.setUserId(userId);
        userPermissionBE.setPermissionName(permission);
        return userPermissionBE;
    }


    private UserFailedSignInAttemptsBE createUserFailedSignInAttemptsBE(final long userId, final int failedAttempts) {
        final UserFailedSignInAttemptsBE userFailedSignInAttemptsBE = new UserFailedSignInAttemptsBE();
        userFailedSignInAttemptsBE.setUserId(userId);
        userFailedSignInAttemptsBE.setFailedLoginAttempts(failedAttempts);
        return userFailedSignInAttemptsBE;
    }


    private UserBE createUserBE() {
        final UserBE userBE = new UserBE();
        userBE.setUserId(USER_ID);
        userBE.setUserEmail(EMAIL);
        userBE.setUserSalt(SALT);
        userBE.setUserPassword(PASSWORD_HASH);
        userBE.setCreatedAtUtc(TIMESTAMP);
        userBE.setCreatedByUserId(OTHER_USER_ID);
        userBE.setVersion(VERSION);
        userBE.setLastModifiedAtUtc(TIMESTAMP);
        userBE.setLastModifiedByUserId(OTHER_USER_ID);
        return userBE;
    }
}