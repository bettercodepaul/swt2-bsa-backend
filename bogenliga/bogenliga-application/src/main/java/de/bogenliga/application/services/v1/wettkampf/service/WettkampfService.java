package de.bogenliga.application.services.v1.wettkampf.service;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.wettkampf.mapper.WettkampfDTOMapper;
import de.bogenliga.application.services.v1.wettkampf.model.WettkampfDTO;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;


/**
 * I'm a REST resource and handle Wettkampf CRUD requests over the HTTP protocol
 *
 * @author Marvin Holm, Daniel Schott
 */
@RestController
@CrossOrigin
@RequestMapping("v1/wettkampf")
public class WettkampfService implements ServiceFacade {
    private static final String PRECONDITION_MSG_WETTKAMPF = "WettkampfDO must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_ID = "Wettkampf ID must not be negative and must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID = "Wettkampfveranstaltungsid must not be negative and must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_DATUM = "Format: YYYY-MM-DD Format must be correct,  Wettkampfdatum must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_STRASSE = "WettkampfStraße must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_PLZ = "Wettkampfplz must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_ORTSNAME = "WettkampfNamemust not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_ORTSINFO = "WettkampfOrstinfo must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_BEGINN = "Format: HH:MM, Format must be correct, Wettkampfbeginn must not be null";
    private static final String PRECONDITION_MSG_WETTKAMPF_TAG = "Must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_DISZIPLIN_ID = "Must not be null and must not be negative";
    private static final String PRECONDITION_MSG_WETTKAMPF_TYP_ID = "Must not be null and must not be negative";

    private static final Logger LOG = LoggerFactory.getLogger(WettkampfService.class);

    private final WettkampfComponent wettkampfComponent;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserComponent userComponent;


    /**
     * Constructor with dependency injection
     *
     * @param wettkampfComponent to handle the database CRUD requests
     * @param jwtTokenProvider
     * @param userComponent
     */

    @Autowired
    public WettkampfService(final WettkampfComponent wettkampfComponent,
                            JwtTokenProvider jwtTokenProvider,
                            UserComponent userComponent) {
        this.wettkampfComponent = wettkampfComponent;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userComponent = userComponent;
    }


    /**
     * findAll-Method gives back all Wettkämpfe safed in the Database.
     *
     * @return wettkampfDoList - List filled with Data Objects of Wettkämpfe
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<WettkampfDTO> findAll() {
        final List<WettkampfDO> wettkampfDoList = wettkampfComponent.findAll();
        return wettkampfDoList.stream().map(WettkampfDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * findByID-Method gives back a specific Wettkampf according to a single Wettkampf_ID
     *
     * @param id - single id of the Wettkampf you want te access
     *
     * @return
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public WettkampfDTO findById(@PathVariable("id") final long id) {
        Preconditions.checkArgument(id > 0, "ID must not be negative.");

        LOG.debug("Receive 'findById' request with ID '{}'", id);

        final WettkampfDO wettkampfDO = wettkampfComponent.findById(id);
        wettkampfDO.toString();
        return WettkampfDTOMapper.toDTO.apply(wettkampfDO);
    }


    /**
     * findAllWettkaempfeByMannschaftsId-Method gives back all Wettkämpfe according to a MannschaftsId.
     *
     * @return wettkampfDoList - List filled with Data Objects of Wettkämpfe
     */
    @RequestMapping(value = "byMannschaftsId/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<WettkampfDTO> findAllWettkaempfeByMannschaftsId(@PathVariable("id") final long id) {
        final List<WettkampfDO> wettkampfDoList = wettkampfComponent.findAllWettkaempfeByMannschaftsId(id);
        return wettkampfDoList.stream().map(WettkampfDTOMapper.toDTO).collect(Collectors.toList());
    }


    @RequestMapping(value = "byVeranstaltungId/{veranstaltungId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<WettkampfDTO> findAllByVeranstaltungId(@PathVariable("veranstaltungId") final long veranstaltungId) {
        Preconditions.checkArgument(veranstaltungId >= 0, "Veranstaltung ID must not be negative.");

        LOG.debug("GET request for findAllByVeranstaltungId with ID '{}'", veranstaltungId);
        final List<WettkampfDO> wettkampfDOList = this.wettkampfComponent.findAllByVeranstaltungId(veranstaltungId);
        return wettkampfDOList.stream().map(WettkampfDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     * create-Method() writes a new entry of Wettkampf into the database
     *
     * @param wettkampfDTO
     * @param principal
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_CREATE_STAMMDATEN)
    public WettkampfDTO create(@RequestBody final WettkampfDTO wettkampfDTO, final Principal principal) {

        checkPreconditions(wettkampfDTO);

        LOG.debug(
                "Received 'create' request with id '{}', Datum '{}', VeranstaltungsID'{}', WettkampfDisziplinID'{}', Wettkampfstrasse'{}', Wettkampfplz'{}', Wettkampfortsname'{}', Wettkampfortsinfo'{}'," +
                        " WettkampfTag '{}', WettkampfBeginn'{}', WettkampfTypID '{}' ",
                wettkampfDTO.getId(),
                wettkampfDTO.getDatum(),
                wettkampfDTO.getwettkampfVeranstaltungsId(),
                wettkampfDTO.getWettkampfDisziplinId(),
                wettkampfDTO.getWettkampfStrasse(),
                wettkampfDTO.getWettkampfPlz(),
                wettkampfDTO.getWettkampfOrtsname(),
                wettkampfDTO.getWettkampfOrtsinfo(),
                wettkampfDTO.getWettkampfTag(),
                wettkampfDTO.getWettkampfBeginn(),
                wettkampfDTO.getWettkampfTypId());

        final WettkampfDO newWettkampfDO = WettkampfDTOMapper.toDO.apply(wettkampfDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final WettkampfDO savedWettkampfDO = wettkampfComponent.create(newWettkampfDO, userId);
        return WettkampfDTOMapper.toDTO.apply(savedWettkampfDO);
    }


    /**
     * Update-Method changes the chosen Wettkampf entry in the Database
     *
     * @param wettkampfDTO
     * @param principal
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_WETTKAMPF, UserPermission.CAN_MODIFY_MY_WETTKAMPF})
    public WettkampfDTO update(@RequestBody final WettkampfDTO wettkampfDTO,
                               final Principal principal) throws NoPermissionException {
        checkPreconditions(wettkampfDTO);

        LOG.debug(
                "Received 'update' request with id '{}', Datum '{}', VeranstaltungsID'{}', WettkampfDisziplinID'{}', Wettkampfort'{}'," +
                        " WettkampfTag '{}', WettkampfBeginn'{}', WettkampfTypID '{}' ",
                wettkampfDTO.getId(),
                wettkampfDTO.getDatum(),
                wettkampfDTO.getwettkampfVeranstaltungsId(),
                wettkampfDTO.getWettkampfDisziplinId(),
                wettkampfDTO.getWettkampfStrasse(),
                wettkampfDTO.getWettkampfPlz(),
                wettkampfDTO.getWettkampfOrtsname(),
                wettkampfDTO.getWettkampfOrtsinfo(),
                wettkampfDTO.getWettkampfTag(),
                wettkampfDTO.getWettkampfBeginn(),
                wettkampfDTO.getWettkampfTypId());
        if (this.hasPermission(UserPermission.CAN_MODIFY_WETTKAMPF) || this.hasSpecificPermission(
                UserPermission.CAN_MODIFY_MY_WETTKAMPF, wettkampfDTO.getId())) {

        } else {
            throw new NoPermissionException();
        }
        final WettkampfDO newWettkampfDO = WettkampfDTOMapper.toDO.apply(wettkampfDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final WettkampfDO updatedWettkampfDO = wettkampfComponent.update(newWettkampfDO, userId);
        return WettkampfDTOMapper.toDTO.apply(updatedWettkampfDO);
    }


    /**
     * Delete-Method removes an entry from the database
     *
     * @param id
     * @param principal
     */
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        // allow value == null, the value will be ignored
        final WettkampfDO wettkampfDO = new WettkampfDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        wettkampfComponent.delete(wettkampfDO, userId);
    }


    /**
     * checks the preconditions defined above in this class
     *
     * @param wettkampfDTO
     */
    private void checkPreconditions(@RequestBody final WettkampfDTO wettkampfDTO) {
        Preconditions.checkNotNull(wettkampfDTO, PRECONDITION_MSG_WETTKAMPF);
        Preconditions.checkNotNull(wettkampfDTO.getDatum(), PRECONDITION_MSG_WETTKAMPF_DATUM);

        Preconditions.checkNotNull(wettkampfDTO.getWettkampfBeginn(), PRECONDITION_MSG_WETTKAMPF_BEGINN);
        Preconditions.checkNotNull(wettkampfDTO.getWettkampfStrasse(), PRECONDITION_MSG_WETTKAMPF_STRASSE);
        Preconditions.checkNotNull(wettkampfDTO.getWettkampfPlz(), PRECONDITION_MSG_WETTKAMPF_PLZ);
        Preconditions.checkNotNull(wettkampfDTO.getWettkampfOrtsname(), PRECONDITION_MSG_WETTKAMPF_ORTSNAME);
        Preconditions.checkNotNull(wettkampfDTO.getWettkampfOrtsinfo(), PRECONDITION_MSG_WETTKAMPF_ORTSINFO);

        Preconditions.checkNotNull(wettkampfDTO.getWettkampfDisziplinId() >= 0,
                PRECONDITION_MSG_WETTKAMPF_DISZIPLIN_ID);
        Preconditions.checkNotNull(wettkampfDTO.getwettkampfVeranstaltungsId() >= 0,
                PRECONDITION_MSG_WETTKAMPF_VERANSTALTUNGS_ID);
        Preconditions.checkArgument(wettkampfDTO.getWettkampfTypId() >= 0, PRECONDITION_MSG_WETTKAMPF_TYP_ID);
        Preconditions.checkArgument(wettkampfDTO.getWettkampfTag() >= 0, PRECONDITION_MSG_WETTKAMPF_TAG);
    }


    /**
     * method to check, if a user has a general permission
     *
     * @param toTest The permission whose existence is getting checked
     *
     * @return Does the User have the searched permission
     */
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


    /**
     * method to check, if a user has a Specific permission with the matching parameters
     *
     * @param toTest The permission whose existence is getting checked
     *
     * @return Does the User have searched permission
     */
    boolean hasSpecificPermission(UserPermission toTest, Long wettkampfID) {
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
                ArrayList<Integer> temp = new ArrayList<>();
                for (WettkampfDO wettkampfDO : this.wettkampfComponent.findByAusrichter(UserId)) {
                    if (wettkampfDO.getId().equals(wettkampfID)) {
                        result = true;
                        break;
                    }
                }

            }
        }
        return result;
    }
}
