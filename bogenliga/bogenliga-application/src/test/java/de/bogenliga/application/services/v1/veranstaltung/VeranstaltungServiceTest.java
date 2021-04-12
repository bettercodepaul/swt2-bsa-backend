package de.bogenliga.application.services.v1.veranstaltung;

import de.bogenliga.application.business.sportjahr.api.types.SportjahrDO;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.user.api.types.UserWithPermissionsDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.veranstaltung.impl.entity.VeranstaltungBE;
import de.bogenliga.application.common.configuration.SecurityJsonWebTokenConfiguration;
import de.bogenliga.application.services.v1.sportjahr.SportjahrDTO;
import de.bogenliga.application.services.v1.veranstaltung.model.VeranstaltungDTO;
import de.bogenliga.application.services.v1.veranstaltung.service.VeranstaltungService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import de.bogenliga.application.springconfiguration.security.authentication.UserAuthenticationProvider;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.sql.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;
import static de.bogenliga.application.springconfiguration.security.types.UserPermission.CAN_MODIFY_MY_VERANSTALTUNG;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VeranstaltungServiceTest {

    private static final long USER = 0;
    private static final long ID = 0;
    private static final long VERSION = 0;

    private static final long VERANSTALTUNG_ID = 0;
    private static final long WETTKAMPFTYP_ID = 0;
    private static final String VERANSTALTUNG_NAME = "";
    private static final long SPORTJAHR = 0;
    private static final Date MELDEDEADLINE = new Date(20180211L);
    private static final long LIGALEITERID = 0;
    private static final long LIGAID = 0;
    private static final String LIGALEITER_EMAIL = "";
    private static final String WETTKAMPTYP_NAME = "";
    private static final String LIGANAME = "";

    private static final long SPORTJAHR_ID = 0;
    private static final long SPORTJAHR_JAHR = 0;

    private static RequestAttributes requestAttributes = new RequestAttributes() {
        @Override
        public Object getAttribute(String s, int i) {
            return null;
        }

        @Override
        public void setAttribute(String s, Object o, int i) { }

        @Override
        public void removeAttribute(String s, int i) { }

        @Override
        public String[] getAttributeNames(int i) {
            return new String[0];
        }

        @Override
        public void registerDestructionCallback(String s, Runnable runnable, int i) { }

        @Override
        public Object resolveReference(String s) {
            return null;
        }

        @Override
        public String getSessionId() {
            return null;
        }

        @Override
        public Object getSessionMutex() {
            return null;
        }
    };
    private static HttpServletRequest httpServletRequest = new HttpServletRequest() {
        @Override
        public String getAuthType() {
            return null;
        }

        @Override
        public Cookie[] getCookies() {
            return new Cookie[0];
        }

        @Override
        public long getDateHeader(String s) {
            return 0;
        }

        @Override
        public String getHeader(String s) {
            return null;
        }

        @Override
        public Enumeration<String> getHeaders(String s) {
            return null;
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            return null;
        }

        @Override
        public int getIntHeader(String s) {
            return 0;
        }

        @Override
        public String getMethod() {
            return null;
        }

        @Override
        public String getPathInfo() {
            return null;
        }

        @Override
        public String getPathTranslated() {
            return null;
        }

        @Override
        public String getContextPath() {
            return null;
        }

        @Override
        public String getQueryString() {
            return null;
        }

        @Override
        public String getRemoteUser() {
            return null;
        }

        @Override
        public boolean isUserInRole(String s) {
            return false;
        }

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public String getRequestedSessionId() {
            return null;
        }

        @Override
        public String getRequestURI() {
            return null;
        }

        @Override
        public StringBuffer getRequestURL() {
            return null;
        }

        @Override
        public String getServletPath() {
            return null;
        }

        @Override
        public HttpSession getSession(boolean b) {
            return null;
        }

        @Override
        public HttpSession getSession() {
            return null;
        }

        @Override
        public String changeSessionId() {
            return null;
        }

        @Override
        public boolean isRequestedSessionIdValid() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromCookie() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromURL() {
            return false;
        }

        @Override
        public boolean isRequestedSessionIdFromUrl() {
            return false;
        }

        @Override
        public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
            return false;
        }

        @Override
        public void login(String s, String s1) throws ServletException { }

        @Override
        public void logout() throws ServletException { }

        @Override
        public Collection<Part> getParts() throws IOException, ServletException {
            return null;
        }

        @Override
        public Part getPart(String s) throws IOException, ServletException {
            return null;
        }

        @Override
        public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {
            return null;
        }

        @Override
        public Object getAttribute(String s) {
            return null;
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return null;
        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public void setCharacterEncoding(String s) throws UnsupportedEncodingException { }

        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public long getContentLengthLong() {
            return 0;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public String getParameter(String s) {
            return null;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return null;
        }

        @Override
        public String[] getParameterValues(String s) {
            return new String[0];
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return null;
        }

        @Override
        public String getProtocol() {
            return null;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public String getServerName() {
            return null;
        }

        @Override
        public int getServerPort() {
            return 0;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return null;
        }

        @Override
        public String getRemoteAddr() {
            return null;
        }

        @Override
        public String getRemoteHost() {
            return null;
        }

        @Override
        public void setAttribute(String s, Object o) { }

        @Override
        public void removeAttribute(String s) { }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public Enumeration<Locale> getLocales() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String s) {
            return null;
        }

        @Override
        public String getRealPath(String s) {
            return null;
        }

        @Override
        public int getRemotePort() {
            return 0;
        }

        @Override
        public String getLocalName() {
            return null;
        }

        @Override
        public String getLocalAddr() {
            return null;
        }

        @Override
        public int getLocalPort() {
            return 0;
        }

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public AsyncContext startAsync() throws IllegalStateException {
            return null;
        }

        @Override
        public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
            return null;
        }

        @Override
        public boolean isAsyncStarted() {
            return false;
        }

        @Override
        public boolean isAsyncSupported() {
            return false;
        }

        @Override
        public AsyncContext getAsyncContext() {
            return null;
        }

        @Override
        public DispatcherType getDispatcherType() {
            return null;
        }
    };
    private static ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(httpServletRequest);
    private static String jwtString = " ";
    private static UserComponent userComponent = new UserComponent() {
        @Override
        public List<UserDO> findAll() {
            return null;
        }

        @Override
        public UserDO findById(Long id) {
            return null;
        }

        @Override
        public UserDO findByEmail(String email) {
            return null;
        }

        @Override
        public UserWithPermissionsDO signIn(String email, String password) {
            return null;
        }

        @Override
        public UserDO create(String email, String password, Long dsb_mitglied_id, Long currentUserId, boolean isUsing2FA) {
            return null;
        }

        @Override
        public UserDO updatePassword(UserDO userDO, String password, String newPassword, Long currentUserId) {
            return null;
        }

        @Override
        public UserDO resetPassword(UserDO userDO, String newPassword, Long currentUserId) {
            return null;
        }

        @Override
        public boolean isTechnicalUser(UserDO userDO) {
            return false;
        }

        @Override
        public boolean deactivate(long id) {
            return false;
        }
    };
    private static SecurityJsonWebTokenConfiguration securityJWTConfig = new SecurityJsonWebTokenConfiguration();
    private static UserAuthenticationProvider userAutentProvider = new UserAuthenticationProvider(userComponent);
    private static JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(securityJWTConfig, userAutentProvider);
    private static Set<UserPermission> userPermission = new HashSet<>();



    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private VeranstaltungComponent VeranstaltungComponent;

    @Mock
    private Principal principal;

    @InjectMocks
    private VeranstaltungService underTest;

    @Captor
    private ArgumentCaptor<VeranstaltungDO> VeranstaltungDOArgumentCaptor;
    private JwtTokenProvider JwtTokenProvider;
    private RequestContextHolder RequestContextHolder;


    /***
     * Utility methods for creating business entities/data objects.
     * Also used by other test classes.
     */
    public static VeranstaltungBE VeranstaltungBE() {
        final VeranstaltungBE expectedBE = new VeranstaltungBE();
        expectedBE.setVeranstaltung_id(VERANSTALTUNG_ID);
        expectedBE.setVeranstaltung_liga_id(LIGAID);
        expectedBE.setVeranstaltung_ligaleiter_id(LIGALEITERID);
        expectedBE.setVeranstaltung_meldedeadline(MELDEDEADLINE);
        expectedBE.setVeranstaltung_name(VERANSTALTUNG_NAME);
        expectedBE.setVeranstaltung_sportjahr(SPORTJAHR);
        expectedBE.setVeranstaltung_wettkampftyp_id(WETTKAMPFTYP_ID);

        return expectedBE;
    }

    public static VeranstaltungDO getVeranstaltungDO() {
        return new VeranstaltungDO(
            VERANSTALTUNG_ID ,
            WETTKAMPFTYP_ID ,
            VERANSTALTUNG_NAME ,
            SPORTJAHR ,
            MELDEDEADLINE ,
            LIGALEITERID ,
            LIGAID ,
            LIGALEITER_EMAIL ,
            WETTKAMPTYP_NAME ,
            LIGANAME
        );
    }

    public static VeranstaltungDTO getVeranstaltungDTO() {
        final VeranstaltungDTO veranstaltungDTO = new VeranstaltungDTO();
        veranstaltungDTO.setId(VERANSTALTUNG_ID);
        veranstaltungDTO.setWettkampfTypId(WETTKAMPFTYP_ID  );
        veranstaltungDTO.setName(VERANSTALTUNG_NAME);
        veranstaltungDTO.setSportjahr(SPORTJAHR);
        veranstaltungDTO.setMeldeDeadline(MELDEDEADLINE);
        veranstaltungDTO.setLigaleiterId(LIGALEITERID);
        veranstaltungDTO.setLigaId(LIGAID);
        veranstaltungDTO.setLigaleiterEmail(LIGALEITER_EMAIL);
        veranstaltungDTO.setWettkampftypName(WETTKAMPTYP_NAME);
        veranstaltungDTO.setLigaName(LIGANAME);

        return veranstaltungDTO;
    }


    public static SportjahrDO getSportjahrDO() {
        return new SportjahrDO(SPORTJAHR_ID, SPORTJAHR_JAHR);
    }

    public static SportjahrDTO getSportjahrDTO() {
        final SportjahrDTO sportjahrDTO = new SportjahrDTO();
        sportjahrDTO.setId(SPORTJAHR_ID);
        sportjahrDTO.setSportjahr(WETTKAMPFTYP_ID  );

        return sportjahrDTO;
    }



    @Before
    public void initMocks() {
        when(principal.getName()).thenReturn(String.valueOf(USER));
    }


    @Test
    public void findAll() {
        // prepare test data
        final VeranstaltungDO VeranstaltungDO = getVeranstaltungDO();
        final List<VeranstaltungDO> VeranstaltungDOList = Collections.singletonList(VeranstaltungDO);

        // configure mocks
        when(VeranstaltungComponent.findAll()).thenReturn(VeranstaltungDOList);

        // call test method
        final List<VeranstaltungDTO> actual = underTest.findAll();

        // assert result
        assertThat(actual)
                .isNotNull()
                .hasSize(1);

        final VeranstaltungDTO actualDTO = actual.get(0);

        assertThat(actualDTO).isNotNull();
        assertThat(actualDTO.getId()).isEqualTo(VeranstaltungDO.getVeranstaltungID());
        assertThat(actualDTO.getName()).isEqualTo(VeranstaltungDO.getVeranstaltungName());

        // verify invocations
        verify(VeranstaltungComponent).findAll();
    }


    @Test
    public void findById() {
        // prepare test data
        final VeranstaltungDO VeranstaltungDO = getVeranstaltungDO();

        // configure mocks
        when(VeranstaltungComponent.findById(anyLong())).thenReturn(VeranstaltungDO);

        // call test method
        final VeranstaltungDTO actual = underTest.findById(ID);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(VeranstaltungDO.getVeranstaltungID());
        assertThat(actual.getName()).isEqualTo(VeranstaltungDO.getVeranstaltungName());

        // verify invocations
        verify(VeranstaltungComponent).findById(ID);
    }


    @Test
    public void findByLigaId() {
        // prepare test data
        final VeranstaltungDO VeranstaltungDO = getVeranstaltungDO();
        final List<VeranstaltungDO> VeranstaltungDOList = Collections.singletonList(VeranstaltungDO);

        // configure mocks
        when(VeranstaltungComponent.findByLigaID(anyLong())).thenReturn(VeranstaltungDOList);

        // call test method
        final List<VeranstaltungDTO> actual = underTest.findByLigaId(ID);

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        // verify invocations
        verify(VeranstaltungComponent).findByLigaID(ID);
    }


    @Test
    public void findAllSportjahrDestinct() {
        // prepare test data
        final SportjahrDO SportjahrDO = getSportjahrDO();
        final List<SportjahrDO> SportjahrDOList = Collections.singletonList(SportjahrDO);

        // configure mocks
        when(VeranstaltungComponent.findAllSportjahreDestinct()).thenReturn(SportjahrDOList);

        // call test method
        final List<SportjahrDTO> actual = underTest.findAllSportjahrDestinct();

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        // verify invocations
        verify(VeranstaltungComponent).findAllSportjahreDestinct();
    }


    @Test
    public void findBySportjahr() {
        // prepare test data
        final VeranstaltungDO VeranstaltungDO = getVeranstaltungDO();
        final List<VeranstaltungDO> VeranstaltungDOList = Collections.singletonList(VeranstaltungDO);

        // configure mocks
        when(VeranstaltungComponent.findBySportjahr(anyLong())).thenReturn(VeranstaltungDOList);

        // call test method
        final List<VeranstaltungDTO> actual = underTest.findBySportjahr(SPORTJAHR);

        // assert result
        assertThat(actual).isNotNull().hasSize(1);

        // verify invocations
        verify(VeranstaltungComponent).findBySportjahr(SPORTJAHR);
    }


    @Test
    public void create() {
        // prepare test data
        final VeranstaltungDTO input = getVeranstaltungDTO();
        final VeranstaltungDO expected = getVeranstaltungDO();

        // configure mocks
        when(VeranstaltungComponent.create(any(), anyLong())).thenReturn(expected);

        // call test method
        final VeranstaltungDTO actual = underTest.create(input, principal);

        // assert result
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(input.getId());
        assertThat(actual.getName()).isEqualTo(input.getName());

        // verify invocations
        verify(VeranstaltungComponent).create(VeranstaltungDOArgumentCaptor.capture(), anyLong());

        final VeranstaltungDO createdVeranstaltung = VeranstaltungDOArgumentCaptor.getValue();

        assertThat(createdVeranstaltung).isNotNull();
        assertThat(createdVeranstaltung.getVeranstaltungID()).isEqualTo(input.getId());
        assertThat(createdVeranstaltung.getVeranstaltungName()).isEqualTo(input.getName());
    }


    @Test
    public void update() {
        // prepare test data
        final VeranstaltungDTO input = getVeranstaltungDTO();
        final VeranstaltungDO expected = getVeranstaltungDO();

        // configure mocks
        when(VeranstaltungComponent.update(any(), anyLong())).thenReturn(expected);

        try {
            // call test method
            final VeranstaltungDTO actual = underTest.update(input, principal);

            // assert result
            assertThat(actual).isNotNull();
            assertThat(actual.getId()).isEqualTo(input.getId());
            assertThat(actual.getName()).isEqualTo(input.getName());

            // verify invocations
            verify(VeranstaltungComponent).update(VeranstaltungDOArgumentCaptor.capture(), anyLong());

            final VeranstaltungDO updatedVeranstaltung = VeranstaltungDOArgumentCaptor.getValue();

            assertThat(updatedVeranstaltung).isNotNull();
            assertThat(updatedVeranstaltung.getVeranstaltungID()).isEqualTo(input.getId());
            assertThat(updatedVeranstaltung.getVeranstaltungName()).isEqualTo(input.getName());

        }catch (NoPermissionException e) { }
    }



    @Test
    public void delete() {
        // prepare test data
        final VeranstaltungDO expected = getVeranstaltungDO();

        // configure mocks

        // call test method
        underTest.delete(ID, principal);

        // assert result

        // verify invocations
        verify(VeranstaltungComponent).delete(VeranstaltungDOArgumentCaptor.capture(), anyLong());

        final VeranstaltungDO deletedVeranstaltung = VeranstaltungDOArgumentCaptor.getValue();

        assertThat(deletedVeranstaltung).isNotNull();
        assertThat(deletedVeranstaltung.getVeranstaltungID()).isEqualTo(expected.getVeranstaltungID());
        assertThat(deletedVeranstaltung.getVeranstaltungName()).isNullOrEmpty();
    }

    /*
    @Test
    public void hasPermission() {
        // prepare test data

        // configure mocks
        when(RequestContextHolder.getRequestAttributes()).thenReturn(requestAttributes);    //Z 271
        when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);         //Z 274
        when(JwtTokenProvider.resolveToken(any())).thenReturn(jwtString);                   //Z 278
        when(jwtTokenProvider.getPermissions(anyString())).thenReturn(userPermission);      //Z 279

        // call test method
        //final boolean actual = underTest.hasPermission();

        // assert result
        //assertThat(actual).isNotNull();

        // verify invocations
        verify(RequestContextHolder).getRequestAttributes();
        verify(servletRequestAttributes).getRequest();
        verify(JwtTokenProvider).resolveToken(httpServletRequest);
        verify(jwtTokenProvider).getPermissions(jwtString);
    }
    */


    /*
    @Test
    public void hasSpecificPermission() {
        // prepare test data
        final RequestAttributes requestAttributes = ;               //Z 301
        final ServletRequestAttributes servletRequestAttributes = ; //Z 303
        final HttpServletRequest httpServletRequest = ;             //Z 304
        final String string = ;                                     //Z 308
        final JwtTokenProvider jwtTokenProvider = ;
        final Long userid = ;                                       //Z 312

        // configure mocks
        when(RequestContextHolder.getRequestAttributes()).thenReturn(requestAttributes);    //Z 301
        when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);         //Z 304
        when(JwtTokenProvider.resolveToken(any())).thenReturn(string);                      //Z 308
        when(jwtTokenProvider.getUserId(anyString())).thenReturn(userid);                   //Z 312

        // call test method

        // assert result
        assertThat(actual).isNotNull();

        // verify invocations
        verify(RequestContextHolder).getRequestAttributes();
        verify(servletRequestAttributes).getRequest();
        verify(JwtTokenProvider).resolveToken(any());
        verify(jwtTokenProvider).getUserId(anyString());
    }
    */

}