package de.bogenliga.application.business.match.impl.business;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.ligamatch.impl.dao.LigamatchDAO;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.match.api.MatchComponent;
import de.bogenliga.application.business.match.api.types.LigamatchDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.dao.MatchDAO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.business.ligamatch.impl.mapper.LigamatchMapper;
import de.bogenliga.application.business.match.impl.mapper.MatchMapper;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
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
    private static final String PRECONDITION_MSG_WT0_MANNSCHAFT_COUNT = "The number of assigned Mannschaften to the Veranstaltung must be 8, 6, 4";
    private static final String PRECONDITION_MSG_WT0_MANNSCHAFT = "The Mannschaft-ID must not be null or negative";


    /**
     * (Kay Scheerer) this method would make the preconditions way easier to check before each SQL
     */
    private static final String PRECONDITION_MSG_TEMPLATE_NULL = "Passe: %s must not be null";
    private static final String PRECONDITION_MSG_TEMPLATE_NEGATIVE = "Passe: %s must not be negative";

    private final MatchDAO matchDAO;
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;
    private WettkampfComponent wettkampfComponent;
    private final LigamatchDAO ligamatchDAO;
    private final long platzhalterId = 99;


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param matchDAO to access the database and return match representations
     */
    @Autowired
    public MatchComponentImpl(final MatchDAO matchDAO,
                              final DsbMannschaftComponent dsbMannschaftComponent,
                              final VereinComponent vereinComponent,
                              final LigamatchDAO ligamatchDAO) {
        this.matchDAO = matchDAO;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.ligamatchDAO = ligamatchDAO;
    }

    @Autowired
    public void setWettkampfComponent(final WettkampfComponent wettkampfComponent){
        this.wettkampfComponent = wettkampfComponent;
    }


    public void checkPreconditions(final Long id, String iDIdentifier) {
        Preconditions.checkNotNull(id, String.format(PRECONDITION_MSG_TEMPLATE_NULL, iDIdentifier));
        Preconditions.checkArgument(id >= 0, String.format(PRECONDITION_MSG_TEMPLATE_NEGATIVE, iDIdentifier));
    }


    @Override
    public List<MatchDO> findAll() {
        final List<MatchBE> matchBEList = matchDAO.findAll();
        return matchBEList.stream().map(MatchMapper.toMatchDO).toList();
    }


    /**
     * checks if the match is in the Ligamatch-View
     */
    @Override
    public Boolean checkIfLigamatch(Long id){
        return ligamatchDAO.checkIfLigamatch(id);
    }


    /**
     * optimized function for Schusszettel
     */
    @Override
    public List<LigamatchBE> getLigamatchesByWettkampfId(Long wettkampfId) {
        checkPreconditions(wettkampfId, PRECONDITION_MSG_WETTKAMPF_ID);
        return ligamatchDAO.findLigamatchesByWettkampfId(wettkampfId);
    }

    /**
     * optimized function for SynService
     */
    @Override
    public List<LigamatchDO> getLigamatchDOsByWettkampfId(Long wettkampfId) {
        checkPreconditions(wettkampfId, PRECONDITION_MSG_WETTKAMPF_ID);
        final List<LigamatchBE> ligaMatches = ligamatchDAO.findLigamatchesByWettkampfId(wettkampfId);
        return ligaMatches.stream().map(LigamatchMapper.toLigamatchDO).toList();
    }

    /**
     * optimized function for Schusszettel
     */
    @Override
    public LigamatchBE getLigamatchById(Long id) {
        checkPreconditions(id, PRECONDITION_MSG_MATCH_NR);

        final LigamatchBE ligamatchBE = ligamatchDAO.findById(id);

        if (ligamatchBE == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No match found for ID '%s'", id));
        }
        return ligamatchBE;
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
     * @param matchNr Number of the match
     * @param scheibenNummer number of the target board
     *
     * @return singleMatchDO
     */
    @Override
    public MatchDO findByWettkampfIDMatchNrScheibenNr(Long wettkampfId, Long matchNr, Long scheibenNummer) {
        checkPreconditions(wettkampfId, "wettkampf_Id");
        checkPreconditions(matchNr, "matchNr");
        checkPreconditions(scheibenNummer, "scheibenNummer");

        final MatchBE matchBE = matchDAO.findByWettkampfIDMatchNrScheibenNr(wettkampfId,matchNr,scheibenNummer);

        if (matchBE == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No match found with attributes wettkampfId: '%d', matchNr: %d, scheibenNummer: %d",
                             wettkampfId, matchNr, scheibenNummer)
            );
        }
        return MatchMapper.toMatchDO.apply(matchBE);
    }


    @Override
    public List<MatchDO> findByWettkampfId(Long wettkampfId) {
        checkPreconditions(wettkampfId, PRECONDITION_MSG_WETTKAMPF_ID);

        final List<MatchBE> matchBEList = matchDAO.findByWettkampfId(wettkampfId);
        return matchBEList.stream().map(MatchMapper.toMatchDO).toList();
    }

    @Override
    public List<MatchDO> findByMannschaftId(Long mannschaftId) {
        checkPreconditions(mannschaftId, PRECONDITION_MSG_MANNSCHAFT_ID);

        final List<MatchBE> matchBEList = matchDAO.findByMannschaftId(mannschaftId);
        return matchBEList.stream().map(MatchMapper.toMatchDO).toList();
    }


    @Override
    public List<MatchDO> findByVeranstaltungId(Long veranstaltungId) {
        checkPreconditions(veranstaltungId, PRECONDITION_MSG_WT0_VERANSTALTUNG);

        final List<MatchBE> matchBEList = matchDAO.findByVeranstaltungId(veranstaltungId);
        return  matchBEList.stream().map(MatchMapper.toMatchDO).toList();
    }


    @Override
    public MatchDO create(MatchDO matchDO, Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_MSG_CURRENT_USER_ID);

        this.checkMatch(matchDO);

        try {
            // Check if the Mannschaft of this match is a Platzhalter
            DsbMannschaftDO checkForVereinsID = dsbMannschaftComponent.findById(matchDO.getMannschaftId());
            if (checkForVereinsID.getVereinId() == platzhalterId) {
                matchDO.setMatchpunkte(0L);
                matchDO.setSatzpunkte(0L);
            }
        }catch (NullPointerException ignored) {}

        MatchBE matchBE = matchDAO.create(MatchMapper.toMatchBE.apply(matchDO), currentUserId);
        return MatchMapper.toMatchDO.apply(matchBE);
    }

    @Override
    public void createInitialMatchesWT0(final Long veranstaltungsId, final Long currentUserId){
        Preconditions.checkNotNull(veranstaltungsId, PRECONDITION_MSG_WT0_VERANSTALTUNG);
        Preconditions.checkArgument(veranstaltungsId >= 0, PRECONDITION_MSG_WT0_VERANSTALTUNG);

        List<DsbMannschaftDO> mannschaften = this.dsbMannschaftComponent.findAllByVeranstaltungsId(veranstaltungsId);

        WettkampfDO wettkampfDO = wettkampfComponent.findWT0byVeranstaltungsId(veranstaltungsId);

        List<Integer> validMannschaftSizes = Arrays.asList(8, 6, 4);

        if(mannschaften == null || !validMannschaftSizes.contains(mannschaften.size()) || wettkampfDO == null
                || wettkampfDO.getId() == null || wettkampfDO.getId() < 0){
            throw new BusinessException(ErrorCode.ENTITY_CONFLICT_ERROR, PRECONDITION_MSG_WT0_MANNSCHAFT_COUNT);
        }else{
            Long wettkampfId = wettkampfDO.getId();
            Long begegnung = 0L;
            for(int i = 0; i < mannschaften.size(); i++){
                if(i%2 == 0){
                    begegnung++;
                }
                this.createWT0Match(wettkampfId, begegnung, mannschaften.get(i).getId(), (long) i,currentUserId);
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
        matchBe.setMatchScheibennummer(scheibennummer);

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

        Preconditions.checkNotNull(matchDO.getMatchScheibennummer(), PRECONDITION_MSG_CURRENT_USER_ID);
        Preconditions.checkArgument(matchDO.getMatchScheibennummer() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);
    }


    @Override
    public void delete(MatchDO matchDO, Long currentUserId) {
        checkPreconditions(currentUserId, PRECONDITION_MSG_CURRENT_USER_ID);

        MatchBE matchBE = MatchMapper.toMatchBE.apply(matchDO);
        matchDAO.delete(matchBE, currentUserId);
    }


    public String getMannschaftsNameByID(long mannschaftID){
        String mannschaftName;
        DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(mannschaftID);
        VereinDO vereinDO = vereinComponent.findById(dsbMannschaftDO.getVereinId());

        if (dsbMannschaftDO.getNummer() > 1) {
            mannschaftName = vereinDO.getName() + " " + dsbMannschaftDO.getNummer();
        } else {
            mannschaftName = vereinDO.getName();
        }
        return mannschaftName;
    }

}
