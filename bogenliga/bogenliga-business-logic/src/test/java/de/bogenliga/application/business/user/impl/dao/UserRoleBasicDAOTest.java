package de.bogenliga.application.business.user.impl.dao;

import de.bogenliga.application.business.user.impl.entity.UserRoleBE;
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
import static org.assertj.core.api.Assertions.in;
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
public class UserRoleBasicDAOTest {

    private static final long ID = 9999;
    private static final long ROLE_ID = 777;
    private static final long ROLE_ID2 = 666;
    private static final long USER = 0;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private BasicDAO basicDao;
    @InjectMocks
    private UserRoleDAO underTest;



    @Test
    public void create() {
        // prepare test data
        final UserRoleBE input = new UserRoleBE();
        input.setUserId(ID);
        input.setRoleId(ROLE_ID);

        // configure mocks
        when(basicDao.insertEntity(any(), any())).thenReturn(input);

        // call test method
        final UserRoleBE actual = underTest.create(input, USER);

        // assert result
        assertThat(actual).isNotNull();

        assertThat(actual.getUserId())
                .isEqualTo(input.getUserId());
        assertThat(actual.getRoleId())
                .isEqualTo(input.getRoleId());

        // verify invocations
        verify(basicDao).insertEntity(any(), eq(input));
    }

    @Test
    public void createOrUpdate(){
        // prepare test data
        final UserRoleBE input = new UserRoleBE();
        input.setUserId(ID);
        input.setRoleId(ROLE_ID);

        final UserRoleBE input2 = new UserRoleBE();
        input2.setUserId(ID+1);
        input2.setRoleId(ROLE_ID+1);
        List<UserRoleBE> list = new ArrayList<>();
        list.add(input2);

        // configure mocks
        when(basicDao.selectEntityList(any(), anyString(), any())).thenReturn(Collections.singletonList(input));
        when(basicDao.insertEntity(any(), any())).thenReturn(input);
        doNothing().when(basicDao).setModificationAttributes(any(), anyLong());
        doNothing().when(basicDao).deleteEntity(any(), any(), any());

        // call test method
        List<UserRoleBE> actual = underTest.createOrUpdate(list, 1L);

        // assert result
        assertThat(actual).contains(input);
    }

    @Test
    public void delete() {
        // prepare test data
        final UserRoleBE input = new UserRoleBE();
        input.setUserId(ID);
        input.setRoleId(ROLE_ID2);

        // configure mocks
        when(basicDao.selectEntityList(any(), any(), any())).thenReturn(Collections.singletonList(input));
        // call test method
        underTest.delete(input, USER);

        // assert result

        // verify invocations
        verify(basicDao).deleteEntity(any(), eq(input), any(), any());
    }
}