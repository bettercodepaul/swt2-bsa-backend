package de.bogenliga.application.springconfiguration.security.permissions;

import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import org.junit.Rule;
import org.junit.Test;

import org.aspectj.lang.ProceedingJoinPoint;

import org.mockito.InjectMocks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.MockitoJUnit;

import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Test for RequiresOnePermissionAspect
 *
 * Some classes are static. If someone knows how to test them feel free to do so or contact me.
 *
 * @author Max Weise, FH RT
 */
public class RequiresOnePermissionAspectTest {
    //Mock Daten
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private VeranstaltungComponent veranstaltungComponent;

    @Mock
    private DsbMitgliedComponent dsbMitgliedComponent;

    @Mock
    private UserComponent userComponent;

    @Mock
    private WettkampfComponent wettkampfComponent;

    @InjectMocks
    private RequiresOnePermissionAspect underTest;


    private static final Long W_id = 5L;
    private static final String W_name = "Liga_kummulativ";

    private static final Long W_vid = 243L;
    private static final Long W_typId = 0L;
    private static final Long W_tag = 5L;
    private static final Date W_datum = new Date(20190521L);
    private static final String W_strasse = "Reutlingerstr. 6";
    private static final String W_plz = "72764";
    private static final String W_ortsname = "Reutlingen";
    private static final String W_ortsinfo= "Hinter dem Haus";
    private static final String W_begin = "gestern";
    private static final Long W_disId = 12345L;

    protected WettkampfDO getWettkampfDO(Long id) {
        return new WettkampfDO(id, W_vid, W_datum, W_strasse, W_plz, W_ortsname, W_ortsinfo, W_begin, W_tag, W_disId, W_typId, null,null,null, null);
    }

    private static final Long V_id =246L;

    private static final String U_email = "user@email";

    protected UserDO getUserDO(Long id, Long dsb_id) {
        UserDO userDO = new UserDO();
        userDO.setId(id);
        userDO.setEmail(U_email);
        userDO.setDsbMitgliedId(dsb_id);
        return userDO;
    }


    protected DsbMitgliedDO getDsbMitgliedDO(Long dsb_id, Long vereinid) {
        return new DsbMitgliedDO(dsb_id, "Vor-Na-Me", "Nach-Na-Me",
                Date.valueOf("1991-09-01"), "nat", "nr", vereinid, "Vereinname", Long.valueOf(1), false, Date.valueOf("2001-01-01"));
     }



    @Test(expected = NullPointerException.class)
    public void checkPermission_whenRequestAttributesNull_throwNullPointerException() throws Throwable {
        ProceedingJoinPoint testJoinPoint = mock(ProceedingJoinPoint.class);
        underTest.checkPermission(testJoinPoint);
    }

    /* Test of static class. If someone knows how, please implement */
//    @Test
//    public void testCheckPermission() {}


    @Test
    public void testHasPermission_requestAttributesNull_returnFalse() {
        UserPermission userPermission = UserPermission.CAN_READ_DEFAULT;
        assertFalse(underTest.hasPermission(userPermission));
    }

    @Test
    public void testHasPermission() {
        // wir mocken keine statischen Aufrufe,
        // sondern erzeugen uns valide Daten für das Ausführen der statischen Calls
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testpermission");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Set<UserPermission> permissions = new HashSet<>(Arrays.asList(UserPermission.CAN_READ_DEFAULT, UserPermission.CAN_CREATE_STAMMDATEN));
        Mockito.doReturn(permissions).when(jwtTokenProvider).getPermissions("testpermission");

        assertTrue(underTest.hasPermission(UserPermission.CAN_READ_DEFAULT));
        assertFalse(underTest.hasPermission(UserPermission.CAN_READ_STAMMDATEN));

    }

    @Test
    public void testHasSpecificPermissionLigaLeiterID() {
        // wir mocken keine statischen Aufrufe,
        // sondern erzeugen uns valide Daten für das Ausführen der statischen Calls
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testpermission");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Set<UserPermission> permissions = new HashSet<>(Arrays.asList(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG, UserPermission.CAN_CREATE_STAMMDATEN));
        Mockito.doReturn(permissions).when(jwtTokenProvider).getPermissions("testpermission");
        Mockito.doReturn(1L).when(jwtTokenProvider).getUserId(anyString());

        VeranstaltungDO veranstaltungDO = new VeranstaltungDO();
        veranstaltungDO.setVeranstaltungID(W_vid);
        ArrayList<VeranstaltungDO> veranstaltungenDOS = new ArrayList<>();
        veranstaltungenDOS.add(veranstaltungDO);
        when(veranstaltungComponent.findByLigaleiterId(anyLong())).thenReturn(veranstaltungenDOS);

        assertTrue(underTest.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG,W_vid));
        assertFalse(underTest.hasSpecificPermissionLigaLeiterID(UserPermission.CAN_MODIFY_MY_VERANSTALTUNG,1L));
    }

    @Test
    public void testHasSpecificPermissionAusrichter() {
        // wir mocken keine statischen Aufrufe,
        // sondern erzeugen uns valide Daten für das Ausführen der statischen Calls
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testpermission");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Set<UserPermission> permissions = new HashSet<>(Arrays.asList(UserPermission.CAN_MODIFY_MY_WETTKAMPF, UserPermission.CAN_CREATE_STAMMDATEN));
        Mockito.doReturn(permissions).when(jwtTokenProvider).getPermissions("testpermission");
        Mockito.doReturn(1L).when(jwtTokenProvider).getUserId(anyString());

        WettkampfDO wettkampfDO = getWettkampfDO(W_id);
        ArrayList<WettkampfDO> wettkaempfeDOS = new ArrayList<>();
        wettkaempfeDOS.add(wettkampfDO);

        when(wettkampfComponent.findByAusrichter(anyLong())).thenReturn(wettkaempfeDOS);

        assertTrue(underTest.hasSpecificPermissionAusrichter(UserPermission.CAN_MODIFY_MY_WETTKAMPF,W_id));
        assertFalse(underTest.hasSpecificPermissionAusrichter(UserPermission.CAN_MODIFY_MY_WETTKAMPF,1L));
    }

    @Test
    public void testHasSpecificPermissionSportleiter() {
        // wir mocken keine statischen Aufrufe,
        // sondern erzeugen uns valide Daten für das Ausführen der statischen Calls
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testpermission");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Set<UserPermission> permissions = new HashSet<>(Arrays.asList(UserPermission.CAN_MODIFY_MY_VEREIN, UserPermission.CAN_CREATE_STAMMDATEN));
        Mockito.doReturn(permissions).when(jwtTokenProvider).getPermissions("testpermission");
        Mockito.doReturn(1L).when(jwtTokenProvider).getUserId(anyString());

        UserDO userDO = getUserDO(55L, 1055L);
        when(userComponent.findById(anyLong())).thenReturn(userDO);
        DsbMitgliedDO dsbmigliedDO = getDsbMitgliedDO(1055L, V_id);
        when(dsbMitgliedComponent.findById(anyLong())).thenReturn(dsbmigliedDO);

        assertTrue(underTest.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN,V_id));
        assertFalse(underTest.hasSpecificPermissionSportleiter(UserPermission.CAN_MODIFY_MY_VEREIN,1L));
    }

    /* TODO: Implement this test */
//    @Test
//    public void getCurrentMethod() {}
}