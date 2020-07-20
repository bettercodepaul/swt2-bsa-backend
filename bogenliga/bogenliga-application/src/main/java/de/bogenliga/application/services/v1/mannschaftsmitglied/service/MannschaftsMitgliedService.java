package de.bogenliga.application.services.v1.mannschaftsmitglied.service;

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
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.mannschaftsmitglied.mapper.MannschaftsMitgliedDTOMapper;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

@RestController
@CrossOrigin
@RequestMapping("v1/mannschaftsmitglied")
public class MannschaftsMitgliedService implements ServiceFacade {


    private static final String PRECONDITION_MSG_MANNSCHAFTSMITGLIED = "MannschaftsMitglied must not be null";
    private static final String PRECONDITION_MSG_MANNSCHAFTSMITGLIED_MANNSCHAFTS_ID = "MannschaftsMitglied ID must not be null";
    private static final String PRECONDITION_MSG_MANNSCHAFTSMITGLIED_DSB_MITGLIED_ID = "MannschaftsMitglied DSB MITGLIED ID must not be null";
    private static final String PRECONDITION_MSG_MANNSCHAFTSMITGLIED_MANNSCHAFTS_ID_NEGATIVE = "MannschaftsMitglied ID must not be negative";
    private static final String PRECONDITION_MSG_MANNSCHAFTSMITGLIED_DSB_MITGLIED_ID_NEGATIVE = "MannschaftsMitglied DSB MITGLIED ID must not be negative";
    private static final Logger LOG = LoggerFactory.getLogger(MannschaftsMitgliedService.class);



    /*
     * Business components
     *
     * dependency injection with {@link Autowired}
     */

    private final MannschaftsmitgliedComponent mannschaftsMitgliedComponent;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserComponent userComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final DsbMannschaftComponent dsbMannschaftComponent;


    @Autowired
    public MannschaftsMitgliedService(MannschaftsmitgliedComponent mannschaftsMitgliedComponent,
                                      JwtTokenProvider jwtTokenProvider,
                                      UserComponent userComponent,
                                      DsbMitgliedComponent dsbMitgliedComponent,
                                      DsbMannschaftComponent dsbMannschaftComponent) {
        this.mannschaftsMitgliedComponent = mannschaftsMitgliedComponent;

        this.jwtTokenProvider = jwtTokenProvider;
        this.userComponent = userComponent;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
    }


    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<MannschaftsMitgliedDTO> findAll() {
        final List<MannschaftsmitgliedDO> mannschaftmitgliedDOList = mannschaftsMitgliedComponent.findAll();
        return mannschaftmitgliedDOList.stream().map(MannschaftsMitgliedDTOMapper.toDTO).collect(Collectors.toList());
    }


    @RequestMapping(value = "{teamId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<MannschaftsMitgliedDTO> findByTeamId(@PathVariable("teamId") final long mannschaftsId) {
        final List<MannschaftsmitgliedDO> mannschaftmitgliedDOList = mannschaftsMitgliedComponent.findByTeamId(
                mannschaftsId);
        return mannschaftmitgliedDOList.stream().map(MannschaftsMitgliedDTOMapper.toDTO).collect(Collectors.toList());
    }


    @RequestMapping(value = "{teamIdInTeam}/{istEingesetzt}/{test3}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<MannschaftsMitgliedDTO> findAllSchuetzeInTeam(@PathVariable("teamIdInTeam") final long mannschaftsId) {
        final List<MannschaftsmitgliedDO> MannschaftmitgliedDOList = mannschaftsMitgliedComponent.findAllSchuetzeInTeamEingesetzt(
                mannschaftsId);
        return MannschaftmitgliedDOList.stream().map(MannschaftsMitgliedDTOMapper.toDTO).collect(Collectors.toList());
    }


    @RequestMapping(value = "{memberId}/{teamId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public MannschaftsMitgliedDTO findByMemberAndTeamId(@PathVariable("teamId") final long mannschaftsId,
                                                        @PathVariable("memberId") final long mitgliedId) {
        Preconditions.checkArgument(mannschaftsId > 0, "ID must not be negative.");
        Preconditions.checkArgument(mitgliedId > 0, "ID must not be negative.");

        LOG.debug("Receive 'findByMemberAndTeamId' request with memberID '{}' and teamID '{}'", mitgliedId,
                mannschaftsId);

        final MannschaftsmitgliedDO mannschaftsmitgliedDO = mannschaftsMitgliedComponent.findByMemberAndTeamId(
                mannschaftsId, mitgliedId);
        System.out.println(MannschaftsMitgliedDTOMapper.toDTO.apply(mannschaftsmitgliedDO));
        return MannschaftsMitgliedDTOMapper.toDTO.apply(mannschaftsmitgliedDO);
    }


    @RequestMapping(value = "/byMemberId/{memberId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<MannschaftsMitgliedDTO> findByMemberId(@PathVariable("memberId") final long memberId) {
        Preconditions.checkArgument(memberId > 0, "ID must not be negative.");

        LOG.debug("Receive 'findByMemberId' request with ID '{}'", memberId);

        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDO = mannschaftsMitgliedComponent.findByMemberId(memberId);
        return mannschaftsmitgliedDO.stream().map(MannschaftsMitgliedDTOMapper.toDTO).collect(Collectors.toList());
    }


    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_MANNSCHAFT, UserPermission.CAN_MODIFY_MY_VEREIN})
    public MannschaftsMitgliedDTO create(@RequestBody final MannschaftsMitgliedDTO mannschaftsMitgliedDTO,
                                         final Principal principal) throws NoPermissionException {

        checkPreconditions(mannschaftsMitgliedDTO);

        LOG.debug("Receive 'create' request with mannschaftsId '{}', dsbMitgliedId '{}', DsbMitgliedEingesetzt '{}',",

                mannschaftsMitgliedDTO.getMannschaftsId(),
                mannschaftsMitgliedDTO.getDsbMitgliedId(),
                mannschaftsMitgliedDTO.getDsbMitgliedEingesetzt());
        long tempId = dsbMannschaftComponent.findById(mannschaftsMitgliedDTO.getMannschaftsId()).getVereinId();
        if (this.hasPermissions(UserPermission.CAN_MODIFY_MANNSCHAFT) || this.hasSpecificPermission(
                UserPermission.CAN_MODIFY_MY_VEREIN, tempId)) {
        } else {
            throw new NoPermissionException();
        }
        final MannschaftsmitgliedDO newMannschaftsmitgliedDO = MannschaftsMitgliedDTOMapper.toDO.apply(
                mannschaftsMitgliedDTO);
        final long currentMemberId = UserProvider.getCurrentUserId(principal);


        final MannschaftsmitgliedDO savedMannschaftsmitgliedDO = mannschaftsMitgliedComponent.create(
                newMannschaftsmitgliedDO, currentMemberId);
        return MannschaftsMitgliedDTOMapper.toDTO.apply(savedMannschaftsmitgliedDO);
    }


    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VEREIN})
    public MannschaftsMitgliedDTO update(@RequestBody final MannschaftsMitgliedDTO mannschaftsMitgliedDTO,
                                         final Principal principal) throws NoPermissionException {
        checkPreconditions(mannschaftsMitgliedDTO);
        Preconditions.checkArgument(mannschaftsMitgliedDTO.getMannschaftsId() >= 0,
                PRECONDITION_MSG_MANNSCHAFTSMITGLIED_MANNSCHAFTS_ID);

        LOG.debug("Receive 'create' request with mannschaftsId '{}', dsbMitgliedId '{}', DsbMitgliedEingesetzt '{}',",
                mannschaftsMitgliedDTO.getMannschaftsId(),
                mannschaftsMitgliedDTO.getDsbMitgliedId(),
                mannschaftsMitgliedDTO.getDsbMitgliedEingesetzt());
        long tempId = dsbMannschaftComponent.findById(mannschaftsMitgliedDTO.getMannschaftsId()).getVereinId();
        if (this.hasPermissions(UserPermission.CAN_MODIFY_MANNSCHAFT) || this.hasSpecificPermission(
                UserPermission.CAN_MODIFY_MY_VEREIN, tempId)) {
        } else {
            throw new NoPermissionException();
        }
        final MannschaftsmitgliedDO newMannschaftsMitgliedDO = MannschaftsMitgliedDTOMapper.toDO.apply(
                mannschaftsMitgliedDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final MannschaftsmitgliedDO updatedMannschaftsmitgliedDO = mannschaftsMitgliedComponent.update(
                newMannschaftsMitgliedDO, userId);
        return MannschaftsMitgliedDTOMapper.toDTO.apply(updatedMannschaftsmitgliedDO);
    }


    /**
     * You are only able to delete a MannschaftsMitglied, if you have the explicit permission to Modify it or if you are
     * the Mannschaftsführer/Sportleiter of the Verein.
     **/

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VEREIN})
    public void delete(@PathVariable("id") final long id, final Principal principal) throws NoPermissionException {
        Preconditions.checkArgument(id >= 0, "Id must not be negative.");

        LOG.debug("Receive 'delete' request with Id '{}'", id);

        // allow value == null, the value will be ignored
        final MannschaftsmitgliedDO mannschaftsMitgliedDO = new MannschaftsmitgliedDO(id);
        final long currentUserId = UserProvider.getCurrentUserId(principal);
        long tempId = dsbMannschaftComponent.findById(mannschaftsMitgliedDO.getMannschaftId()).getVereinId();
        if (this.hasPermissions(UserPermission.CAN_MODIFY_MANNSCHAFT) || this.hasSpecificPermission(
                UserPermission.CAN_MODIFY_MY_VEREIN, tempId)) {
        } else {
            throw new NoPermissionException();
        }
        mannschaftsMitgliedComponent.delete(mannschaftsMitgliedDO, currentUserId);
    }


    @RequestMapping(value = "{mannschaftsId}/{mitgliedId}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void deleteByTeamIdAndMemberId(@PathVariable("mannschaftsId") final long mannschaftsId,
                                          @PathVariable("mitgliedId") final long mitgliedId,
                                          final Principal principal) throws NoPermissionException {
        Preconditions.checkArgument(mannschaftsId >= 0, "mannschaftsId must not be negative.");
        Preconditions.checkArgument(mitgliedId >= 0, "mitgliedId must not be negativ");

        LOG.debug("Receive 'delete' request with mannschaftsId '{}' and mitgliedsId '{}'", mannschaftsId, mitgliedId);

        // allow value == null, the value will be ignored
        final MannschaftsmitgliedDO mannschaftsMitgliedDO = new MannschaftsmitgliedDO(mannschaftsId, mitgliedId);
        final long currentUserId = UserProvider.getCurrentUserId(principal);
        long tempId = dsbMannschaftComponent.findById(mannschaftsMitgliedDO.getMannschaftId()).getVereinId();
        if (this.hasPermissions(UserPermission.CAN_MODIFY_MANNSCHAFT) || this.hasSpecificPermission(
                UserPermission.CAN_MODIFY_MY_VEREIN, tempId)) {
        } else {
            throw new NoPermissionException();
        }
        mannschaftsMitgliedComponent.deleteByTeamIdAndMemberId(mannschaftsMitgliedDO, currentUserId);
    }


    private void checkPreconditions(@RequestBody final MannschaftsMitgliedDTO mannschaftsMitgliedDTO) {
        Preconditions.checkNotNull(mannschaftsMitgliedDTO, PRECONDITION_MSG_MANNSCHAFTSMITGLIED);
        Preconditions.checkNotNull(mannschaftsMitgliedDTO.getMannschaftsId(),
                PRECONDITION_MSG_MANNSCHAFTSMITGLIED_MANNSCHAFTS_ID);
        Preconditions.checkNotNull(mannschaftsMitgliedDTO.getDsbMitgliedId(),
                PRECONDITION_MSG_MANNSCHAFTSMITGLIED_DSB_MITGLIED_ID);

        Preconditions.checkArgument(mannschaftsMitgliedDTO.getMannschaftsId() >= 0,
                PRECONDITION_MSG_MANNSCHAFTSMITGLIED_MANNSCHAFTS_ID_NEGATIVE);
        Preconditions.checkArgument(mannschaftsMitgliedDTO.getDsbMitgliedId() >= 0,
                PRECONDITION_MSG_MANNSCHAFTSMITGLIED_DSB_MITGLIED_ID_NEGATIVE);
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

}
