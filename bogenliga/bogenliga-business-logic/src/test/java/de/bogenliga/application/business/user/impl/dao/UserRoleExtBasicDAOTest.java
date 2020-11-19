package de.bogenliga.application.business.user.impl.dao;

import de.bogenliga.application.business.user.impl.entity.UserRoleBE;
import de.bogenliga.application.business.user.impl.entity.UserRoleExtBE;
import de.bogenliga.application.common.component.dao.BasicDAO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Yann Philippczyk, eXXcellent solutions consulting & software gmbh
 * @see <a href="http://joel-costigliola.github.io/assertj/">
 * AssertJ: Fluent assertions for java</a>
 * @see <a href="https://junit.org/junit4/">
 * JUnit4</a>
 * @see <a href="https://site.mockito.org/">
 * Mockito</a>
 * @see <a href="http://www.vogella.com/tutorials/Mockito/article.html">Using Mockito with JUnit 4</a>
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class UserRoleExtBasicDAOTest {

    private static final long ID = 9999;
    private static final long ID_2 = 9999;
    private static final long ROLE_ID = 777;
    private static final long ROLE_ID2 = 777;
    private static final String EMAIL = "test@test.net";
    private static final String ROLE_NAME = "TEST_USER";
    private static final long USER = 0;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private UserRoleExtDAO underTest;



    @Test
    public void findAll() {
        // prepare test data
        final UserRoleExtBE expectedBE = new UserRoleExtBE();
        expectedBE.setUserId(ID);
        expectedBE.setRoleId(ROLE_ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setRoleName(ROLE_NAME);

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<UserRoleExtBE> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(actual.get(0)).isNotNull();

        assertThat(actual.get(0).getUserId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.get(0).getUserEmail())
                .isEqualTo(expectedBE.getUserEmail());

        assertThat(actual.get(0).getRoleId())
                .isEqualTo(expectedBE.getRoleId());
        assertThat(actual.get(0).getRoleName())
                .isEqualTo(expectedBE.getRoleName());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());


    }


    @Test
    public void findById() {
        // prepare test data
        List<UserRoleExtBE> userRoleExtBEList = new ArrayList<>();
        UserRoleExtBE expectedBE = new UserRoleExtBE();
        expectedBE.setUserId(ID);
        expectedBE.setRoleId(ROLE_ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setRoleName(ROLE_NAME);

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(expectedBE));

        // call test method
        final List<UserRoleExtBE> actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.get(0).getUserId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.get(0).getUserEmail())
                .isEqualTo(expectedBE.getUserEmail());

        assertThat(actual.get(0).getRoleId())
                .isEqualTo(expectedBE.getRoleId());
        assertThat(actual.get(0).getRoleName())
                .isEqualTo(expectedBE.getRoleName());

        // verify invocations
        verify(basicDao).selectEntityList(any(), any(), any());
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
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final UserRoleExtBE actual = underTest.findByEmail(EMAIL);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getUserId())
                .isEqualTo(expectedBE.getUserId());
        assertThat(actual.getUserEmail())
                .isEqualTo(expectedBE.getUserEmail());

        assertThat(actual.getRoleId())
                .isEqualTo(expectedBE.getRoleId());
        assertThat(actual.getRoleName())
                .isEqualTo(expectedBE.getRoleName());

        // verify invocations
        verify(basicDao).selectSingleEntity(any(), any(), any());
    }

    @Test
    public void findAdminEmails() {
        // prepare test data
        final UserRoleExtBE expectedBE = new UserRoleExtBE();
        expectedBE.setUserId(ID);
        expectedBE.setRoleId(ROLE_ID);
        expectedBE.setUserEmail(EMAIL);
        expectedBE.setRoleName(ROLE_NAME);
        final UserRoleExtBE expectedBE2 = new UserRoleExtBE();
        expectedBE2.setUserId(ID_2);
        expectedBE2.setRoleId(ROLE_ID2);
        expectedBE2.setUserEmail(EMAIL);
        expectedBE2.setRoleName(ROLE_NAME);


        // configure mocks
        when(basicDao.selectSingleEntity(any(), any(), any())).thenReturn(expectedBE);

        // call test method
        final List <UserRoleExtBE> actual = underTest.findAdminEmails();

        // assert result


        for(int i=0;i<actual.size();i++){

            assertThat(actual.get(i).getUserId())
                    .isEqualTo(expectedBE.getUserId());
            assertThat(actual.get(i).getUserEmail())
                    .isEqualTo(expectedBE.getUserEmail());

            assertThat(actual.get(i).getRoleId())
                    .isEqualTo(expectedBE.getRoleId());
            assertThat(actual.get(i).getRoleName())
                    .isEqualTo(expectedBE.getRoleName());


        }
        assertThat(actual).isNotNull();



  
    }






}