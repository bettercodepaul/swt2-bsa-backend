package de.bogenliga.application.business.veranstaltung.impl.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.liga.impl.dao.LigaDAO;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.liga.impl.mapper.LigaMapper;
import de.bogenliga.application.business.regionen.impl.entity.RegionenBE;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.dao.VeranstaltungDAO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.veranstaltung.impl.mapper.VeranstaltungMapper;
import de.bogenliga.application.business.wettkampftyp.impl.dao.WettkampftypDAO;
import de.bogenliga.application.business.wettkampftyp.impl.entity.WettkampftypBE;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * TODO [AL] class documentation
 *
 * @author Daniel Schott, daniel.schott@student.reutlingen-university.de
 */
@Component
public class VeranstaltungComponentImpl implements VeranstaltungComponent {

    private static final String PRECONDITION_MSG_VERANSTALTUNG_ID = "VeranstaltungDO must not be null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG ="VeranstaltungDO must not be null";
    private static final String PRECONDITION_MSG_CURRENT_VERANSTALTUNG =" Current veranstaltungDO id must not be null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGA_LEITER_ID ="VeranstaltungLigaLeiterId must not be null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_MELDE_DEADLINE=" veranstaltungMeldeDeadline must be not null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_WETTKAMPF_ID="Veranstaltungwettkampfid must be not null";
   private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGA_ID="Veranstaltungligaid must be not null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_SPORTJAHR="veranstaltungsportjahr must be not null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_NAME="veranstaltungname must be not null";
    private static final String PRECONDITION_MSG_CURRENT_DSBMITGLIED = "Current dsbmitglied id must not be negative";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGALEITER_EMAIL = "ligaleiter email must be not null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_WETTKAMPFTYP_NAME = "veranstaltungtypname must be not null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_LIGA_NAME = "veranstaltungliganame must be not null";
    private final VeranstaltungDAO veranstaltungDAO;
    private final LigaDAO ligaDAO;
    private final WettkampftypDAO wettkampftypDAO;
    private final UserDAO userDAO;

    /**
     * Constructor for VeranstaltungComponentImpl - Autowired by springboot
     * @param veranstaltungDAO
     */

    @Autowired
    public VeranstaltungComponentImpl(final VeranstaltungDAO veranstaltungDAO, final LigaDAO ligaDAO, final WettkampftypDAO wettkampftypDAO, final UserDAO userDAO) {
        this.veranstaltungDAO= veranstaltungDAO;
        this.ligaDAO = ligaDAO;
        this.wettkampftypDAO = wettkampftypDAO;
        this.userDAO = userDAO;
        System.out.println("created DAO object");
    }

    /**
     * findAll-Method gives all Veranstaltungen from the dataBase
     * @return
     */
    @Override
    public List<VeranstaltungDO> findAll() {
        final ArrayList<VeranstaltungDO> returnList = new ArrayList<>();
        final List<VeranstaltungBE> veranstaltungBEList = veranstaltungDAO.findAll();

        for (int i = 0; i < veranstaltungBEList.size(); i++) {

            returnList.add(i, notNull(veranstaltungBEList.get(i)));

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

        return notNull(result);
    }
    @Override
    public VeranstaltungDO update(final VeranstaltungDO veranstaltungDO, final long currentDsbMitgliedId){
        checkVeranstaltungDO(veranstaltungDO,currentDsbMitgliedId);
        Preconditions.checkArgument(veranstaltungDO.getVeranstaltungID() >=0, PRECONDITION_MSG_VERANSTALTUNG_ID);

        final VeranstaltungBE veranstaltungBE;
        final VeranstaltungBE persistedVeranstaltungBE;

        return null;
    }

    @Override
    public VeranstaltungDO create (final VeranstaltungDO veranstaltungDO, final long currentDsbMitgliedId){
        checkVeranstaltungDO(veranstaltungDO,currentDsbMitgliedId);
        Preconditions.checkArgument(veranstaltungDO.getVeranstaltungID() >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);

        final VeranstaltungBE veranstaltungBE = VeranstaltungMapper.toVeranstaltungBE.apply(veranstaltungDO);

     return null;
    }
    @Override
    public void delete(final VeranstaltungDO veranstltungDO, final long currentDsbMitgliedId) {
        Preconditions.checkNotNull(veranstltungDO, PRECONDITION_MSG_VERANSTALTUNG);
        Preconditions.checkArgument(veranstltungDO.getVeranstaltungID() >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);
        Preconditions.checkArgument(currentDsbMitgliedId >= 0, PRECONDITION_MSG_CURRENT_VERANSTALTUNG);

        final VeranstaltungBE  veranstaltungBE = VeranstaltungMapper.toVeranstaltungBE.apply(veranstltungDO);

        veranstaltungDAO.delete(veranstaltungBE, currentDsbMitgliedId);

    }

    private void checkVeranstaltungDO(final VeranstaltungDO veranstaltungDO, final long currentDsbMitgliedId){
    Preconditions.checkNotNull(veranstaltungDO,PRECONDITION_MSG_VERANSTALTUNG);
    Preconditions.checkArgument(currentDsbMitgliedId>=0,PRECONDITION_MSG_CURRENT_DSBMITGLIED);
    Preconditions.checkNotNull(veranstaltungDO.getVeranstaltungName(),PRECONDITION_MSG_VERANSTALTUNG_NAME);
    Preconditions.checkArgument(veranstaltungDO.getVeranstaltungID() >=0,PRECONDITION_MSG_VERANSTALTUNG_ID);
    Preconditions.checkNotNull(veranstaltungDO.getVeranstaltungMeldeDeadline(),PRECONDITION_MSG_VERANSTALTUNG_MELDE_DEADLINE);
    Preconditions.checkArgument(veranstaltungDO.getVeranstaltungLigaleiterID()>=0,PRECONDITION_MSG_VERANSTALTUNG_LIGA_LEITER_ID);
    Preconditions.checkArgument(veranstaltungDO.getVeranstaltungWettkampftypID()>=0,PRECONDITION_MSG_VERANSTALTUNG_WETTKAMPF_ID);
    Preconditions.checkArgument(veranstaltungDO.getVeranstaltungLigaID()>=0,PRECONDITION_MSG_VERANSTALTUNG_LIGA_ID);
    Preconditions.checkNotNull(veranstaltungDO.getVeranstaltungSportJahr(),PRECONDITION_MSG_VERANSTALTUNG_SPORTJAHR);
    Preconditions.checkNotNull(veranstaltungDO.getVeranstaltungLigaleiterEmail(),PRECONDITION_MSG_VERANSTALTUNG_LIGALEITER_EMAIL);
    Preconditions.checkNotNull(veranstaltungDO.getVeranstaltungWettkampftypName(),PRECONDITION_MSG_VERANSTALTUNG_WETTKAMPFTYP_NAME);
    Preconditions.checkNotNull(veranstaltungDO.getVeranstaltungLigaName(),PRECONDITION_MSG_VERANSTALTUNG_LIGA_NAME);
    }

    private VeranstaltungDO notNull(VeranstaltungBE veranstaltungBE) {
        LigaBE tempLigaBE = new LigaBE();
        WettkampftypBE tempWettkampftypBE = new WettkampftypBE();
        UserBE tempUserBE = new UserBE();

        if (veranstaltungBE.getVeranstaltungLigaID() != null) {
            tempLigaBE = ligaDAO.findById(veranstaltungBE.getVeranstaltungLigaID());
        }
        if (veranstaltungBE.getVeranstaltungWettkampftypID() != null) {
            tempWettkampftypBE = wettkampftypDAO.findById(veranstaltungBE.getVeranstaltungWettkampftypID());
        }
        if(veranstaltungBE.getVeranstaltungLigaleiterID() != null) {
            tempUserBE = userDAO.findById(veranstaltungBE.getVeranstaltungLigaleiterID());
        }


        return VeranstaltungMapper.toVeranstaltungDO(veranstaltungBE, tempUserBE, tempWettkampftypBE, tempLigaBE);

    }
}

