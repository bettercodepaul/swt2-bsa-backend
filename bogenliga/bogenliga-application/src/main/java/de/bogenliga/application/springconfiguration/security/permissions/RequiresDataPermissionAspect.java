package de.bogenliga.application.springconfiguration.security.permissions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
 * I implement the validation logic of the {@link RequiresPermission} annotation.
 *
 * The logic will be wrapped around the annotated method.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@Aspect
@Component
public class RequiresDataPermissionAspect {
    private static final Logger LOG = LoggerFactory.getLogger(RequiresDataPermissionAspect.class);

    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public RequiresDataPermissionAspect(final JwtTokenProvider jwtTokenProvider) {
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
    @Around("@annotation(de.bogenliga.application.springconfiguration.security.permissions.RequiresDataPermissions)")
    public Object checkPermission(final ProceedingJoinPoint joinPoint) throws Throwable {
        //Getting the Variables from the Request that was made
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        final HttpServletRequest request = servletRequestAttributes.getRequest();
        final String jwt = JwtTokenProvider.resolveToken(request);
        final String username = jwtTokenProvider.getUsername(jwt);
        Method currentMethod = getCurrentMethod(joinPoint);
       if (currentMethod.isAnnotationPresent(RequiresDataPermissions.class)) {
           RequiresDataPermissions annotation  =currentMethod.getAnnotation(RequiresDataPermissions.class);

           final UserPermission[] override = annotation.value();
           final UserPermission[]  my= annotation.specifc();
           final String type = annotation.type();
           String joinedRequiredPermissions = "";
           if(hasPermisson(override)){

           }else{
               if(hasPermisson(my)){
                int reqid = 0;
                   ArrayList<Integer> allowedids= new ArrayList<Integer>();
                   switch(type){
                       case"verein":

                           break;
                       case"veranstaltung":
                           break;
                       case"mitglied":
                           break;
                   }

                   if(! allowedids.contains(reqid)){
                       throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR,String.format("User '%s' is not allowed to edit Resource with id %i type %s ", username,reqid,type));

                   }
               }else{
                   throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR,String.format("User '%s' has not all required permissions ", username));
               }
           }
        };





        return joinPoint.proceed();
    }

    boolean hasPermisson(UserPermission[] toTest){
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
                final Set<UserPermission> userPermissions = jwtTokenProvider.getPermissions(jwt);

                // verify all jwt permissions are part of the required permissions
                for (UserPermission entry : toTest) {
                    if (userPermissions.contains(entry)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    Method getCurrentMethod(final ProceedingJoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}

