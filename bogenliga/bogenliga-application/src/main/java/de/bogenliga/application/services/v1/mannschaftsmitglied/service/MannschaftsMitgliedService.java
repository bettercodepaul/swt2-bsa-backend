package de.bogenliga.application.services.v1.mannschaftsmitglied.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
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
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.mannschaftsmitglied.mapper.MannschaftsMitgliedDTOMapper;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
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


    @Autowired
    public MannschaftsMitgliedService(MannschaftsmitgliedComponent mannschaftsMitgliedComponent) {
        this.mannschaftsMitgliedComponent = mannschaftsMitgliedComponent;

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

        LOG.debug("Receive 'findByMemberAndTeamId' request with memberID '{}' and teamID '{}'", mitgliedId, mannschaftsId);

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
    @RequiresPermission(UserPermission.CAN_MODIFY_MY_VEREIN)
    public MannschaftsMitgliedDTO create(@RequestBody final MannschaftsMitgliedDTO mannschaftsMitgliedDTO,
                                         final Principal principal) {

        checkPreconditions(mannschaftsMitgliedDTO);

        LOG.debug("Receive 'create' request with mannschaftsId '{}', dsbMitgliedId '{}', DsbMitgliedEingesetzt '{}',",

                mannschaftsMitgliedDTO.getMannschaftsId(),
                mannschaftsMitgliedDTO.getDsbMitgliedId(),
                mannschaftsMitgliedDTO.getDsbMitgliedEingesetzt());

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
    @RequiresPermission(UserPermission.CAN_MODIFY_MY_VEREIN)
    public MannschaftsMitgliedDTO update(@RequestBody final MannschaftsMitgliedDTO mannschaftsMitgliedDTO,
                                         final Principal principal) {
        checkPreconditions(mannschaftsMitgliedDTO);
        Preconditions.checkArgument(mannschaftsMitgliedDTO.getMannschaftsId() >= 0,
                PRECONDITION_MSG_MANNSCHAFTSMITGLIED_MANNSCHAFTS_ID);

        LOG.debug("Receive 'create' request with mannschaftsId '{}', dsbMitgliedId '{}', DsbMitgliedEingesetzt '{}',",
                mannschaftsMitgliedDTO.getMannschaftsId(),
                mannschaftsMitgliedDTO.getDsbMitgliedId(),
                mannschaftsMitgliedDTO.getDsbMitgliedEingesetzt());

        final MannschaftsmitgliedDO newMannschaftsMitgliedDO = MannschaftsMitgliedDTOMapper.toDO.apply(
                mannschaftsMitgliedDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final MannschaftsmitgliedDO updatedMannschaftsmitgliedDO = mannschaftsMitgliedComponent.update(
                newMannschaftsMitgliedDO, userId);
        return MannschaftsMitgliedDTOMapper.toDTO.apply(updatedMannschaftsmitgliedDO);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, "Id must not be negative.");

        LOG.debug("Receive 'delete' request with Id '{}'", id);

        // allow value == null, the value will be ignored
        final MannschaftsmitgliedDO mannschaftsMitgliedDO = new MannschaftsmitgliedDO(id);
        final long currentUserId = UserProvider.getCurrentUserId(principal);

        mannschaftsMitgliedComponent.delete(mannschaftsMitgliedDO, currentUserId);
    }

    @RequestMapping(value = "{mannschaftsId}/{mitgliedId}", method = RequestMethod.DELETE)
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void deleteByTeamIdAndMemberId(@PathVariable("mannschaftsId") final long mannschaftsId,
                       @PathVariable("mitgliedId") final long mitgliedId, final Principal principal) {
        Preconditions.checkArgument(mannschaftsId >= 0, "mannschaftsId must not be negative.");
        Preconditions.checkArgument(mitgliedId >= 0, "mitgliedId must not be negativ");

        LOG.debug("Receive 'delete' request with mannschaftsId '{}' and mitgliedsId '{}'", mannschaftsId, mitgliedId);

        // allow value == null, the value will be ignored
        final MannschaftsmitgliedDO mannschaftsMitgliedDO = new MannschaftsmitgliedDO(mannschaftsId, mitgliedId);
        final long currentUserId = UserProvider.getCurrentUserId(principal);

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
}
