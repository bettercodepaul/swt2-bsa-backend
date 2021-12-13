package de.bogenliga.application.business.ligamatch.impl.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.ligamatch.api.LigamatchComponent;
import de.bogenliga.application.business.ligamatch.api.types.LigamatchDO;
import de.bogenliga.application.business.ligamatch.impl.dao.LigamatchDAO;
import de.bogenliga.application.business.match.impl.dao.MatchDAO;

/**
 * TODO [AL] class documentation
 *
 * @author Paul Zeller, Paul_Johann.Zeller@Student.Reutlingen-University.de
 */
@Component
public class LigamatchComponentImpl implements LigamatchComponent {

    private final MatchDAO matchDAO;

    @Autowired
    public LigamatchComponentImpl(MatchDAO matchDAO) {
        this.matchDAO = matchDAO;
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
