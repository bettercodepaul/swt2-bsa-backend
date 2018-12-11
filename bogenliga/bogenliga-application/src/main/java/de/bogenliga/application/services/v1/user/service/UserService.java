package de.bogenliga.application.services.v1.user.service;

import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.UserProfileComponent;
import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.service.ServiceFacade;
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
}
