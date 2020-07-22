package de.bogenliga.application.springconfiguration.security.permissions;

import java.lang.reflect.Method;
import java.util.Set;
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
 * I implement the validation logic of the {@link RequiresPermission} annotation.
 *
 * The logic will be wrapped around the annotated method.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Aspect
@Component
public class RequiresOnePermissionAspect {
    private static final Logger LOG = LoggerFactory.getLogger(RequiresOnePermissionAspect.class);

    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public RequiresOnePermissionAspect(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    /**
     * I validate the permissions of an user.
     * <p>
     * A JSON Web Token is used for http authentication. The token contains the permissions of the user. The required
     * permissions are defined as annotation parameter.
     * <p>
     * All required permissions have to be a part of the user permissions.
     *
     * @param joinPoint of the annotated method
     *
     * @return next {@link ProceedingJoinPoint}
     *
     * @throws Throwable         if the {@link ProceedingJoinPoint} throws an exception
     * @throws BusinessException if the user has not all required permissions
     */
    @Around("@annotation(de.bogenliga.application.springconfiguration.security.permissions.RequiresOnePermissions)")
    public Object checkPermission(final ProceedingJoinPoint joinPoint) throws Throwable {
        //Getting the Variables from the Request that was made
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        final HttpServletRequest request = servletRequestAttributes.getRequest();
        final String jwt = JwtTokenProvider.resolveToken(request);
        final String username = jwtTokenProvider.getUsername(jwt);
        Method currentMethod = getCurrentMethod(joinPoint);

        if (currentMethod.isAnnotationPresent(RequiresOnePermissions.class)) {
            RequiresOnePermissions annotation = currentMethod.getAnnotation(RequiresOnePermissions.class);

            final UserPermission[] permisson = annotation.perm();
            boolean result = false;
            for(UserPermission entry : permisson){
                if(hasPermission(entry)){
                    result = true;
                }

            }
            if(!result){
            throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR,
                    String.format("User '%s' has not one of the  required permissions a", username));
        }}
        return joinPoint.proceed();
    };
    boolean hasPermission(UserPermission toTest){
        // get current http request from thread
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        boolean result = false;
        if (requestAttributes != null) {
            final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;

            final HttpServletRequest request = servletRequestAttributes.getRequest();

            // if request present
            if (request != null) {
                // parse json web token with roles
                final String jwt = JwtTokenProvider.resolveToken(request);

                // custom permission check
                final Set<UserPermission> userPermissions = jwtTokenProvider.getPermissions(jwt);

                // verify all jwt permissions are part of the required permissions
                    if (userPermissions.contains(toTest)) {
                        result = true;
                    }

            }
        }

        return result;
    }
    Method getCurrentMethod(final ProceedingJoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}

