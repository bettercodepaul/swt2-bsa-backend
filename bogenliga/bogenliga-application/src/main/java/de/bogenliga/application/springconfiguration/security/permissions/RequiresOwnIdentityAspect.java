package de.bogenliga.application.springconfiguration.security.permissions;

import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;
import de.bogenliga.application.springconfiguration.security.jsonwebtoken.JwtTokenProvider;
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

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * I implement the validation logic of the {@link RequiresOwnIdentity} annotation.
 * <p>
 * The logic will be wrapped around the annotated method.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Aspect
@Component
public class RequiresOwnIdentityAspect {
    private static final Logger LOG = LoggerFactory.getLogger(RequiresOwnIdentityAspect.class);

    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public RequiresOwnIdentityAspect(final JwtTokenProvider jwtTokenProvider) {
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
     * @return next {@link ProceedingJoinPoint}
     * @throws Throwable         if the {@link ProceedingJoinPoint} throws an exception
     * @throws BusinessException if the user has not all required permissions
     */
    @Around("@annotation(de.bogenliga.application.springconfiguration.security.permissions.RequiresOwnIdentity)")
    public Object checkOwnIdentity(final ProceedingJoinPoint joinPoint) throws Throwable {

        List<String> requiredOwnIdentityStrings = new ArrayList<>();

        // get permissions from annotation
        if (getCurrentMethod(joinPoint).isAnnotationPresent(RequiresOwnIdentity.class)) {

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
                    final Long id = jwtTokenProvider.getUserId(jwt);
                    Preconditions.checkNotNull(id, "JWT user id must not be null.");
                    final Long idJoinPoint = (Long) joinPoint.getArgs()[0];
                    Preconditions.checkNotNull(id, "ID in parameter must not be null.");
                    LOG.trace("Verify own identity with user id: {} and parameter ID: {}", id, idJoinPoint);

                    // verify all jwt permissions are part of the required permissions
                    if (!id.equals(idJoinPoint)) {
                        throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR,
                                String.format("User id not equal to joinpoint id: %s vs. %s", id, idJoinPoint),
                                requiredOwnIdentityStrings.toArray());
                    }
                }

            }


        }
        return joinPoint.proceed();
    }


    Method getCurrentMethod(final ProceedingJoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
