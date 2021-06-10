package de.bogenliga.application.business.match.impl.business;

import java.util.List;
import java.util.stream.Collectors;

import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.setzliste.impl.business.SetzlisteComponentImpl;
import de.bogenliga.application.business.setzliste.impl.dao.SetzlisteDAO;
import de.bogenliga.application.business.setzliste.impl.entity.SetzlisteBE;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.impl.dao.WettkampfDAO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final String PRECONDITION_WETTKAMPFID = "wettkampfid cannot be negative";
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
    private final DsbMannschaftComponent dsbMannschaftComponent;
    private final VereinComponent vereinComponent;
    private final WettkampfDAO wettkampfDAO;
    private final SetzlisteDAO setzlisteDAO;
    private final int[][] SETZLISTE_STRUCTURE_FOR_MATCHES = {
            {5, 4, 2, 7, 1, 8, 3, 6},
            {3, 5, 8, 4, 7, 1, 6, 2},
            {4, 7, 1, 6, 2, 5, 8, 3},
            {8, 2, 7, 3, 6, 4, 1, 5},
            {7, 6, 5, 8, 3, 2, 4, 1},
            {1, 3, 4, 2, 8, 6, 5, 7},
            {2, 1, 6, 5, 4, 3, 7, 8}};
    private static final Logger LOGGER = LoggerFactory.getLogger(SetzlisteComponentImpl.class);

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
                              final WettkampfDAO wettkampfDAO,
                              final SetzlisteDAO setzlisteDAO
                              ) {

        this.matchDAO = matchDAO;
        this.dsbMannschaftComponent = dsbMannschaftComponent;
        this.vereinComponent = vereinComponent;
        this.wettkampfDAO = wettkampfDAO;
        this.setzlisteDAO = setzlisteDAO;
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

        List<DsbMannschaftDO> mannschaften = this.dsbMannschaftComponent.findAllByVeranstaltungsId(veranstaltungsId);

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
        DsbMannschaftDO dsbMannschaftDO = dsbMannschaftComponent.findById(mannschaftID);
        VereinDO vereinDO = vereinComponent.findById(dsbMannschaftDO.getVereinId());

        if (dsbMannschaftDO.getNummer() > 1) {
            mannschaftName = vereinDO.getName() + " " + dsbMannschaftDO.getNummer();
        } else {
            mannschaftName = vereinDO.getName();
        }
        return mannschaftName;
    }

    private long getTeamIDByTablePos(int tablepos, List<SetzlisteBE> setzlisteBEList) {
        for (SetzlisteBE setzlisteBE : setzlisteBEList) {
            if (setzlisteBE.getLigatabelleTabellenplatz() == tablepos) {
                return setzlisteBE.getMannschaftid();
            }
        }
        return -1;
    }

    // generate matches by button on click "generiere Matches"
    public List<MatchDO> generateMatches(long wettkampfid) {
        LOGGER.info("sind am Endpunkt generateMatches");
        Preconditions.checkArgument(wettkampfid >= 0, PRECONDITION_WETTKAMPFID);
        List<MatchDO> matchDOList = this.findByWettkampfId(wettkampfid);
        List<SetzlisteBE> setzlisteBEList = setzlisteDAO.getTableByWettkampfID(wettkampfid);
        if (!setzlisteBEList.isEmpty()){
            if (matchDOList.isEmpty()){
                //itarate thorugh matches
                for (int i = 0; i < SETZLISTE_STRUCTURE_FOR_MATCHES.length; i++){
                    //iterate through target boards
                    for (int j = 0; j < SETZLISTE_STRUCTURE_FOR_MATCHES[i].length; j++) {
                        long begegnung = Math.round((float) (j + 1) / 2);
                        long currentTeamID = getTeamIDByTablePos(SETZLISTE_STRUCTURE_FOR_MATCHES[i][j], setzlisteBEList);
                        MatchDO newMatchDO = new MatchDO(null, (long) i + 1, wettkampfid, currentTeamID, begegnung, (long) j + 1, null, null,null,null,null,null,null);
                        matchDOList.add(this.create(newMatchDO, (long) 0));
                    }
                }
            }
            else{
                LOGGER.debug("Matches existieren bereits");
            }
        }
        else{
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR, "Der Wettkampf mit der ID " + wettkampfid +" oder die Tabelleneinträge vom vorherigen Wettkampftag existieren noch nicht");
        }
        return matchDOList;
    }
}
