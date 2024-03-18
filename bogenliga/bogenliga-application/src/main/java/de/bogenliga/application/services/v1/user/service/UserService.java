package de.bogenliga.application.services.v1.user.service;

import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.role.api.RoleComponent;
import de.bogenliga.application.business.role.api.types.RoleDO;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.UserRoleComponent;
import de.bogenliga.application.business.user.api.UserProfileComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.business.user.api.types.UserRoleDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.common.errorhandling.ErrorDTO;
import de.bogenliga.application.services.v1.user.mapper.UserDTOMapper;
import de.bogenliga.application.services.v1.user.mapper.UserRoleDTOMapper;
import de.bogenliga.application.services.v1.user.mapper.UserProfileDTOMapper;
import de.bogenliga.application.services.v1.user.model.*;
import de.bogenliga.application.springconfiguration.security.WebSecurityConfiguration;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOwnIdentity;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * I´m a REST resource and handle configuration CRUD requests over the HTTP protocol.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 * @see <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete">Wikipedia - CRUD</a>
 * @see <a href="https://en.wikipedia.org/wiki/Representational_state_transfer">Wikipedia - REST</a>
 * @see <a href="https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol">Wikipedia - HTTP</a>
 * @see <a href="https://en.wikipedia.org/wiki/Design_by_contract">Wikipedia - Design by contract</a>
 * @see <a href="https://spring.io/guides/gs/actuator-service/">
 * Building a RESTful Web Service with Spring Boot Actuator</a>
 * @see <a href="https://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-configuration">
 * Build a REST API with Spring 4 and Java Config</a>
 * @see <a href="https://www.baeldung.com/spring-autowire">Guide to Spring @Autowired</a>
 */
@RestController
@CrossOrigin
@RequestMapping("v1/user")
public class UserService implements ServiceFacade {

    private static final String PRECONDITION_MSG_USER_ID = "User ID must not be null or negative";
    private static final String PRECONDITION_MSG_ROLE_ID = "User Role ID must not be null or negative";
    private static final String PRECONDITION_MSG_USER_EMAIL = "Benutzer email must not be null";
    private static final String PRECONDITION_MSG_USER_PW = "This is not a valid Password";
    private static final String PRECONDITION_MSG_SEARCHTERM = "Search term must not be negative";

    private static final String PRECONDITION_MSG_DSB_MITGLIED_ID = "User must reference an existing DSB-member -not be null or negative";

    private static final String PW_VALIDATION_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d#$^+=!*()@%&?]{8,}$";

    private static final String ROLE_KAMPFRICHTER = "KAMPFRICHTER";

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final JwtTokenProvider jwtTokenProvider;

    private final WebSecurityConfiguration webSecurityConfiguration;

    private final UserComponent userComponent;

    private final UserRoleComponent userRoleComponent;
    private final RoleComponent roleComponent;

    private final UserProfileComponent userProfileComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    @Autowired
    public UserService(final JwtTokenProvider jwtTokenProvider,
                       //final AuthenticationManager authenticationManager
                       final WebSecurityConfiguration webSecurityConfiguration,
                       final UserComponent userComponent,
                       final UserRoleComponent userRoleComponent,
                       final RoleComponent roleComponent,
                       final UserProfileComponent userProfileComponent,
                       final DsbMitgliedComponent dsbMitgliedComponent,
                       final VeranstaltungComponent veranstaltungComponent) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.webSecurityConfiguration = webSecurityConfiguration;
        this.userComponent = userComponent;
        this.userRoleComponent = userRoleComponent;
        this.roleComponent = roleComponent;
        this.userProfileComponent = userProfileComponent;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
        this.veranstaltungComponent = veranstaltungComponent;
    }



    /**
     * Login...
     *
     * @param </UserCredentialsDTO> User zum Anmelden
     *
     * @return ResponseEntity mit der Rückmeldung nzur Anmeldung
     */
    @PostMapping(
            value = "/signin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody final UserCredentialsDTO credentials) {
        Preconditions.checkNotNull(credentials, "Credentials must not be null");
        Preconditions.checkNotNullOrEmpty(credentials.getUsername(), "Username must not be null or empty");
        Preconditions.checkNotNullOrEmpty(credentials.getPassword(), "Password must not be null or empty");

        ErrorDTO errorDetails = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    credentials.getUsername(),
                    credentials.getPassword());
            authenticationToken.setDetails(credentials.getCode());
            final Authentication authentication = webSecurityConfiguration.authenticationManagerBean()
                    .authenticate(authenticationToken);

            if (authentication.isAuthenticated()) {
                // create payload
                final UserWithPermissionsDO userWithPermissionsDO = (UserWithPermissionsDO) authentication.getPrincipal();

                if(userWithPermissionsDO.isActive()) {
                    final UserSignInDTO userSignInDTO = UserDTOMapper.toUserSignInDTO.apply(userWithPermissionsDO);
                    userSignInDTO.setJwt(jwtTokenProvider.createToken(authentication));

                    final HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", "Bearer " + userSignInDTO.getJwt());
                    try {
                        //Get the Verein ID and teh Veranstaltungs ID's
                        userSignInDTO.setVereinId(this.dsbMitgliedComponent.findById(this.userComponent.findById(userSignInDTO.getId()).getDsbMitgliedId()).getVereinsId());
                        ArrayList<Integer> temp = new ArrayList<>();
                        for (VeranstaltungDO veranstaltungDO : this.veranstaltungComponent.findByLigaleiterId(userSignInDTO.getId())) {
                            temp.add(veranstaltungDO.getVeranstaltungID().intValue());
                        }
                        userSignInDTO.setVeranstaltungenIds(temp);
                        ArrayList<Integer> wettkampftemp = new ArrayList<>();
                        userSignInDTO.setWettkampfIds(wettkampftemp);
                    } catch (Exception ignore) {
                       LOG.warn("Failed to define additional user information", ignore);
                    }
                    return ResponseEntity.status(HttpStatus.OK).headers(headers).body(userSignInDTO);
                } else {
                    errorDetails = new ErrorDTO(ErrorCode.INVALID_SIGN_IN_CREDENTIALS, "Sign in failed");
                    return new ResponseEntity<>(errorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } else {
                if (authentication.getDetails() != null) {
                    errorDetails = (ErrorDTO) authentication.getDetails();
                }
            }
        } catch (final Exception e) { // NOSONAR
            LOG.warn("An error occured while SignIn of user {}", credentials.getUsername(), e);
        }

        // return error details from authentication or a default error
        errorDetails = errorDetails != null ? errorDetails : new ErrorDTO(ErrorCode.INVALID_SIGN_IN_CREDENTIALS,
                "Sign in failed");
        return new ResponseEntity<>(errorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @GetMapping(
            value = "/me",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO whoAmI(final HttpServletRequest requestWithHeader) {
        final String jwt = JwtTokenProvider.resolveToken(requestWithHeader);

        return jwtTokenProvider.resolveUserSignInDTO(jwt);
    }


    /**
     * Returns the user profile for a given id.
     *
     * @param id
     *
     * @return
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOwnIdentity
    public UserProfileDTO getUserProfileById(@PathVariable("id") final Long id) {
        Preconditions.checkNotNull(id, "UserID must not be null.");
        Preconditions.checkArgument(id >= 0, "UserID must not be negative.");

        LOG.debug("Receive 'getUserProfileById' request with ID '{}'", id);

        final UserProfileDO userProfileDO = userProfileComponent.findById(id);
        return UserProfileDTOMapper.toDTO.apply(userProfileDO);
    }


    /**
     * I persist a new password for the current user and return this user entry.
     *
     * You are only able to modify the Users, if you have the explicit permission to Modify it or
     * if you are the Ausrichter (Ligaleiter) of the Veranstaltung.
     *
     * <p>
     * Usage:
     * <pre>{@code Request: PUT /v1/user
     * Body:
     * {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     * }
     * }</pre>
     * <pre>{@code Response:
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  }
     * }</pre>
     *
     * @param uptCredentials of the request body
     *
     * @return {@link UserDTO} as JSON
     */
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public UserDTO updatePassword(final HttpServletRequest requestWithHeader,
                                  @RequestBody final UserChangeCredentialsDTO uptCredentials) {
        Preconditions.checkNotNull(uptCredentials, "Credentials must not be null");
        Preconditions.checkNotNullOrEmpty(uptCredentials.getPassword(), "Password must not be null or empty");
        Preconditions.checkNotNullOrEmpty(uptCredentials.getNewPassword(), "New password must not be null or empty");

         //update password is limited to own password,
        // therefore we get the current user id based on system utils

        final String jwt = jwtTokenProvider.resolveToken(requestWithHeader);
        final Long userId = jwtTokenProvider.getUserId(jwt);

        final UserDO userDO = new UserDO();
        userDO.setId(userId);

        //update password
        final UserDO userUpdatedDO = userComponent.updatePassword(userDO, uptCredentials.getPassword(),
                uptCredentials.getNewPassword(), userId);

        //prepare return DTO
         return UserDTOMapper.toUserDTO.apply(userUpdatedDO);
    }

    /**
     * I persist a new password for the current user and return this user entry.
     * <p>
     * Usage:
     * <pre>{@code Request: PUT /v1/user
     * Body:
     * {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     * }
     * }</pre>
     * <pre>{@code Response:
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  }
     * }</pre>
     *
     * @param selectedUser of the request body
     *
     * @return {@link UserDTO} as JSON
     */


    @PutMapping(
            value = "/resetPW",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public UserDTO resetPassword(final HttpServletRequest requestWithHeader,
                                  @RequestBody final UserCredentialsDTO selectedUser) {
        Preconditions.checkNotNull(selectedUser, "resetCredentials must not be null");
        Preconditions.checkNotNull(selectedUser.getPassword(), "New password must not be null");

        UserDTO userUpdatedDTO;

        //bestimmt den aktuell eingeloggten User, damit bei der Überprüfung die User unterschieden werden können
        final String jwt = jwtTokenProvider.resolveToken(requestWithHeader);
        final Long currentLoggedUserId = jwtTokenProvider.getUserId(jwt);

        UserDO selectedUserDO = userComponent.findByEmail(selectedUser.getUsername());

        if(currentLoggedUserId.equals(selectedUserDO.getId())) {
            throw new BusinessException(ErrorCode.PRECONDITION_MSG_RESET_PW_EQUAL_IDS,
                    "Reset failed. Current logged in user id equals selected user id");
        } else {
            final UserDO userDO = new UserDO();
            userDO.setId(selectedUserDO.getId());

            //reset password
            final UserDO userUpdatedDO = userComponent.resetPassword(userDO, selectedUser.getPassword(), selectedUserDO.getId());

            //prepare return DTO
            userUpdatedDTO = UserDTOMapper.toUserDTO.apply(userUpdatedDO);
        }
        return userUpdatedDTO;
    }

    /**
     * Service to update multiple user roles. It will remove all roles that are not send in this request
     * @param requestWithHeader
     * @param updatedUserRoles there is a single UserRoleDTO for every role that is send to this service
     * @return
     */
    @PutMapping(
            value = "/uptRoles",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public List<UserRoleDTO> updateRoles(final HttpServletRequest requestWithHeader,
                                  @RequestBody final List<UserRoleDTO> updatedUserRoles) {
        Preconditions.checkNotNull(updatedUserRoles, "UserRole-Definition must not be null");
        Preconditions.checkNotNull(updatedUserRoles.get(0).getId(), PRECONDITION_MSG_USER_ID);
        Preconditions.checkNotNull(updatedUserRoles.get(0).getRoleId(), PRECONDITION_MSG_ROLE_ID);

        final String jwt = jwtTokenProvider.resolveToken(requestWithHeader);
        final Long userId = jwtTokenProvider.getUserId(jwt);

        List<UserRoleDO> updatedUserRolesDo = new ArrayList<>();
        for(UserRoleDTO userRoleDTO : updatedUserRoles) {
            final UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setId(userRoleDTO.getId());
            userRoleDO.setRoleId(userRoleDTO.getRoleId());
            updatedUserRolesDo.add(userRoleDO);
        }
        final List<UserRoleDO> userRoleUpdatedDO = userRoleComponent.update(updatedUserRolesDo, userId);

        List<UserRoleDTO> userRoleUpdatedDTO = new ArrayList<>();
        for(UserRoleDO userRoleDO : userRoleUpdatedDO){
            userRoleUpdatedDTO.add(UserRoleDTOMapper.toDTO.apply(userRoleDO));
        }

        return userRoleUpdatedDTO;
    }


    /**
     * I return all user entries of the database.
     * <p>
     * <p>
     * Usage:
     * <pre>{@code Request: GET /v1/user}</pre>
     * <p>
     * [ { "id": "app.bogenliga.frontend.autorefresh.active", "value": "true" }, { "id":
     * "app.bogenliga.frontend.autorefresh.interval", "value": "10" } ] }
     * </pre>
     *
     * @return list of {@link UserDTO} as JSON
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_SYSTEMDATEN)
    public List<UserRoleDTO> findAll() {
        final List<UserRoleDO> userRoleDOList = userRoleComponent.findAll();
        return userRoleDOList.stream().map(UserRoleDTOMapper.toDTO).toList();
    }

    @GetMapping(value = "/search/{searchstring}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<UserRoleDTO> findBySearch(@PathVariable("searchstring") final String searchTerm) {
        Preconditions.checkNotNull(searchTerm, PRECONDITION_MSG_SEARCHTERM);

        final List<UserRoleDO> result = userRoleComponent.findBySearch(searchTerm);
        return result.stream().map(UserRoleDTOMapper.toDTO).toList();
    }


    /**
     * I return a specific user-role entries of the database.
     * <p>
     * <p>
     * Usage:
     * <pre>{@code Request: GET /v1/user/userrole/id}</pre>
     * <p>
     * [ { "id": "app.bogenliga.frontend.autorefresh.active", "value": "true" }, { "id":
     * "app.bogenliga.frontend.autorefresh.interval", "value": "10" } ] }
     * </pre>
     *
     * @return list of {@link UserDTO} as JSON
     */
    @GetMapping(
            value = "/userrole/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<UserRoleDTO> getUserRoleById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'getUserRoleById' request with ID '{}'", id);

        final List<UserRoleDO> userRoleDOlist = userRoleComponent.findById(id);
        List<UserRoleDTO> userRoleDTOS = new ArrayList<>();
        for(UserRoleDO userRoleDO : userRoleDOlist){
            userRoleDTOS.add(UserRoleDTOMapper.toDTO.apply(userRoleDO));
        }
        return userRoleDTOS;
    }


    /**
     * Returns a List of all Users that have the given role id
     *
     * @param roleId
     * @return
     */
    @GetMapping(value = "/allusersbyrole/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<UserRoleDTO> getAllUsersByRoleId(@PathVariable("roleId") final long roleId) {
        Preconditions.checkArgument(roleId >= 0, "RoleID must not be negative.");

        LOG.debug("Receive 'getAllUsersByRoleId' request with RoleID '{}'", roleId);

        final List<UserRoleDO> userRoleDOlist = userRoleComponent.findByRoleId(roleId);
        List<UserRoleDTO> userRoleDTOS = new ArrayList<>();

        for(UserRoleDO userRoleDO : userRoleDOlist){
            userRoleDTOS.add(UserRoleDTOMapper.toDTO.apply(userRoleDO));
        }

        return userRoleDTOS;
    }


    /**
     * I persist a new user and return this user entry.
     * <p>
     * Usage:
     * <pre>{@code Request: POST /v1/user
     * Body:
     * {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     * }
     * }</pre>
     * <pre>{@code Response:
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  }
     * }</pre>
     *
     * @param userCredentialsDTO of the request body
     *
     * @return {@link UserDTO} as JSON
     */
    @PostMapping(
            value = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_SYSTEMDATEN, UserPermission.CAN_CREATE_SYSTEMDATEN_LIGALEITER})
    public UserDTO create(final HttpServletRequest requestWithHeader,
                          @RequestBody final UserCredentialsDTO userCredentialsDTO) {

        Preconditions.checkNotNull(userCredentialsDTO, "User Credentials must not be null");
        Preconditions.checkNotNull(userCredentialsDTO.getUsername(), PRECONDITION_MSG_USER_ID);
        Preconditions.checkNotNull(userCredentialsDTO.getPassword(), PRECONDITION_MSG_USER_EMAIL);
        Preconditions.checkNotNull(userCredentialsDTO.getDsbMitgliedId(), PRECONDITION_MSG_DSB_MITGLIED_ID);
        // Check if password is valid by running it against the regular expression for the password
        Preconditions.checkArgument(userCredentialsDTO.getPassword().matches(PW_VALIDATION_REGEX), PRECONDITION_MSG_USER_PW);


        userCredentialsDTO.getCode();

        final String jwt = jwtTokenProvider.resolveToken(requestWithHeader);
        final Long userId = jwtTokenProvider.getUserId(jwt);

        // user anlegen

        final UserDO userCreatedDO = userComponent.create(userCredentialsDTO.getUsername(),
                userCredentialsDTO.getPassword(), userCredentialsDTO.getDsbMitgliedId(), userId, userCredentialsDTO.isUsing2FA());
        //default rolle anlegen (User)
        userRoleComponent.create(userCreatedDO.getId(), userId);
        //add a primitve boolean expression
        boolean hasKampfrichterLizenz = dsbMitgliedComponent.hasKampfrichterLizenz(userCreatedDO.getDsbMitgliedId());
        if(Boolean.TRUE.equals(hasKampfrichterLizenz)){
            final RoleDO roleDO = roleComponent.findByName(ROLE_KAMPFRICHTER);
            userRoleComponent.create(userCreatedDO.getId(),roleDO.getId(),userId);
        }
        return UserDTOMapper.toDTO.apply(userCreatedDO);
    }


    /**
     * Deactivates a user
     * @param id of the user, to be deactivated
     * @param requestWithHeader JwtToken used to prevent deleting your own user
     * @return true, if the deactivation was successful or the user is already disabled
     */
    @DeleteMapping(value = "{id}")
    @RequiresPermission(UserPermission.CAN_DELETE_SYSTEMDATEN)
    public boolean deactivate(@PathVariable("id") final long id, final HttpServletRequest requestWithHeader) {
        Preconditions.checkArgument(id >= 0, "Id must not be negative.");

        final String jwt = JwtTokenProvider.resolveToken(requestWithHeader);
        Preconditions.checkArgument(jwtTokenProvider.resolveUserSignInDTO(jwt).getId() != id, "You cannot delete yourself!");

        LOG.debug("Receive 'deactivate' request with Id '{}'", id);
        return userComponent.deactivate(id);
    }

}
