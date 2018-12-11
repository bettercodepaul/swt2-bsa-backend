package de.bogenliga.application.business.wettkampf.impl.business;


import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.business.wettkampf.impl.dao.WettkampfDAO;
import de.bogenliga.application.business.wettkampf.impl.entity.WettkampfBE;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Daniel Schott daniel.schott@student.reutlingen-university.de
 */
public class WettkampfComponentImplTest {

    private static final long user_Id=13;
    private static final OffsetDateTime created_At_Utc = OffsetDateTime.now();
    private static final long version = 1234;

    private static final long wettkampf_Id = 322;
    private static final long wettkampf_Veranstaltung_Id = 0;
    private static final String wettkampf_Datum = "2019-05-21";
    private static final String wettkampf_Ort ="Sporthalle,72810 Gomaringen";
    private static final String wettkampf_Beginn ="8:00";
    private static final long wettkampf_Tag = 8;
    private static final long wettkampf_Disziplin_Id = 0;
    private static final long wettkampf_Wettkampftyp_Id = 1;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private WettkampfDAO wettkampfDAO;
    @InjectMocks
    private WettkampfComponentImpl underTest;
    @Captor
    private ArgumentCaptor<WettkampfBE> wettkampfBEArgumentCaptor;


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static WettkampfBE getWettkampfBE() {
        final WettkampfBE expectedBE = new WettkampfBE();
      expectedBE.setId(wettkampf_Id);
      expectedBE.setVeranstaltungsId(wettkampf_Veranstaltung_Id);
      expectedBE.setDatum(wettkampf_Datum);
      expectedBE.setWettkampfOrt(wettkampf_Ort);
      expectedBE.setWettkampfBeginn(wettkampf_Beginn);
      expectedBE.setWettkampfTag(wettkampf_Tag);
      expectedBE.setWettkampfDisziplinId(wettkampf_Disziplin_Id);
      expectedBE.setWettkampfTypId(wettkampf_Wettkampftyp_Id);


        return expectedBE;
    }


    public static WettkampfDO getWettkampfDO() {
        return new WettkampfDO( wettkampf_Id,
                wettkampf_Veranstaltung_Id,
                wettkampf_Datum,
                wettkampf_Ort,
                wettkampf_Beginn,
                wettkampf_Tag,
                wettkampf_Disziplin_Id,
                wettkampf_Wettkampftyp_Id,
                created_At_Utc,
                user_Id,
                version
                );
    }

    @Test
    public void findAll() {
        // prepare test data
        final WettkampfBE expectedBE = getWettkampfBE();
        final List<WettkampfBE> expectedBEList = Collections.singletonList(expectedBE);

        // configure mocks
        when(wettkampfDAO.findAll()).thenReturn(expectedBEList);


        // call test method
        final List<WettkampfDO> actual = underTest.findAll();


        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getId());
        assertThat(actual.get(0).getVeranstaltungsId())
                .isEqualTo(expectedBE.getVeranstaltungsId());
        assertThat(actual.get(0).getDatum())
                .isEqualTo(expectedBE.getDatum());
        assertThat(actual.get(0).getWettkampfOrt())
                .isEqualTo(expectedBE.getWettkampfOrt());
        assertThat(actual.get(0).getWettkampfTag())
                .isEqualTo(expectedBE.getWettkampfTag());
        assertThat(actual.get(0).getWettkampfDisziplinId())
                .isEqualTo(expectedBE.getWettkampfDisziplinId());
        assertThat(actual.get(0).getWettkampfTypId())
                .isEqualTo(expectedBE.getWettkampfTypId());

        // verify invocations
        verify(wettkampfDAO).findAll();
    }

    @Test
    public void findById() {
        // prepare test data
        final WettkampfBE expectedBE = getWettkampfBE();

        // configure mocks
        when(wettkampfDAO.findById(wettkampf_Id)).thenReturn(expectedBE);

        // call test method
        final WettkampfDO actual = underTest.findById(wettkampf_Id);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getId());

        // verify invocations
        verify(wettkampfDAO).findById(wettkampf_Id);
    }

    @Test
    public void create() {
        // prepare test data
        final WettkampfDO input = getWettkampfDO();

        final WettkampfBE expectedBE = getWettkampfBE();

        // configure mocks
        when(wettkampfDAO.create(any(WettkampfBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final WettkampfDO actual = underTest.create(input, user_Id);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(input.getId());

        // verify invocations
        verify(wettkampfDAO).create(wettkampfBEArgumentCaptor.capture(), anyLong());

        final WettkampfBE persistedBE = wettkampfBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getId())
                .isEqualTo(input.getId());
    }

    @Test
    public void delete() {
        // prepare test data
        final WettkampfDO input = getWettkampfDO();

        final WettkampfBE expectedBE = getWettkampfBE();

        // configure mocks

        // call test method
        underTest.delete(input, user_Id);

        // assert result

        // verify invocations
        verify(wettkampfDAO).delete(wettkampfBEArgumentCaptor.capture(), anyLong());

        final WettkampfBE persistedBE = wettkampfBEArgumentCaptor.getValue();

        assertThat(persistedBE).isNotNull();

        assertThat(persistedBE.getId())
                .isEqualTo(input.getId());
    }
}
