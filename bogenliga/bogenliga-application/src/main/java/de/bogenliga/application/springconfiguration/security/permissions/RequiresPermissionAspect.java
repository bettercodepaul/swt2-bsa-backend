package de.bogenliga.application.springconfiguration.security.permissions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
import de.bogenliga.application.springconfiguration.security.types.UserPermission;

/**
 * TODO [AL] class documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Aspect
@Component
public class RequiresPermissionAspect {
    private static final Logger LOG = LoggerFactory.getLogger(RequiresPermissionAspect.class);

    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public RequiresPermissionAspect(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    private static Method getCurrentMethod(final ProceedingJoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }


    @Around("@annotation(de.bogenliga.application.springconfiguration.security.permissions.RequiresPermission)")
    public Object checkPermission(final ProceedingJoinPoint joinPoint) throws Throwable {

        List<UserPermission> requiredPermissions = new ArrayList<>();

        // get permissions from annotation
        if (getCurrentMethod(joinPoint).isAnnotationPresent(RequiresPermission.class)) {
            final UserPermission[] roles = getCurrentMethod(joinPoint).getAnnotation(RequiresPermission.class).value();
            requiredPermissions = Arrays.asList(roles);
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

                // custom permission check
                final String username = jwtTokenProvider.getUsername(jwt);
                final Set<UserPermission> userPermissions = jwtTokenProvider.getPermissions(jwt);

                // verify all jwt permissions are part of the required permissions
                if (!userPermissions.containsAll(requiredPermissions)) {
                    final List<String> requiredPermissionStrings = requiredPermissions.stream()
                            .map(UserPermission::name)
                            .collect(Collectors.toList());
                    final String joinedRequiredPermissions = String.join(", ", requiredPermissionStrings);
                    throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR,
                            String.format("User '%s' has not all required permissions [%s]", username,
                                    joinedRequiredPermissions), requiredPermissionStrings.toArray());
                }
            }

        }


        return joinPoint.proceed();
    }
}
