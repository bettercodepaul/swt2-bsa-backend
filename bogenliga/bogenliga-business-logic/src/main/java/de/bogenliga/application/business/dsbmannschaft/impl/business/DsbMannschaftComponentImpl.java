package de.bogenliga.application.business.dsbmannschaft.impl.business;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftSortierungComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.dsbmannschaft.impl.dao.DsbMannschaftDAO;
import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBE;
import de.bogenliga.application.business.dsbmannschaft.impl.mapper.DsbMannschaftMapper;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAO;
import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Philip Dengler
 */

@Component
public class DsbMannschaftComponentImpl implements DsbMannschaftComponent, DsbMannschaftSortierungComponent {

    private static final String PRECONDITION_MSG_DSBMANNSCHAFT= "DsbMannschaftDO must not be null";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_ID = "DsbMannschaftDO ID must not be negative";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_VEREIN_ID = "DsbMannschaftDO Verein ID must not be negative";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_NUMMER = "DsbMannschaftDO Nummer must not be negative";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_BENUTZER_ID = "DsbMannschaftDO Benutzer Id must not be negative";
    private static final String PRECONDITION_MSG_DSBMANNSCHAFT_VERANSTALTUNG_ID = "DsbMannschaftDO Veranstaltung ID must not be negative";
    private static final String PRECONDITION_MSG_CURRENT_DSBMANNSCHAFT = "Current dsbmannschaft id must not be negative";
    private static final String PRECONDITION_MSG_SORTIERUNG = "The Sortierung must not be null or negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNGS_ID = "Veranstaltungs ID must not be negative";

    private static final String EXCEPTION_NO_RESULTS = "No result for ID '%s'";

    private final DsbMannschaftDAO dsbMannschaftDAO;
    private final VereinDAO vereinDAO;


    /**
     * Constructor
     *
     * dependency injection with {@link Autowired}
     * @param dsbMannschaftDAO to access the database and return dsbmannschaft representations
     */

    @Autowired
    public DsbMannschaftComponentImpl(final DsbMannschaftDAO dsbMannschaftDAO,
                                      final VereinDAO vereinDAO) {

        this.dsbMannschaftDAO = dsbMannschaftDAO;
        this.vereinDAO = vereinDAO;
    }

    public DsbMannschaftDAO getDAO(){
        return dsbMannschaftDAO;
    }


    @Override
    public List<DsbMannschaftDO> findAll() {
        final List<DsbMannschaftBE> dsbMannschaftBeList = dsbMannschaftDAO.findAll();
        return this.fillAllNames(dsbMannschaftBeList.stream()
                .map(DsbMannschaftMapper.toDsbMannschaftDO).collect(Collectors.toList()));
    }

    @Override
    public List<DsbMannschaftDO> findAllByVereinsId(long id){
        Preconditions.checkArgument( id>= 0, PRECONDITION_MSG_DSBMANNSCHAFT_ID);
        final List<DsbMannschaftBE> dsbMannschaftBeList = dsbMannschaftDAO.findAllByVereinsId(id);
        if(dsbMannschaftBeList == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format(EXCEPTION_NO_RESULTS, id));
        }

        return fillAllNames(dsbMannschaftBeList.stream()
                .map(DsbMannschaftMapper.toDsbMannschaftDO).collect(Collectors.toList()));
    }

    @Override
    public List<DsbMannschaftDO> findAllByVeranstaltungsId(long id){
        Preconditions.checkArgument( id>= 0, PRECONDITION_MSG_VERANSTALTUNGS_ID);
        final List<DsbMannschaftBE> dsbMannschaftBeList = dsbMannschaftDAO.findAllByVeranstaltungsId(id);
        if(dsbMannschaftBeList == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format(EXCEPTION_NO_RESULTS, id));
        }

        return fillAllNames(dsbMannschaftBeList.stream()
                .map(DsbMannschaftMapper.toDsbMannschaftDO).collect(Collectors.toList()));
    }


    @Override
    public DsbMannschaftDO findById(final long id){
        Preconditions.checkArgument( id>= 0, PRECONDITION_MSG_DSBMANNSCHAFT_ID);

        final DsbMannschaftBE result = dsbMannschaftDAO.findById(id);

        if(result == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format(EXCEPTION_NO_RESULTS, id));
        }
        return fillName(DsbMannschaftMapper.toDsbMannschaftDO.apply(result));
    }



    @Override
    public DsbMannschaftDO create(final DsbMannschaftDO dsbMannschaftDO, final long currentDsbMannschaftId) {
        checkDsbMannschaftDO(dsbMannschaftDO, currentDsbMannschaftId);

        final DsbMannschaftBE dsbMannschaftBE = DsbMannschaftMapper.toDsbMannschaftBE.apply(dsbMannschaftDO);
        final DsbMannschaftBE persistedDsbMannschaftBE = dsbMannschaftDAO.create(dsbMannschaftBE, currentDsbMannschaftId);

        return fillName(DsbMannschaftMapper.toDsbMannschaftDO.apply(persistedDsbMannschaftBE));
    }



    @Override
    public DsbMannschaftDO update(final DsbMannschaftDO dsbMannschaftDO, final long currentDsbMannschaftId) {
        checkDsbMannschaftDO(dsbMannschaftDO, currentDsbMannschaftId);
        Preconditions.checkArgument(dsbMannschaftDO.getId() >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_ID);
        checkSortierung(dsbMannschaftDO); //To avoid corruption of the Sortierung

        final DsbMannschaftBE dsbMannschaftBE = DsbMannschaftMapper.toDsbMannschaftBE.apply(dsbMannschaftDO);
        final DsbMannschaftBE persistedDsbMannschaftBE = dsbMannschaftDAO.update(dsbMannschaftBE, currentDsbMannschaftId);

        return fillName(DsbMannschaftMapper.toDsbMannschaftDO.apply(persistedDsbMannschaftBE));
    }


    @Override
    public void delete(final DsbMannschaftDO dsbMannschaftDO, final long currentDsbMannschaftId) {
        Preconditions.checkNotNull(dsbMannschaftDO, PRECONDITION_MSG_DSBMANNSCHAFT);
        Preconditions.checkArgument(dsbMannschaftDO.getId() >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_ID);
        Preconditions.checkArgument(currentDsbMannschaftId >= 0, PRECONDITION_MSG_CURRENT_DSBMANNSCHAFT);

        final DsbMannschaftBE dsbMannschaftBE = DsbMannschaftMapper.toDsbMannschaftBE.apply(dsbMannschaftDO);

        dsbMannschaftDAO.delete(dsbMannschaftBE, currentDsbMannschaftId);

    }

    /**
     * I set the attribute 'name' of all given Mannschaften by loading the corresponding Verein.
     * Name = Verein_Name +" "+ Mannschaft_Nummer
     *
     * @param mannschaften Several MannschaftDOs with missing name.
     * @return the same Mannschaften as given but with their names filled.
     */
    private List<DsbMannschaftDO> fillAllNames(List<DsbMannschaftDO> mannschaften){
        return mannschaften.stream().map(this::fillName).collect(Collectors.toList());
    }

    /**
     * I set the attribute 'name' of the given Mannschaft by loading the corresponding Verein.
     * Name = Verein_Name +" "+ Mannschaft_Nummer
     *
     * @param mannschaft A MannschaftDO with missing name.
     * @return the same MannschaftDO but with it's name filled.
     */
    private DsbMannschaftDO fillName(DsbMannschaftDO mannschaft) {
        Preconditions.checkNotNull(mannschaft, PRECONDITION_MSG_DSBMANNSCHAFT);
        Preconditions.checkArgument(mannschaft.getVereinId() >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_VEREIN_ID);

        VereinBE vereinBE = this.vereinDAO.findById(mannschaft.getVereinId());
        if (vereinBE != null && vereinBE.getVereinName() != null) {
            mannschaft.setName(vereinBE.getVereinName() + " " + mannschaft.getNummer());
        }
        return mannschaft;
    }


    private void checkDsbMannschaftDO(final DsbMannschaftDO dsbMannschaftDO, final long currentDsbMannschaftId) {
        Preconditions.checkNotNull(dsbMannschaftDO, PRECONDITION_MSG_DSBMANNSCHAFT);
        Preconditions.checkArgument(currentDsbMannschaftId >= 0, PRECONDITION_MSG_CURRENT_DSBMANNSCHAFT);
        Preconditions.checkArgument(dsbMannschaftDO.getVereinId() >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_VEREIN_ID);
        Preconditions.checkArgument(dsbMannschaftDO.getNummer() >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_NUMMER);
        Preconditions.checkArgument(dsbMannschaftDO.getBenutzerId() >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_BENUTZER_ID);
        Preconditions.checkArgument(dsbMannschaftDO.getVeranstaltungId() >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_VERANSTALTUNG_ID);

    }

    //for sorting:

    @Override
    public DsbMannschaftDO updateSortierung(DsbMannschaftDO mannschaftDO, long currentDsbMitgliedID){
        Preconditions.checkNotNull(mannschaftDO,PRECONDITION_MSG_DSBMANNSCHAFT);
        Preconditions.checkArgument(mannschaftDO.getId() >= 0, PRECONDITION_MSG_DSBMANNSCHAFT_ID);
        Preconditions.checkArgument(mannschaftDO.getSortierung() >= 0, PRECONDITION_MSG_SORTIERUNG );

        fillDO(mannschaftDO);

        final DsbMannschaftBE dsbMannschaftBE = DsbMannschaftMapper.toDsbMannschaftBE.apply(mannschaftDO);
        final DsbMannschaftBE persistedDsbMannschaftBE = dsbMannschaftDAO.update(dsbMannschaftBE, currentDsbMitgliedID);

        return DsbMannschaftMapper.toDsbMannschaftDO.apply(persistedDsbMannschaftBE);
    }

    /**
     * Fills the VerinsID, Nummer, VeranstaltungsID, BenutzerID with the values from the Database.
     * Used to update the Sorting of the mannschaft, because with that method the existing update method can be used.
     * @param mannschaftDO
     */
    private void fillDO(DsbMannschaftDO mannschaftDO){
        Preconditions.checkNotNull(mannschaftDO,PRECONDITION_MSG_DSBMANNSCHAFT);
        DsbMannschaftDO doFromDatabase = DsbMannschaftMapper.toDsbMannschaftDO.apply(dsbMannschaftDAO.findById(mannschaftDO.getId()));

        if(doFromDatabase != null) {
            checkSortierung(mannschaftDO, doFromDatabase);
            mannschaftDO.setVereinId(doFromDatabase.getVereinId());
            mannschaftDO.setNummer(doFromDatabase.getNummer());
            mannschaftDO.setVeranstaltungId(doFromDatabase.getVeranstaltungId());
            mannschaftDO.setBenutzerId(doFromDatabase.getBenutzerId());
        }
    }

    /**
     * Wrapper method for the other checkSortierung method.
     * @param mannschaftDO
     * @return
     */
    private DsbMannschaftDO checkSortierung(DsbMannschaftDO mannschaftDO){
        return this.checkSortierung(mannschaftDO, null);
    }

    /**
     * Checks the given MannschaftDO to evade a wrong update of the Soriterung.
     * Example: The sortierung was set to 5. Then the name changes and the update method will be called.
     * In this call the sortierung will be 0.
     * To avoid this the method compares the given value of sortierung with the one from the database.
     * @param mannschaftDO
     * @param doFromDatabase A optional parameter to increase performance in the updateSorteirung mehtod.
     *                       null allowed!
     * @return
     */
    private DsbMannschaftDO checkSortierung(DsbMannschaftDO mannschaftDO, DsbMannschaftDO doFromDatabase){
        Preconditions.checkNotNull(mannschaftDO,PRECONDITION_MSG_DSBMANNSCHAFT);

        if(doFromDatabase == null) {
            doFromDatabase = DsbMannschaftMapper.toDsbMannschaftDO.apply(dsbMannschaftDAO.findById(mannschaftDO.getId()));
        }
        if( (mannschaftDO.getSortierung() == null && doFromDatabase != null) ||
                (mannschaftDO.getSortierung() != null && mannschaftDO.getSortierung().equals(0L) &&
                        doFromDatabase != null && doFromDatabase.getSortierung() > 0) ) {
            mannschaftDO.setSortierung(doFromDatabase.getSortierung());
        }
        return mannschaftDO;
    }


    /**
     * Copys the Mannschaften of an old Veranstaltung into a new Veranstaltung
     * returns a list of all created database entrys of Mannschaften
     * @param lastVeranstaltungId
     * @param currentVeranstaltungId
     * @param userId
     * @return
     */
    @Override
    public List<DsbMannschaftDO> copyMannschaftFromVeranstaltung(long lastVeranstaltungId, long currentVeranstaltungId, long userId) {

        final List<DsbMannschaftBE> lastMannschaftList = dsbMannschaftDAO.findAllByVeranstaltungsId(lastVeranstaltungId);

        List<DsbMannschaftDO> lastMListDO = fillAllNames(lastMannschaftList.stream()
                .map(DsbMannschaftMapper.toDsbMannschaftDO).collect(Collectors.toList()));

        // creates a new database entry for every Mannschaft with the current VeranstaltungId
        List<DsbMannschaftDO> addedMannschaftenList = new ArrayList<>();
        for(DsbMannschaftDO mannschaftToCheck : lastMListDO) {

            mannschaftToCheck.setVeranstaltungId(currentVeranstaltungId);
            checkDsbMannschaftDO(mannschaftToCheck, currentVeranstaltungId);
            addedMannschaftenList.add(mannschaftToCheck);
            final DsbMannschaftBE dsbMannschaftBE = DsbMannschaftMapper.toDsbMannschaftBE.apply(mannschaftToCheck);
            dsbMannschaftDAO.create(dsbMannschaftBE, currentVeranstaltungId);
        }
        return addedMannschaftenList;
    }
}
