package de.bogenliga.application.business.user.impl.business;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.impl.businessactivity.SignInBA;
import de.bogenliga.application.business.user.impl.businessactivity.TechnicalUserBA;
import de.bogenliga.application.business.user.impl.dao.UserDAO;
import de.bogenliga.application.business.user.impl.entity.UserBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class UserComponentImplTest {
    private static final Long ID = 1L;
    private static final Long VERSION = 2L;
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final Long USER = 0L;
    private static final UserDO SYSTEM_USER = new UserDO(0L, "SYSTEM", null, 0L, null, 0L, 0L);


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UserDAO userDAO;
    @Mock
    private SignInBA signInBA;
    @Mock
    private TechnicalUserBA technicalUserBA;

    @InjectMocks
    private UserComponentImpl underTest;

    @Captor
    private ArgumentCaptor<UserBE> userBEArgumentCaptor;


    @Test
    public void findById() {
        // prepare test data
        final UserBE expectedBE = new UserBE();
        expectedBE.setUserId(ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setVersion(VERSION);


        // configure mocks
        when(userDAO.findById(anyLong())).thenReturn(expectedBE);

        // call test method
        final UserDO actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getEmail())
                .isEqualTo(expectedBE.getUserEmail());
        assertThat(actual.getVersion())
                .isEqualTo(expectedBE.getVersion());

        // verify invocations
        verify(userDAO).findById(ID);
    }


    @Test
    public void findById_withoutId_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findById(null))
                .withMessageContaining("must not be negative")
                .withNoCause();

        // assert result

        // verify invocations
        verify(userDAO, never()).findById(anyLong());
    }


    @Test
    public void findById_withInvalidId_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findById(-1L))
                .withMessageContaining("must not be negative")
                .withNoCause();

        // assert result

        // verify invocations
        verify(userDAO, never()).findById(anyLong());
    }


    @Test
    public void findById_withTechnicalUserId() {
        // prepare test data
        final UserDO userDO = new UserDO();
        userDO.setEmail(EMAIL);
        userDO.setId(ID);

        // configure mocks
        when(technicalUserBA.getSystemUser()).thenReturn(userDO);

        // call test method
        final UserDO actual = underTest.findById(0L);

        // assert result
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);

        // verify invocations
        verify(userDAO, never()).findById(anyLong());
        verify(technicalUserBA).getSystemUser();
    }


    @Test
    public void findById_notResult() {
        // prepare test data

        // configure mocks
        when(userDAO.findById(anyLong())).thenReturn(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findById(ID))
                .withMessageContaining("NOT_FOUND")
                .withNoCause();

        // assert result


        // verify invocations
        verify(userDAO).findById(ID);
    }

    @Test
    public void findByEmail() {
        // prepare test data
        final UserBE expectedBE = new UserBE();
        expectedBE.setUserId(ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setVersion(VERSION);


        // configure mocks
        when(userDAO.findByEmail(anyString())).thenReturn(expectedBE);

        // call test method
        final UserDO actual = underTest.findByEmail(EMAIL);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getEmail())
                .isEqualTo(expectedBE.getUserEmail());
        assertThat(actual.getVersion())
                .isEqualTo(expectedBE.getVersion());

        // verify invocations
        verify(userDAO).findByEmail(EMAIL);
    }



    @Test
    public void signIn() {
        // prepare test data

        // configure mocks

        // call test method

        // assert result

        // verify invocations
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
        // prepare test data
        // configure mocks

        // call test method
        final boolean actual = underTest.isTechnicalUser(SYSTEM_USER);

        // assert result
        assertThat(actual).isFalse();

        // verify invocations
    }
}