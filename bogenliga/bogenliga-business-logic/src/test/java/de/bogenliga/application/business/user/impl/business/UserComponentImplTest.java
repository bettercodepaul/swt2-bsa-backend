package de.bogenliga.application.business.user.impl.business;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.user.impl.businessactivity.SignInBA;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class UserComponentImplTest {
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UserDAO userDAO;
    @Mock
    private SignInBA signInBA;

    @InjectMocks
    private UserComponentImpl underTest;


    @Test
    public void findAll() {
    }


    @Test
    public void findById() {
    }


    @Test
    public void findByEmail() {
    }


    @Test
    public void create() {
    }


    @Test
    public void update() {
    }


    @Test
    public void delete() {
    }


    @Test
    public void signIn() {
    }


    @Test
    public void signInUser_withoutEmail_shouldThrowException() {
        // prepare test data
        // configure mocks
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.signIn(null, PASSWORD))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verify(signInBA, never()).signInUser(any(), any());
    }


    @Test
    public void signInUser_withEmptyEmail_shouldThrowException() {
        // prepare test data
        // configure mocks
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.signIn("", PASSWORD))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verify(signInBA, never()).signInUser(any(), any());
    }


    @Test
    public void signInUser_withoutPassword_shouldThrowException() {
        // prepare test data
        // configure mocks
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.signIn(EMAIL, null))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verify(signInBA, never()).signInUser(any(), any());
    }


    @Test
    public void signInUser_withEmptyPassword_shouldThrowException() {
        // prepare test data
        // configure mocks
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.signIn(EMAIL, ""))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verify(signInBA, never()).signInUser(any(), any());
    }


    @Test
    public void isTechnicalUser() {
    }
}