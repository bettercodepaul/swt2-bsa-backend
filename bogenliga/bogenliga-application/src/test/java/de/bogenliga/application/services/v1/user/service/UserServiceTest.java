package de.bogenliga.application.services.v1.user.service;

import de.bogenliga.application.business.user.api.UserProfileComponent;
import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.services.common.errorhandling.ErrorDTO;
import de.bogenliga.application.services.v1.user.model.UserCredentialsDTO;
import de.bogenliga.application.services.v1.user.model.UserDTO;
import de.bogenliga.application.services.v1.user.model.UserProfileDTO;
import de.bogenliga.application.services.v1.user.model.UserSignInDTO;
import de.bogenliga.application.springconfiguration.security.WebSecurityConfiguration;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class UserServiceTest {

    private static final Long ID = 123L;
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String JWT = "jwt";
    private static final String ERROR_MESSAGE = "error";
    private static final List<String> PERMISSIONS = Arrays.asList(
            UserPermission.CAN_READ_SPORTJAHR.name(),
            UserPermission.CAN_READ_STAMMDATEN.name());
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private WebSecurityConfiguration webSecurityConfiguration;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;
    @Mock
    private HttpServletRequest requestWithHeader;
    @Mock
    private UserProfileComponent userProfileComponent;

    @InjectMocks
    private UserService underTest;

    @Captor
    private ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationTokenArgumentCaptor;


    @Test
    public void login() throws Exception {
        // prepare test data
        final UserCredentialsDTO userCredentials = new UserCredentialsDTO();
        userCredentials.setUsername(USERNAME);
        userCredentials.setPassword(PASSWORD);

        final UserWithPermissionsDO userWithPermissionsDO = new UserWithPermissionsDO();
        userWithPermissionsDO.setId(ID);
        userWithPermissionsDO.setEmail(USERNAME);
        userWithPermissionsDO.setPermissions(PERMISSIONS);

        // configure mocks
        when(webSecurityConfiguration.authenticationManagerBean()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(
                authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userWithPermissionsDO);
        when(jwtTokenProvider.createToken(any(Authentication.class))).thenReturn(JWT);

        // call test method
        final ResponseEntity<UserSignInDTO> actual = underTest.login(userCredentials);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getBody()).isNotNull();
        assertThat(actual.getBody().getJwt()).isEqualTo(JWT);
        assertThat(actual.getBody().getEmail()).isEqualTo(USERNAME);
        assertThat(actual.getBody().getId()).isEqualTo(ID);

        assertThat(actual.getHeaders()).isNotNull();
        assertThat(actual.getHeaders().containsKey(AUTHORIZATION_HEADER)).isTrue();
        assertThat(actual.getHeaders().get(AUTHORIZATION_HEADER)).contains("Bearer " + JWT);

        assertThat(actual.getStatusCode()).isNotNull();
        assertThat(actual.getStatusCode().value()).isEqualTo(200);

        // verify invocations
        verify(webSecurityConfiguration).authenticationManagerBean();
        verify(authenticationManager).authenticate(authenticationTokenArgumentCaptor.capture());

        final UsernamePasswordAuthenticationToken actualToken = authenticationTokenArgumentCaptor.getValue();

        assertThat(actualToken.getPrincipal()).isNotNull().isInstanceOf(String.class).isEqualTo(USERNAME);
        assertThat(actualToken.getCredentials()).isNotNull().isInstanceOf(String.class).isEqualTo(PASSWORD);

        verify(authentication).isAuthenticated();
        verify(jwtTokenProvider).createToken(authentication);

    }


    @Test
    public void login_not_authorized() throws Exception {
        // prepare test data
        final UserCredentialsDTO userCredentials = new UserCredentialsDTO();
        userCredentials.setUsername(USERNAME);
        userCredentials.setPassword(PASSWORD);

        final UserWithPermissionsDO userWithPermissionsDO = new UserWithPermissionsDO();
        userWithPermissionsDO.setId(ID);
        userWithPermissionsDO.setEmail(USERNAME);
        userWithPermissionsDO.setPermissions(PERMISSIONS);

        final ErrorDTO errorDTO = new ErrorDTO(ErrorCode.NO_SESSION_ERROR, ERROR_MESSAGE);

        // configure mocks
        when(webSecurityConfiguration.authenticationManagerBean()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(
                authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        when(authentication.getDetails()).thenReturn(errorDTO);

        // call test method
        final ResponseEntity<ErrorDTO> actual = underTest.login(userCredentials);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getBody()).isNotNull();
        assertThat(actual.getBody().getErrorCode()).isEqualTo(errorDTO.getErrorCode());
        assertThat(actual.getBody().getErrorMessage()).isEqualTo(ERROR_MESSAGE);

        assertThat(actual.getHeaders()).isNotNull();
        assertThat(actual.getHeaders().containsKey(AUTHORIZATION_HEADER)).isFalse();

        assertThat(actual.getStatusCode()).isNotNull();
        assertThat(actual.getStatusCode().value()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());

        // verify invocations
        verify(webSecurityConfiguration).authenticationManagerBean();
        verify(authenticationManager).authenticate(authenticationTokenArgumentCaptor.capture());

        final UsernamePasswordAuthenticationToken actualToken = authenticationTokenArgumentCaptor.getValue();

        assertThat(actualToken.getPrincipal()).isNotNull().isInstanceOf(String.class).isEqualTo(USERNAME);
        assertThat(actualToken.getCredentials()).isNotNull().isInstanceOf(String.class).isEqualTo(PASSWORD);

        verify(authentication).isAuthenticated();
        verify(jwtTokenProvider, never()).createToken(any());
    }


    @Test
    public void whoAmI() {
        // prepare test data
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);

        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);
        when(jwtTokenProvider.resolveUserSignInDTO(anyString())).thenReturn(userSignInDTO);

        // call test method
        final UserDTO actual = underTest.whoAmI(requestWithHeader);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(USERNAME);
        assertThat(actual.getId()).isEqualTo(ID);

        // verify invocations
        verify(requestWithHeader).getHeader(AUTHORIZATION_HEADER);
    }

    @Test
    public void getUserProfileById() {

        // prepare test data
        final UserProfileDO userProfileDO = new UserProfileDO();
        userProfileDO.setVorname(USERNAME);

        // configure mocks
        when(userProfileComponent.findById(anyLong())).thenReturn(userProfileDO);

        // call test method
        final UserProfileDTO actual = underTest.getUserProfileById(ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getVorname()).isEqualTo(userProfileDO.getVorname());

    }
}