package de.bogenliga.application.services.v1.veranstaltung.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import org.apache.tomcat.jni.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.common.service.ServiceFacade;
import de.bogenliga.application.common.service.UserProvider;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.services.v1.sportjahr.SportjahrDTO;
import de.bogenliga.application.services.v1.sportjahr.mapper.SportjahrDTOMapper;
import de.bogenliga.application.services.v1.veranstaltung.mapper.VeranstaltungDTOMapper;
import de.bogenliga.application.services.v1.veranstaltung.model.VeranstaltungDTO;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions;
import de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * I'm a REST resource and handle veranstaltung CRUD requests over the HTTP protocol
 *
 * @author Marvin Holm
 */
@RestController
@CrossOrigin
@RequestMapping("v1/veranstaltung")
public class VeranstaltungService implements ServiceFacade {
    private static final Logger LOG = LoggerFactory.getLogger(
            VeranstaltungService.class);

    private final VeranstaltungComponent veranstaltungComponent;

    private static final String PRECONDITION_MSG_VERANSTALTUNG = "Veranstaltung must not be null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_ID = "Veranstaltung Id must not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_NAME = "Veranstaltung Name can not be null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_WETTKAMPFTYP_ID = "Veranstaltung Wettkampftyp id can not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_SPORTJARHR = "Veranstaltung Sportjahr can not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_MELDEDEADLINE = "Veranstaltung Meldedeadline can not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGALEITER_ID= "Veranstaltung Ligaleiter IF id can not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGA_ID= "Veranstaltung Liga id can not be negative";
    private final  JwtTokenProvider jwtTokenProvider;
    private final UserComponent userComponent;


    /**
     * Constructor with dependency injection
     *
     * @param veranstaltungComponent to handle the database CRUD requests
     * @param jwtTokenProvider
     * @param userComponent
     */
    @Autowired
    public VeranstaltungService(final VeranstaltungComponent veranstaltungComponent,
                                JwtTokenProvider jwtTokenProvider,
                                UserComponent userComponent){
        this.veranstaltungComponent = veranstaltungComponent;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userComponent = userComponent;
    }


    /**
     * I return all the teams (veranstaltung) of the database.
     * @return List of VeranstaltungDTOs
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VeranstaltungDTO> findAll(){

        LOG.debug("Received 'findAll' request for Veranstaltung");
        final List<VeranstaltungDO> VeranstaltungDOList = veranstaltungComponent.findAll();
        return VeranstaltungDOList.stream().map(VeranstaltungDTOMapper.toDTO).collect(Collectors.toList());
    }

    /**
     * I return the veranstaltung Entry of the database with a specific id
     *
     * @return list of {@link VeranstaltungDTO} as JSON
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public VeranstaltungDTO findById(@PathVariable ("id") final long id){
        Preconditions.checkArgument(id >= 0 , "ID must not be negative");

        LOG.debug("Receive 'findById' with requested ID '{}'", id);

        final VeranstaltungDO veranstaltungDO = veranstaltungComponent.findById(id);
        return VeranstaltungDTOMapper.toDTO.apply(veranstaltungDO);
    }

    /**
     * I return the veranstaltung Entry of the database with a specific id
     *
     * @return list of {@link VeranstaltungDTO} as JSON

     */
    @GetMapping(value = "findByLigaID/{ligaID}", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VeranstaltungDTO> findByLigaId(@PathVariable ("ligaID") final long ligaID){
        Preconditions.checkArgument(ligaID >= 0 , "ID must not be negative");

        LOG.debug("Receive 'findByLigaID' with requested ID '{}'", ligaID);

        final List<VeranstaltungDO> VeranstaltungDOList = veranstaltungComponent.findByLigaID(ligaID);

        return VeranstaltungDOList.stream().map(VeranstaltungDTOMapper.toDTO).collect(Collectors.toList());

    }

    /**
     *
     * @return a list with all sportjahre distinct
     */
    @GetMapping(value = "destinct/sportjahr",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<SportjahrDTO> findAllSportjahrDestinct(){

        LOG.debug("Received 'findBySportyear' request for Sportjahre in Veranstaltung  ");
        List<SportjahrDO> returnList= veranstaltungComponent.findAllSportjahreDestinct();

        return returnList.stream().map(SportjahrDTOMapper.toDTO).collect(Collectors.toList());
    }


    /**
     *
     * @param sportjahr - filterr for sql-abfrage
     * @return return Veranstaltungen with the same Sportjahr
     */
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "find/by/year/{sportjahr}")
    @RequiresPermission(UserPermission.CAN_READ_DEFAULT)
    public List<VeranstaltungDTO> findBySportjahr(@PathVariable ("sportjahr") final long sportjahr){

        LOG.debug("Received 'findBySportyear' request for Veranstaltung in {}", sportjahr);
        final List<VeranstaltungDO> VeranstaltungDOList = veranstaltungComponent.findBySportjahr(sportjahr);

        return VeranstaltungDOList.stream().map(VeranstaltungDTOMapper.toDTO).collect(Collectors.toList());
    }

    /**
     * I persist a new veranstaltung and return this veranstaltung entry
     *
     * You are only able to create a Veranstaltung, if you have the explicit permission to Create it or
     * if you are the Ligaleiter of the Veranstaltung.
     *
     * @param veranstaltungDTO
     * @param principal
     *
     * @return list of {@link VeranstaltungDTO} as JSON
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresPermission(UserPermission.CAN_CREATE_STAMMDATEN)
    public VeranstaltungDTO create(@RequestBody final VeranstaltungDTO veranstaltungDTO, final Principal principal) {

        checkPreconditions(veranstaltungDTO);
        LOG.debug(
                "Receive 'create' request with veranstaltungId '{}', veranstaltungName '{}', wettkampftypid '{}', sportjahr '{}', meldedeadline '{}', ligaleiteremail '{}', ligaid '{}' ",
                veranstaltungDTO.getId(),
                veranstaltungDTO.getName(),
                veranstaltungDTO.getWettkampfTypId(),
                veranstaltungDTO.getSportjahr(),
                veranstaltungDTO.getMeldeDeadline(),
                veranstaltungDTO.getLigaleiterEmail(),
                veranstaltungDTO.getLigaId());

        final VeranstaltungDO newVeranstaltungDO = VeranstaltungDTOMapper.toDO.apply(veranstaltungDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);


        final VeranstaltungDO savedVeranstaltungDO = veranstaltungComponent.create(newVeranstaltungDO,
                currentDsbMitglied);
        return VeranstaltungDTOMapper.toDTO.apply(savedVeranstaltungDO);
    }


    /**
     * I persist a newer version of the CompetitionClass in the database.
     *
     * You can only update a Competition, if you have the permission to Modify Stammdaten or if
     * you are the Ligaleiter of the Veranstaltung.
     */
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiresOnePermissions(perm = {UserPermission.CAN_MODIFY_STAMMDATEN, UserPermission.CAN_MODIFY_MY_VERANSTALTUNG})
    public VeranstaltungDTO update(@RequestBody final VeranstaltungDTO veranstaltungDTO,
                          final Principal principal) throws NoPermissionException {

        LOG.debug(
                "Receive 'create' request with veranstaltungId '{}', veranstaltungName '{}', wettkampftypId '{}', sportjahr '{}', meldedeadline '{}', ligaleiterId '{}', ligaId '{}'",
                veranstaltungDTO.getId(),
                veranstaltungDTO.getName(),
                veranstaltungDTO.getWettkampfTypId(),
                veranstaltungDTO.getSportjahr(),
                veranstaltungDTO.getMeldeDeadline(),
                veranstaltungDTO.getLigaleiterId(),
                veranstaltungDTO.getLigaId()
                );



        if(this.hasPermission(UserPermission.CAN_MODIFY_STAMMDATEN) || this.hasSpecificPermission(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG,veranstaltungDTO.getId())){

        }else{
            throw new NoPermissionException();
        }
        final VeranstaltungDO newVeranstaltungDO = VeranstaltungDTOMapper.toDO.apply(veranstaltungDTO);
        final long currentDsbMitglied = UserProvider.getCurrentUserId(principal);

        final VeranstaltungDO updatedVeranstaltungDO = veranstaltungComponent.update(newVeranstaltungDO,
                currentDsbMitglied);
        return VeranstaltungDTOMapper.toDTO.apply(updatedVeranstaltungDO);

    }

    /**
     * I delete an existing Veranstaltung entry from the DB.
     */
    @DeleteMapping(value = "{id}")
    @RequiresPermission(UserPermission.CAN_DELETE_STAMMDATEN)
    public void delete (@PathVariable("id") final Long id, final Principal principal){
        Preconditions.checkArgument(id >= 0, "ID must not be negative.");

        LOG.debug("Receive 'delete' request with id '{}'", id);

        final VeranstaltungDO veranstaltungDO = new VeranstaltungDO(id);
        final long userId = UserProvider.getCurrentUserId(principal);
        veranstaltungComponent.delete(veranstaltungDO,userId);
    }


    private void checkPreconditions(@RequestBody final VeranstaltungDTO veranstaltungDTO) {
        Preconditions.checkNotNull(veranstaltungDTO, PRECONDITION_MSG_VERANSTALTUNG);


        Preconditions.checkNotNull(veranstaltungDTO.getName(), PRECONDITION_MSG_VERANSTALTUNG_NAME);
        Preconditions.checkArgument(veranstaltungDTO.getWettkampfTypId() >= 0, PRECONDITION_MSG_VERANSTALTUNG_WETTKAMPFTYP_ID);
        Preconditions.checkArgument(veranstaltungDTO.getSportjahr() >= 0, PRECONDITION_MSG_VERANSTALTUNG_SPORTJARHR);
        Preconditions.checkNotNull(veranstaltungDTO.getMeldeDeadline(), PRECONDITION_MSG_VERANSTALTUNG_MELDEDEADLINE);
        Preconditions.checkNotNull(veranstaltungDTO.getLigaleiterEmail(), PRECONDITION_MSG_VERANSTALTUNG_LIGALEITER_ID);
        Preconditions.checkArgument(veranstaltungDTO.getLigaId() >= 0, PRECONDITION_MSG_VERANSTALTUNG_LIGA_ID);
    }
    /**
     * method to check, if a user has a general permission
     * @param toTest The permission whose existence is getting checked
     * @return Does the User have the searched permission
     */
    boolean hasPermission(UserPermission toTest) {
        //default value is: not allowed
        boolean result = false;
        //get the current http request from thread
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            final HttpServletRequest request = servletRequestAttributes.getRequest();
            //if a request is present:
            if(request != null) {
                //parse the Webtoken and get the UserPermissions of the current User
                final String jwt = JwtTokenProvider.resolveToken(request);
                final Set<UserPermission> userPermissions = jwtTokenProvider.getPermissions(jwt);

                //check if the resolved Permissions
                //contain the required Permission for the task.
                if(userPermissions.contains(toTest)) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * method to check, if a user has a Specific permission with the matching parameters
     * @param toTest The permission whose existence is getting checked
     * @return Does the User have searched permission
     */
    boolean hasSpecificPermission(UserPermission toTest, Long veranstaltungsid) {
        //default value is: not allowed
        boolean result = false;
        //get the current http request from thread
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            final HttpServletRequest request = servletRequestAttributes.getRequest();
            //if a request is present:
            if(request != null) {
                //parse the Webtoken and get the UserPermissions of the current User
                final String jwt = JwtTokenProvider.resolveToken(request);
                final Set<UserPermission> userPermissions = jwtTokenProvider.getPermissions(jwt);

                //check if the current Users vereinsId equals the given vereinsId and if the User has
                //the required Permission (if the permission is specifi
                Long UserId = jwtTokenProvider.getUserId(jwt);
                UserDO userDO = this.userComponent.findById(UserId);
                ArrayList<Integer> temp = new ArrayList<>();
                for(VeranstaltungDO veranstaltungDO : this.veranstaltungComponent.findByLigaleiterId(UserId)) {
                   if(veranstaltungDO.getVeranstaltungID() == veranstaltungsid){
                       result = true;
                   }
                }

            }
        }
        return result;
    }

}
