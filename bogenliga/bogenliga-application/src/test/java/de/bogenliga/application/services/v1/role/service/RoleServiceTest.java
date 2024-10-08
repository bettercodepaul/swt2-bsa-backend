package de.bogenliga.application.services.v1.role.service;

import de.bogenliga.application.business.role.api.RoleComponent;
import de.bogenliga.application.business.role.api.types.RoleDO;
import de.bogenliga.application.services.v1.role.model.RoleDTO;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, BettercallPaul gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class RoleServiceTest {

    private static final Long ID1 = 1L;
    private static final Long ID2 = 2L;
    private static final String ROLE1 = "Admin";
    private static final String ROLE2 = "USER";

    private static final RoleDO role1DO = new RoleDO();
    private static final RoleDO role2DO = new RoleDO();


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private RoleComponent roleComponent;


    @InjectMocks
    private RoleService underTest;



    @Test
    public void getUserProfileById() {

        // prepare test data
        role1DO.setId(ID1);
        role2DO.setId(ID2);
        role1DO.setRoleName(ROLE1);
        role2DO.setRoleName(ROLE2);

        final List<RoleDO> roleDOList = Arrays.asList( role1DO, role2DO);


        // configure mocks
        when(roleComponent.findAll()).thenReturn(roleDOList);

        // call test method
        final List<RoleDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.get(0).getId() ).isEqualTo(ID1);
        assertThat(actual.get(0).getRoleName() ).isEqualTo(ROLE1);
        assertThat(actual.get(1).getId() ).isEqualTo(ID2);
        assertThat(actual.get(1).getRoleName() ).isEqualTo(ROLE2);

    }
}