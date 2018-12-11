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
 * @author Daniel Schott, daniel.schott@student.reutlingen-university.de
 */
public class VeranstaltungComponentImpl implements VeranstaltungComponent {

    private static final String PRECONDITION_MSG_VERANSTALTUNG_ID = "VeranstaltungDO must not be null";

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
}
