package de.bogenliga.application.services.v1.user.service;

import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.user.api.UserRoleComponent;
import de.bogenliga.application.business.user.api.UserProfileComponent;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserRoleDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.business.user.impl.dao.UserRoleDAO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.services.common.errorhandling.ErrorDTO;
import de.bogenliga.application.services.v1.user.model.*;
import de.bogenliga.application.springconfiguration.security.WebSecurityConfiguration;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.assertj.core.api.Java6Assertions;
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
import org.springframework.security.core.parameters.P;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.bogenliga.application.common.errorhandling.exception.BusinessException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class UserServiceTest {

    private static final Long ID = 123L;
    private static final Long DSBMITGLIEDID = 28L;
    private static final Long VEREINID = 5L;
    private static final String USERNAME = "user";
    private static final String PASSWORD = "CorrectPasswordV1";
    private static final String NEUESPASSWORD = "CorrectPasswordV2";
    private static final String EMAIL = "test@test.com";
    private static final Boolean USING2FA = false;
    private static final String JWT = "jwt";
    private static final String ERROR_MESSAGE = "error";
    private static final List<String> PERMISSIONS = Arrays.asList(
            UserPermission.CAN_READ_SPORTJAHR.name(),
            UserPermission.CAN_READ_STAMMDATEN.name(),
            UserPermission.CAN_DELETE_SYSTEMDATEN.name());
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final Long VERSION = 7L;

    private static final Long ROLE_ID = 3L;
    private static final String ROLE_NAME = "rolename";




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
    private UserComponent userComponent;
    @Mock
    private UserProfileComponent userProfileComponent;
    @Mock
    private UserRoleComponent userRoleComponent;
    @Mock
    private DsbMitgliedComponent dsbMitgliedComponent;
    @Mock
    private VeranstaltungComponent veranstaltungComponent;

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
        userWithPermissionsDO.setActive(true);


        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);

        final DsbMitgliedDO dsbMitgliedDO = new DsbMitgliedDO(DSBMITGLIEDID);
        dsbMitgliedDO.setVereinsId(VEREINID);

        final UserDO userDO = new UserDO();
        userDO.setDsb_mitglied_id(DSBMITGLIEDID);
        userDO.setId(ID);


        // configure mocks
        when(webSecurityConfiguration.authenticationManagerBean()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(
                authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userWithPermissionsDO);
        when(jwtTokenProvider.createToken(any(Authentication.class))).thenReturn(JWT);

        when(userComponent.findById(anyLong())).thenReturn(userDO);
        when(dsbMitgliedComponent.findById(userDO.getDsb_mitglied_id())).thenReturn(dsbMitgliedDO);

        // call test method
        final ResponseEntity<UserSignInDTO> actual = underTest.login(userCredentials);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getBody()).isNotNull();
        assertThat(actual.getBody().getJwt()).isEqualTo(JWT);
        assertThat(actual.getBody().getEmail()).isEqualTo(USERNAME);
        assertThat(actual.getBody().getId()).isEqualTo(ID);
        assertThat(actual.getBody().getVereinId()).isEqualTo(VEREINID);

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


    // tests for getAllUsersByRoleId
    @Test
    public void getAllUsersByRoleId() {
        // prepare test data
        final UserRoleDO userRole = new UserRoleDO();
        userRole.setRoleId(ROLE_ID);
        userRole.setEmail(EMAIL);

        final List<UserRoleDO> userRoleList = new ArrayList<>();
        userRoleList.add(userRole);

        // configure mocks
        when(userRoleComponent.findByRoleId(anyLong())).thenReturn(userRoleList);

        // call test method
        final List<UserRoleDTO> actual = underTest.getAllUsersByRoleId(ROLE_ID);

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        assertThat(actual.get(0).getEmail())
                .isEqualTo(userRole.getEmail());
        assertThat(actual.get(0).getRoleId())
                .isEqualTo(userRole.getRoleId());

        // verify invocations
        verify(userRoleComponent).findByRoleId(ROLE_ID);
    }

    @Test
    public void getAllUsersByRoleId_withInvalidRoleId_shouldThrowException() {
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.getAllUsersByRoleId(-1L))
                .withMessageContaining("RoleID must not be negative.")
                .withNoCause();

        // verify invocations
        verify(userRoleComponent, never()).findByRoleId(ROLE_ID);
    }


    // tests for update (password)
    @Test
    public void update_success() {

        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);

        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);
        when(jwtTokenProvider.resolveUserSignInDTO(anyString())).thenReturn(userSignInDTO);
        when(jwtTokenProvider.getUserId(any())).thenReturn(ID);

        //prepare test data ChangePWD
        final UserDO expecteduserDO = new UserDO();
        expecteduserDO.setEmail(USERNAME);
        expecteduserDO.setId(ID);
        expecteduserDO.setVersion(VERSION);

        final UserChangeCredentialsDTO userChangeCredentialsDTO = new UserChangeCredentialsDTO();
        userChangeCredentialsDTO.setPassword(PASSWORD);
        userChangeCredentialsDTO.setNewPassword(NEUESPASSWORD);

        // configure mocks
        when(userComponent.updatePassword(any(UserDO.class), anyString(), anyString(), anyLong())).thenReturn(expecteduserDO);

        // call test method
        final UserDTO actual = underTest.updatePassword(requestWithHeader, userChangeCredentialsDTO);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expecteduserDO.getId());
        assertThat(actual.getEmail()).isEqualTo(expecteduserDO.getEmail());

    }

    @Test
    public void update_withoutCredentials_shouldThrowException() {
        // prepare test data
        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);

        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);


        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.updatePassword(requestWithHeader, null))
                .withMessageContaining("must not be null")
                .withNoCause();


    }

    @Test
    public void update_withoutPassword_shouldThrowException() {
        // prepare test data
        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);


        final UserChangeCredentialsDTO userChangeCredentialsDTO = new UserChangeCredentialsDTO();
        userChangeCredentialsDTO.setPassword(null);
        userChangeCredentialsDTO.setNewPassword(NEUESPASSWORD);


        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);


        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.updatePassword(requestWithHeader, userChangeCredentialsDTO))
                .withMessageContaining("Password must not be null or empty")
                .withNoCause();


    }

    @Test
    public void update_withoutNewPassword_shouldThrowException() {
        // prepare test data
        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);


        final UserChangeCredentialsDTO userChangeCredentialsDTO = new UserChangeCredentialsDTO();
        userChangeCredentialsDTO.setPassword(PASSWORD);
        userChangeCredentialsDTO.setNewPassword(null);


        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);


        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.updatePassword(requestWithHeader, userChangeCredentialsDTO))
                .withMessageContaining("New password must not be null or empty")
                .withNoCause();


    }


    // tests for reset (password)
    /*@Test
    public void reset_success() {

        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);
        when(jwtTokenProvider.getUserId(any())).thenReturn(ID);

        //prepare test data ChangePWD
        final UserDO loggedInUser = new UserDO();
        loggedInUser.setEmail(USERNAME);
        loggedInUser.setId(ID);
        loggedInUser.setVersion(VERSION);

        final UserDO selectedUser = new UserDO();
        selectedUser.setEmail(EMAIL);
        selectedUser.setId(492L);
        selectedUser.setVersion(VERSION);

        final UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setUsername(EMAIL);
        userCredentialsDTO.setPassword(PASSWORD);

        // configure mocks
        when(userComponent.resetPassword(any(UserDO.class), anyString(), anyLong())).thenReturn(selectedUser);

        // call test method
        final UserDTO actual = underTest.resetPassword(requestWithHeader, userCredentialsDTO);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotEqualTo(loggedInUser.getId());
        assertThat(actual.getEmail()).isNotEqualTo(loggedInUser.getEmail());
    }*/

    @Test
    public void reset_withoutCredentials_shouldThrowException() {
        // prepare test data
        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);

        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);


        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.resetPassword(requestWithHeader, null))
                .withMessageContaining("must not be null")
                .withNoCause();


    }

    @Test
    public void reset_withoutNewPassword_shouldThrowException() {
        // prepare test data
        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);


        final UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setPassword(null);


        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);


        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.resetPassword(requestWithHeader, userCredentialsDTO))
                .withMessageContaining("New password must not be null")
                .withNoCause();


    }


    // tests for updateRole
    @Test
    public void updateRole_success() {

        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);

        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);
        when(jwtTokenProvider.resolveUserSignInDTO(anyString())).thenReturn(userSignInDTO);
        when(jwtTokenProvider.getUserId(any())).thenReturn(ID);

        //prepare test data
        List<UserRoleDO> expectedUserRoleDoList = new ArrayList<>();
        final UserRoleDO expectedUserRoleDO = new UserRoleDO();
        expectedUserRoleDO.setEmail(USERNAME);
        expectedUserRoleDO.setId(ID);
        expectedUserRoleDO.setRoleName(ROLE_NAME);
        expectedUserRoleDO.setRoleId(ROLE_ID);
        expectedUserRoleDO.setVersion(VERSION);
        expectedUserRoleDoList.add(expectedUserRoleDO);

        //prepare test data input
        List<UserRoleDTO> inUserRoleDTOList = new ArrayList<>();
        final UserRoleDTO inUserRoleDTO = new UserRoleDTO();
        inUserRoleDTO.setId(ID);
        inUserRoleDTO.setRoleId(ROLE_ID);
        inUserRoleDTO.setVersion(VERSION);
        inUserRoleDTOList.add(inUserRoleDTO);

        // configure mocks
        when(userRoleComponent.update(any(List.class), anyLong())).thenReturn(expectedUserRoleDoList);

        // call test method
        final List<UserRoleDTO> actual = underTest.updateRoles(requestWithHeader, inUserRoleDTOList);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.get(0).getId()).isEqualTo(expectedUserRoleDO.getId());
        assertThat(actual.get(0).getRoleId()).isEqualTo(expectedUserRoleDO.getRoleId());

    }

    @Test
    public void update_withoutUserRoleDTO_shouldThrowException() {
        // prepare test data
        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);

        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.updateRoles(requestWithHeader, null))
                .withMessageContaining("must not be null")
                .withNoCause();


    }

    @Test
    public void updateRole_withoutUserID_shouldThrowException() {
        // prepare test data
        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);


        final UserChangeCredentialsDTO userChangeCredentialsDTO = new UserChangeCredentialsDTO();
        userChangeCredentialsDTO.setPassword(null);
        userChangeCredentialsDTO.setNewPassword(NEUESPASSWORD);


        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);


        //prepare test data input
        final List<UserRoleDTO> inUserRoleDTOList = new ArrayList<>();
        final UserRoleDTO inUserRoleDTO = new UserRoleDTO();
        inUserRoleDTO.setId(null);
        inUserRoleDTO.setRoleId(ROLE_ID);
        inUserRoleDTO.setVersion(VERSION);
        inUserRoleDTOList.add(inUserRoleDTO);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.updateRoles(requestWithHeader, inUserRoleDTOList))
                .withMessageContaining("must not be null")
                .withNoCause();

    }

    @Test
    public void updateRole_withoutRoleId_shouldThrowException() {
        // prepare test data
        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);


        //prepare test data input
        final List<UserRoleDTO> inUserRoleDTOList = new ArrayList<>();
        final UserRoleDTO inUserRoleDTO = new UserRoleDTO();
        inUserRoleDTO.setId(ID);
        inUserRoleDTO.setRoleId(null);
        inUserRoleDTO.setVersion(VERSION);
        inUserRoleDTOList.add(inUserRoleDTO);



        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);


        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.updateRoles(requestWithHeader, inUserRoleDTOList))
                .withMessageContaining("must not be null")
                .withNoCause();


    }


    @Test
    public void findAll() {
        // prepare test data
        final UserRoleDO expectedURDO = new UserRoleDO();
        expectedURDO.setId(ID);
        expectedURDO.setRoleId(ROLE_ID);
        expectedURDO.setEmail(USERNAME);
        expectedURDO.setRoleName(ROLE_NAME);

        // configure mocks
        when(userRoleComponent.findAll()).thenReturn(Collections.singletonList(expectedURDO));

        // call test method
        final List<UserRoleDTO> actual = underTest.findAll();

        // assert result
        Java6Assertions.assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Java6Assertions.assertThat(actual.get(0)).isNotNull();

        Java6Assertions.assertThat(actual.get(0).getId())
                .isEqualTo(expectedURDO.getId());
        Java6Assertions.assertThat(actual.get(0).getEmail())
                .isEqualTo(expectedURDO.getEmail());
        Java6Assertions.assertThat(actual.get(0).getRoleId())
                .isEqualTo(expectedURDO.getRoleId());
        Java6Assertions.assertThat(actual.get(0).getRoleName())
                .isEqualTo(expectedURDO.getRoleName());
        Java6Assertions.assertThat(actual.get(0).getVersion())
                .isEqualTo(expectedURDO.getVersion());


        // verify invocations
        verify(userRoleComponent).findAll();


    }


    @Test
    public void findById() {
        // prepare test data

        List<UserRoleDO> userRoleDOList = new ArrayList<>();
        final UserRoleDO expectedDO = new UserRoleDO();
        expectedDO.setId(ID);
        expectedDO.setRoleId(ROLE_ID);
        expectedDO.setEmail(USERNAME);
        expectedDO.setRoleName(ROLE_NAME);
        userRoleDOList.add(expectedDO);

        // configure mocks
        when(userRoleComponent.findById(anyLong())).thenReturn(userRoleDOList);

        // call test method
        final List<UserRoleDTO> actual = underTest.getUserRoleById(ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedDO.getId());
        assertThat(actual.get(0).getEmail())
                .isEqualTo(expectedDO.getEmail());
        assertThat(actual.get(0).getRoleId())
                .isEqualTo(expectedDO.getRoleId());
        assertThat(actual.get(0).getRoleName())
                .isEqualTo(expectedDO.getRoleName());
        assertThat(actual.get(0).getVersion())
                .isEqualTo(expectedDO.getVersion());

        // verify invocations
        verify(userRoleComponent).findById(ID);
    }


    // tests for createUser
    @Test
    public void create_success() {

        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);

        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);
        when(jwtTokenProvider.resolveUserSignInDTO(anyString())).thenReturn(userSignInDTO);
        when(jwtTokenProvider.getUserId(any())).thenReturn(ID);

        //prepare test data newUser
        final UserDTO expecteduserDTO = new UserDTO();
        expecteduserDTO.setEmail(USERNAME);
        expecteduserDTO.setId(ID);
        expecteduserDTO.setVersion(VERSION);

        final UserRoleDO createdUserRoleDO = new UserRoleDO();
        createdUserRoleDO.setEmail(USERNAME);
        createdUserRoleDO.setId(ID);
        createdUserRoleDO.setRoleName(ROLE_NAME);
        createdUserRoleDO.setRoleId(ROLE_ID);
        createdUserRoleDO.setVersion(VERSION);

        final UserDO userCreatedDO = new UserDO();
        userCreatedDO.setEmail(USERNAME);
        userCreatedDO.setId(ID);
        userCreatedDO.setDsb_mitglied_id(DSBMITGLIEDID);
        userCreatedDO.setVersion(VERSION);

        final UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setUsername(USERNAME);
        userCredentialsDTO.setPassword(PASSWORD);
        userCredentialsDTO.setDsb_mitglied_id(DSBMITGLIEDID);

        // configure mocks
        when(userComponent.create(anyString(), anyString(), anyLong(), anyLong(), anyBoolean())).thenReturn(userCreatedDO);
        when(userRoleComponent.create(anyLong(), anyLong())).thenReturn(createdUserRoleDO);

        // call test method
        final UserDTO actual = underTest.create(requestWithHeader, userCredentialsDTO);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expecteduserDTO.getId());
        assertThat(actual.getEmail()).isEqualTo(expecteduserDTO.getEmail());

    }

    @Test
    public void create_whithoutCredentials_shouldThrowException() {

        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);

        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);
        when(jwtTokenProvider.resolveUserSignInDTO(anyString())).thenReturn(userSignInDTO);
        when(jwtTokenProvider.getUserId(any())).thenReturn(ID);

        //prepare test data newUser

        final UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setUsername(USERNAME);
        userCredentialsDTO.setPassword(PASSWORD);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(requestWithHeader, null))
                .withMessageContaining("must not be null")
                .withNoCause();


    }

    @Test
    public void create_whithoutUsername_shouldThrowException() {

        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);

        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);
        when(jwtTokenProvider.resolveUserSignInDTO(anyString())).thenReturn(userSignInDTO);
        when(jwtTokenProvider.getUserId(any())).thenReturn(ID);

        //prepare test data newUser

        final UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setUsername(null);
        userCredentialsDTO.setPassword(PASSWORD);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(requestWithHeader, userCredentialsDTO))
                .withMessageContaining("must not be null")
                .withNoCause();


    }

    @Test
    public void create_whithoutPassword_shouldThrowException() {

        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);

        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);
        when(jwtTokenProvider.resolveUserSignInDTO(anyString())).thenReturn(userSignInDTO);
        when(jwtTokenProvider.getUserId(any())).thenReturn(ID);

        //prepare test data newUser

        final UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setUsername(USERNAME);
        userCredentialsDTO.setPassword(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(requestWithHeader, userCredentialsDTO))
                .withMessageContaining("must not be null")
                .withNoCause();


    }

    @Test
    public void create_withInvalidPassword_shouldThrowException() {

        // prepare test data identity user
        final UserSignInDTO userSignInDTO = new UserSignInDTO();
        userSignInDTO.setJwt(JWT);
        userSignInDTO.setEmail(USERNAME);
        userSignInDTO.setId(ID);

        // configure mocks
        when(requestWithHeader.getHeader(anyString())).thenReturn("Bearer " + JWT);
        when(jwtTokenProvider.resolveUserSignInDTO(anyString())).thenReturn(userSignInDTO);
        when(jwtTokenProvider.getUserId(any())).thenReturn(ID);

        //prepare test data newUser

        final UserCredentialsDTO userCredentialsDTO = new UserCredentialsDTO();
        userCredentialsDTO.setUsername(USERNAME);
        userCredentialsDTO.setDsb_mitglied_id(DSBMITGLIEDID);

        String[] invalidPasswords = new String[]{
                "ABCabc0",
                "ABCABC00",
                "abcabc00",
                "ABCabcABC",
                "ABCABCABC",
                "abcabcabc",
                "        ",
                "0123456789",
        };

        // Check different passwords for their validity
        for (String invalidPassword : invalidPasswords) {
            userCredentialsDTO.setPassword(invalidPassword);
            assertThatExceptionOfType(BusinessException.class)
                    .isThrownBy(() -> underTest.create(requestWithHeader, userCredentialsDTO))
                    .withMessageContaining("This is not a valid Password")
                    .withNoCause();
        }
    }

}
