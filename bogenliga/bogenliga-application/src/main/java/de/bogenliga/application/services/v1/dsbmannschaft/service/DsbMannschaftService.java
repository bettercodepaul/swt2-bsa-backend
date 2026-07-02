package de.bogenliga.application.services.v1.dsbmannschaft.service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.naming.NoPermissionException;

import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.mannschaftsmitglied.api.MannschaftsmitgliedComponent;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.dsbmannschaft.mapper.DsbMannschaftDTOMapper;
import de.bogenliga.application.services.v1.dsbmannschaft.model.DsbMannschaftDTO;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissionAspect;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 *
 * @author Philip Dengler
 */
@RestController
@CrossOrigin
@RequestMapping("v1/dsbmannschaft")
public class DsbMannschaftService implements ServiceFacade {

    private static final String PRECONDITION_MSG_DSBMANNSCHAFT = "DsbMannschaftDO must not be null";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_ID = "DsbMannschaftDO ID must not be null";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_VEREIN_ID = "DsbMannschaft Verein ID must not be null";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_NUMMER = "DsbMannschaft Nummer must not be null";


    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_VEREIN_ID_NEGATIVE = "DsbMannschaft Vereins Id must not be negative";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_NUMMER_NEGATIVE = "DsbMannschaft Nummer must not be negative";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_BENUTZER_ID_NEGATIVE = "DsbMannschaft Benutzer Id must not be negative";
    private static final String ERROR_MSG_VERANSTALTUNG_FULL = "Die Veranstaltung hat ihre maximale Kapazität erreicht. Es kann keine weitere Mannschaft hinzugefügt werden.";
    private static final String PRECONDITION_MSG_ID_NEGATIVE = "ID must not be negative.";
    private static final String PRECONDITION_MSG_WRONG_YEAR = "Year has to be a valid year.";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_SIZE_NEGATIV = "DsbMannschaft Veranstaltung size can not be negativ";
    private static final String PRECONDITION_MSG_PLATZHALTER_DUPLICATE_VERANSTALTUNG_EXISTING = "Already an existing Platzhalter in this Veranstaltung";
    private static final Logger LOG = LoggerFactory.getLogger(DsbMannschaftService.class);

    private static final Long PLATZHALTER_VEREIN_ID = 99L;



    /*
     * Business components
     *
     * dependency injection with {@link Autowired}
     */
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VeranstaltungComponent veranstaltungComponent;
    private final RequiresOnePermissionAspect requiresOnePermissionAspect;

    private static final String ERROR_MSG_CREATE_MANNSCHAFT_NO_PERMISSION = "Keine Berechtigung zum Erstellen einer Mannschaft";
    private static final String ERROR_MSG_PLATZHALTER_NO_PERMISSION = "Sie haben keine Berechtigung für diese Aktion.";
    private static final String ERROR_MSG_DELETE_MANNSCHAFT_NO_PERMISSION = "Löschen einer Mannschaft ist nur mit entsprechender Berechtigung erlaubt.";
    private static final String ERROR_MSG_UPDATE_MANNSCHAFT_NO_PERMISSION = "Ändern einer Mannschaft ist nur mit entsprechender Berechtigung erlaubt.";
    private static final String ERROR_MSG_VERANSTALTUNG_LAUFEND = "Die Veranstaltung ist in der Phase 'Laufend'. Teilnehmende Mannschaften können in dieser Phase nicht hinzugefügt, geändert oder entfernt werden.";

    MannschaftsmitgliedComponent mannschaftsmitgliedComponent;

    /**
     * Constructor with dependency injection
     *
     * @param dsbMannschaftComponent to handle the database CRUD requests
     */
    @Autowired
    public DsbMannschaftService(final DsbMannschaftComponent dsbMannschaftComponent,
                                final RequiresOnePermissionAspect requiresOnePermissionAspect, final VeranstaltungComponent veranstaltungComponent,
                                final MannschaftsmitgliedComponent mannschaftsmitgliedComponent) {
        this.veranstaltungComponent = veranstaltungComponent;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.requiresOnePermissionAspect = requiresOnePermissionAspect;
        this.mannschaftsmitgliedComponent = mannschaftsmitgliedComponent;
    }
    /**
     * Autowired WebTokenProvider to get the Permissions of the current User when checking them
     */
    JwtTokenProvider jwtTokenProvider;


    /**
     * I return all dsbMannschaft entries of the database.
     * ACHTUNG: Darf wegen Datenschutz in dieser Form nur vom Admin oder auf Testdaten verwendet werden!
     *
     * Usage:
     * <pre>{@code Request: GET /v1/dsbmannschaft}</pre>
     * <pre>{@code Response:
     * [
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  },
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.interval",
     *    "value": "10"
     *  }
     * ]
     * }
     * </pre>
     *
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<DsbMannschaftDTO> findAll() {
        final List<DsbMannschaftDO> dsbMannschaftDOList = dsbMannschaftComponent.findAll();

        return dsbMannschaftDOList.stream().map(DsbMannschaftDTOMapper.toDTO).toList();
    }

    /**
     * I return all dsbMannschaft entries of the database.
     * ACHTUNG: Darf wegen Datenschutz in dieser Form nur vom Admin oder auf Testdaten verwendet werden!
     *
     * Usage:
     * <pre>{@code Request: GET /v1/dsbmannschaft}</pre>
     * <pre>{@code Response:
     * [
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  },
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.interval",
     *    "value": "10"
     *  }
     * ]
     * }
     * </pre>
     *
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @GetMapping(value = "sportjahre", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<Long> findAllSportjahre() {
        return dsbMannschaftComponent.findAllSportjahre();
    }


    /**
     * I return the dsbMannschaft entries of the database with the given vereinsId.
     *
     * Usage:
     * <pre>{@Code Request: GET /v1/dsbmannschaft}</pre>
     * <pre>{@Code Response:
     * [
     *  {
     *      "id": "app.bogenliga.frontend.autorefresh.active",
     *      "value": "true"
     *  },
     *  {
     *      "id": "app.bogenliga.frontend.autorefresh.interval",
     *      "value": 10
     *  }
     * ]
     * }</pre>
     * @param id the given vereinsId
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @GetMapping(value = "byVereinsID/{vereinsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<DsbMannschaftDTO> findAllByVereinsId (
            @PathVariable("vereinsId") final Long id,
            @RequestParam(value = "sportjahr", required = false) final Integer year
    ) {
        final Year usedYear = (year != null) ? Year.of(year) : Year.now();

        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_ID_NEGATIVE);
        Preconditions.checkArgument(usedYear.getValue() >= 0, PRECONDITION_MSG_WRONG_YEAR);

        LOG.debug("Receive 'findAllByVereinsId' request with ID '{}'", id);
        LOG.debug("Receive 'findAllByVereinsId' request with Sportjahr '{}'", usedYear);

        final List<DsbMannschaftDO> dsbMannschaftDOList = dsbMannschaftComponent.findAllByVereinsId(id);

        return dsbMannschaftDOList
                .stream()
                .map(dsbMannschaftDO -> {
                    DsbMannschaftDTO dto = DsbMannschaftDTOMapper.toDTO.apply(dsbMannschaftDO);
                    if (dsbMannschaftDO.getVeranstaltungId() != null) {
                        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(dsbMannschaftDO.getVeranstaltungId());
                        if (veranstaltungDO != null) {
                            dto.setVeranstaltungName(veranstaltungDO.getVeranstaltungName());
                            dto.setLigaId(veranstaltungDO.getVeranstaltungLigaID());
                        }
                    }
                    return dto;
                 })
                 .filter(dsbMannschaftDO -> dsbMannschaftDO.getSportjahr() != null
                         && dsbMannschaftDO.getSportjahr().equals((long) usedYear.getValue())
                 )
                 .toList();
    }


    /**
     * Returns all dsbMannschaft entries that are currently in the waiting queue.
     *
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @GetMapping(value = "byWarteschlangeID/", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<DsbMannschaftDTO> findAllbyWarteschlangeID()

    {

        final List<DsbMannschaftDO> dsbMannschaftDOList  = dsbMannschaftComponent.findAllByWarteschlange();
        return dsbMannschaftDOList.stream().map(DsbMannschaftDTOMapper.toDTO).toList();
    }

    /**
     * I return the dsbMannschaft entries of the database with the given Veranstaltungs-Id.
     *
     * @param id the given Veranstaltungs-Id
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @GetMapping(value = "byVeranstaltungsID/{veranstaltungsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<DsbMannschaftDTO> findAllByVeranstaltungsId(@PathVariable("veranstaltungsId") final Long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_ID_NEGATIVE);

        LOG.debug("Receive 'findAllByVeranstaltungsId' request with ID '{}'", id);

        final List<DsbMannschaftDO> dsbMannschaftDOList  = dsbMannschaftComponent.findAllByVeranstaltungsId(id);
        return dsbMannschaftDOList.stream().map(DsbMannschaftDTOMapper.toDTO).toList();
    }

    /**
     * I return the dsbMannschaft entries of the database having the given SearchTerm.
     *
     * @param name the given SearchTerm
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @GetMapping(value = "byName/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<DsbMannschaftDTO> findAllByName(@PathVariable("name") final String name) {

        final List<DsbMannschaftDO> dsbMannschaftDOList  = dsbMannschaftComponent.findAllByName(name);
        return dsbMannschaftDOList.stream().map(DsbMannschaftDTOMapper.toDTO).toList();
    }


    /**
     * I return the dsbMannschaft entries of the database having the given MannschaftID.
     *
     * @param id the given SearchTerm
     * @return list of {@link DsbMannschaftDTO} as JSON
     */

    @GetMapping(value = "VeranstaltungAndWettkampfByID/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<DsbMannschaftDTO> findAllVeranstaltungAndWettkampfByID(@PathVariable("id") final long id) {

        LOG.debug("Receive 'findAllVeranstaltungAndWettkampfByID' request with id '{}'", id);

        final List<DsbMannschaftDO> dbsMannschaftVerUWettDOList  = dsbMannschaftComponent.findVeranstaltungAndWettkampfByID(id);
        return dbsMannschaftVerUWettDOList.stream().map(DsbMannschaftDTOMapper.toVerUWettDTO).toList();
    }
    /**
     * I return the dsbMannschaft entry of the database with a specific id.
     *
     * Usage:
     * <pre>{@code Request: GET /v1/dsbmitglied/app.bogenliga.frontend.autorefresh.active}</pre>
     * <pre>{@code Response:
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  }
     * }
     * </pre>
     *
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public DsbMannschaftDTO findById(@PathVariable("id") final Long id) {
        Preconditions.checkArgument(id > 0, PRECONDITION_MSG_ID_NEGATIVE);

        LOG.debug("Receive 'findById' request with ID '{}'", id);
        final DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(id);

        return DsbMannschaftDTOMapper.toDTO.apply(dsbMannschaftDO);
    }


    /**
     * I persist a new dsbMannschaft and return this dsbMannschaft entry.
     *
     * Usage:
     * <pre>{@code Request: POST /v1/dsbmannschaft
     * Body:
     * {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     * }
     * }</pre>
     * <pre>{@code Response:
     *  {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     *  }
     * }</pre>
     * @param dsbMannschaftDTO of the request body
     * @param principal authenticated user
     * @return list of {@link DsbMannschaftDTO} as JSON
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_MANNSCHAFT,UserPermission.CAN_MODIFY_MY_VEREIN})
    public DsbMannschaftDTO create(@RequestBody final DsbMannschaftDTO dsbMannschaftDTO, final Principal principal){
        //Check if the User has a General Permission or,
        //check if his vereinId equals the vereinId of the mannschaft he wants to create a Team in
        //and if the user has the permission to modify his verein.
        if(this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_CREATE_MANNSCHAFT) ||
                this.requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, dsbMannschaftDTO.getVereinId())) {

            //if the user has the Specific Permission and the matching VereinId:
            checkPreconditions(dsbMannschaftDTO);
            final Long userId = UserProvider.getCurrentUserId(principal);
            Preconditions.checkArgument(userId >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_BENUTZER_ID_NEGATIVE);

            // Bei laufender Veranstaltung duerfen keine Mannschaften (auch keine Platzhalter) angelegt werden.
            assertVeranstaltungNotLaufend(dsbMannschaftDTO.getVeranstaltungId());

            // Check size of Veranstaltung and if it is full
            // If Veranstaltung does not exist choose 8 as its default size
            if(dsbMannschaftDTO.getVereinId().equals(PLATZHALTER_VEREIN_ID)) {
                int veranstaltungsgroesse = 8;
                VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(
                        dsbMannschaftDTO.getVeranstaltungId());
                if (veranstaltungDO != null) {
                    veranstaltungsgroesse = veranstaltungDO.getVeranstaltungGroesse();
                }

                // Get the current number of teams from the Veranstaltung
                List<DsbMannschaftDO> actualMannschaftInVeranstaltungCount = dsbMannschaftComponent.findAllByVeranstaltungsId(
                        dsbMannschaftDTO.getVeranstaltungId());
                List<DsbMannschaftDO> allExistingPlatzhalterList = dsbMannschaftComponent.findAllByVereinsId(
                        PLATZHALTER_VEREIN_ID);

                // If the list isn´t empty, call the method
                if (!allExistingPlatzhalterList.isEmpty()) {
                    checkForPlatzhalter(actualMannschaftInVeranstaltungCount, allExistingPlatzhalterList,
                            dsbMannschaftDTO, veranstaltungsgroesse, principal);
                }


                if (actualMannschaftInVeranstaltungCount.size() >= veranstaltungsgroesse) {
                    throw new BusinessException(ErrorCode.VERANSTALTUNG_MAX_CAPACITY_ERROR, ERROR_MSG_VERANSTALTUNG_FULL);
                }
            }
            LOG.debug("Receive 'create' request with verein id '{}', nummer '{}', benutzer id '{}', veranstaltung id '{}',",

                    dsbMannschaftDTO.getVereinId(),
                    dsbMannschaftDTO.getNummer(),
                    userId,
                    dsbMannschaftDTO.getVeranstaltungId());

            final DsbMannschaftDO newDsbMannschaftDO = DsbMannschaftDTOMapper.toDO.apply(dsbMannschaftDTO);

            final DsbMannschaftDO savedDsbMannschaftDO = dsbMannschaftComponent.create(newDsbMannschaftDO, userId);

            if(newDsbMannschaftDO.getVereinId().equals(PLATZHALTER_VEREIN_ID)){
                createMannschaftsMitgliedForPlatzhalter(savedDsbMannschaftDO, principal);
            }

            return DsbMannschaftDTOMapper.toDTO.apply(savedDsbMannschaftDO);

        } else {
            throw new BusinessException(
                    ErrorCode.NO_PERMISSION_ERROR,
                    ERROR_MSG_CREATE_MANNSCHAFT_NO_PERMISSION
            );
        }
    }


    /**
     * I create 3 Schuetzen for the Platzhalter when the Platzhalter is created
     * @param savedDsbMannschaftDO the created Platzhalter team
     * @param principal authenticated user
     */
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_MANNSCHAFT,UserPermission.CAN_MODIFY_MY_VEREIN})
    public void createMannschaftsMitgliedForPlatzhalter(@RequestBody final DsbMannschaftDO savedDsbMannschaftDO,
                                                        final Principal principal) {
        Preconditions.checkArgument(savedDsbMannschaftDO.getVereinId().equals(PLATZHALTER_VEREIN_ID), "tja");
    }



    /**
     * I check if an Platzhalter is already in the Veranstaltung
     * @param actualMannschaftInVeranstaltungCount a list of all the current teams in the Veranstaltung
     * @param allExistingPlatzhalterList a list of all existing Platzhalter teams
     * @param dsbMannschaftDTO of the request body
     * @param veranstaltungsgroesse the size of the Veranstaltung
     * @param principal authenticated user
     */

    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_MANNSCHAFT, UserPermission.CAN_MODIFY_MY_VEREIN})
    public void checkForPlatzhalter(@RequestBody final List<DsbMannschaftDO> actualMannschaftInVeranstaltungCount,
                                    final List<DsbMannschaftDO> allExistingPlatzhalterList,
                                    final DsbMannschaftDTO dsbMannschaftDTO,
                                    final int veranstaltungsgroesse,
                                    final Principal principal) {

        // Check if the user has the required permission
        if (this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_CREATE_MANNSCHAFT) ||
                this.requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, dsbMannschaftDTO.getVereinId())) {

            // Check basic preconditions
            checkPreconditions(dsbMannschaftDTO);
            Preconditions.checkArgument(veranstaltungsgroesse >= 0, PRECONDITION_MSG_VERANSTALTUNG_SIZE_NEGATIV);

            final Long userId = UserProvider.getCurrentUserId(principal);
            Preconditions.checkArgument(userId >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_BENUTZER_ID_NEGATIVE);

            // Loop through all the existing Platzhalter teams
            for (int i = 0; i < allExistingPlatzhalterList.size(); i++) {
                Long platzhalterVeranstaltungsId = allExistingPlatzhalterList.get(i).getVeranstaltungId();
                Long platzhalterId = allExistingPlatzhalterList.get(i).getId();

                //  Replaced `continue` with an `if` condition to only process the relevant iteration
                if (platzhalterVeranstaltungsId.equals(dsbMannschaftDTO.getVeranstaltungId())) {

                    // Check if the new team is a Platzhalter
                    Preconditions.checkArgument(!dsbMannschaftDTO.getVereinId().equals(PLATZHALTER_VEREIN_ID),
                            PRECONDITION_MSG_PLATZHALTER_DUPLICATE_VERANSTALTUNG_EXISTING);

                    // If the new team is not a Platzhalter,
                    // The Veranstaltung already has a Platzhalter, and the capacity is reached -> delete the Platzhalter
                    if (platzhalterVeranstaltungsId.equals(dsbMannschaftDTO.getVeranstaltungId())
                            && veranstaltungsgroesse == actualMannschaftInVeranstaltungCount.size()
                            && !dsbMannschaftDTO.getVereinId().equals(platzhalterId)) {

                        // Delete the Platzhalter team
                        delete(platzhalterId, principal);
                        break; // Keep the `break` to exit the loop after deleting the Platzhalter
                    }
                }
                // If the condition does not match, the loop just continues to the next iteration without processing
            }
        } else {
            // Throw an exception if the user does not have permission
            throw new BusinessException(
                    ErrorCode.NO_PERMISSION_ERROR,
                    ERROR_MSG_PLATZHALTER_NO_PERMISSION
            );
        }
    }




    /**
     * I copy the dsbMannschaft entries in the database with the given Veranstaltungs-Ids.
     * @param lastVeranstaltungsId
     * @param currentVeranstaltungsId
     * @param principal
     */
    @GetMapping(value = "byLastVeranstaltungsID/{lastVeranstaltungsId}/{currentVeranstaltungsId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_MANNSCHAFT,UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public void copyMannschaftFromVeranstaltung(@PathVariable("lastVeranstaltungsId") final Long lastVeranstaltungsId,
                                              @PathVariable("currentVeranstaltungsId") final Long currentVeranstaltungsId,
                                              final Principal principal) {

        Preconditions.checkArgument(lastVeranstaltungsId >= 0, PRECONDITION_MSG_ID_NEGATIVE);
        Preconditions.checkArgument(currentVeranstaltungsId >= 0, PRECONDITION_MSG_ID_NEGATIVE);

        final Long userId = UserProvider.getCurrentUserId(principal);
        LOG.debug("Receive 'copyMannschaftOnVeranstaltung' request with ID '{}'", lastVeranstaltungsId);
        LOG.debug("Receive 'copyMannschaftOnVeranstaltung' request with ID '{}'", currentVeranstaltungsId);
        dsbMannschaftComponent.copyMannschaftFromVeranstaltung(lastVeranstaltungsId, currentVeranstaltungsId, userId);

    }
    /**
     * I copy a single dsbMannschaft entries in the database, setting veranstaltung-id = null
     * @param mannschaftId
     * @param principal
     */
    @GetMapping(value = "copyMannschaftID/{mannschaftId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_MANNSCHAFT,UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public void copyMannschaft(@PathVariable("mannschaftId") final Long mannschaftId,
                                                final Principal principal) {

        Preconditions.checkArgument(mannschaftId >= 0, PRECONDITION_MSG_ID_NEGATIVE);

        final Long userId = UserProvider.getCurrentUserId(principal);
        LOG.debug("Receive 'copyMannschaft' request with ID '{}'", mannschaftId);
        dsbMannschaftComponent.copyMannschaft(mannschaftId, userId);

    }

    /**
     * I assign the mannschaft with the given mannschaft id into the veranstaltung with the given veranstaltung id.
     * @param veranstaltungsId
     * @param mannschaftId
     * @param principal
     */
    @GetMapping(value = "assignMannschaftToVeranstaltung/{VeranstaltungsId}/{MannschaftId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_MANNSCHAFT,UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public void assignMannschaftToVeranstaltung(@PathVariable("VeranstaltungsId") final Long veranstaltungsId,
                                                @PathVariable("MannschaftId") final Long mannschaftId,
                                                final Principal principal) throws NoPermissionException {

        final Long userId = UserProvider.getCurrentUserId(principal);



        Preconditions.checkArgument(veranstaltungsId >= 0, PRECONDITION_MSG_ID_NEGATIVE);
        Preconditions.checkArgument(mannschaftId >= 0, PRECONDITION_MSG_ID_NEGATIVE);

        DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(mannschaftId);
        if(!this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_MODIFY_STAMMDATEN)
                && !this.requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG,veranstaltungsId)){
            throw new NoPermissionException();
        }

        dsbMannschaftDO.setVeranstaltungId(veranstaltungsId);
        VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(veranstaltungsId);
        // Bei laufender Veranstaltung duerfen keine Mannschaften zugeordnet werden
        // (Pruefung auf dem bereits geladenen DO -> kein erneutes findById).
        assertVeranstaltungNotLaufend(veranstaltungDO);
        dsbMannschaftDO.setSportjahr(veranstaltungDO.getVeranstaltungSportJahr());

        DsbMannschaftDO neueMannschaft = dsbMannschaftComponent.update(dsbMannschaftDO, userId);


        LOG.debug("Mannschaft '{}'  Veranstaltung mit id '{}' zugeordnet.", neueMannschaft.getName(), neueMannschaft.getVeranstaltungId());

    }

    @GetMapping(value = "unassignMannschaftFromVeranstaltung/{MannschaftId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_CREATE_MANNSCHAFT,UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public void unassignMannschaftFromVeranstaltung(@PathVariable("MannschaftId") final Long mannschaftId,
                                                final Principal principal) {

        final Long userId = UserProvider.getCurrentUserId(principal);


        Preconditions.checkArgument(mannschaftId >= 0, PRECONDITION_MSG_ID_NEGATIVE);

        DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(mannschaftId);

        // Bei laufender Veranstaltung darf die Zuordnung der Mannschaft nicht entfernt werden.
        assertVeranstaltungNotLaufend(dsbMannschaftDO.getVeranstaltungId());

        // Prüfen, ob die Veranstaltung in der Phase "geplant" ist -
        // nur dann können wir löschen, ohne dass Daten verloren gehen könnten...
        // das Sportjahr wird immer parallel gelöscht/gesetzt

        if (dsbMannschaftDO.getVeranstaltungId() != null){ // hier ist eine Veranstaltung zugewiesen
            VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(dsbMannschaftDO.getVeranstaltungId());
            if (veranstaltungDO != null && veranstaltungDO.getVeranstaltungPhase().equals("Geplant")) {
                dsbMannschaftDO.setVeranstaltungId(null);
                dsbMannschaftDO.setSportjahr(null);
                DsbMannschaftDO neueMannschaft = dsbMannschaftComponent.update(dsbMannschaftDO, userId);
                LOG.debug("Mannschaft '{}'  aus Veranstaltung mit id '{}' entfernt.", neueMannschaft.getName(), veranstaltungDO.getVeranstaltungID());
            }
        }
    }

    /**
     * Stellt sicher, dass die Veranstaltung nicht in der Phase 'Laufend' ist.
     * In dieser Phase duerfen teilnehmende Mannschaften weder hinzugefuegt noch
     * entfernt oder umsortiert werden (serverseitige Absicherung der UI-Sperre
     * aus Ticket swt2#2157, vgl. swt2#2229). Bei {@code null} (keine Veranstaltung
     * zugeordnet) passiert nichts.
     *
     * @param veranstaltungsId id der zu pruefenden Veranstaltung (darf null sein)
     * @throws BusinessException wenn die Veranstaltung in der Phase 'Laufend' ist
     */
    private void assertVeranstaltungNotLaufend(final Long veranstaltungsId) {
        if (veranstaltungsId != null && veranstaltungComponent.isVeranstaltungLaufend(veranstaltungsId)) {
            throw new BusinessException(ErrorCode.ENTITY_CONFLICT_ERROR, ERROR_MSG_VERANSTALTUNG_LAUFEND);
        }
    }

    /** Variante fuer eine bereits geladene Veranstaltung (vermeidet erneutes findById). */
    private void assertVeranstaltungNotLaufend(final VeranstaltungDO veranstaltung) {
        if (veranstaltungComponent.isVeranstaltungLaufend(veranstaltung)) {
            throw new BusinessException(ErrorCode.ENTITY_CONFLICT_ERROR, ERROR_MSG_VERANSTALTUNG_LAUFEND);
        }
    }

    /**
     * I persist a newer version of the dsbMannschaft in the database.
     *
     * Usage:
     * <pre>{@code Request: PUT /v1/dsbmannschaft
     * Body:
     * {
     *    "id": "app.bogenliga.frontend.autorefresh.active",
     *    "value": "true"
     * }
     * }</pre>
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions( perm = {UserPermission.CAN_MODIFY_MANNSCHAFT, UserPermission.CAN_MODIFY_MY_VEREIN})
    public DsbMannschaftDTO update(@RequestBody final DsbMannschaftDTO dsbMannschaftDTO, final Principal principal) {
        //Check if the User has a General Permission or,
        //check if his vereinId equals the vereinId of the mannschaft he wants to modify a Team in
        //and if the user has the permission to modify his verein.
        //if the My_Permission is used, the User is not allowed to change the Liga of the Mannschaft
        DsbMannschaftDO dsbMannschaftDO = this.dsbMannschaftComponent.findById(dsbMannschaftDTO.getId());
        if(!this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_MODIFY_MANNSCHAFT) && (!this.requiresOnePermissionAspect.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN, dsbMannschaftDTO.getVereinId())
                    ||!dsbMannschaftDO.getVeranstaltungId().equals(dsbMannschaftDTO.getVeranstaltungId()))) {
                throw new BusinessException(
                        ErrorCode.NO_PERMISSION_ERROR,
                        ERROR_MSG_UPDATE_MANNSCHAFT_NO_PERMISSION
                );
        }

        checkPreconditions(dsbMannschaftDTO);
        Preconditions.checkArgument(dsbMannschaftDTO.getId() >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_ID);
        // If VeranstaltungsId was changed, check size of new Veranstaltung and if it is at capacity
        // If Veranstaltung does not exist choose 8 as its default capacity
        List<DsbMannschaftDTO> mannschaftenInZielveranstaltung = findAllByVeranstaltungsId(dsbMannschaftDTO.getVeranstaltungId());
        int veranstaltungsgroesse = 8;
        if(dsbMannschaftDO != null && (!dsbMannschaftDTO.getVeranstaltungId().equals(dsbMannschaftDO.getVeranstaltungId()))) {
            VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(dsbMannschaftDTO.getVeranstaltungId());
            if(veranstaltungDO != null) {
                veranstaltungsgroesse = veranstaltungDO.getVeranstaltungGroesse();
            }
            if (mannschaftenInZielveranstaltung.size() >= veranstaltungsgroesse) {
                throw new BusinessException(ErrorCode.VERANSTALTUNG_MAX_CAPACITY_ERROR, ERROR_MSG_VERANSTALTUNG_FULL);
            }
        }


        LOG.debug(
                "Receive 'create' request with verein nummer '{}', mannschaft-nr '{}',  benutzer id '{}', veranstaltung id '{}',",
                // dsbMannschaftDTO.getId(),
                dsbMannschaftDTO.getVereinId(),
                dsbMannschaftDTO.getNummer(),
                dsbMannschaftDTO.getBenutzerId(),
                dsbMannschaftDTO.getVeranstaltungId());

        final DsbMannschaftDO newDsbMannschaftDO = DsbMannschaftDTOMapper.toDO.apply(dsbMannschaftDTO);
        final Long userId = UserProvider.getCurrentUserId(principal);

        final DsbMannschaftDO updatedDsbMannschaftDO = dsbMannschaftComponent.update(newDsbMannschaftDO, userId);
        return DsbMannschaftDTOMapper.toDTO.apply(updatedDsbMannschaftDO);
    }


    /**
     * I delete an existing dsbMannschaft entry from the database.
     *
     * Usage:
     * <pre>{@code Request: DELETE /v1/dsbmitglied/app.bogenliga.frontend.autorefresh.active}</pre>
     */
    @DeleteMapping(value = "{id}")
    @RequiresOnePermissions(perm = {UserPermission.CAN_DELETE_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, UserPermission.CAN_MODIFY_MY_VEREIN})
    public void delete(@PathVariable("id") final long id, final Principal principal) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_ID_NEGATIVE);
        // allow value == null, the value will be ignored
        final DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(id);
        final long userId = UserProvider.getCurrentUserId(principal);

        LOG.debug("Receive 'delete' request with id '{}'", id);

        if (!this.requiresOnePermissionAspect.hasPermission(UserPermission.CAN_DELETE_STAMMDATEN)
                && !this.requiresOnePermissionAspect.hasSpecificPermissionLigaLeiterID(
                UserPermission.CAN_MODIFY_MY_VERANSTALTUNG,
                dsbMannschaftDO.getVeranstaltungId())
                && !this.requiresOnePermissionAspect.hasSpecificPermissionSportleiter(
                UserPermission.CAN_MODIFY_MY_VEREIN,
                dsbMannschaftDO.getVereinId())) {
            throw new BusinessException(
                    ErrorCode.NO_PERMISSION_ERROR,
                    ERROR_MSG_DELETE_MANNSCHAFT_NO_PERMISSION
            );
        }

        // Wenn eine Veranstaltung zugeordnet ist (id!=null) und die Phase ist nicht "Geplant", dann nicht löschen
        if (dsbMannschaftDO.getVeranstaltungId() != null) {
            final VeranstaltungDO veranstaltungDO =
                    veranstaltungComponent.findById(dsbMannschaftDO.getVeranstaltungId());

            if (veranstaltungDO != null) {

                if (!veranstaltungDO.getVeranstaltungPhase().equals("Geplant")) {
                    throw new BusinessException(
                            ErrorCode.ENTITY_CONFLICT_ERROR,
                            "Mannschaft kann nicht gelöscht werden - es liegen weitere abhängige Daten vor."
                    );
                }

                // Neue Prüfung: nur vor Meldedeadline löschen
                if (veranstaltungDO.getVeranstaltungMeldeDeadline() != null) {
                    final Date today = new Date();
                    final Date meldeDeadline = veranstaltungDO.getVeranstaltungMeldeDeadline();

                    if (!today.before(meldeDeadline)) {
                        throw new BusinessException(
                                ErrorCode.MANNSCHAFT_DELETE_AFTER_MELDEDEADLINE,
                                "Mannschaft kann nach Erreichen der Meldedeadline nicht mehr gelöscht werden."
                        );
                    }
                }
            }
        }

        dsbMannschaftComponent.delete(dsbMannschaftDO, userId);
    }

    public static void checkPreconditions(@RequestBody final DsbMannschaftDTO dsbMannschaftDTO) {
        Preconditions.checkNotNull(dsbMannschaftDTO, PRECONDITION_MSG_DSBMANNSCHAFT);
        Preconditions.checkNotNull(dsbMannschaftDTO.getVereinId(), PRECONDITION_MSG_DSBMANNSCHAFT_VEREIN_ID);
        Preconditions.checkNotNull(dsbMannschaftDTO.getNummer(), PRECONDITION_MSG_DSBMANNSCHAFT_NUMMER);


        Preconditions.checkArgument(dsbMannschaftDTO.getVereinId() >= 0,
                PRECONDITION_MSG_DSBMANNSCHAFT_VEREIN_ID_NEGATIVE);
        Preconditions.checkArgument(dsbMannschaftDTO.getNummer() >= 0,
                PRECONDITION_MSG_DSBMANNSCHAFT_NUMMER_NEGATIVE);

    }
}
