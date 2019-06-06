package de.bogenliga.application.springconfiguration.security.authentication;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.time.DateProvider;
import de.bogenliga.application.services.common.errorhandling.ErrorDTO;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class UserAuthenticationProviderTest {

    private static final long USER_ID = 123;
    private static final long OTHER_USER_ID = 321;
    private static final long VERSION = 1;
    private static final String EMAIL = "email";
    private static final String SALT = "salt";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_HASH = "password_hashed";
    private static final OffsetDateTime TIMESTAMP = DateProvider.currentDateTimeUtc();
    private static final UserPermission PERMISSION = UserPermission.CAN_MODIFY_SPORTJAHR;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UserComponent userComponent;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserAuthenticationProvider underTest;


    @Test
    public void authenticate() {
        // prepare test data
        final UserWithPermissionsDO userWithPermissionsDO = createUserWithPermissionsDO();

        // configure mocks
        UserDO userDo = new UserDO();
        userDo.setUsing2FA(false);
        when(userComponent.findByEmail(any())).thenReturn(userDo);
        when(authentication.getName()).thenReturn(EMAIL);
        when(authentication.getCredentials()).thenReturn(PASSWORD);
        when(userComponent.signIn(any(), any())).thenReturn(userWithPermissionsDO);

        // call test method
        final Authentication actual = underTest.authenticate(authentication);

        // assert result
        assertThat(actual.getPrincipal()).isNotNull();

        final UserWithPermissionsDO actualUser = (UserWithPermissionsDO) actual.getPrincipal();
        assertThat(actualUser.getEmail()).isEqualTo(userWithPermissionsDO.getEmail());
        assertThat(actualUser.getId()).isEqualTo(userWithPermissionsDO.getId());

        assertThat(actual.getAuthorities()).isNotNull();

        final List<UserPermission> actualPermissions = (List<UserPermission>) actual.getAuthorities();

        assertThat(actualPermissions).contains(PERMISSION);

        // verify invocations
        verify(userComponent).signIn(EMAIL, PASSWORD);
    }


    @Test
    public void authenticate_withFailedSignIn_shouldReturnError() {
        // prepare test data
        final UserWithPermissionsDO userWithPermissionsDO = createUserWithPermissionsDO();

        // configure mocks
        UserDO userDo = new UserDO();
        userDo.setUsing2FA(false);
        when(userComponent.findByEmail(any())).thenReturn(userDo);
        when(authentication.getName()).thenReturn(EMAIL);
        when(authentication.getCredentials()).thenReturn(PASSWORD);
        when(userComponent.signIn(any(), any()))
                .thenThrow(new BusinessException(ErrorCode.INVALID_SIGN_IN_CREDENTIALS, ""));

        // call test method
        final Authentication actual = underTest.authenticate(authentication);

        // assert result
        assertThat(actual.getPrincipal()).isNull();
        assertThat(actual.getDetails()).isNotNull();

        final ErrorDTO actualError = (ErrorDTO) actual.getDetails();

        assertThat(actualError.getErrorCode()).isEqualTo(ErrorCode.INVALID_SIGN_IN_CREDENTIALS);

        // verify invocations
        verify(userComponent).signIn(EMAIL, PASSWORD);
    }


    @Test
    public void authenticate_withOtherError_shouldReturnDefaultError() {
        // prepare test data
        final UserWithPermissionsDO userWithPermissionsDO = createUserWithPermissionsDO();

        // configure mocks
        UserDO userDo = new UserDO();
        userDo.setUsing2FA(false);
        when(userComponent.findByEmail(any())).thenReturn(userDo);
        when(authentication.getName()).thenReturn(EMAIL);
        when(authentication.getCredentials()).thenReturn(PASSWORD);
        when(userComponent.signIn(any(), any()))
                .thenThrow(new RuntimeException(""));

        // call test method
        final Authentication actual = underTest.authenticate(authentication);

        // assert result
        assertThat(actual.getPrincipal()).isNull();
        assertThat(actual.getDetails()).isNotNull();

        final ErrorDTO actualError = (ErrorDTO) actual.getDetails();

        assertThat(actualError.getErrorCode()).isEqualTo(ErrorCode.UNEXPECTED_ERROR);

        // verify invocations
        verify(userComponent).signIn(EMAIL, PASSWORD);
    }


    @Test
    public void supports() {
        assertThat(underTest.supports(UsernamePasswordAuthenticationToken.class)).isTrue();
    }


    @Test
    public void createAuthenticationPlaceholder() {

        final UsernamePasswordAuthenticationToken actual = underTest.createAuthenticationPlaceholder(EMAIL,
                Collections.singleton(PERMISSION));

        assertThat(actual.getPrincipal()).isEqualTo(0);

        assertThat(actual.getAuthorities()).isNotNull();
        final Collection<GrantedAuthority> actualPermissions = actual.getAuthorities();

        assertThat(actualPermissions).contains(PERMISSION);
    }


    private UserWithPermissionsDO createUserWithPermissionsDO() {
        final UserWithPermissionsDO userWithPermissionsDO = new UserWithPermissionsDO();
        userWithPermissionsDO.setId(USER_ID);
        userWithPermissionsDO.setEmail(EMAIL);

        userWithPermissionsDO.setCreatedAtUtc(TIMESTAMP);
        userWithPermissionsDO.setCreatedByUserId(OTHER_USER_ID);
        userWithPermissionsDO.setVersion(VERSION);
        userWithPermissionsDO.setLastModifiedAtUtc(TIMESTAMP);
        userWithPermissionsDO.setLastModifiedByUserId(OTHER_USER_ID);

        userWithPermissionsDO.setPermissions(Collections.singletonList(PERMISSION.name()));

        return userWithPermissionsDO;
    }
}