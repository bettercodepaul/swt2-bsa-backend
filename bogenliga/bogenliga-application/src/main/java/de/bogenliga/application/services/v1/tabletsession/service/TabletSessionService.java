package de.bogenliga.application.services.v1.tabletsession.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("v1/tabletsessions/")
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


    @RequestMapping(value = "{wettkampfId}/{scheibenNr}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public TabletSessionDTO findById(@PathVariable("wettkampfId") Long wettkampfId,
                                     @PathVariable("scheibenNr") Long scheibennummer) {
        TabletSessionDO tabDO = tabletSessionComponent.findByIdScheibennummer(wettkampfId, scheibennummer);
        TabletSessionDTO tabDTO = TabletSessionDTOMapper.toDTO.apply(tabDO);
        this.log(tabDTO, "findById");
        return tabDTO;
    }


    @RequestMapping(value = "{wettkampfId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_MODIFY_WETTKAMPF)
    public List<TabletSessionDTO> findById(@PathVariable("wettkampfId") Long wettkampfId, final Principal principal) {
        final long userId = UserProvider.getCurrentUserId(principal);
        List<TabletSessionDO> tabDOs = tabletSessionComponent.findById(wettkampfId);
        TabletSessionDO[] tabArr = new TabletSessionDO[8];
        for (TabletSessionDO tabDO : tabDOs) {
            tabArr[tabDO.getScheibennummer().intValue()] = tabDO;
        }

        for (int i = 0; i < 8; i++) {
            if (tabArr[i] == null) {
                tabArr[i] = create(fillMatchIdSatzNr(wettkampfId, i), userId);
            }
        }

        List<TabletSessionDTO> tabDTOs = tabDOs.stream().map(TabletSessionDTOMapper.toDTO).collect(
                Collectors.toList());
        LOG.debug("Receive 'findById' request with ID '{}'", wettkampfId);
        return tabDTOs;
    }


    private TabletSessionDTO fillMatchIdSatzNr(Long wettkampfId, int scheibennummer) {
        scheibennummer++;
        List<MatchDO> matches = matchComponent.findByWettkampfId(wettkampfId);
        TabletSessionDTO tab = new TabletSessionDTO();
        Optional<MatchDO> matchDO = matches.stream().filter(x -> x.getScheibenNummer().equals(scheibennummer)).filter(
                x -> x.getNr().equals(1L)).findFirst();
        if (matchDO.isPresent()) {
            tab.setMatchId(matchDO.get().getId());
            tab.setSatznummer(1L);
            tab.setWettkampfId(wettkampfId);
            tab.setScheibennummer((long) scheibennummer);
            tab.setActive(false);
        }
        return tab;
    }


    private TabletSessionDO create(final TabletSessionDTO tabletSessionDTO, final Long userId) {

        return tabletSessionComponent.create(TabletSessionDTOMapper.toDO.apply(tabletSessionDTO),
                userId);
    }


    /**
     * @RequestMapping(value = "{wettkampfId}/{scheibenNr}/", method = RequestMethod.POST, produces =
     * MediaType.APPLICATION_JSON_VALUE)
     * @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
     * @OnError() public TabletSessionDTO create(@RequestBody final TabletSessionDTO tabletSessionDTO, final Principal
     * principal) {
     * <p>
     * final long userId = UserProvider.getCurrentUserId(principal); if (tabletSessionComponent.findByIdScheibennummer(tabletSessionDTO.getWettkampfId(),
     * tabletSessionDTO.getScheibennummer()) == null) {
     * <p>
     * TabletSessionDO tabDO = tabletSessionComponent.create(TabletSessionDTOMapper.toDO.apply(tabletSessionDTO),
     * userId); TabletSessionDTO tabDTO = TabletSessionDTOMapper.toDTO.apply(tabDO);
     * <p>
     * this.log(tabDTO, "create"); return tabDTO; } else { throw } }
     */

    @ResponseStatus(
            value = HttpStatus.BAD_REQUEST,
            reason = "Illegal arguments")
    @ExceptionHandler(IllegalArgumentException.class)
    public void illegalArgumentHandler() {
        //
    }


    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF)
    public TabletSessionDTO update(TabletSessionDTO tabletSessionDTO, final Principal principal) {

        final long userId = UserProvider.getCurrentUserId(principal);
        TabletSessionDO tabletSessionDO = tabletSessionComponent.update(
                TabletSessionDTOMapper.toDO.apply(tabletSessionDTO), userId);
        this.log(tabletSessionDTO, "update");
        return TabletSessionDTOMapper.toDTO.apply(tabletSessionDO);
    }

/**
 * not needed anymore
 @RequestMapping(value = "{wettkampfId}/{scheibenNr}/", method = RequestMethod.DELETE,
 consumes = MediaType.APPLICATION_JSON_VALUE)
 @RequiresPermission(UserPermission.CAN_READ_WETTKAMPF) public void delete(TabletSessionDTO tabletSessionDTO, final Principal principal) {

 final long userId = UserProvider.getCurrentUserId(principal);
 tabletSessionComponent.delete(TabletSessionDTOMapper.toDO.apply(tabletSessionDTO), userId);
 }
 */

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
