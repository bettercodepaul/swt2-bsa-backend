package de.bogenliga.application.business.veranstaltung.impl.business;

import java.util.List;
import java.util.stream.Collectors;

import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.veranstaltung.impl.mapper.VeranstaltungMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.business.veranstaltung.impl.dao.VeranstaltungDAO;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class VeranstaltungComponentImpl implements VeranstaltungComponent {

    private static final String PRECONDITION_MSG_DSBMITGLIED = "DsbMitgliedDO must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_ID = "DsbMitgliedDO ID must not be negative";
    private static final String PRECONDITION_MSG_DSBMITGLIED_VORNAME = "DsbMitglied vorname must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_NACHNAME = "DsbMitglied nachname must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_GEBURTSDATUM = "DsbMitglied geburtsdatum must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_NATIONALITAET = "DsbMitglied nationalitaet must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_MITGLIEDSNUMMER = "DsbMitglied mitgliedsnummer must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID = "DsbMitglied vereins id must not be null";
    private static final String PRECONDITION_MSG_DSBMITGLIED_VEREIN_ID_NEGATIVE = "DsbMitglied vereins id must not be negative";
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
        final List<DsbMitgliedBE> veranstaltugBEList = veranstaltungDAO.findAll();
        return veranstaltugBEList.stream().map(DsbMitgliedMapper.toDsbMitgliedDO).collect(Collectors.toList());
    }



    @Override
    public VeranstaltungDO findById(final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_VERANSTALTUNG_ID);

        final VeranstaltungBE result = veranstaltungDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }

        return DsbMitgliedMapper.toDsbMitgliedDO.apply(result);
    }
}
