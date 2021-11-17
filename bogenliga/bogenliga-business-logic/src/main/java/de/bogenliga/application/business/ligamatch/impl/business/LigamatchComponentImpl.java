package de.bogenliga.application.business.ligamatch.impl.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.ligamatch.api.LigamatchComponent;
import de.bogenliga.application.business.ligamatch.api.types.LigamatchDO;
import de.bogenliga.application.business.ligamatch.impl.dao.LigamatchDAO;

/**
 * TODO [AL] class documentation
 *
 * @author Paul Zeller, Paul_Johann.Zeller@Student.Reutlingen-University.de
 */
@Component
public class LigamatchComponentImpl implements LigamatchComponent {

    private final LigamatchDAO ligamatchDAO;

    @Autowired
    public LigamatchComponentImpl(LigamatchDAO ligamatchDAO) {
        this.ligamatchDAO = ligamatchDAO;
    }


    @Override
    public List<LigamatchDO> findAll() {
        return null;
    }


    @Override
    public LigamatchDO findById(long wettkampfId) {
        return null;
    }


}
