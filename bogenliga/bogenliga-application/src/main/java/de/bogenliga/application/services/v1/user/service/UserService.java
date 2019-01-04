package de.bogenliga.application.services.v1.user.service;

import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.UserProfileComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.common.errorhandling.ErrorCategory;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.common.errorhandling.ErrorDTO;
import de.bogenliga.application.services.v1.user.mapper.UserDTOMapper;
import de.bogenliga.application.services.v1.user.mapper.UserProfileDTOMapper;
import de.bogenliga.application.services.v1.user.model.UserCredentialsDTO;
import de.bogenliga.application.services.v1.user.model.UserDTO;
import de.bogenliga.application.services.v1.user.model.UserProfileDTO;
import de.bogenliga.application.services.v1.user.model.UserSignInDTO;
import de.bogenliga.application.springconfiguration.security.WebSecurityConfiguration;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
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
import javax.validation.constraints.Null;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * I´m a REST resource and handle configuration CRUD requests over the HTTP protocol.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
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

    private static final String PRECONDITION_MSG_USER = "BenutzerDO must not be null";
    private static final String PRECONDITION_MSG_USER_ID = "BenutzerDO ID must not be negative";
    private static final String PRECONDITION_MSG_USER_EMAIL = "Benutzer email must not be null";
    private static final String PRECONDITION_MSG_USER_PASSWORD = "Benutzer password must not be null";
    private static final String PRECONDITION_MSG_USER_NEWPASSWORD = "Benutzer new password must not be null";

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final JwtTokenProvider jwtTokenProvider;

    private final WebSecurityConfiguration webSecurityConfiguration;

    private final UserComponent userComponent;

    private final UserProfileComponent userProfileComponent;


    @Autowired
    public UserService(final JwtTokenProvider jwtTokenProvider,
                       //final AuthenticationManager authenticationManager
                       final WebSecurityConfiguration webSecurityConfiguration,
                       final UserComponent userComponent,
                       final UserProfileComponent userProfileComponent) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.webSecurityConfiguration = webSecurityConfiguration;
        this.userComponent = userComponent;
        this.userProfileComponent = userProfileComponent;
    }


    @RequestMapping(
            method = RequestMethod.POST,
            value = "/signin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody final UserCredentialsDTO credentials) {
        Preconditions.checkNotNull(credentials, "Credentials must not be null");
        Preconditions.checkNotNullOrEmpty(credentials.getUsername(), "Username must not be null or empty");
        Preconditions.checkNotNullOrEmpty(credentials.getPassword(), "Password must not be null or empty");

        ErrorDTO errorDetails = null;
        try {
            final Authentication authentication = webSecurityConfiguration.authenticationManagerBean()
                    .authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));

            if (authentication.isAuthenticated()) {
                // create payload
                final UserWithPermissionsDO userWithPermissionsDO = (UserWithPermissionsDO) authentication.getPrincipal();
                final UserSignInDTO userSignInDTO = UserDTOMapper.toUserSignInDTO.apply(userWithPermissionsDO);
                userSignInDTO.setJwt(jwtTokenProvider.createToken(authentication));

                final HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Bearer " + userSignInDTO.getJwt());

                return ResponseEntity.status(HttpStatus.OK).headers(headers).body(userSignInDTO);
            } else {
                if (authentication.getDetails() != null) {
                    errorDetails = (ErrorDTO) authentication.getDetails();
                }
            }
        } catch (final Exception e) { // NOSONAR
            LOG.warn("An error occured while SignIn of user {}", credentials.getUsername(), e);
        }

        // return error details from authentication or a default error
        errorDetails = errorDetails != null ? errorDetails : new ErrorDTO(ErrorCode.INVALID_SIGN_IN_CREDENTIALS, "Sign in failed");
        return new ResponseEntity<>(errorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
    }



    @RequestMapping(
            method = RequestMethod.POST,
            value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOwnIdentity
    public ResponseEntity update(@RequestBody final UserCredentialsDTO uptcredentials,  final Principal principal) {
        Preconditions.checkNotNull(uptcredentials, "Credentials must not be null");
        Preconditions.checkNotNullOrEmpty(uptcredentials.getUsername(), "Username must not be null or empty");
        Preconditions.checkNotNullOrEmpty(uptcredentials.getPassword(), "Password must not be null or empty");

        ErrorDTO errorDetails = null;

        final long userId = UserProvider.getCurrentUserId(principal);
        final UserDO userDO = userComponent.findByEmail(uptcredentials.getUsername());

        // we will only allow changes to password in case autheticated user changes his own password
        if(userId == userDO.getId() ){
            final UserDO userUpdatedDO = userComponent.update(userDO, uptcredentials.getPassword(), userId );
            final UserDTO userUpdatedDTO = UserDTOMapper.toUserDTO.apply(userUpdatedDO);

            final HttpHeaders headers = new HttpHeaders();
            headers.add("Update User", "Bearer " + userUpdatedDTO.getEmail());

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(userUpdatedDTO);
        }
        else {

            // return error details from authentication or a default error
            errorDetails = errorDetails != null ? errorDetails : new ErrorDTO(ErrorCode.INSUFFICIENT_CREDENTIALS, "Update failed");
            return new ResponseEntity<>(errorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


/*
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/admchpwd",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public UserDTO admchpwd(@RequestBody final UserCredentialsDTO uptcredentials,  final Principal principal) {
        Preconditions.checkNotNull(uptcredentials, "Credentials must not be null");
        Preconditions.checkNotNullOrEmpty(uptcredentials.getUsername(), "Username must not be null or empty");
        Preconditions.checkNotNullOrEmpty(uptcredentials.getPassword(), "Password must not be null or empty");

        // admins may update all passwords
        final long userId = UserProvider.getCurrentUserId(principal);
        final UserDO userDO = userComponent.findByEmail(uptcredentials.getUsername());

        final UserDO userUpdatedDO = userComponent.update(userDO, uptcredentials.getPassword(), userId );

        return UserDTOMapper.toDTO.apply(userUpdatedDO);
    }
*/


    @RequestMapping(
            method = RequestMethod.GET,
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
     * @return
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOwnIdentity
    public UserProfileDTO getUserProfileById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'getUserProfileById' request with ID '{}'", id);

        final UserProfileDO userProfileDO = userProfileComponent.findById(id);
        return UserProfileDTOMapper.toDTO.apply(userProfileDO);
    }

    /**
     * I return all user entries of the database.

     *
     * Usage:
     * <pre>{@code Request: GET /v1/user}</pre>
     *
     * [
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  },
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.interval",
     *    "value": "10"
     *  }
     * ]
     * }
     * </pre>
     *
     * @return list of {@link UserDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_SYSTEMDATEN)
    public List<UserDTO> findAll() {
        final List<UserDO> userDOList = userComponent.findAll();
        return userDOList.stream().map(UserDTOMapper.toDTO).collect(Collectors.toList());
    }

    /**
     * I persist a new user and return this user entry.
     *
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
     * @param userCredentialsDTO of the request body
     * @param principal authenticated user
     * @return  {@link userDO} as JSON
     */


    @RequestMapping(method = RequestMethod.POST,
            value = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_SYSTEMDATEN)
    public UserDTO create(@RequestBody final UserCredentialsDTO userCredentialsDTO, final Principal principal) {

        ErrorDTO errorDetails = null;

        LOG.debug("Receive 'create' request with username '{}', password '{}'",
                userCredentialsDTO.getUsername(),
                userCredentialsDTO.getPassword());

        final long userId = UserProvider.getCurrentUserId(principal);

        final UserDO userCreatedDO = userComponent.create(userCredentialsDTO.getUsername(), userCredentialsDTO.getPassword(), userId);
        return UserDTOMapper.toDTO.apply(userCreatedDO);
    }



}
