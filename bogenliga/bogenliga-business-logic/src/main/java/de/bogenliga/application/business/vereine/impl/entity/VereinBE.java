package de.bogenliga.application.business.vereine.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * I represent the verein business entitity
 * <p>
 * A Verein always belongs to a Region of the DSB.
 *
 * @author Giuseppe Ferrera, giuseppe.ferrera@student.reutlingen-university.de
 */
public class VereinBE extends CommonBusinessEntity implements BusinessEntity {
    private Long vereinId;
    private String vereinName;
    private String vereinDsbIdentifier;
    private Long vereinRegionId;
    private String vereinWebsite;
    private String vereinDescription;
    private String vereinIcon;

    /**
     * Empty constructor
     */
    public VereinBE() {
        // empty constructor
    }


    public Long getVereinId() {
        return vereinId;
    }


    public void setVereinId(Long vereinId) {
        this.vereinId = vereinId;
    }


    public String getVereinName() {
        return vereinName;
    }


    public void setVereinName(String vereinName) {
        this.vereinName = vereinName;
    }


    public String getVereinDsbIdentifier() {
        return vereinDsbIdentifier;
    }


    public void setVereinDsbIdentifier(String vereinDsbIdentifier) {
        this.vereinDsbIdentifier = vereinDsbIdentifier;
    }


    public Long getVereinRegionId() {
        return vereinRegionId;
    }


    public void setVereinRegionId(Long vereinRegionId) {
        this.vereinRegionId = vereinRegionId;
    }


    public String getVereinWebsite() { return vereinWebsite; }


    public void setVereinWebsite(String vereinWebsite) { this.vereinWebsite = vereinWebsite; }


    public String getVereinDescription() { return vereinDescription; }


    public void setVereinDescription(String vereinDescription) { this.vereinDescription = vereinDescription; }


    public String getVereinIcon() { return vereinIcon; }


    public void setVereinIcon(String vereinIcon) { this.vereinIcon = vereinIcon; }


    @Override
    public String toString() {
        return "VereinBE{" +
                "vereinId='" + vereinId + '\'' +
                ", vereinName='" + vereinName + '\'' +
                ", vereinDsbIdentifier='" + vereinDsbIdentifier + '\'' +
                ", vereinRegionId='" + vereinRegionId + '\'' +
                ", vereinWebsite='" + vereinWebsite + '\'' +
                ", vereinDescription='" + vereinDescription + '\'' +
                ", vereinIcon='" + vereinIcon + '\'' +
                "}";
    }
}
