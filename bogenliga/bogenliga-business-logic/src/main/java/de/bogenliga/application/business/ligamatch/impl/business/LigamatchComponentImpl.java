package de.bogenliga.application.business.ligamatch.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.ligamatch.api.LigamatchComponent;
import de.bogenliga.application.business.ligamatch.api.types.LigamatchDO;
import de.bogenliga.application.business.ligamatch.impl.dao.LigamatchDAO;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.ligamatch.impl.mapper.LigamatchMapper;


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
    public List<LigamatchDO> findById(long wettkampfId) {
        final List<LigamatchBE> LigamatchBEList = ligamatchDAO.getLigamatchWettkampf(wettkampfId);
        return LigamatchBEList.stream().map(LigamatchMapper.toMatchDO).collect(Collectors.toList());
    }


}
