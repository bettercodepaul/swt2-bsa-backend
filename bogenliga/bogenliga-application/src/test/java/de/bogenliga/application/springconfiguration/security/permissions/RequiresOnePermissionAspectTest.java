package de.bogenliga.application.springconfiguration.security.permissions;

import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Assert;

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

    @InjectMocks
    private RequiresOnePermissionAspect underTest;

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

    /* TODO: Implement this test */
//    @Test
//    public void getCurrentMethod() {}
}