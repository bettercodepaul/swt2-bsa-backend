package de.bogenliga.application.business.match.impl.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import de.bogenliga.application.business.dsbmannschaft.api.DsbMannschaftComponent;
import de.bogenliga.application.business.dsbmannschaft.api.types.DsbMannschaftDO;
import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.business.match.impl.BaseMatchTest;
import de.bogenliga.application.business.match.impl.dao.MatchDAO;
import de.bogenliga.application.business.match.impl.entity.MatchBE;
import de.bogenliga.application.business.match.impl.mapper.MatchMapper;
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
    private DsbMannschaftComponent mannschaftComponent;
    @Mock
    private WettkampfComponent wettkampfComponent;

    @InjectMocks
    private MatchComponentImpl underTest;


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
        when(matchDAO.create(any(MatchBE.class), anyLong())).thenReturn(expectedMatchBE);
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
        //Preconditions.checkArgument(matchDO.getScheibenNummer() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);

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
        //Preconditions.checkArgument(matchDO.getScheibenNummer() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);

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
        //Preconditions.checkArgument(matchDO.getScheibenNummer() >= 0, PRECONDITION_MSG_CURRENT_USER_ID);

        MatchDO MatchDOOScheibeNummerlessThanZero = new MatchDO(0L, 0L, 0L, 0L, 0L,0L,0L,0L,0L,0L,0L,0L,0L);

        MatchDOOScheibeNummerlessThanZero.setScheibenNummer(-5L);

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
    public void createInitialMatchesWT0(){


        DsbMannschaftDO mannschaft1 = new DsbMannschaftDO(1L,"TEST",1L,1L,1L,1L,1L);
        DsbMannschaftDO mannschaft2 = new DsbMannschaftDO(1L,"TEST",1L,1L,1L,1L,1L);
        DsbMannschaftDO mannschaft3 = new DsbMannschaftDO(1L,"TEST",1L,1L,1L,1L,1L);
        DsbMannschaftDO mannschaft4 = new DsbMannschaftDO(1L,"TEST",1L,1L,1L,1L,1L);
        DsbMannschaftDO mannschaft5 = new DsbMannschaftDO(1L,"TEST",1L,1L,1L,1L,1L);
        DsbMannschaftDO mannschaft6 = new DsbMannschaftDO(1L,"TEST",1L,1L,1L,1L,1L);
        DsbMannschaftDO mannschaft7 = new DsbMannschaftDO(1L,"TEST",1L,1L,1L,1L,1L);
        DsbMannschaftDO mannschaft8 = new DsbMannschaftDO(1L,"TEST",1L,1L,1L,1L,1L);

        List<DsbMannschaftDO> mannschaften = new ArrayList<>();

        mannschaften.add(mannschaft1);
        mannschaften.add(mannschaft2);
        mannschaften.add(mannschaft3);
        mannschaften.add(mannschaft4);
        mannschaften.add(mannschaft5);
        mannschaften.add(mannschaft6);
        mannschaften.add(mannschaft7);
        mannschaften.add(mannschaft8);

        when(mannschaftComponent.findAllByVeranstaltungsId(1L)).thenReturn(mannschaften);

        WettkampfDO wettkampf = new WettkampfDO(1L);

        when(wettkampfComponent.findWT0byVeranstaltungsId(1L)).thenReturn(wettkampf);

        underTest.createInitialMatchesWT0(1L,1L);

        verify(wettkampfComponent).findWT0byVeranstaltungsId(1L);
        verify(mannschaftComponent).findAllByVeranstaltungsId(1L);
    }
}