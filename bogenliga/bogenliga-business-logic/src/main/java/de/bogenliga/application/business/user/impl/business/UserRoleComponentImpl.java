package de.bogenliga.application.business.user.impl.business;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.bogenliga.application.business.configuration.api.ConfigurationComponent;
import de.bogenliga.application.business.configuration.api.types.ConfigurationDO;
import de.bogenliga.application.business.role.impl.dao.RoleDAO;
import de.bogenliga.application.business.role.impl.entity.RoleBE;
import de.bogenliga.application.business.user.api.UserComponent;
import de.bogenliga.application.business.user.api.UserRoleComponent;
import de.bogenliga.application.business.user.api.types.UserRoleDO;
import de.bogenliga.application.business.user.impl.dao.UserRoleExtDAO;
import de.bogenliga.application.business.user.impl.entity.UserRoleBE;
import de.bogenliga.application.business.user.impl.entity.UserRoleExtBE;
import de.bogenliga.application.business.user.impl.mapper.UserRoleMapper;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import de.bogenliga.application.common.validation.Preconditions;

/**
 * Implementation of {@link UserComponent}
 */
@Component
public class UserRoleComponentImpl implements UserRoleComponent {

    private static final String PRECONDITION_MSG_USERROLE = "UserRoleDO must not be null";
    private static final String PRECONDITION_MSG_USER_ID = "UserID must not be null or negative";
    private static final String PRECONDITION_MSG_USER_EMAIL = "UserEmail must not be null or empty";
    private static final String PRECONDITION_MSG_ROLE_ID = "RoleID must not be null or negative";
    private static final String USER_ROLE_DEFAULT = "USER";
    private final UserRoleExtDAO userRoleExtDAO;

    private final RoleDAO roleDAO;

    private ConfigurationComponent einstellungenComponent;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleComponentImpl.class);


    /**
     * Constructor
     * <p>
     * dependency injection with {@link Autowired}
     *
     * @param userRoleExtDAO to access the database and return user role (including name, email - not IDs only)
     * @param roleDAO        to access the database and return default role
     */
    @Autowired
    public UserRoleComponentImpl(final UserRoleExtDAO userRoleExtDAO, RoleDAO roleDAO,
                                 ConfigurationComponent einstellungenComponent) {

        this.userRoleExtDAO = userRoleExtDAO;
        this.roleDAO = roleDAO;
        this.einstellungenComponent = einstellungenComponent;
    }



    @Override
    public List<UserRoleDO> findAll() {
        final List<UserRoleExtBE> userRoleExtBEList = userRoleExtDAO.findAll();
        return userRoleExtBEList.stream().map(UserRoleMapper.extToUserRoleDO).collect(Collectors.toList());
    }


    @Override
    public List<UserRoleDO> findById(final Long id) {
        Preconditions.checkNotNull(id, PRECONDITION_MSG_USERROLE);
        Preconditions.checkArgument(id >= 0, PRECONDITION_MSG_USER_ID);

        final List<UserRoleExtBE> result = userRoleExtDAO.findById(id);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for ID '%s'", id));
        }
        List<UserRoleDO> userRoleDOList = new ArrayList<>();
        for (UserRoleExtBE userRoleExtBE : result) {
            userRoleDOList.add(UserRoleMapper.extToUserRoleDO.apply(userRoleExtBE));
        }

        return userRoleDOList;
    }


    /**
     * gets all users from findAll() and checks if they have the
     * special role with the given "roleId"
     * @param roleId
     * @return List of all users with this role
     */
    @Override
    public List<UserRoleDO> findByRoleId(final Long roleId) {
        Preconditions.checkNotNull(roleId, PRECONDITION_MSG_USERROLE);
        Preconditions.checkArgument(roleId >= 0, PRECONDITION_MSG_ROLE_ID);

        List<UserRoleDO> allUsersOfRole = new ArrayList<>();
        List<UserRoleExtBE> allUsers = userRoleExtDAO.findAll();
        if (allUsers == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for roleID '%s'", roleId));
        }


        for (UserRoleExtBE i: allUsers) {
            if( i.getRoleId().equals(roleId)){
                allUsersOfRole.add(UserRoleMapper.extToUserRoleDO.apply(i));
            }
        }

        return allUsersOfRole;
    }


    @Override
    public UserRoleDO findByEmail(final String email) {
        Preconditions.checkNotNullOrEmpty(email, PRECONDITION_MSG_USER_EMAIL);

        final UserRoleExtBE result = userRoleExtDAO.findByEmail(email);

        if (result == null) {
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND_ERROR,
                    String.format("No result found for email '%s'", email));
        }

        return UserRoleMapper.extToUserRoleDO.apply(result);
    }


    // create with default role
    public UserRoleDO create(final Long userId, final Long currentUserId) {
        Preconditions.checkNotNull(userId, PRECONDITION_MSG_USER_ID);
        Preconditions.checkNotNull(currentUserId, PRECONDITION_MSG_USER_ID);

        // find the default role
        final RoleBE defaultRoleBE = roleDAO.findByName(USER_ROLE_DEFAULT);


        final UserRoleBE result = new UserRoleBE();
        result.setUserId(userId);
        result.setRoleId(defaultRoleBE.getRoleId());

        final UserRoleBE persistedUserRoleBE = userRoleExtDAO.create(result, currentUserId);

        return UserRoleMapper.toUserRoleDO.apply(persistedUserRoleBE);
    }


    // create with role and userid
    public UserRoleDO create(final Long userId, final Long roleId, final Long currentUserId) {
        Preconditions.checkNotNull(userId, PRECONDITION_MSG_USER_ID);
        Preconditions.checkNotNull(roleId, PRECONDITION_MSG_ROLE_ID);
        Preconditions.checkNotNull(currentUserId, PRECONDITION_MSG_USER_ID);

        final UserRoleBE result = new UserRoleBE();
        result.setUserId(userId);
        result.setRoleId(roleId);

        final UserRoleBE persistedUserBE = userRoleExtDAO.create(result, currentUserId);

        return UserRoleMapper.toUserRoleDO.apply(persistedUserBE);
    }


    /**
     * Implementation for userRole update method
     *
     * @param userRoleDOS   list of userroles
     * @param currentUserId current user
     *
     * @return
     */
    public List<UserRoleDO> update(final List<UserRoleDO> userRoleDOS, final Long currentUserId) {
        Preconditions.checkNotNull(userRoleDOS, PRECONDITION_MSG_USERROLE);
        Preconditions.checkNotNull(userRoleDOS.get(0).getId(), PRECONDITION_MSG_USER_ID);
        Preconditions.checkNotNull(userRoleDOS.get(0).getRoleId(), PRECONDITION_MSG_ROLE_ID);
        Preconditions.checkArgument(userRoleDOS.get(0).getId() >= 0, PRECONDITION_MSG_USER_ID);
        Preconditions.checkArgument(userRoleDOS.get(0).getRoleId() >= 0, PRECONDITION_MSG_ROLE_ID);
        Preconditions.checkNotNull(currentUserId, PRECONDITION_MSG_USER_ID);

        List<UserRoleBE> userRoleBES = new ArrayList<>();
        for (UserRoleDO userRoleDO : userRoleDOS) {
            UserRoleBE result = new UserRoleBE();
            result.setUserId(userRoleDO.getId());
            result.setRoleId(userRoleDO.getRoleId());
            userRoleBES.add(result);
        }

        final List<UserRoleBE> persistedUserRoleBE = userRoleExtDAO.createOrUpdate(userRoleBES, currentUserId);


        List<UserRoleDO> persistedUserRoleDO = new ArrayList<>();
        for (UserRoleBE userRoleBE : persistedUserRoleBE) {
            persistedUserRoleDO.add(UserRoleMapper.toUserRoleDO.apply(userRoleBE));
        }

        return persistedUserRoleDO;
    }


    /**
     * Implementation sendFeedback method
     *
     * @param text Feedback text given in the Frontend, optinal with Email of the sender
     *
     * @return void
     */
    @Override
    public void sendFeedback(final String text) {

        //this returns all DB entries with the Benutzer_rolle_rolle_id of 1
        List<UserRoleExtBE> result = userRoleExtDAO.findAdminEmails();
        String[] recipients = new String[result.size()];

        //here we filter all the Mail-addresses
        for (int i = 0; i < result.size(); i++) {
            recipients[i] = (result.get(i).getUserEmail());
        }

        List<ConfigurationDO> einstellungen = einstellungenComponent.findAll();

        String smtpHost = "";
        String smtpPW = "";
        String smtpBenutzer = "";
        String smtpEMail = "";
        String smtpPort = "";

        for (int i = 0; i < einstellungen.size(); i++) {
            String tempKey = einstellungen.get(i).getKey();
            if (tempKey.equals("SMTPHost")) {
                smtpHost = einstellungen.get(i).getValue();
            } else if (tempKey.equals("SMTPPasswort")) {
                smtpPW = einstellungen.get(i).getValue();
            } else if (tempKey.equals("SMTPBenutzer")) {
                smtpBenutzer = einstellungen.get(i).getValue();
            } else if (tempKey.equals("SMTPEmail")) {
                smtpEMail = einstellungen.get(i).getValue();
            } else if (tempKey.equals("SMTPPort")) {
                smtpPort = einstellungen.get(i).getValue();
            }

        }

        LOGGER.debug("Found smtpHost {} and smtpPort {}", smtpHost, smtpPort);

        final String username = smtpBenutzer;
        final String password = smtpPW;

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.checkserveridentity", true);

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(smtpEMail, "NoReply-Bogenliga"));

            msg.setSubject("Feedback", "UTF-8");

            msg.setText(text);

            for (String recipient : recipients) {
                msg.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipient));
            }

            Transport.send(msg);

        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            LOGGER.debug(unsupportedEncodingException.getMessage());
        } catch (AddressException addressException) {
            LOGGER.debug(addressException.getMessage());
        } catch (MessagingException messagingException) {
            LOGGER.debug(messagingException.getMessage());
        }

    }

}
