package de.bogenliga.application.services.v1.mannschaftsmitglied.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import javax.naming.NoPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.mannschaftsmitglied.api.types.MannschaftsmitgliedDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.mannschaftsmitglied.mapper.MannschaftsMitgliedDTOMapper;
import de.bogenliga.application.services.v1.mannschaftsmitglied.model.MannschaftsMitgliedDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;

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
    private static final String PRECONDITION_MSG_ID_NEGATIVE = "ID must not be negative.";



    /*
     * Business components
     *
     * dependency injection with {@link Autowired}
     */
    private final MannschaftsmitgliedComponent mannschaftsMitgliedComponent;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final RequiresOnePermissionAspect requiresOnePermissionAspect;


    @Autowired
    public MannschaftsMitgliedService(MannschaftsmitgliedComponent mannschaftsMitgliedComponent,
                                      DsbMannschaftComponent dsbMannschaftComponent,
                                      final RequiresOnePermissionAspect requiresOnePermissionAspect) {
        this.mannschaftsMitgliedComponent = mannschaftsMitgliedComponent;

        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.requiresOnePermissionAspect = requiresOnePermissionAspect;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<MannschaftsMitgliedDTO> findAll() {
        final List<MannschaftsmitgliedDO> mannschaftmitgliedDOList = mannschaftsMitgliedComponent.findAll();
        return mannschaftmitgliedDOList.stream().map(MannschaftsMitgliedDTOMapper.toDTO).collect(Collectors.toList());
    }


    @GetMapping(value = "{teamId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<MannschaftsMitgliedDTO> findByTeamId(@PathVariable("teamId") final long mannschaftsId) {
        final List<MannschaftsmitgliedDO> mannschaftmitgliedDOList = mannschaftsMitgliedComponent.findByTeamId(
                mannschaftsId);
        return mannschaftmitgliedDOList.stream().map(MannschaftsMitgliedDTOMapper.toDTO).collect(Collectors.toList());
    }


    @GetMapping(value = "{memberId}/{teamId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public MannschaftsMitgliedDTO findByMemberAndTeamId(@PathVariable("teamId") final long mannschaftsId,
                                                        @PathVariable("memberId") final long mitgliedId) {
        Preconditions.checkArgument(mannschaftsId > 0, PRECONDITION_MSG_ID_NEGATIVE);
        Preconditions.checkArgument(mitgliedId > 0, PRECONDITION_MSG_ID_NEGATIVE);

        LOG.debug("Receive 'findByMemberAndTeamId' request with memberID '{}' and teamID '{}'", mitgliedId,
                mannschaftsId);

        final MannschaftsmitgliedDO mannschaftsmitgliedDO = mannschaftsMitgliedComponent.findByMemberAndTeamId(
                mannschaftsId, mitgliedId);
        return MannschaftsMitgliedDTOMapper.toDTO.apply(mannschaftsmitgliedDO);
    }

    @GetMapping(value = "{teamId}/byRueckennummer/{rueckennummer}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public MannschaftsMitgliedDTO findByTeamIdAndRueckennummer(@PathVariable("teamId") final long mannschaftsId,
                                                            @PathVariable("rueckennummer") final long rueckennummer) {
        Preconditions.checkArgument(mannschaftsId > 0, PRECONDITION_MSG_ID_NEGATIVE);
        Preconditions.checkArgument(rueckennummer > 0, PRECONDITION_MSG_ID_NEGATIVE);

        LOG.debug("Receive 'findByTeamIdAndRueckennummer' request with rueckennummer '{}' and teamID '{}'", rueckennummer,
                mannschaftsId);

        final MannschaftsmitgliedDO mannschaftsmitgliedDO = mannschaftsMitgliedComponent.findByTeamIdAndRueckennummer(
                mannschaftsId, rueckennummer);
        return MannschaftsMitgliedDTOMapper.toDTO.apply(mannschaftsmitgliedDO);
    }


    @GetMapping(value = "{teamIdInTeam}/{istEingesetzt}/{test3}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<MannschaftsMitgliedDTO> findAllSchuetzeInTeam(@PathVariable("teamIdInTeam") final long mannschaftsId) {
        final List<MannschaftsmitgliedDO> mannschaftmitgliedDOList = mannschaftsMitgliedComponent.findAllSchuetzeInTeamEingesetzt(
                mannschaftsId);
        return mannschaftmitgliedDOList.stream().map(MannschaftsMitgliedDTOMapper.toDTO).collect(Collectors.toList());
    }


    @GetMapping(value = "/byMemberId/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<MannschaftsMitgliedDTO> findByMemberId(@PathVariable("memberId") final long memberId) {
        Preconditions.checkArgument(memberId > 0, PRECONDITION_MSG_ID_NEGATIVE);

        LOG.debug("Receive 'findByMemberId' request with ID '{}'", memberId);

        final List<MannschaftsmitgliedDO> mannschaftsmitgliedDO = mannschaftsMitgliedComponent.findByMemberId(memberId);
        return mannschaftsmitgliedDO.stream().map(MannschaftsMitgliedDTOMapper.toDTO).collect(Collectors.toList());
    }


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

        if (!this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_MODIFY_MANNSCHAFT)
                && !this.requiresOnePermissionAspect.hasSpecificPermissionSportleiter(
                UserPermission.CAN_MODIFY_MY_VEREIN, tempId)) {
            throw new NoPermissionException();
        }

        final MannschaftsmitgliedDO newMannschaftsMitgliedDO = MannschaftsMitgliedDTOMapper.toDO.apply(mannschaftsMitgliedDTO);
        final long userId = UserProvider.getCurrentUserId(principal);
        final MannschaftsmitgliedDO updatedMannschaftsmitgliedDO = mannschaftsMitgliedComponent.update(
                newMannschaftsMitgliedDO, userId);

        return MannschaftsMitgliedDTOMapper.toDTO.apply(updatedMannschaftsmitgliedDO);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_MANNSCHAFT, UserPermission.CAN_MODIFY_MY_VEREIN})
    public MannschaftsMitgliedDTO create(@RequestBody final MannschaftsMitgliedDTO mannschaftsMitgliedDTO,
                                         final Principal principal) throws NoPermissionException {
        checkPreconditions(mannschaftsMitgliedDTO);

        LOG.debug("Receive 'create' request with mannschaftsId '{}', dsbMitgliedId '{}', DsbMitgliedEingesetzt '{}',",
                mannschaftsMitgliedDTO.getMannschaftsId(),
                mannschaftsMitgliedDTO.getDsbMitgliedId(),
                mannschaftsMitgliedDTO.getDsbMitgliedEingesetzt());

        long tempId = dsbMannschaftComponent.findById(mannschaftsMitgliedDTO.getMannschaftsId()).getVereinId();

        if (!this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_MODIFY_MANNSCHAFT)
                && !this.requiresOnePermissionAspect.hasSpecificPermissionSportleiter(
                UserPermission.CAN_MODIFY_MY_VEREIN, tempId)) {
            throw new NoPermissionException();
        }

        final MannschaftsmitgliedDO newMannschaftsmitgliedDO = MannschaftsMitgliedDTOMapper.toDO.apply(mannschaftsMitgliedDTO);
        final long currentMemberId = UserProvider.getCurrentUserId(principal);
        final MannschaftsmitgliedDO savedMannschaftsmitgliedDO = mannschaftsMitgliedComponent.create(
                newMannschaftsmitgliedDO, currentMemberId);

        return MannschaftsMitgliedDTOMapper.toDTO.apply(savedMannschaftsmitgliedDO);
    }


    /**
     * You are only able to delete a MannschaftsMitglied, if you have the explicit permission
     * Sportleiter should have a specific right to delete a single member but there is a
     * read-vy-id function missing in MannschaftsMitgliedDAO/ MannschaftsMitgliedComponent
     **/
    @DeleteMapping(value = "{id}")
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN})
    public void delete(@PathVariable("id") final long id, final Principal principal) throws NoPermissionException {
        Preconditions.checkArgument(id >= 0, "Id must not be negative.");

        LOG.debug("Receive 'delete' request with Id '{}'", id);

        // allow value == null, the value will be ignored
        final MannschaftsmitgliedDO mannschaftsMitgliedDO = new MannschaftsmitgliedDO(id);
        final long currentUserId = UserProvider.getCurrentUserId(principal);
        mannschaftsMitgliedComponent.delete(mannschaftsMitgliedDO, currentUserId);
    }


    @DeleteMapping(value = "{mannschaftsId}/{mitgliedId}")
    @RequiresOnePermissions(perm = {UserPermission.CAN_DELETE_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VEREIN})
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
        if (!this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_MODIFY_MANNSCHAFT)
                && !this.requiresOnePermissionAspect.hasSpecificPermissionSportleiter(
                UserPermission.CAN_MODIFY_MY_VEREIN, tempId)) {
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
}
