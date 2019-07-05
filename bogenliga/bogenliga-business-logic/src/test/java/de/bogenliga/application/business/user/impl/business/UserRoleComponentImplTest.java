package de.bogenliga.application.business.user.impl.business;

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
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private static final String EMAIL = "test@test.net";
    private static final String ROLE_NAME = "TEST_USER";
    private static final Long VERSION = 2L;
    private static final Long NEWVERSION = 3L;
    private static final Long USER = 1L;
    private static final long ID = 9999;

    private static final long ROLE_DEFAULT = 3;
    private static final String ROLE_NAME_DEFAULT = "USER";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UserRoleExtDAO userRoleExtDAO;
    @Mock
    private RoleDAO roleDAO;

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


}