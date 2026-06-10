package de.bogenliga.application.business.anzeigen;

import de.bogenliga.application.business.wettkampf.impl.dao.AnzeigenDAO;
import de.bogenliga.application.business.wettkampf.impl.entity.AnzeigenBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnzeigenDAOTest extends AnzeigenDAOTestHelper {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BasicDAO basicDao;

    @InjectMocks
    private AnzeigenDAO underTest;

    private AnzeigenBE expectedBE;
    private static final long USER_ID = 4L;

    @Before
    public void setUp() {
        expectedBE = getAnzeigenBE();
    }

    @Test
    public void testFindById() {
        // 1. Vorbereitung (Mocking von basicDao)
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // 2. Ausführung
        final AnzeigenBE actual = underTest.findById(11L);

        // 3. Überprüfung
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(expectedBE.getId());
        assertThat(actual.getPhysischeBildschirmId()).isEqualTo(expectedBE.getPhysischeBildschirmId());

        // Verifizieren, dass das BasicDAO mit der richtigen Query und ID aufgerufen wurde
        verify(basicDao).selectSingleEntity(any(), any(), eq(11L));
    }

    @Test
    public void testFindByWettkampfId() {
        // 1. Vorbereitung
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // 2. Ausführung
        final List<AnzeigenBE> actual = underTest.findByWettkampfId(1L);

        // 3. Überprüfung
        assertThat(actual).isNotNull().hasSize(1);
        assertThat(actual.get(0).getWettkampfId()).isEqualTo(expectedBE.getWettkampfId());

        // Auch beim Verify nutzen wir das offene any() für die Varargs
        verify(basicDao).selectEntityList(any(), any(), any());
    }

    @Test
    public void testFindByPhysischeBildschirmId() {
        // 1. Vorbereitung (Mocking von basicDao)
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // 2. Ausführung
        final AnzeigenBE actual = underTest.findByPhysischeBildschirmId("abcd");

        // 3. Überprüfung
        assertThat(actual).isNotNull();
        assertThat(actual.getPhysischeBildschirmId()).isEqualTo(expectedBE.getPhysischeBildschirmId());
        assertThat(actual.getPhysischeBildschirmId()).isEqualTo(expectedBE.getPhysischeBildschirmId());

        // Verifizieren, dass das BasicDAO mit der richtigen Query und ID aufgerufen wurde
        verify(basicDao).selectSingleEntity(any(), any(), eq("abcd"));
    }

    @Test
    public void testFindAll() {
        // 1. Vorbereitung
        when(basicDao.selectEntityList(any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // 2. Ausführung
        final List<AnzeigenBE> actual = underTest.findAll();

        // 3. Überprüfung
        assertThat(actual).isNotNull().hasSize(1);

        verify(basicDao).selectEntityList(any(), any());
    }

    @Test
    public void testCreate() {
        // 1. Vorbereitung
        when(basicDao.insertEntity(any(), any())).thenReturn(expectedBE);

        // 2. Ausführung
        final AnzeigenBE actual = underTest.create(expectedBE, USER_ID);

        // 3. Überprüfung
        assertThat(actual).isNotNull();

        // Prüfen, ob die technischen Attribute (Erstellungsdatum/User) gesetzt wurden
        verify(basicDao).setCreationAttributes(expectedBE, USER_ID);
        // Prüfen, ob das Entity in die DB geschrieben wurde
        verify(basicDao).insertEntity(any(), eq(expectedBE));
    }

    @Test
    public void testUpdate() {
        // 1. Vorbereitung
        when(basicDao.updateEntity(any(), any(), any())).thenReturn(expectedBE);

        // 2. Ausführung
        final AnzeigenBE actual = underTest.update(expectedBE, USER_ID);

        // 3. Überprüfung
        assertThat(actual).isNotNull();

        // Prüfen, ob die Änderungs-Attribute gesetzt wurden
        verify(basicDao).setModificationAttributes(expectedBE, USER_ID);
        // Prüfen, ob das Update mit dem Primärschlüssel-Feldnamen ausgeführt wurde
        verify(basicDao).updateEntity(any(), eq(expectedBE), eq("id"));
    }

    @Test
    public void testDelete() {
        // 1. Ausführung (delete liefert void)
        underTest.delete(expectedBE, USER_ID);

        // 2. Überprüfung via Verifizierung der Interaktionen mit basicDao
        verify(basicDao).setModificationAttributes(expectedBE, USER_ID);
        verify(basicDao).deleteEntity(any(), eq(expectedBE), eq("id"));
    }
}
