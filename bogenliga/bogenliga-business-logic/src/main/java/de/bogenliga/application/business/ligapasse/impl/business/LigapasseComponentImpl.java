package de.bogenliga.application.business.ligapasse.impl.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.ligapasse.api.LigapasseComponent;
import de.bogenliga.application.business.ligapasse.api.types.LigapasseDO;
import de.bogenliga.application.business.ligapasse.impl.dao.LigapasseDAO;

/**
 * TODO [AL] class documentation
 *
 * @author Paul Zeller, Paul_Johann.Zeller@Student.Reutlingen-University.de
 */
@Component
public class LigapasseComponentImpl implements LigapasseComponent {

    private final LigapasseDAO ligapasseDAO;

    @Autowired
    public LigapasseComponentImpl(LigapasseDAO ligapasseDAO) {
        this.ligapasseDAO = ligapasseDAO;
    }


    @Override
    public List<LigapasseDO> findAll() {
        return null;
    }


    @Override
    public LigapasseDO findById(long wettkampfId) {
        return null;
    }
}
