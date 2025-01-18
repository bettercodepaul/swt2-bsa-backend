package de.bogenliga.application.business.dsbmannschaft.impl.dao;

import de.bogenliga.application.business.dsbmannschaft.impl.entity.DsbMannschaftBEext;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static de.bogenliga.application.business.dsbmannschaft.impl.business.DsbMannschaftComponentImplTest.getDsbMannschaftBEext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DsbMannschaftBasicDAOextTest {

    private static final long WETTKAMPFID=31;

    private static final long ID = 31;
    private static final String NAME = "TestName";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private DsbMannschaftDAOext underTest;



    @Test
    public void findVeranstaltungAndWettkampfById() {
        // prepare test data
        final DsbMannschaftBEext expectedBE = getDsbMannschaftBEext();

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));
        expectedBE.setVeranstaltungId(null);

        // call test method
        final List<DsbMannschaftBEext> actual = underTest.findVeranstaltungAndWettkampfById(WETTKAMPFID);

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getNummer())
                .isEqualTo(expectedBE.getNummer());
        assertThat(actual.get(0).getVereinName())
                .isEqualTo(expectedBE.getVereinName());
        assertThat(actual.get(0).getWettkampfOrtsname())
                .isEqualTo(expectedBE.getWettkampfOrtsname());
        assertThat(actual.get(0).getWettkampfTag())
                .isEqualTo(expectedBE.getWettkampfTag());
        assertThat(actual.get(0).getVeranstaltungName())
                .isEqualTo(expectedBE.getVeranstaltungName());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());


    }

    @Test
    public void findAllByWettkampfId() {
        // Arrange
        final DsbMannschaftBEext expectedBE = getDsbMannschaftBEext();
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // Act
        final List<DsbMannschaftBEext> actual = underTest.findAllByWettkampfId(ID);

        // Assert
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
        assertThat(actual.get(0)).isEqualTo(expectedBE);

        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void findAllByWettkampfIdWithName() {
        // Arrange
        final DsbMannschaftBEext expectedBE = getDsbMannschaftBEext();
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // Act
        final List<DsbMannschaftBEext> actual = underTest.findAllByWettkampfIdWithName(ID);

        // Assert
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
        assertThat(actual.get(0)).isEqualTo(expectedBE);

        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void findByIdwithName() {
        // Arrange
        final DsbMannschaftBEext expectedBE = getDsbMannschaftBEext();
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // Act
        final DsbMannschaftBEext actual = underTest.findByIdwithName(ID);

        // Assert
        assertThat(actual).isNotNull().isEqualTo(expectedBE);

        verify(basicDao).selectSingleEntity(any(), any(), any());
    }

    @Test
    public void findAllByVereinsIdwithName() {
        // Arrange
        final DsbMannschaftBEext expectedBE = getDsbMannschaftBEext();
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // Act
        final List<DsbMannschaftBEext> actual = underTest.findAllByVereinsIdwithName(ID);

        // Assert
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
        assertThat(actual.get(0)).isEqualTo(expectedBE);

        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void findAllByNameWithName() {
        // Arrange
        final DsbMannschaftBEext expectedBE = getDsbMannschaftBEext();
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // Act
        final List<DsbMannschaftBEext> actual = underTest.findAllByNameWithName(NAME);

        // Assert
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
        assertThat(actual.get(0)).isEqualTo(expectedBE);

        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void findAllByWarteschlangewithName() {
        // Arrange
        final DsbMannschaftBEext expectedBE = getDsbMannschaftBEext();
        when(basicDao.selectEntityList(any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // Act
        final List<DsbMannschaftBEext> actual = underTest.findAllByWarteschlangewithName();

        // Assert
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
        assertThat(actual.get(0)).isEqualTo(expectedBE);

        verify(basicDao).selectEntityList(any(), any());
    }

    @Test
    public void findAllwithName() {
        // Arrange
        final DsbMannschaftBEext expectedBE = getDsbMannschaftBEext();
        when(basicDao.selectEntityList(any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // Act
        final List<DsbMannschaftBEext> actual = underTest.findAllwithName();

        // Assert
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
        assertThat(actual.get(0)).isEqualTo(expectedBE);

        verify(basicDao).selectEntityList(any(), any());
    }

    @Test
    public void findAllByVeranstaltungsIdwithName() {
        // Arrange
        final DsbMannschaftBEext expectedBE = getDsbMannschaftBEext();
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // Act
        final List<DsbMannschaftBEext> actual = underTest.findAllByVeranstaltungsIdwithName(ID);

        // Assert
        assertThat(actual).isNotNull().isNotEmpty().hasSize(1);
        assertThat(actual.get(0)).isEqualTo(expectedBE);

        verify(basicDao).selectEntityList(any(), any(), any());
    }

}
