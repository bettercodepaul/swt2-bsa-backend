package de.bogenliga.application.business.match.impl.business;

import java.util.List;
import java.util.stream.Collectors;

import de.bogenliga.application.business.mannschaft.api.MannschaftComponent;
import de.bogenliga.application.business.mannschaft.api.types.MannschaftDO;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.impl.dao.WettkampfDAO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
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

    private static final String PRECONDITION_MSG_WT0_VERANSTALTUNG = "Veranstaltungs-ID must not be Null or negative";
    private static final String PRECONDITION_MSG_WT0_MANNSCHAFT_COUNT = "The number of assigned Mannschaften to the Veranstaltung must be exactly 8";
    private static final String PRECONDITION_MSG_WT0_MANNSCHAFT = "The Mannschaft-ID must not be null or negative";


    /**
     * (Kay Scheerer) this method would make the preconditions way easier to check before each SQL
     */
    private static final String PRECONDITION_MSG_TEMPLATE_NULL = "Passe: %s must not be null";
    private static final String PRECONDITION_MSG_TEMPLATE_NEGATIVE = "Passe: %s must not be negative";

    private final MatchDAO matchDAO;
    private final MannschaftComponent mannschaftComponent;
    private final VereinComponent vereinComponent;
    private final WettkampfDAO wettkampfDAO;


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param matchDAO to access the database and return match representations
     */
    @Autowired
    public MatchComponentImpl(final MatchDAO matchDAO,
                              final MannschaftComponent mannschaftComponent,
                              final VereinComponent vereinComponent,
                              final WettkampfDAO wettkampfDAO
                              ) {

        this.matchDAO = matchDAO;
        this.mannschaftComponent = mannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.wettkampfDAO = wettkampfDAO;
    }


    public void checkPreconditions(final Long id, String iDIdentifier) {
        Preconditions.checkNotNull(id, String.format(PRECONDITION_MSG_TEMPLATE_NULL, iDIdentifier));
        Preconditions.checkArgument(id >= 0, String.format(PRECONDITION_MSG_TEMPLATE_NEGATIVE, iDIdentifier));
    }


    @Override
    public List<MatchDO> findAll() {
        final List<MatchBE> matchBEList = matchDAO.findAll();
        return matchBEList.stream().map(MatchMapper.toMatchDO).collect(Collectors.toList());
    }


    @Override
    public MatchDO findById(Long id) {
        checkPreconditions(id, PRECONDITION_MSG_MATCH_NR);

        final MatchBE matchBE = matchDAO.findById(id);

        if (matchBE == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No match found for ID '%s'", id));
        }

        return MatchMapper.toMatchDO.apply(matchBE);
    }


    @Override
    public MatchDO findByPk(Long nr, Long wettkampfId, Long mannschaftId, Long begegnung, Long scheibenNummer) {
        checkPreconditions(nr, "matchNr");
        checkPreconditions(wettkampfId, "wettkampf_Id");
        checkPreconditions(mannschaftId, "mannschaftId");
        checkPreconditions(begegnung, "begegnungId");
        checkPreconditions(scheibenNummer, "scheibenNummer");
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


    /**
     * Return a single match by combined attributes
     *
     * @param wettkampfId ID from Wettkampf
     * @param MatchNr Number of the match
     * @param scheibenNummer number of the target board
     *
     * @return singleMatchDO
     */
    @Override
    public MatchDO findByWettkampfIDMatchNrScheibenNr(Long wettkampfId, Long MatchNr, Long scheibenNummer) {
        checkPreconditions(wettkampfId, "wettkampf_Id");
        checkPreconditions(MatchNr, "matchNr");
        checkPreconditions(scheibenNummer, "scheibenNummer");

        final MatchBE matchBE = matchDAO.findByWettkampfIDMatchNrScheibenNr(wettkampfId,MatchNr,scheibenNummer);

        if (matchBE == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No match found with attributes wettkampfId: '%d', MatchNr: %d, scheibenNummer: %d",
                             wettkampfId, MatchNr, scheibenNummer)
            );
        }
        return MatchMapper.toMatchDO.apply(matchBE);
    }


    @Override
    public List<MatchDO> findByWettkampfId(Long wettkampfId) {
        checkPreconditions(wettkampfId, PRECONDITION_MSG_WETTKAMPF_ID);

        final List<MatchBE> matchBEList = matchDAO.findByWettkampfId(wettkampfId);
        return matchBEList.stream().map(MatchMapper.toMatchDO).collect(Collectors.toList());
    }



    /*    @Override
    public List<MatchBegegnungDO> findBegegnungByWettkampfId (Long wettkampfId) {
        checkPreconditions(wettkampfId, PRECONDITION_MSG_WETTKAMPF_ID);

        final List<MatchBE> matchBEList = matchDAO.findByWettkampfId(wettkampfId);


        return matchBEList.stream().map(MatchMapper.toMatchDO).collect(Collectors.toList());
    }
*/

    @Override
    public List<MatchDO> findByMannschaftId(Long mannschaftId) {
        checkPreconditions(mannschaftId, PRECONDITION_MSG_MANNSCHAFT_ID);

        final List<MatchBE> matchBEList = matchDAO.findByMannschaftId(mannschaftId);
        return matchBEList.stream().map(MatchMapper.toMatchDO).collect(Collectors.toList());
    }


    @Override
    public MatchDO create(MatchDO matchDO, Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_MSG_CURRENT_USER_ID);

        this.checkMatch(matchDO);

        MatchBE matchBE = matchDAO.create(MatchMapper.toMatchBE.apply(matchDO), currentUserId);
        return MatchMapper.toMatchDO.apply(matchBE);
    }

    @Override
    public void createInitialMatchesWT0(final Long veranstaltungsId, final Long currentUserId){
        Preconditions.checkNotNull(veranstaltungsId, PRECONDITION_MSG_WT0_VERANSTALTUNG);
        Preconditions.checkArgument(veranstaltungsId >= 0, PRECONDITION_MSG_WT0_VERANSTALTUNG);

        List<MannschaftDO> mannschaften = this.mannschaftComponent.findAllByVeranstaltungsId(veranstaltungsId);

        WettkampfBE wettkampfBE = wettkampfDAO.findWT0byVeranstaltungsId(veranstaltungsId);

        if(mannschaften == null || mannschaften.size() != 8 || wettkampfBE == null
                || wettkampfBE.getId() == null || wettkampfBE.getId() < 0){
            throw new BusinessException(ErrorCode.ENTITY_CONFLICT_ERROR, PRECONDITION_MSG_WT0_MANNSCHAFT_COUNT);
        }else{
            Long wettkampfId = wettkampfBE.getId();
            Long begegnung = 0L;
            for(int i = 0; i< 8; i++){
                if(i%2 == 0){
                    begegnung++;
                }
                this.createWT0Match(wettkampfId, begegnung, mannschaften.get(i).getId(), new Long(i) ,currentUserId);
            }
        }
    }

    private void createWT0Match(final Long wettkampfId, final Long begegnung, final Long mannschaftId, final Long scheibennummer, final Long currentUserId){
        Preconditions.checkNotNull(wettkampfId, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkArgument(wettkampfId >= 0, PRECONDITION_MSG_WETTKAMPF_ID);
        Preconditions.checkNotNull(begegnung, PRECONDITION_MSG_BEGEGNUNG);
        Preconditions.checkArgument(begegnung >= 0, PRECONDITION_MSG_BEGEGNUNG);
        Preconditions.checkNotNull(mannschaftId, PRECONDITION_MSG_WT0_MANNSCHAFT);
        Preconditions.checkArgument(mannschaftId >= 0, PRECONDITION_MSG_WT0_MANNSCHAFT);
        Preconditions.checkNotNull(scheibennummer, PRECONDITION_MSG_SCHEIBENNUMMER);
        Preconditions.checkArgument(scheibennummer >= 0, PRECONDITION_MSG_SCHEIBENNUMMER);

        MatchBE matchBe = new MatchBE();
        matchBe.setWettkampfId(wettkampfId);
        matchBe.setNr(1L);
        matchBe.setBegegnung(begegnung);
        matchBe.setMannschaftId(mannschaftId);
        matchBe.setScheibenNummer(scheibennummer);

        this.matchDAO.create(matchBe,currentUserId);
    }

    @Override
    public MatchDO update(MatchDO matchDO, Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_MSG_CURRENT_USER_ID);

        this.checkMatch(matchDO);

        MatchBE matchBE = matchDAO.update(MatchMapper.toMatchBE.apply(matchDO), currentUserId);
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
        checkPreconditions(currentUserId, PRECONDITION_MSG_CURRENT_USER_ID);

        MatchBE matchBE = MatchMapper.toMatchBE.apply(matchDO);
        matchDAO.delete(matchBE, currentUserId);
    }


    public String getMannschaftsNameByID(long mannschaftID){
        String mannschaftName;
        MannschaftDO mannschaftDO = mannschaftComponent.findById(mannschaftID);
        VereinDO vereinDO = vereinComponent.findById(mannschaftDO.getVereinId());

        if (mannschaftDO.getNummer() > 1) {
            mannschaftName = vereinDO.getName() + " " + mannschaftDO.getNummer();
        } else {
            mannschaftName = vereinDO.getName();
        }
        return mannschaftName;
    }

}
