package de.bogenliga.application.services.v1.dsbmannschaft.service;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftSortierungComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.dsbmannschaft.mapper.MannschaftSortierungDTOMapper;
import de.bogenliga.application.services.v1.dsbmannschaft.model.MannschaftSortierungDTO;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("v1/dsbmannschaft/sortierung")
public class MannschaftSortierungService implements ServiceFacade {

    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_DO = "DsbMannschaftDO must not be null";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_ID = "DsbMannschaft-ID must not be null or less than 0";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_SORTIERUNG = "DsbMannschaft-Sortierung must not be null or less than 0";

    private static final Logger LOG = LoggerFactory.getLogger(MannschaftSortierungService.class);

    /*
     * Business components
     *
     * dependency injection with {@link Autowired}
     */
    private final DsbMannschaftSortierungComponent maSortierungComponent;


    /**
     * Constructor with dependency injection
     *
     * @param maSortierungComponent to handle the database CRUD requests
     */
    @Autowired
    public MannschaftSortierungService(final DsbMannschaftSortierungComponent maSortierungComponent) {
        this.maSortierungComponent = maSortierungComponent;
    }


    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public MannschaftSortierungDTO update(@RequestBody final MannschaftSortierungDTO maSortierungDTO, final Principal principal) {
        Preconditions.checkNotNull(maSortierungDTO,PRECONDITION_MSG_DSBMANNSCHAFT_DO);
        Preconditions.checkNotNull(maSortierungDTO.getId(), PRECONDITION_MSG_DSBMANNSCHAFT_ID);
        Preconditions.checkArgument(maSortierungDTO.getId() >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_ID);
        Preconditions.checkNotNull(maSortierungDTO.getSortierung(), PRECONDITION_MSG_DSBMANNSCHAFT_SORTIERUNG);
        Preconditions.checkArgument(maSortierungDTO.getSortierung() >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_SORTIERUNG);

        LOG.debug("Receive 'update' request with Mannschafts-ID '{}' and Mannschafts-Sortierung '{}'.",
                maSortierungDTO.getId(), maSortierungDTO.getSortierung());

        final DsbMannschaftDO newDsbMannschaftDO = MannschaftSortierungDTOMapper.toDO.apply(maSortierungDTO);
        final long userId = UserProvider.getCurrentUserId(principal);

        final DsbMannschaftDO updatedDsbMannschaftDO = maSortierungComponent.updateSortierung(newDsbMannschaftDO, userId);
        return MannschaftSortierungDTOMapper.toDTO.apply(updatedDsbMannschaftDO);
    }
}
