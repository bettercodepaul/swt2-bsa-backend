package de.bogenliga.application.business.match.impl.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.ligamatch.impl.dao.BaseLigamatchTest;
import de.bogenliga.application.business.ligamatch.impl.dao.LigamatchDAO;
import de.bogenliga.application.business.ligamatch.impl.entity.LigamatchBE;
import de.bogenliga.application.business.match.api.types.LigamatchDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.BaseMatchTest;
import de.bogenliga.application.business.match.impl.dao.MatchDAO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.business.match.impl.mapper.MatchMapper;
import de.bogenliga.application.business.vereine.api.VereinComponent;
import de.bogenliga.application.business.vereine.api.types.VereinDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * @author Dominik Halle, HSRT MKI SS19 - SWT2
 */
public class MatchComponentImplTest extends BaseMatchTest {

    @Mock
    private MatchDAO matchDAO;
    @Mock
    private LigamatchDAO ligamatchDAO;
    @Mock
    private DsbMannschaftComponent mannschaftComponent;
    @Mock
    private WettkampfComponent wettkampfComponent;

    @InjectMocks
    private MatchComponentImpl underTest;

    @Mock
    private VereinComponent vereinComponent;

    private void validateObjectList (List<MatchDO> actual) {
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();
    }


    private void assertValid(MatchBE expectedMatchBE, MatchDO actual) {
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expectedMatchBE.getId()).isEqualTo(MATCH_ID);
        assertThat(actual.getBegegnung()).isEqualTo(expectedMatchBE.getBegegnung()).isEqualTo(MATCH_BEGEGNUNG);
        assertThat(actual.getMannschaftId()).isEqualTo(expectedMatchBE.getMannschaftId()).isEqualTo(
                MATCH_MANNSCHAFT_ID);
        assertThat(actual.getWettkampfId()).isEqualTo(expectedMatchBE.getWettkampfId()).isEqualTo(MATCH_WETTKAMPF_ID);
        assertThat(actual.getMatchpunkte()).isEqualTo(expectedMatchBE.getMatchpunkte()).isEqualTo(MATCH_MATCHPUNKTE);
        assertThat(actual.getSatzpunkte()).isEqualTo(expectedMatchBE.getSatzpunkte()).isEqualTo(MATCH_SATZPUNKTE);
        assertThat(actual.getStrafPunkteSatz1()).isEqualTo(expectedMatchBE.getStrafPunkteSatz1()).isEqualTo(
                MATCH_STRAFPUNKT_SATZ_1);
        assertThat(actual.getStrafPunkteSatz2()).isEqualTo(expectedMatchBE.getStrafPunkteSatz2()).isEqualTo(
                MATCH_STRAFPUNKT_SATZ_2);
        assertThat(actual.getStrafPunkteSatz3()).isEqualTo(expectedMatchBE.getStrafPunkteSatz3()).isEqualTo(
                MATCH_STRAFPUNKT_SATZ_3);
        assertThat(actual.getStrafPunkteSatz4()).isEqualTo(expectedMatchBE.getStrafPunkteSatz4()).isEqualTo(
                MATCH_STRAFPUNKT_SATZ_4);
        assertThat(actual.getStrafPunkteSatz5()).isEqualTo(expectedMatchBE.getStrafPunkteSatz5()).isEqualTo(
                MATCH_STRAFPUNKT_SATZ_5);

        assertThat(actual.getNr()).isEqualTo(expectedMatchBE.getNr()).isEqualTo(MATCH_NR);
    }

    public static DsbMannschaftDO getDsbMannschaftDO() {
        return new DsbMannschaftDO(
                1L,
                "MA_NAME",
                5L,
                99L,
                0L,
                444L,
                8L,
                2024L);

    }

    public static VereinDO getVereinDO() {
        return new VereinDO(1L, "name", "final String dsbIdentifier", 1L,
        null, null, null, null, 0L, 1L);

    }


    @Test
    public void findAll() {
        MatchBE expectedMatchBE = getMatchBE();

        final List<MatchBE> expectedBEList = Collections.singletonList(expectedMatchBE);

        // configure mocks
        when(matchDAO.findAll()).thenReturn(expectedBEList);

        // call test method
        final List<MatchDO> actual = underTest.findAll();

        // assert result
        validateObjectList(actual);
    }


    @Test
    public void findById() {
        MatchBE expectedMatchBE = getMatchBE();

        // configure mocks to return the expectedMatchBE when findById is called
        when(matchDAO.findById(anyLong())).thenReturn(expectedMatchBE);

        final MatchDO actual = underTest.findById(MATCH_ID);

        // assert result
        assertValid(expectedMatchBE, actual);

        verify(matchDAO).findById(MATCH_ID);
    }


    @Test
    public void findById_Null_Return() {
        when(matchDAO.findById(anyLong())).thenReturn(null);

        assertThatThrownBy(() -> {
            underTest.findById(MATCH_ID);
        }).isInstanceOf(BusinessException.class);
    }


    @Test
    public void findById_Null_Param() {
        MatchBE expectedMatchBE = getMatchBE();
        when(matchDAO.findById(any())).thenReturn(expectedMatchBE);

        assertThatThrownBy(() -> {
            underTest.findById(null);
        }).isInstanceOf(BusinessException.class);
    }


    @Test
    public void findByPk() {
        MatchBE expectedMatchBE = getMatchBE();

        // configure mocks to return the expectedMatchBE when findById is called
        when(matchDAO.findByPk(
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong()
        )).thenReturn(expectedMatchBE);

        final MatchDO actual = underTest.findByPk(
                MATCH_NR, MATCH_WETTKAMPF_ID,
                MATCH_MANNSCHAFT_ID, MATCH_BEGEGNUNG,
                MATCH_SCHEIBENNUMMER
        );

        // assert result
        assertValid(expectedMatchBE, actual);

        verify(matchDAO).findByPk(
                MATCH_NR, MATCH_WETTKAMPF_ID,
                MATCH_MANNSCHAFT_ID, MATCH_BEGEGNUNG,
                MATCH_SCHEIBENNUMMER
        );
    }


    @Test
    public void findByPk_Null_Return() {
        when(matchDAO.findByPk(
                anyLong(), anyLong(), anyLong(), anyLong(), anyLong()
        )).thenReturn(null);

        assertThatThrownBy(() -> {
            underTest.findByPk(
                    MATCH_NR, MATCH_WETTKAMPF_ID,
                    MATCH_MANNSCHAFT_ID, MATCH_BEGEGNUNG,
                    MATCH_SCHEIBENNUMMER
            );
        }).isInstanceOf(BusinessException.class);
    }


    @Test
    public void findByWettkampfIDMatchNrScheibenNr() {
        MatchBE expectedMatchBE = getMatchBE();

        // configure mocks
        when(matchDAO.findByWettkampfIDMatchNrScheibenNr(anyLong(), anyLong(), anyLong())).thenReturn(expectedMatchBE);

        // call test method
        final MatchDO actual = underTest.findByWettkampfIDMatchNrScheibenNr(1L,1L,1L);

        // assert result
        assertValid(expectedMatchBE, actual);
        verify(matchDAO).findByWettkampfIDMatchNrScheibenNr(1L,1L,1L);
    }

    @Test
    public void findByWettkampfIDMatchNrScheibenNr_ThrowsException() {
        MatchBE expectedMatchBE = null;

        // configure mocks
        when(matchDAO.findByWettkampfIDMatchNrScheibenNr(anyLong(), anyLong(), anyLong())).thenReturn(expectedMatchBE);

        // call test method
        assertThatThrownBy(()->{
            underTest.findByWettkampfIDMatchNrScheibenNr(1L,1L,1L);
        })
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("No match");

        // assert result
        verify(matchDAO).findByWettkampfIDMatchNrScheibenNr(1L,1L,1L);
    }


    @Test
    public void findByWettkampfId() {
        MatchBE expectedMatchBE = getMatchBE();

        final List<MatchBE> expectedBEList = Collections.singletonList(expectedMatchBE);

        // configure mocks
        when(matchDAO.findByWettkampfId(anyLong())).thenReturn(expectedBEList);

        // call test method
        final List<MatchDO> actual = underTest.findByWettkampfId(MATCH_WETTKAMPF_ID);

        // assert result
        validateObjectList(actual);
        verify(matchDAO).findByWettkampfId(MATCH_WETTKAMPF_ID);
    }


    @Test
    public void getLigamatchesByWettkampfId(){
        LigamatchBE expectedLigamatchBE = BaseLigamatchTest.getLigamatchBE();

        final List<LigamatchBE> expectedBEList = Collections.singletonList(expectedLigamatchBE);

        //configure mocks
        when(ligamatchDAO.findLigamatchesByWettkampfId(anyLong())).thenReturn(expectedBEList);

        //call test method
        final List<LigamatchBE> actual = underTest.getLigamatchesByWettkampfId(MATCH_WETTKAMPF_ID);

        //assert result
        BaseLigamatchTest.validateObjectList(actual);
        verify(ligamatchDAO).findLigamatchesByWettkampfId(MATCH_WETTKAMPF_ID);
    }

    @Test
    public void getLigamatchDOsByWettkampfId(){
        LigamatchBE expectedLigamatchBE = BaseLigamatchTest.getLigamatchBE();

        final List<LigamatchBE> expectedBEList = Collections.singletonList(expectedLigamatchBE);

        //configure mocks
        when(ligamatchDAO.findLigamatchesByWettkampfId(anyLong())).thenReturn(expectedBEList);

        //call test method
        final List<LigamatchDO> actual = underTest.getLigamatchDOsByWettkampfId(MATCH_WETTKAMPF_ID);

        //assert result
        BaseLigamatchTest.validateDOObjectList(actual);
        verify(ligamatchDAO).findLigamatchesByWettkampfId(MATCH_WETTKAMPF_ID);
    }

    @Test
    public void getLigamatchById(){
        LigamatchBE expectedLigamatchBE = BaseLigamatchTest.getLigamatchBE();

        // configure mocks
        when(ligamatchDAO.findById(anyLong())).thenReturn(expectedLigamatchBE);

        // call test method
        final LigamatchBE actual = underTest.getLigamatchById(MATCH_ID);

        // assert result

        BaseLigamatchTest.assertValid(expectedLigamatchBE, actual);
        verify(ligamatchDAO).findById(MATCH_ID);
    }

    @Test
    public void getLigagmatchByIdThrowsExecption(){
        LigamatchBE expectedLigamatchBE = null;

        //configure mocks
        when(ligamatchDAO.findById(anyLong())).thenReturn(expectedLigamatchBE);

        //call the method
        assertThatThrownBy(()->{
            underTest.getLigamatchById(1L);
        })
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("No match found for ID");
        verify(ligamatchDAO).findById(1L);
    }



    @Test
    public void findByMannschaftId() {
        MatchBE expectedMatchBE = getMatchBE();

        final List<MatchBE> expectedBEList = Collections.singletonList(expectedMatchBE);

        // configure mocks
        when(matchDAO.findByMannschaftId(anyLong())).thenReturn(expectedBEList);

        // call test method
        final List<MatchDO> actual = underTest.findByMannschaftId(MATCH_MANNSCHAFT_ID);

        // assert result
        validateObjectList(actual);
        verify(matchDAO).findByMannschaftId(MATCH_MANNSCHAFT_ID);
    }


    @Test
    public void create() {
        MatchBE expectedMatchBE = getMatchBE();
        DsbMannschaftDO expectedPlatzhalter = getPlatzhalter();


        // configure mocks
        when(matchDAO.create(any(MatchBE.class), anyLong())).thenReturn(expectedMatchBE);
        when(mannschaftComponent.findById(anyLong())).thenReturn(expectedPlatzhalter);

        // call test method
        MatchDO matchDO = MatchMapper.toMatchDO.apply(expectedMatchBE);
        MatchDO actual = underTest.create(matchDO, CURRENT_USER_ID);

        // assert result
        assertValid(expectedMatchBE, actual);

        // cannot verify invocation as BE is different in component impl method (from mapper)
    }


    @Test
    public void update() {
        MatchBE expectedMatchBE = getMatchBE();
        // configure mocks to return the expectedMatchBE when findById is called
        when(matchDAO.findById(anyLong())).thenReturn(expectedMatchBE);
        when(matchDAO.update(any(MatchBE.class), anyLong())).thenReturn(expectedMatchBE);
        MatchDO matchDO = MatchMapper.toMatchDO.apply(expectedMatchBE);
        MatchDO actual = underTest.update(matchDO, CURRENT_USER_ID);

        // assert result
        assertValid(expectedMatchBE, actual);

        // cannot verify invocation as BE is different in component impl method (from mapper)
    }


    /*
    @Test
    public void delete() {
        MatchBE expectedMatchBE = getMatchBE();
        MatchDO matchDO = MatchMapper.toMatchDO.apply(expectedMatchBE);
        underTest.delete(matchDO, CURRENT_USER_ID);
    }

     */

    @Test
    public void getMannschaftsNameByID() {
        DsbMannschaftDO expected = getDsbMannschaftDO();
        VereinDO expectedVerein = getVereinDO();

        when(mannschaftComponent.findById(anyLong())).thenReturn(expected);
        when(vereinComponent.findById(anyLong())).thenReturn(expectedVerein);
        String name = underTest.getMannschaftsNameByID(1L);

        assertThat(name).isNotNull();
        assertThat(name).contains(expectedVerein.getName());
    }

    @Test
    public void createInitialMatchesWT0_NegativeVeranstalungsID(){

        //final Long veranstaltungsId, final Long currentUserId

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.createInitialMatchesWT0(-1L,10L))
                        .withMessageContaining("Veranstaltungs-ID must not be Null or negative")
                        .withNoCause();
    }

    @Test
    public void createInitialMatchesWT0_NullVeranstalungsID(){

        //final Long veranstaltungsId, final Long currentUserId

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.createInitialMatchesWT0(null,10L))
                .withMessageContaining("Veranstaltungs-ID must not be Null or negative")
                .withNoCause();
    }

    @Test
    public void findByWettkampfIDMatchNrScheibenNr_wettkampfIDisNull(){
        /*

        Passe: %s must not be null

        Passe: %s must not be negative

        checkPreconditions(wettkampfId, "wettkampf_Id"); null + negative
        checkPreconditions(MatchNr, "matchNr");
        checkPreconditions(scheibenNummer, "scheibenNummer");
         */

        //Long wettkampfId, Long MatchNr, Long scheibenNummer

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.findByWettkampfIDMatchNrScheibenNr(null,10L,10L))
                .withMessageContaining("Passe: wettkampf_Id must not be null")
                .withNoCause();
    }

    @Test
    public void findByWettkampfIDMatchNrScheibenNr_wettkampfIDisNegative(){
        /*

        Passe: %s must not be null

        Passe: %s must not be negative

        checkPreconditions(wettkampfId, "wettkampf_Id"); null + negative
        checkPreconditions(MatchNr, "matchNr");
        checkPreconditions(scheibenNummer, "scheibenNummer");
         */

        //Long wettkampfId, Long MatchNr, Long scheibenNummer

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.findByWettkampfIDMatchNrScheibenNr(-1L,10L,10L))
                .withMessageContaining("Passe: wettkampf_Id must not be negative")
                .withNoCause();
    }

    @Test
    public void findByWettkampfIDMatchNrScheibenNr_MatchNrisNull(){
        /*

        Passe: %s must not be null

        Passe: %s must not be negative

        checkPreconditions(wettkampfId, "wettkampf_Id"); null + negative
        checkPreconditions(MatchNr, "matchNr");
        checkPreconditions(scheibenNummer, "scheibenNummer");
         */

        //Long wettkampfId, Long MatchNr, Long scheibenNummer

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.findByWettkampfIDMatchNrScheibenNr(1L,null,10L))
                .withMessageContaining("Passe: matchNr must not be null")
                .withNoCause();
    }

    @Test
    public void findByWettkampfIDMatchNrScheibenNr_MatchNrisNegative(){
        /*

        Passe: %s must not be null

        Passe: %s must not be negative

        checkPreconditions(wettkampfId, "wettkampf_Id"); null + negative
        checkPreconditions(MatchNr, "matchNr");
        checkPreconditions(scheibenNummer, "scheibenNummer");
         */

        //Long wettkampfId, Long MatchNr, Long scheibenNummer

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.findByWettkampfIDMatchNrScheibenNr(1L,-1L,10L))
                .withMessageContaining("Passe: matchNr must not be negative")
                .withNoCause();
    }

    @Test
    public void findByWettkampfIDMatchNrScheibenNr_ScheibenNummerisNull(){
        /*

        Passe: %s must not be null

        Passe: %s must not be negative

        checkPreconditions(wettkampfId, "wettkampf_Id"); null + negative
        checkPreconditions(MatchNr, "matchNr");
        checkPreconditions(scheibenNummer, "scheibenNummer");
         */

        //Long wettkampfId, Long MatchNr, Long scheibenNummer

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.findByWettkampfIDMatchNrScheibenNr(1L,1L,null))
                .withMessageContaining("Passe: scheibenNummer must not be null")
                .withNoCause();
    }

    @Test
    public void findByWettkampfIDMatchNrScheibenNr_ScheibenNummerisNegative(){
        /*

        Passe: %s must not be null

        Passe: %s must not be negative

        checkPreconditions(wettkampfId, "wettkampf_Id"); null + negative
        checkPreconditions(MatchNr, "matchNr");
        checkPreconditions(scheibenNummer, "scheibenNummer");
         */

        //Long wettkampfId, Long MatchNr, Long scheibenNummer

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.findByWettkampfIDMatchNrScheibenNr(1L,1L,-1L))
                .withMessageContaining("Passe: scheibenNummer must not be negative")
                .withNoCause();
    }

    @Test
    public void checkMatch_MatchDOWettkampfIDlessThanZero(){

        //Match: currentUserId must not be null and must not be negative
        //Preconditions.checkArgument(matchDO.getWettkampfId() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);
        //Preconditions.checkArgument(matchDO.getMannschaftId() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);
        //Preconditions.checkArgument(matchDO.getMatchScheibennummer() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);

        MatchDO MatchDOWettkampfIDlessThanZero = new MatchDO(0L, 0L, 0L, 0L, 0L,0L,0L,0L,0L,0L,0L,0L,0L);

        MatchDOWettkampfIDlessThanZero.setWettkampfId(-5L);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.update(MatchDOWettkampfIDlessThanZero, -1L))
                .withMessageContaining("Passe: Match: currentUserId must not be null and must not be negative")
                .withNoCause();

    }

    @Test
    public void checkMatch_MatchDOMannschaftIDlessThanZero(){

        //Match: currentUserId must not be null and must not be negative
        //Preconditions.checkArgument(matchDO.getWettkampfId() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);
        //Preconditions.checkArgument(matchDO.getMannschaftId() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);
        //Preconditions.checkArgument(matchDO.getMatchScheibennummer() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);

        MatchDO MatchDOMannschaftIDlessThanZero = new MatchDO(0L, 0L, 0L, 0L, 0L,0L,0L,0L,0L,0L,0L,0L,0L);

        MatchDOMannschaftIDlessThanZero.setMannschaftId(-5L);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.update(MatchDOMannschaftIDlessThanZero, -1L))
                .withMessageContaining("Passe: Match: currentUserId must not be null and must not be negative")
                .withNoCause();

    }

    @Test
    public void checkMatch_MatchDOScheibeNummerlessThanZero(){

        //Match: currentUserId must not be null and must not be negative
        //Preconditions.checkArgument(matchDO.getWettkampfId() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);
        //Preconditions.checkArgument(matchDO.getMannschaftId() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);
        //Preconditions.checkArgument(matchDO.getMatchScheibennummer() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);

        MatchDO MatchDOOScheibeNummerlessThanZero = new MatchDO(0L, 0L, 0L, 0L, 0L,0L,0L,0L,0L,0L,0L,0L,0L);

        MatchDOOScheibeNummerlessThanZero.setMatchScheibennummer(-5L);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.update(MatchDOOScheibeNummerlessThanZero, -1L))
                .withMessageContaining("Passe: Match: currentUserId must not be null and must not be negative")
                .withNoCause();

    }

    @Test
    public void checkPreconditionsIDlessThanZero(){
        //Preconditions.checkArgument(id >= 0, String.format(PRECONDITION_MSG_TEMPLATE_NEGATIVE, iDIdentifier));

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(()-> underTest.checkPreconditions(-1L, "TEST"))
                .withMessageContaining("Passe: TEST must not be negative")
                .withNoCause();
    }


    @Test
    public void createInitialMatchesWT0_8_Teams(){

        for (int numberOfTeams : new int[]{8}) {
            List<DsbMannschaftDO> mannschaften = new ArrayList<>();

            for (int i = 0; i < numberOfTeams; i++) {
                DsbMannschaftDO mannschaft = new DsbMannschaftDO(1L, "TEST", 1L, 1L, 1L, 1L, 1L, 1L);
                mannschaften.add(mannschaft);
            }

            when(mannschaftComponent.findAllByVeranstaltungsId(1L)).thenReturn(mannschaften);

            WettkampfDO wettkampf = new WettkampfDO(1L);

            when(wettkampfComponent.findWT0byVeranstaltungsId(1L)).thenReturn(wettkampf);

            underTest.createInitialMatchesWT0(1L, 1L);

            verify(wettkampfComponent).findWT0byVeranstaltungsId(1L);
            verify(mannschaftComponent).findAllByVeranstaltungsId(1L);
        }
    }
    @Test
    public void createInitialMatchesWT0_6_Teams(){

        for (int numberOfTeams : new int[]{6}) {
            List<DsbMannschaftDO> mannschaften = new ArrayList<>();

            for (int i = 0; i < numberOfTeams; i++) {
                DsbMannschaftDO mannschaft = new DsbMannschaftDO(1L, "TEST", 1L, 1L, 1L, 1L, 1L, 1L);
                mannschaften.add(mannschaft);
            }

            when(mannschaftComponent.findAllByVeranstaltungsId(1L)).thenReturn(mannschaften);

            WettkampfDO wettkampf = new WettkampfDO(1L);

            when(wettkampfComponent.findWT0byVeranstaltungsId(1L)).thenReturn(wettkampf);

            underTest.createInitialMatchesWT0(1L, 1L);

            verify(wettkampfComponent).findWT0byVeranstaltungsId(1L);
            verify(mannschaftComponent).findAllByVeranstaltungsId(1L);
        }
    }
    @Test
    public void createInitialMatchesWT0_4_Teams(){

        for (int numberOfTeams : new int[]{4}) {
            List<DsbMannschaftDO> mannschaften = new ArrayList<>();

            for (int i = 0; i < numberOfTeams; i++) {
                DsbMannschaftDO mannschaft = new DsbMannschaftDO(1L, "TEST", 1L, 1L, 1L, 1L, 1L, 1L);
                mannschaften.add(mannschaft);
            }

            when(mannschaftComponent.findAllByVeranstaltungsId(1L)).thenReturn(mannschaften);

            WettkampfDO wettkampf = new WettkampfDO(1L);

            when(wettkampfComponent.findWT0byVeranstaltungsId(1L)).thenReturn(wettkampf);

            underTest.createInitialMatchesWT0(1L, 1L);

            verify(wettkampfComponent).findWT0byVeranstaltungsId(1L);
            verify(mannschaftComponent).findAllByVeranstaltungsId(1L);
        }
    }
    @Test (expected = BusinessException.class)
    public void createInitialMatchesWT0_7_Teams(){

        for (int numberOfTeams : new int[]{7}) {
            List<DsbMannschaftDO> mannschaften = new ArrayList<>();

            for (int i = 0; i < numberOfTeams; i++) {
                DsbMannschaftDO mannschaft = new DsbMannschaftDO(1L, "TEST", 1L, 1L, 1L, 1L, 1L, 1L);
                mannschaften.add(mannschaft);
            }

            when(mannschaftComponent.findAllByVeranstaltungsId(1L)).thenReturn(null);

            WettkampfDO wettkampf = new WettkampfDO(1L);

            when(wettkampfComponent.findWT0byVeranstaltungsId(1L)).thenReturn(null);

            underTest.createInitialMatchesWT0(1L, 1L);
        }
    }
}