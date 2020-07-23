package de.bogenliga.application.springconfiguration.security.jsonwebtoken;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.common.configuration.SecurityJsonWebTokenConfiguration;
import de.bogenliga.application.springconfiguration.security.authentication.UserAuthenticationProvider;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class JwtTokenProviderTest {
    private static final String REFRESH_COUNTER = "refreshCounter";
    private static final String USER_INFO = "usr";
    private static final String USER_INFO_VERSION = "version";
    private static final String USER_INFO_ID = "id";
    private static final String AUTH = "auth";
    private static final Long ID = 123L;
    private static final Long VERSION = 1L;
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String ERROR_MESSAGE = "error";
    private static final List<UserPermission> PERMISSIONS = Arrays.asList(
            UserPermission.CAN_READ_SPORTJAHR,
            UserPermission.CAN_READ_STAMMDATEN);

    private static final long EXPIRATION_TIME = 1000000000L;
    private static final String SECRET = "secret-key";
    private static final int REFRESH_TIME = 2;
    private static final String JWT = generateJwtToken();
    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private HttpServletRequest servletRequest;
    @Mock
    private HttpServletResponse servletResponse;
    @Mock
    private FilterChain filterChain;
    @Mock
    private Authentication authentication;
    @Mock
    private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

    @Mock
    private SecurityJsonWebTokenConfiguration securityJsonWebTokenConfiguration;
    @Mock
    private UserAuthenticationProvider userAuthenticationProvider;
    @InjectMocks
    private JwtTokenProvider underTest;


    private static String generateJwtToken() {
        // subject
        final Claims claims = Jwts.claims().setSubject(USERNAME);

        // add permissions
        claims.put(AUTH, PERMISSIONS.stream()
                .map(UserPermission::getAuthority)
                .collect(Collectors.toList()));

        // add user info
        final Map<String, String> userInfo = new HashMap<>();
        userInfo.put(USER_INFO_VERSION, String.valueOf(VERSION));
        userInfo.put(USER_INFO_ID, String.valueOf(ID));

        claims.put(USER_INFO, userInfo);

        // add refresh counter
        claims.put(REFRESH_COUNTER, REFRESH_TIME);

        // expiration time
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + EXPIRATION_TIME);


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(SECRET.getBytes()))
                .compact();
    }


    @Before
    public void initConfig() {
        when(securityJsonWebTokenConfiguration.getExpiration()).thenReturn(EXPIRATION_TIME);
        when(securityJsonWebTokenConfiguration.getSecret()).thenReturn(SECRET);
        when(securityJsonWebTokenConfiguration.getRefresh()).thenReturn(REFRESH_TIME);

        underTest.init();
    }


    @Test
    public void resolveToken() {
        // prepare test data

        // configure mocks
        when(servletRequest.getHeader(AUTHORIZATION_HEADER)).thenReturn("Bearer " + JWT);

        // call test method
        final String actual = JwtTokenProvider.resolveToken(servletRequest);

        // assert result
        assertThat(actual).isEqualTo(JWT);

        // verify invocations
    }


    @Test
    public void resolveToken_withoutBearer() {
        // prepare test data

        // configure mocks
        when(servletRequest.getHeader(AUTHORIZATION_HEADER)).thenReturn("Basic ");

        // call test method
        final String actual = JwtTokenProvider.resolveToken(servletRequest);

        // assert result
        assertThat(actual).isNull();

        // verify invocations
    }


    @Test
    public void resolveToken_withoutAuthHeader() {
        // prepare test data

        // configure mocks
        when(servletRequest.getHeader(AUTHORIZATION_HEADER)).thenReturn(null);

        // call test method
        final String actual = JwtTokenProvider.resolveToken(servletRequest);

        // assert result
        assertThat(actual).isNull();

        // verify invocations
    }


    @Test
    public void getUsername() {
        // prepare test data

        // configure mocks

        // call test method
        final String actual = underTest.getUsername(JWT);

        // assert result
        assertThat(actual).isEqualTo(USERNAME);

        // verify invocations
    }


    @Test
    public void resolveUserSignInDTO() {
    }


    @Test
    public void getPermissions() {
        // prepare test data

        // configure mocks

        // call test method
        final Set<UserPermission> actual = underTest.getPermissions(JWT);

        // assert result
        assertThat(actual).containsAll(PERMISSIONS);

        // verify invocations
    }


    @Test
    public void createToken() {
        // prepare test data
        final UserWithPermissionsDO userWithPermissionsDO = new UserWithPermissionsDO();
        userWithPermissionsDO.setId(ID);
        userWithPermissionsDO.setEmail(USERNAME);
        userWithPermissionsDO.setPermissions(
                PERMISSIONS.stream().map(UserPermission::name).collect(Collectors.toList()));

        final UserDO userDO = new UserDO();
        userDO.setDsb_mitglied_id(1L);

        final DsbMitgliedDO dsbMitgliedDO = new DsbMitgliedDO(1L);
        dsbMitgliedDO.setVereinsId(1L);

        final VeranstaltungDO veranstaltungDO1 = new VeranstaltungDO(1L);
        final VeranstaltungDO veranstaltungDO2 = new VeranstaltungDO(2L);
        final VeranstaltungDO veranstaltungDO3 = new VeranstaltungDO(3L);

        // configure mocks
        when(authentication.getPrincipal()).thenReturn(userWithPermissionsDO);

        // call test method
        final String actual = underTest.createToken(authentication);

        // assert result

        // verify invocations
    }


    @Test
    public void getAuthentication() {
        // prepare test data
        final UserWithPermissionsDO userWithPermissionsDO = new UserWithPermissionsDO();
        userWithPermissionsDO.setId(ID);
        userWithPermissionsDO.setEmail(USERNAME);
        userWithPermissionsDO.setPermissions(
                PERMISSIONS.stream().map(UserPermission::name).collect(Collectors.toList()));

        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);

        // configure mocks
        when(authentication.getPrincipal()).thenReturn(userWithPermissionsDO);
        when(userAuthenticationProvider.createAuthenticationPlaceholder(USERNAME, new HashSet<>(PERMISSIONS)))
                .thenReturn(usernamePasswordAuthenticationToken);

        // call test method
        final Authentication actual = underTest.getAuthentication(JWT);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getPrincipal()).isNotNull().isInstanceOf(String.class).isEqualTo(USERNAME);
        assertThat(actual.getCredentials()).isNotNull().isInstanceOf(String.class).isEqualTo(PASSWORD);

        // verify invocations
    }


    @Test
    public void getOptionsAuthentication() {
        // prepare test data
        final UserWithPermissionsDO userWithPermissionsDO = new UserWithPermissionsDO();
        userWithPermissionsDO.setId(ID);
        userWithPermissionsDO.setEmail(USERNAME);
        userWithPermissionsDO.setPermissions(
                PERMISSIONS.stream().map(UserPermission::name).collect(Collectors.toList()));

        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);

        // configure mocks
        when(userAuthenticationProvider.createAuthenticationPlaceholder(any(), any()))
                .thenReturn(usernamePasswordAuthenticationToken);

        // call test method
        final Authentication actual = underTest.getOptionsAuthentication();

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getPrincipal()).isNotNull().isInstanceOf(String.class).isEqualTo(USERNAME);
        assertThat(actual.getCredentials()).isNotNull().isInstanceOf(String.class).isEqualTo(PASSWORD);

        // verify invocations
    }


    @Test
    public void validateToken() {
        // prepare test data

        // configure mocks

        // call test method
        final boolean actual = underTest.validateToken(JWT);

        // assert result
        assertThat(actual).isTrue();

        // verify invocations
    }


    @Test
    public void refreshToken() {
        // prepare test data

        // configure mocks

        // call tes t method
        final String actual = underTest.refreshToken(JWT);

        // assert result
        assertThat(actual).isEqualTo(JWT);

        // verify invocations
    }


    @Test
    public void getRefreshCounter() {
    }


    @Test
    public void getRemainingValidityTime() {
    }


    @Test
    public void init() {
    }

}