package de.bogenliga.application.business.anzeigen;

import de.bogenliga.application.business.wettkampf.api.types.AnzeigenDO;
import de.bogenliga.application.business.wettkampf.impl.business.AnzeigenComponentImpl;
import de.bogenliga.application.business.wettkampf.impl.dao.AnzeigenDAO;
import de.bogenliga.application.business.wettkampf.impl.entity.AnzeigenBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AnzeigenComponentImplTest extends AnzeigenDAOTestHelper {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BasicDAO basicDAO;

    @Mock
    private AnzeigenDAO anzeigenDAO;

    private AnzeigenBE expectedBE;

    private AnzeigenComponentImpl underTest;

    @Before
    public void testSetup() {
        expectedBE = getAnzeigenBE();
        underTest = new AnzeigenComponentImpl(anzeigenDAO);

        Map<String, Object> valuesToMethodNames = getValuesToMethodMap();
        valuesToMethodNames.put(
                "getId",
                expectedBE.getId()
        );
        valuesToMethodNames.put(
                "getPhysischeBildschirmId",
                expectedBE.getPhysischeBildschirmId()
        );
        valuesToMethodNames.put(
                "getTableTyp",
                expectedBE.getTableTyp()
        );
        valuesToMethodNames.put(
                "getVeranstaltungsId",
                expectedBE.getVeranstaltungsId()
        );
        valuesToMethodNames.put(
                "getAktuellesMatch",
                expectedBE.getAktuellesMatch()
        );
    }

    @Test
    public void testFindByVeranstaltungsIdRecordExists() {
        final List<AnzeigenBE> expectedBEList = Collections.singletonList(expectedBE);
        when(anzeigenDAO.findByVeranstaltungsId(1L)).thenReturn(expectedBEList);

        List<AnzeigenDO> result = underTest.findByVeranstaltungsId(1L);

        // Check that Optional contains a value
        assertThat(result).hasSize(1);
        AnzeigenDO anzeigenDO = result.get(0);
        assertThat(anzeigenDO.getId()).isEqualTo(expectedBE.getId());
        assertThat(anzeigenDO.getPhysischeBildschirmId()).isEqualTo(expectedBE.getPhysischeBildschirmId());
        assertThat(anzeigenDO.getTableTyp()).isEqualTo(expectedBE.getTableTyp());
        assertThat(anzeigenDO.getVeranstaltungsId()).isEqualTo(expectedBE.getVeranstaltungsId());
        assertThat(anzeigenDO.getAktuellesMatch()).isEqualTo(expectedBE.getAktuellesMatch());
    }


    @Test
    public void testFindByVeranstaltungsIdRecordNotExists() {
        when(basicDAO.selectSingleEntity(any(), any(), any())).thenThrow(new RuntimeException("Record not found"));

        List<AnzeigenDO> emptyResult = underTest.findByVeranstaltungsId(999L);
        assertThat(emptyResult.isEmpty()).isTrue();
    }


    @Test
    public void testFindById() {
        final Long id = 11L;

        // prepare DAO response
        final AnzeigenBE expectedAnzeigenBE = new AnzeigenBE();
        when(anzeigenDAO.findById(id)).thenReturn(expectedAnzeigenBE);

        // call method under test
        final AnzeigenDO actual = underTest.findById(id);
        // assert result and interaction
        assertNotNull("Result must not be null", actual);
    }

    @Test
    public void testFindByPhysischeBildschirmId() {
        final String physischeBildschirmId = "-";

        // prepare DAO response
        final AnzeigenBE expectedAnzeigenBE = new AnzeigenBE();
        when(anzeigenDAO.findByPhysischeBildschirmId(physischeBildschirmId)).thenReturn(expectedAnzeigenBE);

        // call method under test
        final AnzeigenDO actual = underTest.findByPhysischeBildschirmId(physischeBildschirmId);
        // assert result and interaction
        assertNotNull("Result must not be null", actual);
    }

    @Test
    public void testCreate() {
        // 1. Vorbereitung (Gegeneinander ausgetauschte Daten vorbereiten)
        AnzeigenDO inputDO = getAnzeigenDO();

        // Wir simulieren, dass das DAO das erstellte Backend-Entity (BE) erfolgreich zurückgibt
        when(anzeigenDAO.create(any(), any())).thenReturn(expectedBE);

        // 2. Ausführung der Methode, die getestet werden soll (underTest)
        AnzeigenDO resultDO = underTest.create(inputDO, 4L); // 4L entspricht der USER-ID aus der Base-Klasse

        // 3. Überprüfung (Assertions)
        assertNotNull("Das Ergebnis-DO darf nicht null sein", resultDO);
        assertThat(resultDO.getId()).isEqualTo(expectedBE.getId());
        assertThat(resultDO.getPhysischeBildschirmId()).isEqualTo(expectedBE.getPhysischeBildschirmId());
        assertThat(resultDO.getTableTyp()).isEqualTo(expectedBE.getTableTyp());
        assertThat(resultDO.getVeranstaltungsId()).isEqualTo(expectedBE.getVeranstaltungsId());
        assertThat(resultDO.getAktuellesMatch()).isEqualTo(expectedBE.getAktuellesMatch());
    }

    @Test
    public void testUpdate() {
        // 1. Vorbereitung
        AnzeigenDO inputDO = getAnzeigenDO();

        // Wir simulieren, dass das DAO das aktualisierte Backend-Entity zurückgibt
        when(anzeigenDAO.update(any(AnzeigenBE.class), any(Long.class))).thenReturn(expectedBE);

        // 2. Ausführung
        AnzeigenDO resultDO = underTest.update(inputDO, 4L);

        // 3. Überprüfung
        assertNotNull("Das aktualisierte DO darf nicht null sein", resultDO);
        assertThat(resultDO.getId()).isEqualTo(expectedBE.getId());
        assertThat(resultDO.getPhysischeBildschirmId()).isEqualTo(expectedBE.getPhysischeBildschirmId());
    }

    @Test
    public void testDelete() {
        // 1. Vorbereitung
        AnzeigenDO inputDO = getAnzeigenDO();

        // Da 'delete' im DAO typischerweise 'void' zurückgibt (bzw. nichts),
        // müssen wir hier kein spezielles 'when(...).thenReturn(...)' für das DAO definieren.
        // Falls deine Schnittstelle ein verändertes Objekt zurückgibt, passe es analog zu 'update' an.

        // 2. Ausführung
        underTest.delete(inputDO, 4L);

        // 3. Überprüfung
        // Da die Methode meist void ist, prüfen wir stattdessen mit Mockito,
        // ob das DAO auch wirklich mit den korrekten Parametern aufgerufen wurde.
        org.mockito.Mockito.verify(anzeigenDAO).delete(any(AnzeigenBE.class), org.mockito.ArgumentMatchers.eq(4L));
    }

    @Test
    public void testGeneratePhysischeBildschirmId() {
        final String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "abcdefghijklmnopqrstuvwxyz" +
                "0123456789";

        final String id = underTest.generatePhysischeBildschirmId();
        final char[] characters = id.toCharArray();

        assertThat(id.length() == 4 ).isTrue();
        assertThat(validCharacters).contains(String.valueOf(characters[0]));
        assertThat(validCharacters).contains(String.valueOf(characters[1]));
        assertThat(validCharacters).contains(String.valueOf(characters[2]));
        assertThat(validCharacters).contains(String.valueOf(characters[3]));
    }
}