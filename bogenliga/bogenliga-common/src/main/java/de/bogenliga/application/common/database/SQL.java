package de.bogenliga.application.common.database;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import de.bogenliga.application.common.component.entity.CommonBusinessEntity;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;

/**
 * Helper-Klasse um fuer komplexe SQL Spalten-Eindeutigkeit herzustellen, indem jeder Spalte der zugehoerige
 * Tabellenname mit _ vorangestellt wird.
 *
 * @author Alexander Jost
 */
public final class SQL {


    private static final String VERSION = "version";


    /**
     * Baut ein SELECT version FROM table_name WHERE <fieldSelector> = ?; aus dem uebergebenen Object. Fuer die
     * ?-Parameter werden auch die Werte in der richtigen Reihenfolge ermittelt.
     *
     * @param selectObj            Object, fuer das das Statement gebaut wird
     * @param tableName            Tabellenname, falls abweichend vom Object-Namen
     * @param fieldSelector        Selektor für eine Zeile, falls abweichend von "id"
     * @param columnToFieldMapping Mapping zwischen Object-Parameternamen und Tabellen-Spaltennamen
     *
     * @return SELECT SQL und zugehoerige Parameterliste
     */
    public static SQLWithParameter selectSQL(final Object selectObj, final String tableName,
                                             final String[] fieldSelector,
                                             final Map<String, String> columnToFieldMapping) {
        final SQLWithParameter sqlWithParameter = new SQL().new SQLWithParameter();
        final StringBuilder sql = new StringBuilder();
        final List<Object> para = new ArrayList<>();

        sql.append("SELECT *");

        try {

            sql.append(" FROM ");

            final Field[] fields = getAllFields(selectObj);
            for (String selector : fieldSelector) {
                para.add(findFieldSelectorValue(selectObj, selector, fields));
            }

            final String tName = selectObj.getClass().getSimpleName();

            if (tableName != null) {
                sql.append(tableName);
            } else {
                sql.append(tName);
            }

        } catch (final SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, e);
        }

        sqlWithParameter.setParameter(para.toArray());
        return appendWhereStatements(sql, fieldSelector, columnToFieldMapping, sqlWithParameter);
    }


    /**
     * @param updateObj das objekt in welchem die Felder gesucht werden
     *
     * @return alle felder und felder der oberklasse
     */
    private static Field[] getAllFields(Object updateObj) {
        Class startClass = updateObj.getClass();
        List<Field> currentClassFields = new ArrayList<>();
        currentClassFields.addAll(Arrays.asList(startClass.getDeclaredFields()));

        Class superclass = startClass.getSuperclass();
        if (superclass != null && superclass == CommonBusinessEntity.class) {
            currentClassFields.addAll(Arrays.asList(superclass.getDeclaredFields()));
        }
        Field[] fields = new Field[currentClassFields.size()];
        return currentClassFields.toArray(fields);
    }


    /**
     * Überprüft ob die Felder der Tabelle geupdatet werden dürfen
     *
     * @param fName name des feldes
     *
     * @return checkt ob das Feld geupdatet werden darf
     */
    private static boolean isUpdatableField(String fName) {
        return !fName.equals("createdAtUtc")
                && !fName.equals("createdByUserId")
                && !fName.equals(VERSION);
    }


    /**
     * searchs for a method, including the first super class
     *
     * @param obj   the object to look for the method
     * @param field the of which the getter is searched
     * @param name  the name of the field
     *
     * @return
     *
     * @throws NoSuchMethodException
     */
    private static Method getGetterMethod(Object obj, Field field, String name) throws NoSuchMethodException {

        final String getterName = retrieveGetterName(field, name);
        Method getter;
        try {
            getter = obj.getClass().getDeclaredMethod(getterName);
        } catch (NoSuchMethodException e) {
            // if it's a method of the superclass CommonBusinessEntity
            if (obj.getClass().getSuperclass() == CommonBusinessEntity.class) {
                getter = obj.getClass().getSuperclass().getDeclaredMethod(getterName);
            } else {
                throw new NoSuchMethodException(Arrays.toString(e.getStackTrace()));
            }

        }

        return getter;
    }


    /**
     * @param selectObj     das Objekt  von welchem der Wert genommen wird
     * @param fieldSelector das Feld nach welchem die Entität genommen wird
     * @param fields        Alle Felder die durchsucht werden
     *
     * @return der Wert des Feldes
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static Object findFieldSelectorValue(final Object selectObj, final String fieldSelector,
                                                 final Field[] fields) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (final Field field : fields) {
            if (isMappableField(field)) {
                final String fName = field.getName();
                if (fName.equals("id") || fName.equals(fieldSelector)) {
                    final Method getter = getGetterMethod(selectObj, field, fName);
                    final Object value = getter.invoke(selectObj);
                    return value;
                }
            }

        }
        throw new NoSuchMethodException("fieldSelector doesn't exist");
    }


    private static Object appendFieldsToSelectStatement(final Object selectObj, final String fieldSelector,
                                                        final Map<String, String> columnToFieldMapping,
                                                        final StringBuilder sql,
                                                        final Field[] fields)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        boolean first = true;
        Object idValue = null;
        for (final Field field : fields) {
            if (isMappableField(field)) {
                final String fName = field.getName();
                final Method getter = getGetterMethod(selectObj, field, fName);
                final Object value = getter.invoke(selectObj);

                if (fName.equals("id") || fName.equals(fieldSelector)) {
                    idValue = value;
                }

                if (first) {
                    first = false;
                } else {
                    sql.append(", ");
                }

                final String columnName = resolveColumName(fName, columnToFieldMapping);

                sql.append(columnName);
            }
        }
        return idValue;
    }


    /**
     * Baut ein INSERT INTO table_name (column1, column2, column3,...) VALUES (?, ?, ?,...); aus dem uebergebenen
     * Object. Fuer die ?-Parameter werden auch die Werte in der richtigen Reihenfolge ermittelt.
     *
     * @param insertObj Object, fuer das das Statement gebaut wird
     *
     * @return INSERT SQL und zugehoerige Parameterliste
     */
    public static SQLWithParameter insertSQL(final Object insertObj) {
        return insertSQL(insertObj, null, Collections.emptyMap());
    }


    /**
     * Baut ein INSERT INTO {table_name} ({column1}, {column2}, {column3},...) VALUES (?, ?, ?,...); aus dem
     * uebergebenen Object. Fuer die ?-Parameter werden die Werte in der richtigen Reihenfolge ermittelt.
     * <p>
     * Die Bezeichnungen im Object (Klassenname und Parameternamen) koennen von den Bezeichnungen in der Datenbank
     * abweichen.
     * <p>
     * Die Platzhalter fuer den Tabellennamen {table_name} wird durch {@code tableName} ersetzt. Die Platzhalter fuer
     * die Spalten {column1} werden ueber das {@code columnToFieldMapping} aufgeloest.
     *
     * @param insertObj            Object, fuer das das Statement gebaut wird
     * @param tableName            Definiert den Tabellennamen für das Object
     * @param columnToFieldMapping Definiert die Spaltennamen für die Object Parameter
     *
     * @return INSERT SQL und zugehoerige Parameterliste
     */
    public static SQLWithParameter insertSQL(final Object insertObj, final String tableName,
                                             final Map<String, String> columnToFieldMapping) {
        final SQLWithParameter sqlWithParameter = new SQL().new SQLWithParameter();
        final StringBuilder sql = new StringBuilder();
        final StringBuilder values = new StringBuilder();
        final List<Object> para = new ArrayList<>();

        sql.append("INSERT INTO ");
        values.append(") VALUES (");

        try {
            final String tName = insertObj.getClass().getSimpleName();

            if (tableName != null) {
                sql.append(tableName);
            } else {
                sql.append(tName);
            }

            sql.append(" (");
            final Field[] fields = getAllFields(insertObj);


            appendFieldsToInsertStatement(insertObj, columnToFieldMapping, sql, values, para, fields);

        } catch (final SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, e);
        }

        sql.append(values);
        sql.append(");");

        sqlWithParameter.setSql(sql.toString());
        sqlWithParameter.setParameter(para.toArray());

        return sqlWithParameter;
    }


    /**
     * Baut ein UPDATE table_name SET column1=?,column2=?,column3=?,...) WHERE id = ?; aus dem uebergebenen Object. Fuer
     * die ?-Parameter werden auch die Werte in der richtigen Reihenfolge ermittelt.
     *
     * @param updateObj Object, fuer das das Statement gebaut wird
     *
     * @return UPDATE SQL und zugehoerige Parameterliste
     */
    public static SQLWithParameter updateSQL(final Object updateObj) {
        return updateSQL(updateObj, null, null, Collections.emptyMap());
    }


    /**
     * Baut ein UPDATE {table_name} SET {column1}=?, {column2}=?, {column3}=?, ...) WHERE {fieldSelector} = ?; aus dem
     * uebergebenen Object. Fuer die ?-Parameter werden auch die Werte in der richtigen Reihenfolge ermittelt.
     * <p>
     * Die Bezeichnungen im Object (Klassenname und Parameternamen) koennen von den Bezeichnungen in der Datenbank
     * abweichen.
     * <p>
     * Der Platzhalter fuer den Tabellennamen {table_name} wird durch {@code tableName} ersetzt. Die Platzhalter fuer
     * die Spalten {column1} werden ueber das {@code columnToFieldMapping} aufgeloest. Der Platzhalter fuer den
     * Identifier {fieldSelector} wird durch {@code fieldSelector} ersetzt.
     *
     * @param updateObj            Object, fuer das das Statement gebaut wird
     * @param tableName            Definiert den Tabellennamen für das Object
     * @param fieldSelector        Definiert den Identifier fuer die betroffene Zeile
     * @param columnToFieldMapping Definiert die Spaltennamen für die Object Parameter
     *
     * @return UPDATE SQL und zugehoerige Parameterliste
     */
    public static SQLWithParameter updateSQL(final Object updateObj, final String tableName,
                                             final String fieldSelector[],
                                             final Map<String, String> columnToFieldMapping) {
        final StringBuilder sql = new StringBuilder();
        final List<Object> para = new ArrayList<>();
        sql.append("UPDATE ");

        try {
            final String tName = updateObj.getClass().getSimpleName();

            if (tableName != null) {
                sql.append(tableName);
            } else {
                sql.append(tName);
            }

            sql.append(" SET ");
            final Field[] fields = getAllFields(updateObj);

            return appendFieldsToUpdateStatement(updateObj, fieldSelector, columnToFieldMapping, sql, para, fields);
        } catch (final SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, e);
        }
    }


    /**
     * @param sql der sql string
     * @param fieldSelector Felder welche beim where statement gesucht werden
     * @param columnToFieldMapping Felder mapper um auf die Datenbank zu mappen
     * @param sqlWithParameter der schlussendlicher SQL mit String und Parametern
     *
     * @return
     */
    private static SQLWithParameter appendWhereStatements(StringBuilder sql, String[] fieldSelector,
                                                          Map<String, String> columnToFieldMapping,
                                                          SQLWithParameter sqlWithParameter) {
        sql.append(" WHERE ");
        int i = 0;
        for (String selector : fieldSelector) {
            if (i > 0) {
                sql.append(" AND ");
            }

            if (selector != null) {
                sql.append(resolveColumName(selector, columnToFieldMapping));
            } else {
                throw new TechnicalException(ErrorCode.DATABASE_ERROR, "Field selector specified not found");
            }
            sql.append(" = ? ");
            i++;
        }

        sql.append(";");

        sqlWithParameter.setSql(sql.toString());

        return sqlWithParameter;
    }


    /**
     * Das hier sollte nie wieder verwendet werden. Die wenigsten Tabellen verwenden ein die wirklich ID heißt
     *
     * Baut ein DELETE FROM table_name WHERE id = ?; aus dem uebergebenen Object. Fuer die ?-Parameter werden auch die
     * Werte in der richtigen Reihenfolge ermittelt.
     *
     * @param updateObj Object, fuer das das Statement gebaut wird
     *
     * @return DELETE SQL und zugehoerige Parameterliste

    public static SQLWithParameter deleteSQL(final Object updateObj) {
        return deleteSQL(updateObj, null, null, null);
    }
    */


    /**
     * Baut ein DELETE FROM {table_name} WHERE {fieldSelector} = ?; aus dem uebergebenen Object. Fuer die ?-Parameter
     * werden auch die Werte in der richtigen Reihenfolge ermittelt.
     * <p>
     * Die Bezeichnungen im Object (Klassenname und Parameternamen) koennen von den Bezeichnungen in der Datenbank
     * abweichen.
     * <p>
     * Der Platzhalter fuer den Tabellennamen {table_name} wird durch {@code tableName} ersetzt. Der Platzhalter fuer
     * den Identifier {fieldSelector} wird durch {@code fieldSelector} ersetzt.
     *
     * @param updateObj            Object, fuer das das Statement gebaut wird
     * @param tableName            Definiert den Tabellennamen für das Object
     * @param fieldSelector        Definiert den Identifier fuer die betroffene Zeile
     * @param columnToFieldMapping Definiert die Spaltennamen für die Object Parameter. Auch der {@code fieldSelector}
     *                             wird mit diesem Mapping konvertiert.
     *
     * @return DELETE SQL und zugehoerige Parameterliste
     */
    public static SQLWithParameter deleteSQL(final Object updateObj, final String tableName,
                                             final String[] fieldSelector,
                                             final Map<String, String> columnToFieldMapping) {
        final SQLWithParameter sqlWithParameter = new SQL().new SQLWithParameter();
        final StringBuilder sql = new StringBuilder();
        final List<Object> para;

        sql.append("DELETE FROM ");

        try {
            final String tName = updateObj.getClass().getSimpleName();

            if (tableName != null) {
                sql.append(tableName);
            } else {
                sql.append(tName);
            }

            final Field[] fields = updateObj.getClass().getDeclaredFields();

            para = appendFieldsToDeleteStatement(updateObj, fieldSelector, fields);
        } catch (final SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new TechnicalException(ErrorCode.DATABASE_ERROR, e);
        }

        sqlWithParameter.setParameter(para.toArray());
        return appendWhereStatements(sql, fieldSelector, columnToFieldMapping, sqlWithParameter);
    }


    /**
     * Checks if the given field can be mapped. Only non-transient non-static fields can be mapped types the data base.
     *
     * @param field a field
     *
     * @return <code>true</code> if the field can be mapped, <code>false</code> otherwise
     */
    private static boolean isMappableField(final Field field) {
        return !Modifier.isTransient(field.getModifiers()) && !Modifier.isStatic(field.getModifiers());
    }


    private static void appendFieldsToInsertStatement(final Object insertObj,
                                                      final Map<String, String> columnToFieldMapping,
                                                      final StringBuilder sql, final StringBuilder values,
                                                      final List<Object> para,
                                                      final Field[] fields)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        boolean first = true;
        for (final Field field : fields) {
            if (isMappableField(field)) {
                final String fName = field.getName();

                if (!VERSION.equals(fName)) {
                    final Method getter = getGetterMethod(insertObj, field, fName);
                    Object value = getter.invoke(insertObj);

                    if (fName.equals("id") || Objects.isNull(value)) {
                        continue;
                    } else if (value.getClass().isEnum()) {
                        value = ((Enum) value).name();
                    }

                    if (first) {
                        first = false;
                    } else {
                        sql.append(", ");
                        values.append(", ");
                    }

                    final String columnName = resolveColumName(fName, columnToFieldMapping);

                    sql.append(columnName);
                    values.append("?");
                    para.add(value);
                }
            }
        }
    }


    /**
     *
     * @param field Das Feld von welchem der Getter gesucht wird
     * @param fName Name des Attributs
     * @return den String des Getters
     */
    private static String retrieveGetterName(final Field field, final String fName) {
        final String suffix;
        if (boolean.class.isAssignableFrom(field.getType())
                || Boolean.class.isAssignableFrom(field.getType())) {
            suffix = "is";
        } else {
            suffix = "get";
        }

        return suffix + fName.substring(0, 1).toUpperCase() + fName.substring(1);
    }


    /**
     *
     * @param fieldName das feld
     * @param columnToFieldMapping das mapping
     * @return der Name aus der Datenbank durch das mapping
     */
    private static String resolveColumName(final String fieldName, final Map<String, String> columnToFieldMapping) {
        for (final Map.Entry<String, String> entry : columnToFieldMapping.entrySet()) {
            if (fieldName.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        return fieldName;
    }


    /**
     *
     * @param updateObj das Objekt in welchem gesucht wird
     * @param fieldSelector die Felder in welchen gesucht wird
     * @param columnToFieldMapping das mapping
     * @param sql der StringBuilder um den SQL aufzubauen
     * @param para die Parameter welche erweitert werden müssen
     * @param fields die Felder in welchen gesucht werden soll
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static SQLWithParameter appendFieldsToUpdateStatement(final Object updateObj, final String[] fieldSelector,
                                                                  final Map<String, String> columnToFieldMapping,
                                                                  final StringBuilder sql,
                                                                  final List<Object> para,
                                                                  final Field[] fields)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ArrayList<String> fieldselector = new ArrayList<>(Arrays.asList(fieldSelector));
        ArrayList<Object> params = new ArrayList<>();
        boolean first = true;
        for (final Field field : fields) {
            if (isMappableField(field)) {
                final String fName = field.getName();

                if (!VERSION.equals(fName) && isUpdatableField(fName)) {

                    final Method getter = getGetterMethod(updateObj, field, fName);
                    Object value = getter.invoke(updateObj);

                    if (fName.equals("id") || fieldselector.contains(fName)) {
                        params.add(value);
                        continue;
                    } else if (value != null && value.getClass().isEnum()) {
                        value = ((Enum) value).name();
                    }

                    if (first) {
                        first = false;
                    } else {
                        sql.append(", ");
                    }

                    final String columnName = resolveColumName(fName, columnToFieldMapping);

                    sql.append(columnName).append("=").append("?");
                    para.add(value);
                }
            }
        }
        para.addAll(params);

        final SQLWithParameter sqlWithParameter = new SQL().new SQLWithParameter();
        sqlWithParameter.setSql(sql.toString());
        sqlWithParameter.setParameter(para.toArray());
        return appendWhereStatements(sql, fieldSelector, columnToFieldMapping, sqlWithParameter);
    }


    /**
     *
     * @param updateObj das Objekt das deletet werden soll
     * @param selectors die Felder nach welchen die Entität gesucht wird
     * @param fields die Felder welche durchsucht werden
     * @return Alle Werte welche in den Feldselectoren im Objekt zu finden sind
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static List<Object> appendFieldsToDeleteStatement(final Object updateObj, final String[] selectors,
                                                              final Field[] fields)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Object> idValue = new ArrayList<>();
        for (String fieldSelector : selectors) {
            String identifier = "id";
            if (fieldSelector != null && !fieldSelector.equals(identifier)) {
                identifier = fieldSelector;
            }

            for (final Field field : fields) {
                if (isMappableField(field)) {
                    final String fName = field.getName();

                    if (!VERSION.equals(fName) && fName.equals(identifier)) {

                        final String getterName = retrieveGetterName(field, fName);
                        final Method getter = updateObj.getClass().getDeclaredMethod(getterName);

                        Object value = getter.invoke(updateObj);

                        if (value != null && value.getClass().isEnum()) {
                            value = ((Enum) value).name();
                        }

                        idValue.add(value);
                    }
                }
            }
        }
        return idValue;
    }


    /**
     * Private Klasse mit dem SQL-Statement und der Parameter-Liste.
     * <p>
     * Die Parameter koennen der Reihe nach in die ?-Parameter-Platzhalter im SQL-Statement eingefuegt werden.
     */
    public class SQLWithParameter {
        private String sql;
        private Object[] parameter;


        public String getSql() {
            return sql;
        }


        void setSql(final String sql) {
            this.sql = sql;
        }


        public Object[] getParameter() {
            return parameter;
        }


        void setParameter(final Object[] parameter) {
            this.parameter = parameter;
        }
    }

}
