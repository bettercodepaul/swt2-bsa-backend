package de.bogenliga.application.common.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LigaSlugUtilTest {

    // toSlug: null oder Blank -> "liga"
    @Test
    public void toSlug_returnsLigaForNullOrBlank() {
        assertEquals("liga", LigaSlugUtil.toSlug(null));
        assertEquals("liga", LigaSlugUtil.toSlug(""));
        assertEquals("liga", LigaSlugUtil.toSlug(" "));
        assertEquals("liga", LigaSlugUtil.toSlug("\t"));
        assertEquals("liga", LigaSlugUtil.toSlug("\n"));
    }

    // toSlug: diverse Eingaben
    @Test
    public void toSlug_variousInputs_expectedSlug() {
        assertEquals("bundesliga", LigaSlugUtil.toSlug("Bundesliga"));
        assertEquals("1-bundesliga", LigaSlugUtil.toSlug("1. Bundesliga"));
        assertEquals("regionalliga-sued-west", LigaSlugUtil.toSlug("Regionalliga Süd-West"));
        assertEquals("aeoeue-aeoeue-ss", LigaSlugUtil.toSlug("ÄÖÜ äöü ß"));
        assertEquals("oesterreich", LigaSlugUtil.toSlug("Österreich"));
        assertEquals("fussball-liga", LigaSlugUtil.toSlug("Fußball-Liga"));
        assertEquals("fc-koeln-2", LigaSlugUtil.toSlug("FC Köln 2"));

        // Säuberung/Normalisierung
        assertEquals("liga", LigaSlugUtil.toSlug("  -- Liga !!! "));
        assertEquals("a", LigaSlugUtil.toSlug("--A--"));
        assertEquals("liga-name-test", LigaSlugUtil.toSlug("Liga___Name  ++ Test"));
        assertEquals("creme-brulee", LigaSlugUtil.toSlug("Crème Brûlée"));

        // Nur Sonderzeichen -> Fallback
        assertEquals("liga", LigaSlugUtil.toSlug("!!!"));

        // Nicht-lateinische Zeichen -> Fallback
        assertEquals("liga", LigaSlugUtil.toSlug("Лига"));
    }

    // isValid: gültige Slugs -> true
    @Test
    public void isValid_validSlugs_true() {
        assertTrue(LigaSlugUtil.isValid("liga"));
        assertTrue(LigaSlugUtil.isValid("1-bundesliga"));
        assertTrue(LigaSlugUtil.isValid("regionalliga-sued-west"));
        assertTrue(LigaSlugUtil.isValid("aeoeue-ss"));
        assertTrue(LigaSlugUtil.isValid("a1-b2-c3"));
        assertTrue(LigaSlugUtil.isValid("fc-koeln-2"));
    }

    // isValid: ungültige Slugs -> false
    @Test
    public void isValid_invalidSlugs_false() {
        assertFalse(LigaSlugUtil.isValid(null));     // null
        assertFalse(LigaSlugUtil.isValid(""));       // leer
        assertFalse(LigaSlugUtil.isValid("Liga"));   // Großbuchstaben
        assertFalse(LigaSlugUtil.isValid("liga_"));  // Unterstrich
        assertFalse(LigaSlugUtil.isValid("liga--name")); // doppelte Bindestriche
        assertFalse(LigaSlugUtil.isValid("-liga"));  // führender Bindestrich
        assertFalse(LigaSlugUtil.isValid("liga-"));  // abschließender Bindestrich
        assertFalse(LigaSlugUtil.isValid("liga!"));  // ungültiges Zeichen
        assertFalse(LigaSlugUtil.isValid("ä"));      // Nicht-konformer Buchstabe
    }

    // Eigenschaft: isValid(toSlug(x)) ist immer true
    @Test
    public void property_isValid_ofToSlug_isAlwaysTrue() {
        String[] inputs = new String[] {
                null,
                "",
                "   ",
                "Bundesliga",
                "1. Bundesliga",
                "Regionalliga Süd-West",
                "ÄÖÜ äöü ß",
                "Österreich",
                "Fußball-Liga",
                "FC Köln 2",
                "Crème Brûlée",
                "  -- Liga !!! ",
                "!!!",
                "Лига"
        };

        for (String input : inputs) {
            String slug = LigaSlugUtil.toSlug(input);
            assertTrue("Ungültiger Slug: " + slug + " für Eingabe: " + input,
                    LigaSlugUtil.isValid(slug));
        }
    }

    // Utility-Klasse: privater, parameterloser Konstruktor vorhanden (und für Coverage aufrufbar)
    @Test
    public void utilityClass_hasPrivateNoArgConstructor() throws Exception {
        Constructor<LigaSlugUtil> ctor = LigaSlugUtil.class.getDeclaredConstructor();
        assertTrue("Konstruktor sollte private sein", Modifier.isPrivate(ctor.getModifiers()));
        ctor.setAccessible(true);
        Object instance = ctor.newInstance();
        assertNotNull(instance);
    }
}