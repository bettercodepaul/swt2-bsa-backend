package de.bogenliga.application.services.v1.tabletsession.service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.tabletsession.api.TabletSessionComponent;
import de.bogenliga.application.business.tabletsession.api.types.TabletSessionDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.services.v1.tabletsession.mapper.TabletSessionDTOMapper;
import de.bogenliga.application.services.v1.tabletsession.model.TabletSessionDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * @author Kay Scheerer
 */

@RestController
@CrossOrigin
@RequestMapping("v1/tabletsessions")
public class TabletSessionService implements ServiceFacade {
    private static final Logger LOG = LoggerFactory.getLogger(TabletSessionService.class);

    private final TabletSessionComponent tabletSessionComponent;

    private final MatchComponent matchComponent;


    /**
     * Constructor with dependency injection
     *
     * @param tabletSessionComponent to handle the database CRUD requests
     */
    @Autowired
    public TabletSessionService(final TabletSessionComponent tabletSessionComponent,
                                final MatchComponent matchComponent) {
        this.tabletSessionComponent = tabletSessionComponent;
        this.matchComponent = matchComponent;
    }
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public List<TabletSessionDTO> findAll() {
        List<TabletSessionDO> sessionDOs = tabletSessionComponent.findAll();
        return sessionDOs.stream().map(TabletSessionDTOMapper.toDTO).collect(Collectors.toList());
    }



    @GetMapping(value = "/{wettkampfId}/{scheibenNr}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public TabletSessionDTO findById(@PathVariable("wettkampfId") Long wettkampfId,
                                     @PathVariable("scheibenNr") Long scheibennummer) {
        TabletSessionDO tabDO = tabletSessionComponent.findByIdScheibennummer(wettkampfId, scheibennummer);
        TabletSessionDTO tabDTO = TabletSessionDTOMapper.toDTO.apply(tabDO);
        this.addMatchIds(tabDTO);
        this.log(tabDTO, "findById");
        return tabDTO;
    }


    @GetMapping(value = "/{wettkampfId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public List<TabletSessionDTO> findByWettkampfId(@PathVariable("wettkampfId") Long wettkampfId,
                                                    final Principal principal) {
        final long userId = UserProvider.getCurrentUserId(principal);
        List<TabletSessionDO> tabDOs = tabletSessionComponent.findByWettkampfId(wettkampfId);

        // in case the tabletsessions weren't created yet, create them once on request
        if (tabDOs.isEmpty()) {
            tabDOs = tabletSessionComponent.createInitialForWettkampf(
                    wettkampfId, matchComponent, userId
            );
        }

        List<TabletSessionDTO> tsDTOs = tabDOs.stream()
                .map(TabletSessionDTOMapper.toDTO)
                .collect(Collectors.toList());

        for (TabletSessionDTO tsDTO : tsDTOs) {
            addMatchIds(tsDTO);
        }

        LOG.debug("Receive 'findByWettkampfId' request with ID '{}'", wettkampfId);
        return tsDTOs;
    }


    private void addMatchIds(TabletSessionDTO tsDTO) {
        List<MatchDO> relatedMatches = tabletSessionComponent.getRelatedMatches(
                TabletSessionDTOMapper.toDO.apply(tsDTO),
                matchComponent
        );
        if (relatedMatches.size() == 2) {
            MatchDO first;
            MatchDO second;
            first = relatedMatches.get(0);
            second = relatedMatches.get(1);
            if (first.getScheibenNummer().equals(tsDTO.getScheibennummer())) {
                tsDTO.setMatchId(first.getId());
                tsDTO.setOtherMatchId(second.getId());
            } else {
                tsDTO.setMatchId(second.getId());
                tsDTO.setOtherMatchId(first.getId());
            }
        }
    }


    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RequiresPermission(UserPermission.CAN_MODIFY_WETTKAMPF)
    public TabletSessionDTO update(@RequestBody final TabletSessionDTO tabletSessionDTO, final Principal principal) {
        final long userId = UserProvider.getCurrentUserId(principal);
        TabletSessionDO tabletSessionDO = tabletSessionComponent.update(
                TabletSessionDTOMapper.toDO.apply(tabletSessionDTO), userId);
        this.log(tabletSessionDTO, "update");
        TabletSessionDTO tabDTO = TabletSessionDTOMapper.toDTO.apply(tabletSessionDO);
        this.addMatchIds(tabDTO);
        return tabDTO;
    }



    /**
     * Logs received data when request arrives
     *
     * @param passeDTO
     * @param fromService: name of the service the log came from
     */
    private void log(TabletSessionDTO passeDTO, String fromService) {
        LOG.debug(
                "Received '{}' request for passe with  WettkampfID: '{}', " +
                        " Scheibennummer: '{}', Satznummer: '{}', MatchID: '{}'",
                fromService,
                passeDTO.getWettkampfId(),
                passeDTO.getScheibennummer(),
                passeDTO.getSatznummer(),
                passeDTO.getMatchId()
        );
    }
}
