package de.bogenliga.application.business.match.impl.business;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.dao.MatchDAO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.business.match.impl.mapper.MatchMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
@Component
public class MatchComponentImpl implements MatchComponent {

    private static final String PRECONDITION_MSG_TEMPLATE = "Match: %s must not be null and must not be negative";
    public static final String PRECONDITION_MSG_MATCH_ID = String.format(PRECONDITION_MSG_TEMPLATE, "id");
    public static final String PRECONDITION_MSG_MATCH_DO = String.format(PRECONDITION_MSG_TEMPLATE, "DO");
    public static final String PRECONDITION_MSG_MATCH_NR = String.format(PRECONDITION_MSG_TEMPLATE, "nr");
    public static final String PRECONDITION_MSG_WETTKAMPF_ID = String.format(PRECONDITION_MSG_TEMPLATE, "wettkampfId");
    public static final String PRECONDITION_MSG_CURRENT_USER_ID = String.format(PRECONDITION_MSG_TEMPLATE,
            "currentUserId");
    public static final String PRECONDITION_MSG_MANNSCHAFT_ID = String.format(PRECONDITION_MSG_TEMPLATE,
            "mannschaftId");
    public static final String PRECONDITION_MSG_BEGEGNUNG = String.format(PRECONDITION_MSG_TEMPLATE, "begegnung");
    public static final String PRECONDITION_MSG_SCHEIBENNUMMER = String.format(PRECONDITION_MSG_TEMPLATE,
            "scheibennummer");

    private final MatchDAO matchDAO;


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param matchDAO to access the database and return match representations
     */
    @Autowired
    public MatchComponentImpl(final MatchDAO matchDAO) {
        this.matchDAO = matchDAO;
    }


    @Override
    public List<MatchDO> findAll() {
        final List<MatchBE> matchBEList = matchDAO.findAll();
        return matchBEList.stream().map(MatchMapper.toMatchDO).collect(Collectors.toList());
    }


    @Override
    public MatchDO findById(Long id) {
        Preconditions.checkNotNull(id, PRECONDITION_MSG_MATCH_NR);
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_MATCH_NR);

        final MatchBE matchBE = matchDAO.findById(id);

        if (matchBE == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No match found for ID '%s'", id));
        }

        return MatchMapper.toMatchDO.apply(matchBE);
    }


    @Override
    public MatchDO findByPk(Long nr, Long wettkampfId, Long mannschaftId, Long begegnung, Long scheibenNummer) {
        final MatchBE matchBE = matchDAO.findByPk(nr, wettkampfId, mannschaftId, begegnung, scheibenNummer);

        if (matchBE == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No match found with attributes nr: '%d', wettkampfId: %d, mannschaftId: %d, " +
                                    "begegnung: %d, scheibenNummer: %d",
                            nr, wettkampfId, mannschaftId, begegnung, scheibenNummer)
            );
        }

        return MatchMapper.toMatchDO.apply(matchBE);
    }


    @Override
    public List<MatchDO> findByWettkampfId(Long wettkampfId) {
        Preconditions.checkNotNull(wettkampfId, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_WETTKAMPF_ID);

        final List<MatchBE> matchBEList = matchDAO.findByWettkampfId(wettkampfId);
        return matchBEList.stream().map(MatchMapper.toMatchDO).collect(Collectors.toList());
    }


    @Override
    public List<MatchDO> findByMannschaftId(Long mannschaftId) {
        Preconditions.checkNotNull(mannschaftId, PRECONDITION_MSG_MANNSCHAFT_ID);
        Preconditions.checkArgument(mannschaftId >= 0, PRECONDITION_MSG_MANNSCHAFT_ID);

        final List<MatchBE> matchBEList = matchDAO.findByMannschaftId(mannschaftId);
        return matchBEList.stream().map(MatchMapper.toMatchDO).collect(Collectors.toList());
    }


    @Override
    public MatchDO create(MatchDO matchDO, Long currentUserId) {
        Preconditions.checkNotNull(currentUserId, PRECONDITION_MSG_CURRENT_USER_ID);
        Preconditions.checkArgument(currentUserId >= 0, PRECONDITION_MSG_CURRENT_USER_ID);

        this.checkMatch(matchDO);

        MatchBE matchBE = MatchMapper.toMatchBE.apply(matchDO);
        matchBE = matchDAO.create(matchBE, currentUserId);
        return MatchMapper.toMatchDO.apply(matchBE);
    }


    @Override
    public MatchDO update(MatchDO matchDO, Long currentUserId) {
        Preconditions.checkNotNull(currentUserId, PRECONDITION_MSG_CURRENT_USER_ID);
        Preconditions.checkArgument(currentUserId >= 0, PRECONDITION_MSG_CURRENT_USER_ID);

        this.checkMatch(matchDO);

        MatchBE matchBE = matchDAO.findById(matchDO.getNr());

        matchBE.setSatzpunkte(matchDO.getSatzpunkte());
        matchBE.setMatchpunkte(matchDO.getMatchpunkte());
        matchBE.setScheibenNummer(matchDO.getScheibenNummer());
        matchBE.setWettkampfId(matchDO.getWettkampfId());
        matchBE.setMannschaftId(matchDO.getMannschaftId());

        matchBE = matchDAO.update(matchBE, currentUserId);
        return MatchMapper.toMatchDO.apply(matchBE);
    }


    private void checkMatch(MatchDO matchDO) {
        Preconditions.checkNotNull(matchDO, PRECONDITION_MSG_MATCH_DO);

        Preconditions.checkNotNull(matchDO.getMannschaftId(), PRECONDITION_MSG_CURRENT_USER_ID);
        Preconditions.checkArgument(matchDO.getMannschaftId() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);

        Preconditions.checkNotNull(matchDO.getWettkampfId(), PRECONDITION_MSG_CURRENT_USER_ID);
        Preconditions.checkArgument(matchDO.getWettkampfId() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);

        Preconditions.checkNotNull(matchDO.getScheibenNummer(), PRECONDITION_MSG_CURRENT_USER_ID);
        Preconditions.checkArgument(matchDO.getScheibenNummer() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);
    }


    @Override
    public void delete(MatchDO matchDO, Long currentUserId) {
        Preconditions.checkNotNull(currentUserId, PRECONDITION_MSG_CURRENT_USER_ID);
        Preconditions.checkArgument(currentUserId >= 0, PRECONDITION_MSG_CURRENT_USER_ID);

        MatchBE matchBE = MatchMapper.toMatchBE.apply(matchDO);
        matchDAO.delete(matchBE, currentUserId);
    }
}
