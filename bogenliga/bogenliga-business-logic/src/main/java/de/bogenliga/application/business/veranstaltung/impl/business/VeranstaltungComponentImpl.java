package de.bogenliga.application.business.veranstaltung.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.dao.VeranstaltungDAO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.business.veranstaltung.impl.mapper.VeranstaltungMapper;
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
    private static final String PRECONDITION_MSG_VERANSTALTUNG_SPORTJAHR="veranstaltungsportjahr must be not null";
    private static final String PRECONDITION_MSG_VERANSTALTUNG_NAME="veranstaltungname must be not null";
    private static final String PRECONDITION_MSG_CURRENT_DSBMITGLIED = "Current dsbmitglied id must not be negative";
    private final VeranstaltungDAO veranstaltungDAO;

    /**
     * Constructor for VeranstaltungComponentImpl - Autowired by springboot
     * @param veranstaltungDAO
     */

    @Autowired
    public VeranstaltungComponentImpl(final VeranstaltungDAO veranstaltungDAO) {
        this.veranstaltungDAO= veranstaltungDAO;
        System.out.println("created DAO object");
    }

    /**
     * findAll-Method gives all Veranstaltungen from the dataBase
     * @return
     */
    @Override
    public List<VeranstaltungDO> findAll() {
        final List<VeranstaltungBE> veranstaltugBEList = veranstaltungDAO.findAll();
        return veranstaltugBEList.stream().map(VeranstaltungMapper.toVeranstaltungDO).collect(Collectors.toList());
    }



    @Override
    public VeranstaltungDO findById(final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);

        final VeranstaltungBE result = veranstaltungDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return VeranstaltungMapper.toVeranstaltungDO.apply(result);
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
    Preconditions.checkNotNull(veranstaltungDO.getVeranstaltungSportJahr(),PRECONDITION_MSG_VERANSTALTUNG_SPORTJAHR);
    }
}

