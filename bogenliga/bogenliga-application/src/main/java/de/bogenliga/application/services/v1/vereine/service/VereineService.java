package de.bogenliga.application.services.v1.vereine.service;

import java.security.Principal;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.vereine.mapper.VereineDTOMapper;
import de.bogenliga.application.services.v1.vereine.model.VereineDTO;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I'm a REST resource and handle vereine CRUD requests over the HTTP protocol
 *
 * @author Dennis Goericke, dennis.goericke@student.reutlingen-university.de
 */
@RestController
@CrossOrigin
@RequestMapping("v1/vereine")

public class VereineService implements ServiceFacade {
    private static final String PRECONDITION_MSG_VEREIN = "Verein must not be null";
    private static final String PRECONDITION_MSG_VEREIN_ID = "Verein ID must not be negative";
    private static final String PRECONDITION_MSG_NAME = "Name must not be null ";
    private static final String PRECONDITION_MSG_VEREIN_DSB_IDENTIFIER = "Verein dsb Identifier must not be null";
    private static final String PRECONDITION_MSG_REGION_ID_NOT_NEG = "Verein regio ID must not be negative";
    private static final String PRECONDITION_MSG_REGION_ID = "Verein regio ID can not be null";

    private static final Logger LOG = LoggerFactory.getLogger(VereineService.class);

    private final VereinComponent vereinComponent;

    private final JwtTokenProvider jwtTokenProvider;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final UserComponent userComponent;


    /**
     * Constructor with dependency injection
     *
     * @param vereinComponent to handle the database CRUD requests
     */
    @Autowired
    public VereineService(final VereinComponent vereinComponent, final JwtTokenProvider jwtTokenProvider,
                          final DsbMitgliedComponent dsbMitgliedComponent, final UserComponent userComponent) {
        this.vereinComponent = vereinComponent;
        this.userComponent = userComponent;
        this.jwtTokenProvider = jwtTokenProvider;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
    }

    /**
     * I return all the teams (Vereine) of the database.
     *
     * @return list of {@link VereineDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VereineDTO> findAll() {
        final List<VereinDO> vereinDOList = vereinComponent.findAll();
        return vereinDOList.stream().map(VereineDTOMapper.toDTO).collect(Collectors.toList());
    }

    /**
     * I return the verein Entry of the database with a specific id
     *
     * @return list of {@link VereineDTO} as JSON
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public VereineDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative");

        LOG.debug("Receive 'findById' with requested ID '{}'", id);

        final VereinDO vereinDO = vereinComponent.findById(id);

        return VereineDTOMapper.toDTO.apply(vereinDO);
    }

    /**
     * I persist a newer version of the dsbMitglied in the database.
     * <p>
     * You are only able to modify the Verein, if you have the explicit permission to Modify it or if you are the
     * Mannschaftsführer/Sportleiter of the Verein.
     */
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VEREIN})
    public VereineDTO update(@RequestBody final VereineDTO vereineDTO,
                             final Principal principal) throws NoPermissionException {
        checkPreconditions(vereineDTO);
        Preconditions.checkArgument(vereineDTO.getId() >= 0, PRECONDITION_MSG_VEREIN_ID);

        LOG.debug(
                "Receive  'update' request with id '{}', name '{}', dsb_identifier '{}'," +
                "region_id '{}', website '{}', description '{}', icon '{}'",
                vereineDTO.getId(),
                vereineDTO.getName(),
                vereineDTO.getIdentifier(),
                vereineDTO.getRegionId(),
                vereineDTO.getWebsite(),
                vereineDTO.getDescription(),
                vereineDTO.getIcon());

        if (this.hasPermissions(UserPermission.CAN_MODIFY_STAMMDATEN)) {
        } else if (this.hasSpecificPermission(UserPermission.CAN_MODIFY_MY_VEREIN, vereineDTO.getId())) {
            VereinDO temp = vereinComponent.findById(vereineDTO.getId());
            if (temp.getRegionId() != vereineDTO.getRegionId()) {
                throw new NoPermissionException();
            }
        } else {
            throw new NoPermissionException();
        }
        final VereinDO newVereinDo = VereineDTOMapper.toDO.apply(vereineDTO);
        final long userID = UserProvider.getCurrentUserId(principal);

        final VereinDO updateVereinDO = vereinComponent.update(newVereinDo, userID);
        return VereineDTOMapper.toDTO.apply(updateVereinDO);
    }

    /**
     * I delete an existing Verein entry from the DB.
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        final VereinDO vereinDO = new VereinDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);
        vereinComponent.delete(vereinDO, userId);
    }

    /**
     * I persist a new verein and return this verein entry.
     *
     * @param vereineDTO of the request body
     * @param principal  authenticated user
     *
     * @return list of {@link VereineDTO} as JSON
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_CREATE_STAMMDATEN)
    public VereineDTO create(@RequestBody final VereineDTO vereineDTO, final Principal principal) {
        checkPreconditions(vereineDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        LOG.debug(
                "Receive 'create' request with name '{}', identifier '{}', region id '{}', website '{}', " +
                        "description '{}', icon '{}', version '{}', createdBy '{}'",
                vereineDTO.getName(),
                vereineDTO.getIdentifier(),
                vereineDTO.getRegionId(),
                vereineDTO.getWebsite(),
                vereineDTO.getDescription(),
                vereineDTO.getIcon(),
                vereineDTO.getVersion(),
                userId);

        final VereinDO vereinDO = VereineDTOMapper.toDO.apply(vereineDTO);
        final VereinDO persistedVereinDO = vereinComponent.create(vereinDO, userId);

        return VereineDTOMapper.toDTO.apply(persistedVereinDO);
    }

    private void checkPreconditions(@RequestBody final VereineDTO vereinDTO) {
        Preconditions.checkNotNull(vereinDTO, PRECONDITION_MSG_VEREIN);
        Preconditions.checkNotNull(vereinDTO.getName(), PRECONDITION_MSG_NAME);
        Preconditions.checkNotNull(vereinDTO.getIdentifier(), PRECONDITION_MSG_VEREIN_DSB_IDENTIFIER);
        Preconditions.checkNotNull(vereinDTO.getRegionId(), PRECONDITION_MSG_REGION_ID);

        Preconditions.checkArgument(vereinDTO.getRegionId() >= 0, PRECONDITION_MSG_REGION_ID_NOT_NEG);
    }

    /**
     * method to check, if a user has a Specific permission with the matching parameters
     *
     * @param toTest The permission whose existence is getting checked
     *
     * @return Does the User have searched permission
     */
    boolean hasSpecificPermission(UserPermission toTest, Long vereinsId) {
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

                //check if the current Users vereinsId equals the given vereinsId and if the User has
                //the required Permission (if the permission is specifi
                Long UserId = jwtTokenProvider.getUserId(jwt);
                UserDO userDO = this.userComponent.findById(UserId);
                DsbMitgliedDO dsbMitgliedDO = this.dsbMitgliedComponent.findById(userDO.getDsb_mitglied_id());
                if ((dsbMitgliedDO.getVereinsId() == vereinsId) && userPermissions.contains(toTest)) {
                    result = true;
                }
            }
        }
        return result;
    }

    boolean hasPermissions(UserPermission toTest) {
        boolean result = false;
        // get current http request from thread
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;

            final HttpServletRequest request = servletRequestAttributes.getRequest();

            // if request present
            if (request != null) {
                // parse json web token with roles
                final String jwt = JwtTokenProvider.resolveToken(request);

                // custom permission check
                final Set<UserPermission> userPermissions = jwtTokenProvider.getPermissions(jwt);
                if (userPermissions.contains(toTest)) {
                    result = true;
                }
            }
        }
        return result;
    }
}