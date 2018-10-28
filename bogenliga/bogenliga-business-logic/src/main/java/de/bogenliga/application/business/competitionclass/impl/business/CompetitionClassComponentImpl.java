package de.bogenliga.application.business.competitionclass.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import de.bogenliga.application.business.competitionclass.api.CompetitionClassComponent;
import de.bogenliga.application.business.competitionclass.api.types.CompetitionClassDO;
import de.bogenliga.application.business.competitionclass.impl.dao.CompetitionClassDAO;
import de.bogenliga.application.business.competitionclass.impl.entity.CompetitionClassBE;
import de.bogenliga.application.business.competitionclass.impl.mapper.CompetitionClassMapper;

/**
 * Implementation of {@link CompetitionClassComponent}
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */

@Component
public class CompetitionClassComponentImpl implements CompetitionClassComponent {

    public final CompetitionClassDAO competitionClassDAO;


    /**
     * Constuctor
     * <p>
     * Dependency injection with {@link Autowired}
     *
     * @param competitionClassDAO
     */
    public CompetitionClassComponentImpl(
            CompetitionClassDAO competitionClassDAO) {
        this.competitionClassDAO = competitionClassDAO;
    }


    @Override
    public List<CompetitionClassDO> findAll() {
        final List<CompetitionClassBE> competitionClassBEList = competitionClassDAO.findAll();
        return competitionClassBEList.stream().map(CompetitionClassMapper.toCompetitionClassDO).collect(
                Collectors.toList());
    }
}
