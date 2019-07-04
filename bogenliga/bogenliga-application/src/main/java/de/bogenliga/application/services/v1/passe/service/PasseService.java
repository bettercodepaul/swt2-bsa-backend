package de.bogenliga.application.services.v1.passe.service;

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
import de.bogenliga.application.business.passe.api.PasseComponent;
import de.bogenliga.application.business.passe.api.types.PasseDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.dsbmannschaft.service.DsbMannschaftService;
import de.bogenliga.application.services.v1.passe.mapper.PasseDTOMapper;
import de.bogenliga.application.services.v1.passe.model.PasseDTO;

/**
 *
 * @author Dominik Schneider, SWT-2 SS19 HSRT MKI
 */


@RestController
@CrossOrigin
@RequestMapping("v1/passe")
public class PasseService implements ServiceFacade {

    private static final String PRECONDITION_MSG_PASSE = "Passe must not be null";
    private static final String PRECONDITION_MSG_PASSE_ID = "Passe ID must not be null";
    private static final String PRECONDITION_MSG_PASSE_MANNSCHAFT_ID = "Passe Mannschaft ID must not be null";
    private static final String PRECONDITION_MSG_PASSE_MATCH_NUMMER = "Passe Nummer must not be null";
    private static final String PRECONDITION_MSG_PASSE_WETTKAMPF_ID = "Passe Wettkampf Id must not be null";
    private static final String PRECONDITION_MSG_PASSE_DSBMITGLIED_ID = "Passe DsbMitglied ID must not be null";


    private static final String PRECONDITION_MSG_PASSE_ID_NEGATIVE = "Passe Id must not be negative";
    private static final String PRECONDITION_MSG_PASSE_MANNSCHAFT_ID_NEGATIVE = "Passe Mannschaft Id must not be negative";
    private static final String PRECONDITION_MSG_PASSE_MATCH_NUMMER_NEGATIVE = "Passe Match Nummer must not be negative";
    private static final String PRECONDITION_MSG_PASSE_WETTKAMPF_ID_NEGATIVE = "Passe Wettkampf Id must not be negative";
    private static final String PRECONDITION_MSG_PASSE_DSBMITGLIED_ID_NEGATIVE = "Passe DsbMitglied Id must not be negative";

    private static final Logger LOG = LoggerFactory.getLogger(DsbMannschaftService.class);

    private final PasseComponent passeComponent;


    /**
     * Constructor with dependency injection
     *
     * @param passeComponent to handle database CRUD requests
     */
    @Autowired
    public PasseService(PasseComponent passeComponent) {
        this.passeComponent = passeComponent;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PasseDTO create(@RequestBody final PasseDTO passeDTO, final Principal principal) {
        this.checkPreconditions(passeDTO);
        final Long userId = UserProvider.getCurrentUserId(principal);
        Preconditions.checkArgument(userId >= 0 , "User ID must not be negative nor null");

        final PasseDO newPasse = PasseDTOMapper.toDO.apply(passeDTO);
        final PasseDO savedPasse = passeComponent.create(newPasse, userId);
        return PasseDTOMapper.toDTO.apply(savedPasse);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PasseDTO update(@RequestBody final PasseDTO passeDTO, final Principal principal) {
        this.checkPreconditions(passeDTO);
        final Long userId = UserProvider.getCurrentUserId(principal);
        Preconditions.checkArgument(userId >= 0 , "User ID must not be negative nor null");

        final PasseDO newPasse = PasseDTOMapper.toDO.apply(passeDTO);
        final PasseDO updatedPasse = passeComponent.update(newPasse, userId);
        return PasseDTOMapper.toDTO.apply(updatedPasse);
    }

    @RequestMapping(path = "byWettkampfIdAndDsbMitgliedId/{wettkampfId}/{dsbMitgliedId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PasseDTO> findAllByWettkampfIdAndDsbMitgliedId(@PathVariable("wettkampfId") final long wettkampfId,
                                                            @PathVariable("dsbMitgliedId") final long dsbMitgliedId) {
        Preconditions.checkArgument(wettkampfId >= 0, "wettkampfId must not be negative");
        Preconditions.checkArgument(dsbMitgliedId >= 0, "dsbMitgliedId must not be negative");

        LOG.debug("Received 'findAllByWettkampfIdAndDsbMitgliedId' request with WettkampfId: '{}' and DsbMitgliedId: '{}'", wettkampfId, dsbMitgliedId);

        final List<PasseDO> passeDOList = this.passeComponent.findByWettkampfIdAndMitgliedId(wettkampfId, dsbMitgliedId);
        return passeDOList.stream().map(PasseDTOMapper.toDTO).collect(Collectors.toList());

    }

    private void checkPreconditions(PasseDTO passeDTO) {
        Preconditions.checkNotNull(passeDTO, PRECONDITION_MSG_PASSE);
        Preconditions.checkNotNull(passeDTO.getId(), PRECONDITION_MSG_PASSE_ID);
        Preconditions.checkNotNull(passeDTO.getMannschaftId(), PRECONDITION_MSG_PASSE_MANNSCHAFT_ID);
        Preconditions.checkNotNull(passeDTO.getMatchNr(), PRECONDITION_MSG_PASSE_MATCH_NUMMER);
        Preconditions.checkNotNull(passeDTO.getDsbMitgliedId(), PRECONDITION_MSG_PASSE_DSBMITGLIED_ID);
        Preconditions.checkNotNull(passeDTO.getWettkampfId(), PRECONDITION_MSG_PASSE_WETTKAMPF_ID);

        Preconditions.checkArgument(passeDTO.getId() >= 0, PRECONDITION_MSG_PASSE_ID_NEGATIVE);
        Preconditions.checkArgument(passeDTO.getMannschaftId() >= 0, PRECONDITION_MSG_PASSE_MANNSCHAFT_ID_NEGATIVE);
        Preconditions.checkArgument(passeDTO.getMatchNr() >= 0, PRECONDITION_MSG_PASSE_MATCH_NUMMER_NEGATIVE);
        Preconditions.checkArgument(passeDTO.getWettkampfId() >= 0, PRECONDITION_MSG_PASSE_WETTKAMPF_ID_NEGATIVE);
        Preconditions.checkArgument(passeDTO.getDsbMitgliedId() >= 0, PRECONDITION_MSG_PASSE_DSBMITGLIED_ID_NEGATIVE);
    }




}
