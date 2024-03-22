package de.bogenliga.application.business.competitionclass.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.competitionclass.api.CompetitionClassComponent;
import de.bogenliga.application.business.competitionclass.api.types.CompetitionClassDO;
import de.bogenliga.application.business.competitionclass.impl.dao.CompetitionClassDAO;
import de.bogenliga.application.business.competitionclass.impl.entity.CompetitionClassBE;
import de.bogenliga.application.business.competitionclass.impl.mapper.CompetitionClassMapper;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link CompetitionClassComponent}
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */

@Component
public class CompetitionClassComponentImpl implements CompetitionClassComponent {

    private static final String PRECONDITION_MSG_KLASSE = "CompetitionClass must not be null";
    private static final String PRECONDITION_MSG_KLASSE_ID = "CompetitionClass ID must not be negative";
    private static final String PRECONDITION_MSG_KLASSE_JAHRGANG_MIN = "Minimum Age must not be negative";
    private static final String PRECONDITION_MSG_KLASSE_JAHRGANG_MAX = "Max Age must be higher than Min Age and must not be negative";
    private static final String PRECONDITION_MSG_KLASSE_NR = "Something is wrong with the CompetitionClass Number";
    private static final String PRECONDITION_MSG_NAME = "The CompetitionClass must be given a name";
    private static final String PRECONDITION_MSG_CURRENT_DSB_ID = "The currentDsbId cannot be negative";


    private final CompetitionClassDAO competitionClassDAO;


    /**
     * Constuctor
     * <p>
     * Dependency injection with {@link Autowired}
     *
     * @param competitionClassDAO DAO Object of Competition Class
     */
    @Autowired
    public CompetitionClassComponentImpl(
            final CompetitionClassDAO competitionClassDAO) {
        this.competitionClassDAO = competitionClassDAO;
    }


    @Override
    public List<CompetitionClassDO> findAll() {
        final List<CompetitionClassBE> competitionClassBEList = competitionClassDAO.findAll();

        return competitionClassBEList.stream().map(CompetitionClassMapper.toCompetitionClassDO).toList();
    }

    @Override
    public List<CompetitionClassDO> findBySearch(final String searchTerm) {
        final List<CompetitionClassBE> competitionClassBEList = competitionClassDAO.findBySearch(searchTerm);

        return competitionClassBEList
                .stream()
                    .map(CompetitionClassMapper.toCompetitionClassDO)
                        .toList();
    }

    @Override
    public CompetitionClassDO findById(final long id) {
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_KLASSE_ID);

        final CompetitionClassBE competitionClassBE = competitionClassDAO.findById(id);

        return CompetitionClassMapper.toCompetitionClassDO.apply(competitionClassBE);
    }

    @Override
    public CompetitionClassDO create(final CompetitionClassDO competitionClassDO, final long currentDsbMitglied) {

        checkCompetitionClassDO(competitionClassDO, currentDsbMitglied);

        final CompetitionClassBE competitionClassBE = CompetitionClassMapper.toCompetitionClassBE.apply(competitionClassDO);

        final CompetitionClassBE persistedCompetitionClassBE = competitionClassDAO.create(competitionClassBE, currentDsbMitglied);

        return CompetitionClassMapper.toCompetitionClassDO.apply(persistedCompetitionClassBE);
    }



    @Override
    public CompetitionClassDO update(final CompetitionClassDO competitionClassDO, final long currentDsbMitglied) {
        checkCompetitionClassDO(competitionClassDO, currentDsbMitglied);
        Preconditions.checkArgument(competitionClassDO.getId() >= 0, PRECONDITION_MSG_KLASSE_ID);

        final CompetitionClassBE competitionClassBE = CompetitionClassMapper.toCompetitionClassBE.apply(competitionClassDO);

        final CompetitionClassBE persistedCompetitionClassBE = competitionClassDAO.update(competitionClassBE,currentDsbMitglied);

        return CompetitionClassMapper.toCompetitionClassDO.apply(persistedCompetitionClassBE);
    }

    private void checkCompetitionClassDO(final CompetitionClassDO competitionClassDO, final long currentDsbMitglied){
        Preconditions.checkNotNull(competitionClassDO, PRECONDITION_MSG_KLASSE);
        Preconditions.checkNotNull(currentDsbMitglied >= 0, PRECONDITION_MSG_CURRENT_DSB_ID);
        Preconditions.checkNotNull(competitionClassDO.getKlasseJahrgangMin(), PRECONDITION_MSG_KLASSE_JAHRGANG_MIN);
        Preconditions.checkNotNull(competitionClassDO.getKlasseJahrgangMax(), PRECONDITION_MSG_KLASSE_JAHRGANG_MAX);
        Preconditions.checkNotNull(competitionClassDO.getKlasseNr(), PRECONDITION_MSG_KLASSE_NR);
        Preconditions.checkNotNull(competitionClassDO.getKlasseName(), PRECONDITION_MSG_NAME);
        Preconditions.checkArgument(competitionClassDO.getKlasseJahrgangMin() >= competitionClassDO.getKlasseJahrgangMax(),
                PRECONDITION_MSG_KLASSE_JAHRGANG_MIN);
    }
}
