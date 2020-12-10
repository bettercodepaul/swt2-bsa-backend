package de.bogenliga.application.business.einstellungen.impl.entity;

import de.bogenliga.application.common.component.entity.BusinessEntity;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;

/**
 * TODO [AL] class documentation
 *
 * @author Fabio Care, fabio_silas.care@student.reutlingen-university.de
 */
public class EinstellungenBE extends CommonBusinessEntity implements BusinessEntity {
    private static final long serialVersionUID = -76389969048178948L;
    private String einstellungenKey;
    private String einstellungenValue;

    public EinstellungenBE() {
        //empty constructor
    }

    @Override
    public String toString() {
        return "EinstellungenBE{" +
                ", key=" + einstellungenKey + '\'' +
                ", value=" + einstellungenValue + '\'' +
                "}";
    }

    public String geteinstellungenKey() {
        return einstellungenKey;
    }

    public void seteinstellungenKey(final String einstellungenKey) {
        this.einstellungenKey = einstellungenKey;
    }

    public String geteinstellungenValue() {
        return einstellungenValue;
    }

    public void seteinstellungenValue(final String einstellungenValue) {
        this.einstellungenValue = einstellungenValue;
    }




}
