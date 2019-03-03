package de.bogenliga.application.services.v1.liga.service;

import de.bogenliga.application.business.liga.api.LigaComponent;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.services.v1.liga.model.LigaDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * * @author Bruno Michael Cunha Teixeira, Bruno_Michael.Cunha_teixeira@Student.Reutlingen-University.de
 */
public class LigaServiceTest {
    private static final long ID =0;
    private static final String NAME ="Liga B";
    private static final long REGIONID = 1;
    private static final String REGIONNAME ="Region A";
    private static final long LIGAUEBERGEORDNETID = 2;
    private static final String LIGAUEBERGEORDNETNAME ="Liga A";
    private static final long VERANTWORTLICHERID= 3;
    private static final String VERANTWORTLICHEREMAIL="test@test.de";
    private static final long USER = 4;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private LigaComponent ligaComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private LigaService underTest;

    @Captor
    private ArgumentCaptor<LigaDO> ligaVOArgumentCaptor;

    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static LigaBE getLigaBE() {
        final LigaBE expectedBE = new LigaBE();
        expectedBE.setLigaId(ID);
        expectedBE.setLigaName(NAME);
        expectedBE.setLigaRegionId(REGIONID);
        expectedBE.setLigaVerantwortlichId(VERANTWORTLICHERID);
        expectedBE.setLigaUebergeordnetId(LIGAUEBERGEORDNETID);
        return expectedBE;
    }

    public static LigaDO getLigaDO(){
        return new LigaDO(
                ID,
                NAME,
                REGIONID,
                REGIONNAME,
                LIGAUEBERGEORDNETID,
                LIGAUEBERGEORDNETNAME,
                VERANTWORTLICHERID,
                VERANTWORTLICHEREMAIL
        );
    }

    public static LigaDTO getLigaDTO(){
        final LigaDTO ligaDTO = new LigaDTO();
        ligaDTO.setId(ID);
        ligaDTO.setName(NAME);
        ligaDTO.setRegionId(REGIONID);
        ligaDTO.setRegionName(REGIONNAME);
        ligaDTO.setLigaUebergeordnetId(LIGAUEBERGEORDNETID);
        ligaDTO.setLigaUebergeordnetName(LIGAUEBERGEORDNETNAME);
        ligaDTO.setLigaUebergeordnetId(VERANTWORTLICHERID);
        ligaDTO.setLigaVerantwortlichMail(VERANTWORTLICHEREMAIL);
        return ligaDTO;
    }

    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }


    @Test
    public void findAll() {
        // prepare test data
        final LigaDO ligaDO = getLigaDO();

        final List<LigaDO> ligaDOList = Collections.singletonList(ligaDO);

        // configure mocks
        when(ligaComponent.findAll()).thenReturn(ligaDOList);

        // call test method
        final List<LigaDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final LigaDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(ligaDO.getId());
        assertThat(actualDTO.getName()).isEqualTo(ligaDO.getName());

        // verify invocations
        verify(ligaComponent).findAll();
    }

    @Test
    public void findById() {
        // prepare test data
        final LigaDO ligaDO = getLigaDO();

        // configure mocks
        when(ligaComponent.findById(anyLong())).thenReturn(ligaDO);

        // call test method
        final LigaDTO actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(ligaDO.getId());
        assertThat(actual.getName()).isEqualTo(ligaDO.getName());

        // verify invocations
        verify(ligaComponent).findById(ID);
    }

    @Test
    public void create() {
        // prepare test data
        final LigaDTO input = getLigaDTO();

        final LigaDO expected = getLigaDO();

        // configure mocks
        when(ligaComponent.create(any(), anyLong())).thenReturn(expected);

        // call test method
        final LigaDTO actual = underTest.create(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getName()).isEqualTo(input.getName());

        // verify invocations
        verify(ligaComponent).create(ligaVOArgumentCaptor.capture(), anyLong());

        final LigaDO createdLiga = ligaVOArgumentCaptor.getValue();

        assertThat(createdLiga).isNotNull();
        assertThat(createdLiga.getId()).isEqualTo(input.getId());
        assertThat(createdLiga.getName()).isEqualTo(input.getName());
    }

    @Test
    public void update() {
        // prepare test data
        final LigaDTO input = getLigaDTO();

        final LigaDO expected = getLigaDO();

        // configure mocks
        when(ligaComponent.update(any(), anyLong())).thenReturn(expected);

        // call test method
        final LigaDTO actual = underTest.update(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getName()).isEqualTo(input.getName());

        // verify invocations
        verify(ligaComponent).update(ligaVOArgumentCaptor.capture(), anyLong());

        final LigaDO updatedLiga = ligaVOArgumentCaptor.getValue();

        assertThat(updatedLiga).isNotNull();
        assertThat(updatedLiga.getId()).isEqualTo(input.getId());
        assertThat(updatedLiga.getName()).isEqualTo(input.getName());
    }

    @Test
    public void delete() {
        // prepare test data
        final LigaDO expected = getLigaDO();

        // configure mocks

        // call test method
        underTest.delete(ID, principal);

        // assert result

        // verify invocations
        verify(ligaComponent).delete(ligaVOArgumentCaptor.capture(), anyLong());

        final LigaDO deletedDsbMitglied = ligaVOArgumentCaptor.getValue();

        assertThat(deletedDsbMitglied).isNotNull();
        assertThat(deletedDsbMitglied.getId()).isEqualTo(expected.getId());
        assertThat(deletedDsbMitglied.getName()).isNullOrEmpty();
    }
}