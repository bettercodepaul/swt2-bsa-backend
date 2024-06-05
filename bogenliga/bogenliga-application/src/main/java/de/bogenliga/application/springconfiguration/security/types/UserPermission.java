package de.bogenliga.application.springconfiguration.security.types;

import org.springframework.security.core.GrantedAuthority;

/**
 * I contain the permissions of an user.
 * <p>
 * The permissions are configured in the database and stored in the JSON Web Token after the signIn.
 *
 * @author Andre Lehnert, BettercallPaul gmbh
 */
public enum UserPermission implements GrantedAuthority {

/* Default permission for:
 * Homepage anzeigen - /#/home
 * Liste der Regionen anzeigen - /#/regionen
 * Liste der Vereine anzeigen - /#/vereine
 * Liga-Tabelle anzeigen - /#/wettkaempfe
 * Wettkampfergebnisse der Mannschaften anzeigen - /#/mannschaften
 * Mitglieder der Mannschaften anzeigen - noch nicht implementiert
 * Ergebnisse eines Mannschaftsmitglieds anzeigen - noch nicht implementiert
 * Lesen in den Entitäten
 * DISZIPLIN
 * MANNSCHAFT
 * MANNSCHAFTSMITGLIED
 * MATCH
 * PASSE
 * REGION
 * VERANSTALTUNG
 * VEREIN
 * WETTKAMPF
 * WETTKAMPFTYP
 *
 * ggf. Attribute aus:
 * DSB-Mitglied (Name)
 *
 */

    CAN_READ_DEFAULT,


    /*
     * Berechtigung zum Lesen der Benutzer Stammdaten
     * Entitäten:
     * BENUTZER
     * BENUTZER-ROLLE
     * ROLLE
     * ROLLE-RECHT
     * RECHT
     */
    CAN_READ_STAMMDATEN,

    /*
     * Berechtigung zum Ändern der Stammdaten
     * Entitäten:
     *
     * DISZIPLIN
     * DSB-MITGLIED
     * LIGA
     * WETTKAMPFTYP
     */
    CAN_MODIFY_STAMMDATEN,


    /*
     * Berechtigung zum Löschen der Stammdaten
     * Entitäten:
     *
     * DISZIPLIN
     * DSB-MITGLIED
     * KLASSE
     * LIGA
     * WETTKAMPFTYP
     *
     * auch die Bewegungsdaten löschen gehört hierzu
     * VERANSTALTUNG
     * WETTKAMPF
     * MATCH
     * PASSE
     */
    CAN_DELETE_STAMMDATEN,

    /* Rechte zum Lesen/Ändern/Löschen der System-Konfiguration
    * Entitäten
    * CONFIGURATION
     * BENUTZER
     * BENUTZER-ROLLE
     * ROLLE
     * ROLLE-RECHT
     * RECHT
    *
    */
    CAN_READ_SYSTEMDATEN,
    CAN_MODIFY_SYSTEMDATEN,
    CAN_DELETE_SYSTEMDATEN,


    /*
     * Permissions to work with:
     * Wettkampf
     * Match
     * Passe
     * Kampfrichter
     */
    CAN_READ_WETTKAMPF,
    CAN_MODIFY_WETTKAMPF,

    /* spezielle Rechte, die eine zusätzliche Datenprüfung im Code voraussetzen
     * Sportleiter dürfen ihre Vereinsdaten, Mannschaften und Mannscjaftsmitlgider pflegen -
     * aber nur für den eigenen Verein. D.h. beim Init vom Dialog prüfen!!
     */

    CAN_READ_MY_VEREIN,
    CAN_MODIFY_MY_VEREIN,

    /* spezielle Rechte, die eine zusätzliche Datenprüfung im Code voraussetzen
     * Ligaleiter dürfen ihre Ligadaten, zugeordnete Mannschaften und Wettkampfdaten pflegen -
     * aber nur für den eigenen Event. D.h. beim Init vom Dialog prüfen!!
     */

    CAN_READ_MY_VERANSTALTUNG,
    CAN_MODIFY_MY_VERANSTALTUNG,

    /* spezielle Rechte, die eine zusätzliche Datenprüfung im Code voraussetzen
     * Ausrichter dürfen ihre Ligadaten, zugeordnete Mannschaften und Wettkampfdaten pflegen -
     * aber nur für den Event in ihrer Location. D.h. beim Init vom Dialog prüfen!!
     */

    CAN_READ_MY_ORT,
    CAN_MODIFY_MY_ORT,

    /*
     * Permissions to work with:
     * Veranstaltung
     * Wettkampftyp
     * Ligatabelle
     * Klasse
     * Disziplin
     */
    CAN_READ_SPORTJAHR,
    CAN_MODIFY_SPORTJAHR,

    /* technisches Recht für technischen User (Tablet-Einsätze)
    * Datenerfassung ermöglichen
     */
    CAN_OPERATE_SPOTTING,
    CAN_CREATE_STAMMDATEN,
    CAN_CREATE_SYSTEMDATEN,
    CAN_CREATE_WETTKAMPF,

    CAN_READ_DSBMITGLIEDER,
    CAN_CREATE_DSBMITGLIEDER,
    CAN_MODIFY_DSBMITGLIEDER,
    CAN_DELETE_DSBMITGLIEDER,

    CAN_CREATE_MANNSCHAFT,

    CAN_CREATE_VEREIN_DSBMITGLIEDER,
    CAN_MODIFY_VEREIN_DSBMITGLIEDER,

    CAN_DELETE_MANNSCHAFT,
    CAN_MODIFY_MANNSCHAFT,
    CAN_MODIFY_MY_WETTKAMPF,

    CAN_MODIFY_SYSTEMDATEN_LIGALEITER,
    CAN_CREATE_SYSTEMDATEN_LIGALEITER,
    CAN_CREATE_STAMMDATEN_LIGALEITER,
    CAN_MODIFY_STAMMDATEN_LIGALEITER,

    /*Spezielles Recht für den Ligaleiter der untersten Region, der eine neue Liga als unterste Liga anlegen darf und diese bearbeiten kann*/
    CAN_CREATE_MY_LIGA,
    CAN_READ_MY_LIGA,
    CAN_MODIFY_MY_LIGA
    ;


    private static final String PREFIX = "ROLE_";


    /**
     * Get the enum of a given value.
     *
     * @param value string representation of the permission
     *
     * @return enum of the value or null if not match is found
     */
    public static UserPermission fromValue(final String value) {
        for (final UserPermission userPermission : UserPermission.values()) {
            final String permissionWithPrefix = PREFIX + userPermission.name();
            final String permissionWithoutPrefix = userPermission.name();

            if (permissionWithPrefix.equalsIgnoreCase(value)
                    || permissionWithoutPrefix.equalsIgnoreCase(value)) {
                return userPermission;
            }
        }
        return null;
    }


    @Override
    public String getAuthority() {
        return PREFIX + name();
    }

}
