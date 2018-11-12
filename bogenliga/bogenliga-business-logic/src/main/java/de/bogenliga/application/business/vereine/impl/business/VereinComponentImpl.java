package de.bogenliga.application.business.vereine.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.vereine.impl.dao.VereinDAO;
import de.bogenliga.application.business.vereine.impl.entity.VereinBE;
import de.bogenliga.application.business.vereine.impl.mapper.VereinMapper;

/**
 * Implementation of {@link VereinComponent}
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
@Component
public class VereinComponentImpl implements VereinComponent {

    private final VereinDAO vereinDAO;


    @Autowired
    public VereinComponentImpl(VereinDAO vereinDAO) {
        this.vereinDAO = vereinDAO;
    }


    @Override
    public List<VereinDO> findAll() {
        final List<VereinBE> vereinBEList = vereinDAO.findAll();
        return vereinBEList.stream().map(VereinMapper.toVereinDO).collect(Collectors.toList());
    }
}
