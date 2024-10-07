package de.bogenliga.application.services.v1.kampfrichter.service;

import java.security.Principal;
import java.util.List;

import de.bogenliga.application.common.service.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.kampfrichter.api.KampfrichterComponent;
import de.bogenliga.application.business.kampfrichter.api.types.KampfrichterDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterDTO;
import de.bogenliga.application.services.v1.kampfrichter.mapper.KampfrichterDTOMapper;
import de.bogenliga.application.services.v1.kampfrichter.model.KampfrichterExtendedDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I´m a REST resource and handle Kampfrichter CRUD requests over the HTTP protocol.
 *
 * @author swt2
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
    private static final String PRECONDITION_MSG_KAMPFRICHTER = "KampfrichterDO must not be null";
    private static final String PRECONDITION_MSG_KAMPFRICHTER_BENUTZER_ID = "KampfrichterBenutzerID must not be negative and must not be null";
    private static final String PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID = "KampfrichterWettkampfID must not be negative and must not be null";
    private static final String PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID_NEGATIVE = "Wettkampf-ID must not be negative.";

    private static final Logger LOG = LoggerFactory.getLogger(KampfrichterService.class);

    private final KampfrichterComponent kampfrichterComponent;


    @Autowired
    public KampfrichterService(final KampfrichterComponent kampfrichterComponent) {
        this.kampfrichterComponent = kampfrichterComponent;
    }


    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<KampfrichterDTO> findAll() {
        final List<KampfrichterDO> kampfrichterDOList = kampfrichterComponent.findAll();
        return kampfrichterDOList.stream().map(KampfrichterDTOMapper.toDTO).toList();
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public KampfrichterDTO create(@RequestBody final KampfrichterDTO kampfrichterDTO, final Principal principal) {

        checkPreconditions(kampfrichterDTO);

        LOG.debug("Received 'create' request with userID '{}', wettkampfID '{}', leitend'{}'",
                kampfrichterDTO.getUserID(),
                kampfrichterDTO.getWettkampfID(),
                kampfrichterDTO.getLeitend());
        final long userID = UserProvider.getCurrentUserId(principal);

        final KampfrichterDO newKampfrichterDO = KampfrichterDTOMapper.toDO.apply(kampfrichterDTO);

        final KampfrichterDO savedKampfrichterDO = kampfrichterComponent.create(newKampfrichterDO,
                userID);
        return KampfrichterDTOMapper.toDTO.apply(savedKampfrichterDO);
    }

    //Returns a List with KampfrichterExtended who are not assinged to the Wettkampftag
    @GetMapping(value= "/NotAssignedKampfrichter/{wettkampfId}")
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public List<KampfrichterExtendedDTO> findByWettkampfidNotInWettkampftag(@PathVariable("wettkampfId") final long wettkampfId){
        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID_NEGATIVE);
        List<KampfrichterDO> kampfrichterDOList = kampfrichterComponent.findByWettkampfidNotInWettkampftag(wettkampfId);

        return kampfrichterDOList.stream().map(KampfrichterDTOMapper.toDTOExtended).toList();
    }

    //Returns a List with KampfrichterExtended who are assinged to the Wettkampftag
    @GetMapping(value= "/AssignedKampfrichter/{wettkampfId}")
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public List<KampfrichterExtendedDTO> findByWettkampfidInWettkampftag(@PathVariable("wettkampfId") final long wettkampfId){
        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID_NEGATIVE);
        List<KampfrichterDO> kampfrichterDOList = kampfrichterComponent.findByWettkampfidInWettkampftag(wettkampfId);

        return kampfrichterDOList.stream().map(KampfrichterDTOMapper.toDTOExtended).toList();
    }

    @DeleteMapping(value = "{userID}/{wettkampfID}")
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public void delete(@PathVariable("userID") final long userID, @PathVariable("wettkampfID") final long wettkampfID,
                       final Principal principal) {
        Preconditions.checkArgument(userID >= 0, "User-ID must not be negative.");
        Preconditions.checkArgument(wettkampfID >= 0, PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID_NEGATIVE);

        final long changeUserID = UserProvider.getCurrentUserId(principal);
        LOG.debug("Receive 'delete' request with user-ID '{}' and wettkampf-ID '{}'", userID, wettkampfID);

        // allow value == null, the value will be ignored
        final KampfrichterDO kampfrichterDO = new KampfrichterDO(userID, wettkampfID, false);

        kampfrichterComponent.delete(kampfrichterDO, changeUserID);
    }


    private void checkPreconditions(@RequestBody final KampfrichterDTO kampfrichterDTO) {
        Preconditions.checkNotNull(kampfrichterDTO, PRECONDITION_MSG_KAMPFRICHTER);
        Preconditions.checkNotNull(kampfrichterDTO.getUserID(), PRECONDITION_MSG_KAMPFRICHTER_BENUTZER_ID);
        Preconditions.checkNotNull(kampfrichterDTO.getWettkampfID(), PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID);
        Preconditions.checkArgument(kampfrichterDTO.getUserID() >= 0, PRECONDITION_MSG_KAMPFRICHTER_BENUTZER_ID);
        Preconditions.checkArgument(kampfrichterDTO.getWettkampfID() >= 0, PRECONDITION_MSG_KAMPFRICHTER_WETTKAMPF_ID);
    }
}
