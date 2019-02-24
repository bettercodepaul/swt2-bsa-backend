package de.bogenliga.application.services.v1.role.service;

import de.bogenliga.application.business.role.api.RoleComponent;
import de.bogenliga.application.business.role.api.types.RoleDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.services.common.errorhandling.ErrorDTO;
import de.bogenliga.application.services.v1.role.model.RoleDTO;
import de.bogenliga.application.springconfiguration.security.WebSecurityConfiguration;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class RoleServiceTest {

    private static final Long ID = 123L;

    private static final Long ID1 = 1L;
    private static final Long ID2 = 2L;
    private static final String ROLE1 = "Admin";
    private static final String ROLE2 = "USER";

    private static final RoleDO role1DO = new RoleDO();
    private static final RoleDO role2DO = new RoleDO();


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private WebSecurityConfiguration webSecurityConfiguration;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;
    @Mock
    private HttpServletRequest requestWithHeader;
    @Mock
    private RoleComponent roleComponent;


    @InjectMocks
    private RoleService underTest;

    @Captor
    private ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationTokenArgumentCaptor;



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