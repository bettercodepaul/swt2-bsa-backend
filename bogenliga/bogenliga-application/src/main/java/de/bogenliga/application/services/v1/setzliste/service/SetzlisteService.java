package de.bogenliga.application.services.v1.setzliste.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import de.bogenliga.application.common.service.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.setzliste.api.SetzlisteComponent;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.match.mapper.MatchDTOMapper;
import de.bogenliga.application.services.v1.match.model.MatchDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * This is a rest resource that generates the matches.
 *
 * @author Robin Müller, Marcel Neumann
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
@RequestMapping("v1/setzliste")
public class SetzlisteService implements ServiceFacade {

    /*
     * Business components
     *
     * dependency injection with {@link Autowired}
     */
    private final SetzlisteComponent setzlisteComponent;

    /**
     * Constructor with dependency injection
     */
    @Autowired
    public SetzlisteService(final SetzlisteComponent setzlisteComponent) {
        this.setzlisteComponent = setzlisteComponent;
    }


    /**
     * You can only generate a Setzliste if you have the right to read the Wettkampf or
     * if you are the Ausrichter/Ligaleiter of the Veranstaltung.
     * @param wettkampfid id of the selected Wettkampf
     * @return ArrayList of MatchDTOs of the wettkampf to fill in the pdf generator later
     */

    @CrossOrigin(maxAge = 0)
    @GetMapping(path = "/generate")
    @RequiresOnePermissions(perm = {UserPermission.CAN_READ_WETTKAMPF, UserPermission.CAN_READ_MY_VERANSTALTUNG})
    public
    List<MatchDTO> generateSetzliste(@RequestParam("wettkampfid") final long wettkampfid, final Principal principal) {
        Preconditions.checkArgument(wettkampfid > 0, "wettkampfid needs to be higher than 0");

        final Long userId = UserProvider.getCurrentUserId(principal);
        List<MatchDO>  matchDOList = this.setzlisteComponent.generateMatchesBySetzliste(wettkampfid, userId);
        ArrayList<MatchDTO> matchDTOList = new ArrayList<>();
        for (MatchDO matchDO : matchDOList) {
            matchDTOList.add(MatchDTOMapper.toDTO.apply(matchDO));
        }
        return matchDTOList;
    }
}
