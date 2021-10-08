package de.bogenliga.application.business.user.api.types;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.bogenliga.application.common.component.types.CommonDataObject;
import de.bogenliga.application.common.component.types.DataObject;

/**
 * Contains the values of the user business entity.
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
public class UserDO extends CommonDataObject implements DataObject {
    private static final long serialVersionUID = 298357103627898987L;

    private static final Logger LOG = LoggerFactory.getLogger(UserDO.class);

    /**
     * business parameter
     */
    private Long id;
    private String email;
    private Long dsb_mitglied_id;
    private boolean using2FA;
    private boolean active;
    private String secret;
    private String qrCode;

    /**
     * Constructor with optional parameters
     */
    public UserDO() {
        // empty constructor
    }


    /**
     * Constructor with all parameters
     */
    public UserDO(final Long id, final String email, final Long dsb_mitglied_id, boolean using2FA,
                  boolean active, String secret, final OffsetDateTime createdAtUtc,
                  final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                  final Long lastModifiedByUserId, final Long version) {
        this.id = id;
        this.email = email;
        this.dsb_mitglied_id = dsb_mitglied_id;
        this.using2FA = using2FA;
        this.active = active;
        this.secret = secret;

        // set parameter from CommonDataObject
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }

    /**
     * Constructor with mandatory parameters
     */
    public UserDO(final Long id, final String email, boolean using2FA,
                  boolean active, String secret, final OffsetDateTime createdAtUtc,
                  final Long createdByUserId, final OffsetDateTime lastModifiedAtUtc,
                  final Long lastModifiedByUserId, final Long version) {
        this.id = id;
        this.email = email;
        this.dsb_mitglied_id = 0L;
        this.using2FA = using2FA;
        this.active = active;
        this.secret = secret;

        // set parameter from CommonDataObject
        this.createdAtUtc = createdAtUtc;
        this.createdByUserId = createdByUserId;
        this.lastModifiedAtUtc = lastModifiedAtUtc;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.version = version;
    }


    public Long getId() {
        return id;
    }


    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(final String email) {
        this.email = email;
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getCreatedAtUtc(), getCreatedByUserId(), getLastModifiedAtUtc(),
                getLastModifiedByUserId(), getVersion());
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDO)) {
            return false;
        }
        final UserDO userDO = (UserDO) o;
        return getId().equals(userDO.getId()) &&
                getVersion() == userDO.getVersion() &&
                Objects.equals(getEmail(), userDO.getEmail()) &&
                getDsb_mitglied_id().equals(userDO.getDsb_mitglied_id()) &&
                Objects.equals(getCreatedAtUtc(), userDO.getCreatedAtUtc()) &&
                Objects.equals(getCreatedByUserId(), userDO.getCreatedByUserId()) &&
                Objects.equals(getLastModifiedAtUtc(), userDO.getLastModifiedAtUtc()) &&
                Objects.equals(getLastModifiedByUserId(), userDO.getLastModifiedByUserId());
    }

    public Long getDsb_mitglied_id() {
        return dsb_mitglied_id;
    }

    public void setDsb_mitglied_id(final long dsb_mitglied_id) {
        this.dsb_mitglied_id = dsb_mitglied_id;
    }

    public boolean isUsing2FA() {
        return using2FA;
    }


    public void setUsing2FA(boolean using2FA) {
        this.using2FA = using2FA;
    }

    public boolean isActive() {return active;}

    public void setActive(boolean active) {this.active = active;}


    public String getSecret() {
        return secret;
    }


    public void setSecret(String secret) {
        this.secret = secret;
    }


    public String getQrCode() {
        String appName = "Bogenschussapp";
        String prefix =
                "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
        try {
            return prefix + URLEncoder.encode(String.format(
                    "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                    appName, getEmail(), getSecret(), appName),
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error(Arrays.toString(e.getStackTrace()));
        }
        return "QR Code could not be loaded";
    }



}
