package de.bogenliga.application.services.v1.user.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("v1/user")
public class KeepAlive {

    private static final Logger LOG = LoggerFactory.getLogger(KeepAlive.class);
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public KeepAlive(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = "/keepalive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KeepAliveResponse> keepAlive(final HttpServletRequest requestWithHeader) {
        final String jwt = JwtTokenProvider.resolveToken(requestWithHeader);

        if (jwt == null) {
            KeepAliveResponse errorResponse = new KeepAliveResponse("Invalid or expired token", null, ErrorCode.INVALID_SIGN_IN_CREDENTIALS);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        final String newJwt = refreshToken(jwt);

        KeepAliveResponse successResponse = new KeepAliveResponse("Session extended", newJwt, null);
        return ResponseEntity.ok().body(successResponse);
    }

    private String refreshToken(String token) {
        try {
            Method method = JwtTokenProvider.class.getDeclaredMethod("refreshToken", String.class);
            method.setAccessible(true);
            return (String) method.invoke(jwtTokenProvider, token);
        } catch (Exception e) {
            LOG.error("Error refreshing token", e);
            return token;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class KeepAliveResponse {
        @JsonProperty("message")
        private String message;

        @JsonProperty("token")
        private String token;

        @JsonProperty("errorCode")
        private ErrorCode errorCode;

        public KeepAliveResponse(String message, String token, ErrorCode errorCode) {
            this.message = message;
            this.token = token;
            this.errorCode = errorCode;
        }

    }
}
