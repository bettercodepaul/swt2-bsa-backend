package de.bogenliga.application.springconfiguration.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.services.v1.user.model.UserRole;
import de.bogenliga.application.springconfiguration.JwtTokenProvider;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Aspect
@Component
public class RequiresRoleAspect {
    private static final Logger LOG = LoggerFactory.getLogger(RequiresRoleAspect.class);

    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public RequiresRoleAspect(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    private static Method getCurrentMethod(final ProceedingJoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }


    @Around("@annotation(RequiresRole)")
    public Object logExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {

        List<UserRole> requiredRoles = new ArrayList<>();

        // get roles from annotation
        if (getCurrentMethod(joinPoint).isAnnotationPresent(RequiresRole.class)) {
            final UserRole[] roles = getCurrentMethod(joinPoint).getAnnotation(RequiresRole.class).value();
            requiredRoles = Arrays.asList(roles);
        }

        // get current http request from thread
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {
            final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;

            final HttpServletRequest request = servletRequestAttributes.getRequest();

            // if request present
            if (request != null) {
                // parse json web token with roles
                final String jwt = JwtTokenProvider.resolveToken(request);
                final String username = jwtTokenProvider.getUsername(jwt);
                final List<UserRole> userRoles = jwtTokenProvider.getRoles(jwt);

                // verify all jwt roles are part of the required roles
                if (!userRoles.containsAll(requiredRoles)) {
                    final String requiredRolesString = String.join(", ", requiredRoles.stream()
                            .map(UserRole::name)
                            .collect(Collectors.toList()));
                    throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR,
                            String.format("User '%s' has not all required roles [%s]", username, requiredRolesString));
                }
            }

        }


        return joinPoint.proceed();
    }
}
