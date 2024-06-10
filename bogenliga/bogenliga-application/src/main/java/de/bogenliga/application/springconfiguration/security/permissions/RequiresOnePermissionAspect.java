package de.bogenliga.application.springconfiguration.security.permissions;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import de.bogenliga.application.business.dsbmitglied.api.DsbMitgliedComponent;
import de.bogenliga.application.business.dsbmitglied.api.types.DsbMitgliedDO;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.types.UserDO;
import de.bogenliga.application.business.veranstaltung.api.VeranstaltungComponent;
import de.bogenliga.application.business.veranstaltung.api.types.VeranstaltungDO;
import de.bogenliga.application.business.wettkampf.api.WettkampfComponent;
import de.bogenliga.application.business.wettkampf.api.types.WettkampfDO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
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
 * @author Andre Lehnert, BettercallPaul gmbh
 */
@Aspect
@Component
public class RequiresOnePermissionAspect {

    private final JwtTokenProvider jwtTokenProvider;
    private final VeranstaltungComponent veranstaltungComponent;
    private final DsbMitgliedComponent dsbMitgliedComponent;
    private final UserComponent userComponent;
    private final WettkampfComponent wettkampfComponent;

    @Autowired
    public RequiresOnePermissionAspect(
            final JwtTokenProvider jwtTokenProvider,
            final WettkampfComponent wettkampfComponent,
            final DsbMitgliedComponent dsbMitgliedComponent,
            final UserComponent userComponent,
            final VeranstaltungComponent veranstaltungComponent
            ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.veranstaltungComponent = veranstaltungComponent;
        this.dsbMitgliedComponent = dsbMitgliedComponent;
        this.userComponent = userComponent;
        this.wettkampfComponent = wettkampfComponent;
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
        final ServletRequestAttributes servletRequestAttributes;
        final HttpServletRequest request;
        final String jwt;
        final String username;
        Method currentMethod;

            //Getting the Variables from the Request that was made
            final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                throw new NullPointerException();
            } else {
                servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
                request = servletRequestAttributes.getRequest();
                jwt = JwtTokenProvider.resolveToken(request);
                username = jwtTokenProvider.getUsername(jwt);
                currentMethod = getCurrentMethod(joinPoint);
            }

            if (currentMethod.isAnnotationPresent(RequiresOnePermissions.class)) {
                RequiresOnePermissions annotation = currentMethod.getAnnotation(RequiresOnePermissions.class);

                final UserPermission[] permisson = annotation.perm();
                boolean result = false;
                for (UserPermission entry : permisson) {
                    if (hasPermission(entry)) {
                        result = true;
                    }

                }
                if (!result) {
                    throw new BusinessException(ErrorCode.NO_PERMISSION_ERROR,
                            String.format("User '%s' has not one of the  required permissions a", username));
                }
            }
            return joinPoint.proceed();
    }

    public boolean hasPermission(UserPermission toTest){
        // get current http request from thread
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        boolean result = false;
        if (requestAttributes != null) {
            final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;

            final HttpServletRequest request = servletRequestAttributes.getRequest();


            // parse json web token with roles
            final String jwt = JwtTokenProvider.resolveToken(request);

            // custom permission check
            final Set<UserPermission> userPermissions = jwtTokenProvider.getPermissions(jwt);

            // verify all jwt permissions are part of the required permissions
            if (userPermissions.contains(toTest)) {
                result = true;
            }

        }

        return result;
    }

    /**
     * method to check, if a user has a Specific permission and
     * when LigaLeiterID of Veranstaltung is identical to userId
     * @param toTest The permission whose existence is getting checked
     * @param veranstaltungsid the Veranstaltung to read
     * @return Does the User have searched permission
     */
    public boolean hasSpecificPermissionLigaLeiterID(UserPermission toTest, Long veranstaltungsid) {
        //get the current http request from thread
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            final HttpServletRequest request = servletRequestAttributes.getRequest();
            //if a request is present:

            //parse the Webtoken and get the UserPermissions of the current User
            final String jwt = JwtTokenProvider.resolveToken(request);
            final Set<UserPermission> userPermissions = jwtTokenProvider.getPermissions(jwt);

            //check if the current Users vereinsId equals the given vereinsId and if the User has
            //the required Permission (if the permission is specifi
            // verify all jwt permissions are part of the required permissions
            if (userPermissions.contains(toTest)) {
                Long userId = jwtTokenProvider.getUserId(jwt);
                for (VeranstaltungDO veranstaltungDO : this.veranstaltungComponent.findByLigaleiterId(userId)) {
                    if (veranstaltungDO.getVeranstaltungID().equals(veranstaltungsid) ){
                        return true;
                    }
                }

            }
        }
        return false;
    }
    /**
     * method to check, if a user has a Specific permission and
     * UserId is matching the AusrichterID of the wettkampf
     * @param toTest The permission whose existence is getting checked
     * @param wettkampfid to check of user persmission (ausrichterId)
     * @return Does the User have searched permission
     */
    public boolean hasSpecificPermissionAusrichter(UserPermission toTest, Long wettkampfid) {
        //get the current http request from thread
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            final HttpServletRequest request = servletRequestAttributes.getRequest();
            //if a request is present:

            //parse the Webtoken and get the UserPermissions of the current User
            final String jwt = JwtTokenProvider.resolveToken(request);
            final Set<UserPermission> userPermissions = jwtTokenProvider.getPermissions(jwt);

            //check if the current Users vereinsId equals the given vereinsId and if the User has
            //the required Permission (if the permission is specifi
            // verify all jwt permissions are part of the required permissions
            if (userPermissions.contains(toTest)) {
                Long userId = jwtTokenProvider.getUserId(jwt);
                for (WettkampfDO wettkampfDO : this.wettkampfComponent.findByAusrichter(userId)) {
                    if (wettkampfDO.getId().equals(wettkampfid) ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * method to check, if a user has a Specific permission and
     * UserId is matching the Users Verein is the same as the vereinsID
     * @param toTest The permission whose existence is getting checked
     * @param vereinsId to check is user is a meber of the Verein
     * @return Does the User have searched permission
     */
    public boolean hasSpecificPermissionSportleiter(UserPermission toTest, Long vereinsId) {
        //get the current http request from thread
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            final HttpServletRequest request = servletRequestAttributes.getRequest();
            //if a request is present:

            //parse the Webtoken and get the UserPermissions of the current User
            final String jwt = JwtTokenProvider.resolveToken(request);
            final Set<UserPermission> userPermissions = jwtTokenProvider.getPermissions(jwt);

            //check if the current Users vereinsId equals the given vereinsId and if the User has
            //the required Permission (if the permission is specifi
            // verify all jwt permissions are part of the required permissions
            if (userPermissions.contains(toTest)) {
                Long userId = jwtTokenProvider.getUserId(jwt);
                UserDO userDO = this.userComponent.findById(userId);
                DsbMitgliedDO dsbMitgliedDO = this.dsbMitgliedComponent.findById(userDO.getDsbMitgliedId());
                if (dsbMitgliedDO.getVereinsId().equals(vereinsId)) {
                    return true;
                }
            }
        }
        return false;
    }

    Method getCurrentMethod(final ProceedingJoinPoint joinPoint) {
        if(joinPoint == null) {
            Logger.getLogger("joinPoint is null");  //NullPointerException will be thrown
            return null;
        }
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}