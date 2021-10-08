package de.bogenliga.application.business.user.impl.business;

import de.bogenliga.application.business.configuration.api.ConfigurationComponent;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.business.user.api.types.UserRoleDO;
import de.bogenliga.application.business.user.impl.businessactivity.PasswordHashingBA;
import de.bogenliga.application.business.user.impl.businessactivity.SignInBA;
import de.bogenliga.application.business.user.impl.businessactivity.TechnicalUserBA;
import de.bogenliga.application.business.role.impl.entity.RoleBE;
import de.bogenliga.application.business.role.impl.dao.RoleDAO;
import de.bogenliga.application.business.user.impl.dao.UserRoleDAO;
import de.bogenliga.application.business.user.impl.dao.UserRoleExtDAO;
import de.bogenliga.application.business.user.impl.entity.UserRoleBE;
import de.bogenliga.application.business.user.impl.entity.UserRoleExtBE;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.annotation.HttpMethodConstraint;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class UserRoleComponentImplTest {
    private static final long ROLE_ID = 777;
    private static final long ROLE_ROLE_ID = 777;
    private static final String EMAIL = "test@test.net";
    private static final String ROLE_NAME = "TEST_USER";
    private static final Long VERSION = 2L;
    private static final Long NEWVERSION = 3L;
    private static final Long USER = 1L;
    private static final long ID = 9999;
    private static final String FEEDBACK = "TEST";
    private static final String KEY_1 = "SMTPHost";
    private static final String KEY_2 = "SMTPPasswort";
    private static final String KEY_3 = "SMTPBenutzer";
    private static final String KEY_4 = "SMTPEmail";
    private static final String KEY_5 = "SMTPPort";
    private static final String VALUE = "TEST_VALUE";


    private static final long ROLE_DEFAULT = 3;
    private static final String ROLE_NAME_DEFAULT = "USER";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UserRoleExtDAO userRoleExtDAO;
    @Mock
    private RoleDAO roleDAO;

    @Mock
    private ConfigurationDO configurationDO;
    @Mock
    private ConfigurationComponent configurationComponent;

    @InjectMocks
    private UserRoleComponentImpl underTest;

    @Captor
    private ArgumentCaptor<UserRoleExtBE> userRoleExtBEArgumentCaptor;

    @Captor
    private ArgumentCaptor<UserRoleBE> userRoleBEArgumentCaptor;



    @Test
    public void findAll() {
        // prepare test data
        final UserRoleExtBE expectedBE = new UserRoleExtBE();
        expectedBE.setUserId(ID);
        expectedBE.setRoleId(ROLE_ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setRoleName(ROLE_NAME);

        // configure mocks
        when(userRoleExtDAO.findAll()).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<UserRoleDO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.get(0).getEmail())
                .isEqualTo(expectedBE.getUserEmail());
        assertThat(actual.get(0).getRoleId())
                .isEqualTo(expectedBE.getRoleId());
        assertThat(actual.get(0).getRoleName())
                .isEqualTo(expectedBE.getRoleName());
        assertThat(actual.get(0).getVersion())
                .isEqualTo(expectedBE.getVersion());


        // verify invocations
        verify(userRoleExtDAO).findAll();


    }


    // tests for findById
    @Test
    public void findById() {
        // prepare test data
        List<UserRoleExtBE> userRoleExtBEList = new ArrayList<>();
        final UserRoleExtBE expectedBE = new UserRoleExtBE();
        expectedBE.setUserId(ID);
        expectedBE.setRoleId(ROLE_ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setRoleName(ROLE_NAME);
        userRoleExtBEList.add(expectedBE);

        // configure mocks
        when(userRoleExtDAO.findById(anyLong())).thenReturn(userRoleExtBEList);

        // call test method
        final List<UserRoleDO> actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.get(0).getEmail())
                .isEqualTo(expectedBE.getUserEmail());
        assertThat(actual.get(0).getRoleId())
                .isEqualTo(expectedBE.getRoleId());
        assertThat(actual.get(0).getRoleName())
                .isEqualTo(expectedBE.getRoleName());
        assertThat(actual.get(0).getVersion())
                .isEqualTo(expectedBE.getVersion());

        // verify invocations
        verify(userRoleExtDAO).findById(ID);
    }

    @Test
    public void findById_withoutId_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findById(null))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verify(userRoleExtDAO, never()).findById(anyLong());
    }

    @Test
    public void findById_withInvalidId_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findById(-1L))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verify(userRoleExtDAO, never()).findById(anyLong());
    }

    @Test
    public void findById_notResult() {
        // prepare test data

        // configure mocks
        when(userRoleExtDAO.findById(anyLong())).thenReturn(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findById(ID))
                .withMessageContaining("No result found")
                .withNoCause();

        // assert result


        // verify invocations
        verify(userRoleExtDAO).findById(ID);
    }


    // tests for findByRoleId
    @Test
    public void findByRoleId_IfSuccess() {
        // prepare test data
        final UserRoleExtBE userRole = new UserRoleExtBE();
        userRole.setRoleId(ROLE_ID);
        userRole.setUserEmail(EMAIL);

        List<UserRoleExtBE> userRoleList = new ArrayList<>();
        userRoleList.add(userRole);

        // configure mocks
        when(userRoleExtDAO.findAll()).thenReturn(userRoleList);

        // call test method
        final List<UserRoleDO> actual = underTest.findByRoleId(ROLE_ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getEmail())
                .isEqualTo(userRole.getUserEmail());
        assertThat(actual.get(0).getRoleId())
                .isEqualTo(userRole.getRoleId());

        // verify invocations
        verify(userRoleExtDAO).findAll();
    }

    @Test
    public void findByRoleId_IfFail() {
        // prepare test data
        final UserRoleExtBE userRole = new UserRoleExtBE();
        userRole.setRoleId(1L);
        userRole.setUserEmail(EMAIL);

        List<UserRoleExtBE> userRoleList = new ArrayList<>();
        userRoleList.add(userRole);

        // configure mocks
        when(userRoleExtDAO.findAll()).thenReturn(userRoleList);

        // call test method
        final List<UserRoleDO> actual = underTest.findByRoleId(2L);

        // assert result
        assertThat(actual).isNotNull().isEmpty();

        // verify invocations
        verify(userRoleExtDAO).findAll();
    }

    @Test
    public void findByRoleId_withoutRoleId_shouldThrowException() {
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findByRoleId(null))
                .withMessageContaining("must not be null")
                .withNoCause();

        // verify invocations
        verify(userRoleExtDAO, never()).findAll();
    }

    @Test
    public void findByRoleId_withInvalidRoleId_shouldThrowException() {
        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findByRoleId(-1L))
                .withMessageContaining("must not be null")
                .withNoCause();

        // verify invocations
        verify(userRoleExtDAO, never()).findAll();
    }

    @Test
    public void findByRoleId_notResult() {
        // configure mocks
        when(userRoleExtDAO.findAll()).thenReturn(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findByRoleId(ROLE_ID))
                .withMessageContaining("No result found")
                .withNoCause();

        // verify invocations
        verify(userRoleExtDAO).findAll();
    }


    // tests for findByEmail
    @Test
    public void findByEmail() {
        // prepare test data
        final UserRoleExtBE expectedBE = new UserRoleExtBE();
        expectedBE.setUserId(ID);
        expectedBE.setRoleId(ROLE_ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setRoleName(ROLE_NAME);

        // configure mocks
        when(userRoleExtDAO.findByEmail(anyString())).thenReturn(expectedBE);


        // call test method
        final UserRoleDO actual = underTest.findByEmail(EMAIL);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getEmail())
                .isEqualTo(expectedBE.getUserEmail());
        assertThat(actual.getRoleId())
                .isEqualTo(expectedBE.getRoleId());
        assertThat(actual.getRoleName())
                .isEqualTo(expectedBE.getRoleName());
        assertThat(actual.getVersion())
                .isEqualTo(expectedBE.getVersion());

        // verify invocations
        verify(userRoleExtDAO).findByEmail(EMAIL);
    }

    @Test
    public void findByEmail_withoutEmail_shouldThrowException() {
        // prepare test data

        // configure mocks

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findByEmail(null))
                .withMessageContaining("must not be null")
                .withNoCause();

        // assert result

        // verify invocations
        verify(userRoleExtDAO, never()).findById(anyLong());
    }

    @Test
    public void findByEmail_notResult() {
        // prepare test data

        // configure mocks
        when(userRoleExtDAO.findByEmail(anyString())).thenReturn(null);

        // call test method
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.findByEmail(EMAIL))
                .withMessageContaining("No result found")
                .withNoCause();

        // assert result


        // verify invocations
        verify(userRoleExtDAO).findByEmail(EMAIL);
    }


    @Test
    public void signIn() {
        // prepare test data

        // configure mocks

        // call test method

        // assert result

        // verify invocations
    }


    // tests for create
    @Test
    public void create_UserIdnotNull(){

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create( null, ID))
                .withMessageContaining("must not be null")
                .withNoCause();

    }

    @Test
    public void create_CurrentUserIdnotNull(){

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(ID, null))
                .withMessageContaining("must not be null")
                .withNoCause();

    }

    @Test
    public void create_RoleDefault_sucessful(){

        final OffsetDateTime dateTime = OffsetDateTime.now();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final UserRoleBE expectedBE = new UserRoleBE();
        expectedBE.setUserId(ID);
        expectedBE.setRoleId(ROLE_DEFAULT);
        expectedBE.setVersion(VERSION);
        expectedBE.setCreatedAtUtc(timestamp);
        expectedBE.setLastModifiedAtUtc(timestamp);
        expectedBE.setCreatedByUserId(USER);
        expectedBE.setLastModifiedByUserId(USER);

        final RoleBE roleBE = new RoleBE();
        roleBE.setRoleId(ROLE_DEFAULT);
        roleBE.setRoleName(ROLE_NAME_DEFAULT);


        // configure mocks
        when(roleDAO.findByName(anyString())).thenReturn(roleBE);
        when(userRoleExtDAO.create(any(UserRoleBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final UserRoleDO actual =  underTest.create(ID, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getRoleId())
                .isEqualTo(expectedBE.getRoleId());
        assertThat(actual.getVersion())
                .isEqualTo(expectedBE.getVersion());

        // verify invocations
        verify(userRoleExtDAO).create(userRoleBEArgumentCaptor.capture(), anyLong());

    }

    @Test
    public void create_Role_UserIdnotNull(){

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create( null, ROLE_ID, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

    }

    @Test
    public void create_Role_RoleIdnotNull(){

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(ID, null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

    }

    @Test
    public void create_Role_CurrentUserIdnotNull(){

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.create(ID, ROLE_ID, null))
                .withMessageContaining("must not be null")
                .withNoCause();

    }

    @Test
    public void create_Role_sucessful(){

        final OffsetDateTime dateTime = OffsetDateTime.now();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final UserRoleBE expectedBE = new UserRoleBE();
        expectedBE.setUserId(ID);
        expectedBE.setRoleId(ROLE_ID);
        expectedBE.setVersion(VERSION);
        expectedBE.setCreatedAtUtc(timestamp);
        expectedBE.setLastModifiedAtUtc(timestamp);
        expectedBE.setCreatedByUserId(USER);
        expectedBE.setLastModifiedByUserId(USER);

        // configure mocks
        when(userRoleExtDAO.create(any(UserRoleBE.class), anyLong())).thenReturn(expectedBE);

        // call test method
        final UserRoleDO actual =  underTest.create(ID, ROLE_ID, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getRoleId())
                .isEqualTo(expectedBE.getRoleId());
        assertThat(actual.getVersion())
                .isEqualTo(expectedBE.getVersion());

        // verify invocations
        verify(userRoleExtDAO).create(userRoleBEArgumentCaptor.capture(), anyLong());

    }


    // tests for update
    @Test
    public void update_UserRoleDO_notNull(){

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(null, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

    }

    @Test
    public void update_UserDO_ID_notneagtive(){

        final List<UserRoleDO> userRoleDOList = new ArrayList<>();
        final UserRoleDO inputDO = new UserRoleDO();
        inputDO.setId(null);
        inputDO.setRoleId(ROLE_ID);
        inputDO.setEmail(EMAIL);
        inputDO.setRoleName(ROLE_NAME);
        inputDO.setVersion(VERSION);
        userRoleDOList.add(inputDO);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(userRoleDOList, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

    }

    @Test
    public void update_UserDO_RoleID_notneagtive(){

        final List<UserRoleDO> userRoleDOList = new ArrayList<>();
        final UserRoleDO inputDO = new UserRoleDO();
        inputDO.setId(ID);
        inputDO.setRoleId(null);
        inputDO.setEmail(EMAIL);
        inputDO.setRoleName(ROLE_NAME);
        inputDO.setVersion(VERSION);
        userRoleDOList.add(inputDO);

        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> underTest.update(userRoleDOList, USER))
                .withMessageContaining("must not be null")
                .withNoCause();

    }


    @Test
    public void sendFeedback() {
        // Test Object
        final UserRoleExtBE expectedBE = new UserRoleExtBE();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        expectedBE.setUserId(ID);
        expectedBE.setRoleId(ROLE_ID);
        expectedBE.setVersion(VERSION);
        expectedBE.setCreatedAtUtc(timestamp);
        expectedBE.setLastModifiedAtUtc(timestamp);
        expectedBE.setCreatedByUserId(USER);
        expectedBE.setLastModifiedByUserId(USER);
        expectedBE.setUserEmail(EMAIL);
        final List<UserRoleExtBE> UserRoleBEList = Collections.singletonList(expectedBE);

        final ConfigurationDO configurationDO_1 = new ConfigurationDO();
        configurationDO_1.setKey(KEY_1);
        configurationDO_1.setValue(VALUE);
        final ConfigurationDO configurationDO_2 = new ConfigurationDO();
        configurationDO_2.setKey(KEY_2);
        configurationDO_2.setValue(VALUE);
        final ConfigurationDO configurationDO_3 = new ConfigurationDO();
        configurationDO_3.setKey(KEY_3);
        configurationDO_3.setValue(VALUE);
        final ConfigurationDO configurationDO_4 = new ConfigurationDO();
        configurationDO_4.setKey(KEY_4);
        configurationDO_4.setValue(VALUE);
        final ConfigurationDO configurationDO_5 = new ConfigurationDO();
        configurationDO_5.setKey(KEY_5);
        configurationDO_5.setValue(VALUE);
        final List<ConfigurationDO> configurationDOList = new ArrayList<>();
        configurationDOList.add(configurationDO_1);
        configurationDOList.add(configurationDO_2);
        configurationDOList.add(configurationDO_3);
        configurationDOList.add(configurationDO_4);
        configurationDOList.add(configurationDO_5);

        // Mock method calls
        when(userRoleExtDAO.findAdminEmails()).thenReturn(UserRoleBEList);
        when(configurationComponent.findAll()).thenReturn(configurationDOList);

        // Call test method
        underTest.sendFeedback(FEEDBACK);
        // Verify call
        verify(userRoleExtDAO).findAdminEmails();
        verify(configurationComponent).findAll();


    }

}