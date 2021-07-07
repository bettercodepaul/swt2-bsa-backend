package de.bogenliga.application.business.veranstaltung.impl.business;

import java.util.ArrayList;
import java.util.List;

import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampftyp.api.types.WettkampfTypDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.dao.VeranstaltungDAO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.veranstaltung.impl.mapper.VeranstaltungMapper;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.wettkampftyp.api.WettkampfTypComponent;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;


/**
 * @author Daniel Schott, daniel.schott@student.reutlingen-university.de
 */
@Component
public class VeranstaltungComponentImpl implements VeranstaltungComponent {

    private static final String PRECONDITION_MSG_VERANSTALTUNG_ID = "VeranstaltungDO must not be null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG = "VeranstaltungDO must not be null";
    private static final String PRECONDITION_MSG_CURRENT_VERANSTALTUNG = " Current veranstaltungDO id must not be null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGA_LEITER_ID = "VeranstaltungLigaLeiterId must not be null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_MELDE_DEADLINE = " veranstaltungMeldeDeadline must be not null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_WETTKAMPF_ID = "Veranstaltungwettkampfid must be not null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGA_ID = "Veranstaltungligaid must be not null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_SPORTJAHR = "veranstaltungsportjahr must be not null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_NAME = "veranstaltungname must be not null";
    private static final String PRECONDITION_MSG_CURRENT_DSBMITGLIED = "Current dsbmitglied id must not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGA_ALREADY_HAS_VERANSTALTUNG = "liga already has a veranstaltung assigned for this year";
    private final VeranstaltungDAO veranstaltungDAO;
    private final WettkampfComponent wettkampfComponent;
    private final LigaComponent ligaComponent;
    private final WettkampfTypComponent wettkampfTypComponent;
    private final UserComponent userComponent;


    /**
     * Constructor for VeranstaltungComponentImpl - Autowired by springboot
     *
     * @param veranstaltungDAO Data Access Object für Entität Veranstaltung
     */

    @Autowired
    public VeranstaltungComponentImpl(final VeranstaltungDAO veranstaltungDAO,
                                      final WettkampfComponent wettkampfComponent,
                                      final LigaComponent ligaComponent,
                                      final WettkampfTypComponent wettkampfTypComponent,
                                      final UserComponent userComponent) {


        this.wettkampfComponent = wettkampfComponent;
        this.ligaComponent = ligaComponent;
        this.wettkampfTypComponent = wettkampfTypComponent;
        this.userComponent = userComponent;

        this.veranstaltungDAO = veranstaltungDAO;
    }


    /**
     * findAll-Method gives all Veranstaltungen from the dataBase
     *
     * @return liefert die Liste aller Veranstaltungen
     */
    @Override
    public List<VeranstaltungDO> findAll() {
        final ArrayList<VeranstaltungDO> returnList = new ArrayList<>();
        final List<VeranstaltungBE> veranstaltungBEList = veranstaltungDAO.findAll();
        final List<String> cacheList = new ArrayList<>();


        for (int i = 0; i < veranstaltungBEList.size(); i++) {

            returnList.add(i, completeNames(veranstaltungBEList.get(i)));

        }
        /*
        for(int x = 0;x<returnList.size();x++){
            cacheList.add(wettkampftypDAO.findById(returnList.get(x).getVeranstaltung_wettkampftyp_id()).getName());
            System.out.println("test: " + cacheList.get(x));
            System.out.println("test2: " + returnList.get(x).getVeranstaltung_wettkampftyp_id());
        }*/
        return returnList;
    }

    public List<VeranstaltungDO> findBySportjahrDestinct(long sportjahr){
        final ArrayList<VeranstaltungDO> returnList = new ArrayList<>();
        final List<VeranstaltungBE> veranstaltungBEList = veranstaltungDAO.findBySportjahrDestinct(sportjahr);

        for (int i = 0; i < veranstaltungBEList.size(); i++) {

            returnList.add(i, completeNames(veranstaltungBEList.get(i)));

        }

        return returnList;
    }

    @Override
    public VeranstaltungDO findById(final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);

        final VeranstaltungBE result = veranstaltungDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return completeNames(result);
    }


    @Override
    public List<VeranstaltungDO> findByLigaleiterId(long ligaleiterId) {

        final ArrayList<VeranstaltungDO> returnList = new ArrayList<>();
        final List<VeranstaltungBE> veranstaltungBEList = veranstaltungDAO.findByLigaleiterId(ligaleiterId);

        for (int i = 0; i < veranstaltungBEList.size(); i++) {

            returnList.add(i, completeNames(veranstaltungBEList.get(i)));

        }

        return returnList;
    }


    @Override
    public VeranstaltungDO update(final VeranstaltungDO veranstaltungDO, final long currentDsbMitgliedId) {
        checkVeranstaltungDO(veranstaltungDO, currentDsbMitgliedId);
        Preconditions.checkArgument(veranstaltungDO.getVeranstaltungID() >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);

        final VeranstaltungBE veranstaltungBE = VeranstaltungMapper.toVeranstaltungBE.apply(veranstaltungDO);
//        System.out.println("\n\n");
//        System.out.println(veranstaltungBE.toString());
        final VeranstaltungBE persistedVeranstaltungBE = veranstaltungDAO.update(veranstaltungBE, currentDsbMitgliedId);

        return completeNames(persistedVeranstaltungBE);
    }


    @Override
    public VeranstaltungDO create(final VeranstaltungDO veranstaltungDO, final long currentDsbMitgliedId) {
        checkVeranstaltungDO(veranstaltungDO, currentDsbMitgliedId);
//        Preconditions.checkArgument(
//              validLiga(veranstaltungDO.getVeranstaltungLigaID(), veranstaltungDO.getVeranstaltungSportJahr()),
//               PRECONDITION_MSG_VERANSTALTUNG_LIGA_ALREADY_HAS_VERANSTALTUNG);

        final VeranstaltungBE veranstaltungBE = VeranstaltungMapper.toVeranstaltungBE.apply(veranstaltungDO);
        final VeranstaltungBE persistedVeranstaltungBE = veranstaltungDAO.create(veranstaltungBE, currentDsbMitgliedId);

        //create Wettkampftag 0
        //TODO die Bestiummung der User-ID im Service funktioniert nicht korrekt - daher kann diese nicht
        // als ID für den Ligaleiter genutzt werden - wir benötigen für die Fremdschlüsselbeziehung aber existierende
        // User-id - daher wird hier die Ligaleiter-Id als User-id übergeben
        // fehler: ind er DB wird ein Eintrag unter diesem User angelegt, obwohl das nicht der aktuelle User ist.
        final WettkampfDO wettkampfTag0 = wettkampfComponent.createWT0(persistedVeranstaltungBE.getVeranstaltung_id(), persistedVeranstaltungBE.getVeranstaltung_ligaleiter_id());

        return completeNames(persistedVeranstaltungBE);
    }


    @Override
    public void delete(final VeranstaltungDO veranstaltungDO, final long currentDsbMitgliedId) {
        Preconditions.checkNotNull(veranstaltungDO, PRECONDITION_MSG_VERANSTALTUNG);
        Preconditions.checkArgument(veranstaltungDO.getVeranstaltungID() >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);
        Preconditions.checkArgument(currentDsbMitgliedId >= 0, PRECONDITION_MSG_CURRENT_VERANSTALTUNG);

        final VeranstaltungBE veranstaltungBE = VeranstaltungMapper.toVeranstaltungBE.apply(veranstaltungDO);

        veranstaltungDAO.delete(veranstaltungBE, currentDsbMitgliedId);

    }


    @Override
    public List<VeranstaltungDO> findBySportjahr(long sportjahr) {
        final ArrayList<VeranstaltungDO> returnList = new ArrayList<>();
        final List<VeranstaltungBE> veranstaltungBEList = veranstaltungDAO.findBySportjahr(sportjahr);
        for (int i = 0; i < veranstaltungBEList.size(); i++) {

            returnList.add(i, completeNames(veranstaltungBEList.get(i)));

        }
        return returnList;
    }


    @Override
    public List<SportjahrDO> findAllSportjahreDestinct() {
        return veranstaltungDAO.findAllSportjahreDestinct();

    }

    @Override
    public List<VeranstaltungDO> findByLigaID(long ligaID) {
        final ArrayList<VeranstaltungDO> returnList = new ArrayList<>();
        final List<VeranstaltungBE> veranstaltungBEList = veranstaltungDAO.findByLigaID(ligaID);
        for (int i = 0; i < veranstaltungBEList.size(); i++) {

            returnList.add(i, completeNames(veranstaltungBEList.get(i)));

        }
        return returnList;
    }


    /**
     * returns the last Veranstaltung by using the current Veranstaltung ID
     * @param veranstaltungId ID of the current veranstaltung to query the last Veranstaltung.
     *
     * @return last Veranstaltung based on current one
     */
    @Override
    public VeranstaltungDO findLastVeranstaltungById(final long veranstaltungId){
        Preconditions.checkArgument(veranstaltungId >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);

        VeranstaltungDO currentVeranstaltung = this.findById(veranstaltungId);
        Long targetSportjahr = currentVeranstaltung.getVeranstaltungSportJahr();
        Long targetLiga = currentVeranstaltung.getVeranstaltungLigaID();
        Long targetWettkampf = currentVeranstaltung.getVeranstaltungWettkampftypID();

        // targeting last year's Veranstaltung with last sportjahr, liga id and wettkampftyp id
        List<VeranstaltungDO> targetVeranstaltungen = this.findBySportjahr(targetSportjahr - 1);
        VeranstaltungDO lastVeranstaltung = new VeranstaltungDO();
        for(VeranstaltungDO t : targetVeranstaltungen){
            if(t.getVeranstaltungLigaID().equals(targetLiga) && t.getVeranstaltungWettkampftypID().equals(targetWettkampf)){
                lastVeranstaltung = t;
            }
        }
        if(lastVeranstaltung.getVeranstaltungID() == null){
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No last Veranstaltung found for ID '%s'", veranstaltungId));
        }
        return lastVeranstaltung;
    }

    private void checkVeranstaltungDO(final VeranstaltungDO veranstaltungDO, final long currentDsbMitgliedId) {
        Preconditions.checkNotNull(veranstaltungDO, PRECONDITION_MSG_VERANSTALTUNG);
        Preconditions.checkArgument(currentDsbMitgliedId >= 0, PRECONDITION_MSG_CURRENT_DSBMITGLIED);
        Preconditions.checkNotNull(veranstaltungDO.getVeranstaltungName(), PRECONDITION_MSG_VERANSTALTUNG_NAME);
        //
        Preconditions.checkNotNull(veranstaltungDO.getVeranstaltungMeldeDeadline(),
                PRECONDITION_MSG_VERANSTALTUNG_MELDE_DEADLINE);
        Preconditions.checkArgument(veranstaltungDO.getVeranstaltungLigaleiterID() >= 0,
                PRECONDITION_MSG_VERANSTALTUNG_LIGA_LEITER_ID);
        Preconditions.checkArgument(veranstaltungDO.getVeranstaltungWettkampftypID() >= 0,
                PRECONDITION_MSG_VERANSTALTUNG_WETTKAMPF_ID);
        Preconditions.checkArgument(veranstaltungDO.getVeranstaltungLigaID() >= 0,
                PRECONDITION_MSG_VERANSTALTUNG_LIGA_ID);
        Preconditions.checkNotNull(veranstaltungDO.getVeranstaltungSportJahr(),
                PRECONDITION_MSG_VERANSTALTUNG_SPORTJAHR);

    }


    /**
     * Checks if a Liga already has a Veranstaltung in a specific Sportjahr
     *
     * @param liga_id   The ID of the Liga to check
     * @param sportjahr The Sportjahr to check
     *
     * @return true: when no Veranstaltung exists for Liga in Sportjahr false: when there already is a Veranstaltung for
     * Liga in Sportjahr
     */
    private boolean validLiga(final long liga_id, final long sportjahr) {
        List<VeranstaltungDO> all_veranstaltungen = this.findAll();
        for (VeranstaltungDO vdo : all_veranstaltungen) {
            if (vdo.getVeranstaltungLigaID() == liga_id && vdo.getVeranstaltungSportJahr() == sportjahr) {
                return false;
            }
        }
        return true;
    }

    // we will add all information required in VeranstaltungDO which are not stored in the entity
    // especially names in addition to IDs
    private VeranstaltungDO completeNames(VeranstaltungBE veranstaltungBE) {

        LigaDO tempLigaDO = new LigaDO();
        WettkampfTypDO tempWettkampfTypDO = new WettkampfTypDO(0L);
        UserDO tempUserDO = new UserDO();

        if (veranstaltungBE.getVeranstaltung_liga_id() != null) {
              tempLigaDO = ligaComponent.findById(veranstaltungBE.getVeranstaltung_liga_id());
        }
        if (veranstaltungBE.getVeranstaltung_wettkampftyp_id() != null) {
             tempWettkampfTypDO = wettkampfTypComponent.findById(veranstaltungBE.getVeranstaltung_wettkampftyp_id());
        }
        if (veranstaltungBE.getVeranstaltung_ligaleiter_id() != null) {
             tempUserDO = userComponent.findById(veranstaltungBE.getVeranstaltung_ligaleiter_id());
        }

        return VeranstaltungMapper.toVeranstaltungDO(veranstaltungBE, tempUserDO, tempWettkampfTypDO, tempLigaDO);
    }


}

