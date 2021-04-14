package de.bogenliga.application.springconfiguration.security.permissions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.Assert;

import org.aspectj.lang.ProceedingJoinPoint;

import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.MockitoJUnit;

import de.bogenliga.application.springconfiguration.security.types.UserPermission;

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
        Assert.assertEquals(false, underTest.hasPermission(userPermission));
    }

    /* TODO: Implement this test */
//    @Test
//    public void getCurrentMethod() {}
}