package de.bogenliga.application.services.v1.kampfrichter.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import de.bogenliga.application.business.kampfrichter.api.KampfrichterComponent;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterDTO;
import de.bogenliga.application.services.v1.kampfrichter.mapper.KampfrichterDTOMapper;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I´m a REST resource and handle Kampfrichter CRUD requests over the HTTP protocol.
 *
 * @author
 * @see <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete">Wikipedia - CRUD</a>
 * @see <a href="https://en.wikipedia.org/wiki/Representational_state_transfer">Wikipedia - REST</a>
 * @see <a href="https://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol">Wikipedia - HTTP</a>
 * @see <a href="https://en.wikipedia.org/wiki/Design_by_contract">Wikipedia - Design by contract</a>
 * @see <a href="https://spring.io/guides/gs/actuator-service/">
 * Building a RESTful Web Service with Spring Boot Actuator</a>
 * @see <a href="https://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-dsbMitglied">
 * Build a REST API with Spring 4 and Java Config</a>
 * @see <a href="https://www.baeldung.com/spring-autowire">Guide to Spring @Autowired</a>
 */
@RestController
@CrossOrigin
@RequestMapping("v1/kampfrichter")
public class KampfrichterService implements ServiceFacade {
    // TODO: Implement this entire class
    private static final String PRECONDITION_MSG_KAMPFRICHTER = "KampfrichterDO must not be null";
    private static final String PRECONDITION_MSG_KAMPFRICHTER_BENUTZER_ID = "KampfrichterBenutzerID must not be negative and must not be null";
    private static final String PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID = "KampfrichterWettkampfID must not be negative and must not be null";

    private static final Logger LOG = LoggerFactory.getLogger(KampfrichterService.class);

    private final KampfrichterComponent kampfrichterComponent;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserComponent userComponent;


    @Autowired
    public KampfrichterService(final KampfrichterComponent kampfrichterComponent,
                               JwtTokenProvider jwtTokenProvider,
                               UserComponent userComponent) {
        this.kampfrichterComponent = kampfrichterComponent;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userComponent = userComponent;
    }


    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<KampfrichterDTO> findAll() {
        final List<KampfrichterDO> kampfrichterDOList = kampfrichterComponent.findAll();
        return kampfrichterDOList.stream().map(KampfrichterDTOMapper.toDTO).collect(Collectors.toList());
    }


    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_CREATE_STAMMDATEN)
    public KampfrichterDTO create(@RequestBody final KampfrichterDTO kampfrichterDTO, final Principal principal) {

        checkPreconditions(kampfrichterDTO);

        LOG.debug("Received 'create' request with userID '{}', wettkampfID '{}', leitend'{}'",
                kampfrichterDTO.getUserId(),
                kampfrichterDTO.getWettkampfId(),
                kampfrichterDTO.getLeitend());

        final KampfrichterDO newKampfrichterDO = KampfrichterDTOMapper.toDO.apply(kampfrichterDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final KampfrichterDO savedKampfrichterDO = kampfrichterComponent.create(newKampfrichterDO, userId);
        return KampfrichterDTOMapper.toDTO.apply(savedKampfrichterDO);
    }


    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF, UserPermission.CAN_MODIFY_MY_WETTKAMPF})
    public KampfrichterDTO update(@RequestBody final KampfrichterDTO kampfrichterDTO,
                                  final Principal principal) throws NoPermissionException {
        checkPreconditions(kampfrichterDTO);

        LOG.debug(
                "Received 'update' request with userID '{}', wettkampfID '{}', leitend'{}'",
                kampfrichterDTO.getUserId(),
                kampfrichterDTO.getWettkampfId(),
                kampfrichterDTO.getLeitend());

//        if (this.hasPermission(UserPermission.CAN_MODIFY_WETTKAMPF) || this.hasSpecificPermission(
//                UserPermission.CAN_MODIFY_MY_WETTKAMPF, kampfrichterDTO.getUserId())) {
//
//        } else {
//            throw new NoPermissionException();
//        }
        final KampfrichterDO newKampfrichterDO = KampfrichterDTOMapper.toDO.apply(kampfrichterDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final KampfrichterDO updatedKampfrichterDO = kampfrichterComponent.update(newKampfrichterDO, userId);
        return KampfrichterDTOMapper.toDTO.apply(updatedKampfrichterDO);
    }


    boolean hasPermission(UserPermission toTest) {
        //default value is: not allowed
        boolean result = false;
        //get the current http request from thread
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            final HttpServletRequest request = servletRequestAttributes.getRequest();
            //if a request is present:
            if (request != null) {
                //parse the Webtoken and get the UserPermissions of the current User
                final String jwt = JwtTokenProvider.resolveToken(request);
                final Set<UserPermission> userPermissions = jwtTokenProvider.getPermissions(jwt);

                //check if the resolved Permissions
                //contain the required Permission for the task.
                if (userPermissions.contains(toTest)) {
                    result = true;
                }
            }
        }
        return result;
    }


//    boolean hasSpecificPermission(UserPermission toTest, Long wettkampfID) {
//        //default value is: not allowed
//        boolean result = false;
//        //get the current http request from thread
//        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        if (requestAttributes != null) {
//            final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
//            final HttpServletRequest request = servletRequestAttributes.getRequest();
//            //if a request is present:
//            if (request != null) {
//                //parse the Webtoken and get the UserPermissions of the current User
//                final String jwt = JwtTokenProvider.resolveToken(request);
//                final Set<UserPermission> userPermissions = jwtTokenProvider.getPermissions(jwt);
//
//                //check if the current Users vereinsId equals the given vereinsId and if the User has
//                //the required Permission (if the permission is specifi
//                Long UserId = jwtTokenProvider.getUserId(jwt);
//                UserDO userDO = this.userComponent.findById(UserId);
//                ArrayList<Integer> temp = new ArrayList<>();
//                for (WettkampfDO wettkampfDO : this.wettkampfComponent.findByAusrichter(UserId)) {
//                    if (wettkampfDO.getId().equals(wettkampfID)) {
//                        result = true;
//                        break;
//                    }
//                }
//
//            }
//        }
//        return result;
//    }


    private void checkPreconditions(@RequestBody final KampfrichterDTO kampfrichterDTO) {
        Preconditions.checkNotNull(kampfrichterDTO, PRECONDITION_MSG_KAMPFRICHTER);
        Preconditions.checkArgument(kampfrichterDTO.getUserId() >= 0, PRECONDITION_MSG_KAMPFRICHTER_BENUTZER_ID);
        Preconditions.checkArgument(kampfrichterDTO.getWettkampfId() >= 0, PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID);
    }
}
