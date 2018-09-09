package online.bogenliga.application.common.database;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import online.bogenliga.application.common.errorhandling.exception.TechnicalException;

/**
 * Helper-Klasse um fuer komplexe SQL Spalten-Eindeutigkeit herzustellen, indem
 * jeder Spalte der zugehoerige Tabellenname mit _ vorangestellt wird.
 *
 * @author Alexander Jost
 */
public final class SQL {


    /**
     * Baut ein INSERT INTO table_name (column1, column2, column3,...) VALUES (?, ?, ?,...); aus dem uebergebenen
     * Object.
     * Fuer die ?-Parameter werden auch die Werte in der richtigen Reihenfolge ermittelt.
     *
     * @param insertObj Object, fuer das das Statement gebaut wird
     * @return INSERT SQL und zugehoerige Parameterliste
     */
    public static SQLWithParameter insertSQL(final Object insertObj) {
        return insertSQL(insertObj, null, Collections.emptyMap());
    }


    /**
     * Baut ein INSERT INTO {table_name} ({column1}, {column2}, {column3},...) VALUES (?, ?, ?,...);
     * aus dem uebergebenen Object.
     * Fuer die ?-Parameter werden die Werte in der richtigen Reihenfolge ermittelt.
     *
     * Die Bezeichnungen im Object (Klassenname und Parameternamen) koennen von den Bezeichnungen in der Datenbank
     * abweichen.
     *
     * Die Platzhalter fuer den Tabellennamen {table_name} wird durch {@code tableName} ersetzt.
     * Die Platzhalter fuer die Spalten {column1} werden ueber das {@code columnToFieldMapping} aufgeloest.
     *
     * @param insertObj            Object, fuer das das Statement gebaut wird
     * @param tableName            Definiert den Tabellennamen für das Object
     * @param columnToFieldMapping Definiert die Spaltennamen für die Object Parameter
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
            final Field[] fields = insertObj.getClass().getDeclaredFields();

            appendFieldsToInsertStatement(insertObj, columnToFieldMapping, sql, values, para, fields);

        } catch (final SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new TechnicalException(e);
        }

        sql.append(values);
        sql.append(");");

        sqlWithParameter.setSql(sql.toString());
        sqlWithParameter.setParameter(para.toArray());

        return sqlWithParameter;
    }


    /**
     * Baut ein UPDATE table_name SET column1=?,column2=?,column3=?,...) WHERE
     * id = ?; aus dem uebergebenen Object. Fuer die ?-Parameter werden auch die
     * Werte in der richtigen Reihenfolge ermittelt.
     *
     * @param updateObj Object, fuer das das Statement gebaut wird
     * @return UPDATE SQL und zugehoerige Parameterliste
     */
    public static SQLWithParameter updateSQL(final Object updateObj) {
        return updateSQL(updateObj, null, null, Collections.emptyMap());
    }


    /**
     * Baut ein UPDATE {table_name} SET {column1}=?, {column2}=?, {column3}=?, ...) WHERE {fieldSelector} = ?;
     * aus dem uebergebenen Object.
     * Fuer die ?-Parameter werden auch die Werte in der richtigen Reihenfolge ermittelt.
     *
     * Die Bezeichnungen im Object (Klassenname und Parameternamen) koennen von den Bezeichnungen in der Datenbank
     * abweichen.
     *
     * Der Platzhalter fuer den Tabellennamen {table_name} wird durch {@code tableName} ersetzt.
     * Die Platzhalter fuer die Spalten {column1} werden ueber das {@code columnToFieldMapping} aufgeloest.
     * Der Platzhalter fuer den Identifier {fieldSelector} wird durch {@code fieldSelector} ersetzt.
     *
     * @param updateObj            Object, fuer das das Statement gebaut wird
     * @param tableName            Definiert den Tabellennamen für das Object
     * @param fieldSelector        Definiert den Identifier fuer die betroffene Zeile
     * @param columnToFieldMapping Definiert die Spaltennamen für die Object Parameter
     * @return UPDATE SQL und zugehoerige Parameterliste
     */
    public static SQLWithParameter updateSQL(final Object updateObj, final String tableName,
                                             final String fieldSelector,
                                             final Map<String, String> columnToFieldMapping) {
        final SQLWithParameter sqlWithParameter = new SQL().new SQLWithParameter();
        final StringBuilder sql = new StringBuilder();
        final List<Object> para = new ArrayList<>();
        final Object idValue;
        sql.append("UPDATE ");

        try {
            final String tName = updateObj.getClass().getSimpleName();

            if (tableName != null) {
                sql.append(tableName);
            } else {
                sql.append(tName);
            }

            sql.append(" SET ");
            final Field[] fields = updateObj.getClass().getDeclaredFields();

            idValue = appendFieldsToUpdateStatement(updateObj, fieldSelector, columnToFieldMapping, sql, para, fields);
        } catch (final SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new TechnicalException(e);
        }

        sql.append(" WHERE ");
        if (fieldSelector != null) {
            sql.append(resolveColumName(fieldSelector, columnToFieldMapping));
        } else {
            sql.append("id");
        }
        sql.append(" = ?;");
        para.add(idValue);

        sqlWithParameter.setSql(sql.toString());
        sqlWithParameter.setParameter(para.toArray());

        return sqlWithParameter;
    }


    /**
     * Baut ein DELETE FROM table_name WHERE id = ?; aus dem uebergebenen Object.
     * Fuer die ?-Parameter werden auch die Werte in der richtigen Reihenfolge ermittelt.
     *
     * @param updateObj Object, fuer das das Statement gebaut wird
     * @return DELETE SQL und zugehoerige Parameterliste
     */
    public static SQLWithParameter deleteSQL(final Object updateObj) {
        return deleteSQL(updateObj, null, null, null);
    }


    /**
     * Baut ein DELETE FROM {table_name} WHERE {fieldSelector} = ?; aus dem uebergebenen Object.
     * Fuer die ?-Parameter werden auch die Werte in der richtigen Reihenfolge ermittelt.
     *
     * Die Bezeichnungen im Object (Klassenname und Parameternamen) koennen von den Bezeichnungen in der Datenbank
     * abweichen.
     *
     * Der Platzhalter fuer den Tabellennamen {table_name} wird durch {@code tableName} ersetzt.
     * Der Platzhalter fuer den Identifier {fieldSelector} wird durch {@code fieldSelector} ersetzt.
     *
     * @param updateObj            Object, fuer das das Statement gebaut wird
     * @param tableName            Definiert den Tabellennamen für das Object
     * @param fieldSelector        Definiert den Identifier fuer die betroffene Zeile
     * @param columnToFieldMapping Definiert die Spaltennamen für die Object Parameter.
     *                             Auch der {@code fieldSelector} wird mit diesem Mapping konvertiert.
     * @return DELETE SQL und zugehoerige Parameterliste
     */
    public static SQLWithParameter deleteSQL(final Object updateObj, final String tableName,
                                             final String fieldSelector,
                                             final Map<String, String> columnToFieldMapping) {
        final SQLWithParameter sqlWithParameter = new SQL().new SQLWithParameter();
        final StringBuilder sql = new StringBuilder();
        final List<Object> para = new ArrayList<>();
        final Object idValue;

        sql.append("DELETE FROM ");

        try {
            final String tName = updateObj.getClass().getSimpleName();

            if (tableName != null) {
                sql.append(tableName);
            } else {
                sql.append(tName);
            }

            final Field[] fields = updateObj.getClass().getDeclaredFields();

            idValue = appendFieldsToDeleteStatement(updateObj, fieldSelector, fields);
        } catch (final SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new TechnicalException(e);
        }

        sql.append(" WHERE ");
        if (fieldSelector != null) {
            sql.append(resolveColumName(fieldSelector, columnToFieldMapping));
        } else {
            sql.append("id");
        }
        sql.append(" = ?;");
        para.add(idValue);

        sqlWithParameter.setSql(sql.toString());
        sqlWithParameter.setParameter(para.toArray());

        return sqlWithParameter;
    }


    /**
     * Checks if the given field can be mapped. Only non-transient non-static fields can be mapped types the data base.
     *
     * @param field a field
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

                final String getterName = retrieveGetterName(field, fName);
                final Method getter = insertObj.getClass().getDeclaredMethod(getterName);
                Object value = getter.invoke(insertObj);

                if (fName.equals("id")) {
                    continue;
                } else if (value != null && value.getClass().isEnum()) {
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


    private static String resolveColumName(final String fieldName, final Map<String, String> columnToFieldMapping) {
        for (final Map.Entry<String, String> entry : columnToFieldMapping.entrySet()) {
            if (fieldName.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        return fieldName;
    }


    private static Object appendFieldsToUpdateStatement(final Object updateObj, final String fieldSelector,
                                                        final Map<String, String> columnToFieldMapping,
                                                        final StringBuilder sql,
                                                        final List<Object> para,
                                                        final Field[] fields)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        boolean first = true;
        Object idValue = null;
        for (final Field field : fields) {
            if (isMappableField(field)) {
                final String fName = field.getName();
                final String getterName = retrieveGetterName(field, fName);
                final Method getter = updateObj.getClass().getDeclaredMethod(getterName);
                Object value = getter.invoke(updateObj);

                if (fName.equals("id") || fName.equals(fieldSelector)) {
                    idValue = value;
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
        return idValue;
    }


    private static Object appendFieldsToDeleteStatement(final Object updateObj, final String fieldSelector,
                                                        final Field[] fields)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object idValue = null;

        String identifier = "id";
        if (fieldSelector != null && !fieldSelector.equals(identifier)) {
            identifier = fieldSelector;
        }

        for (final Field field : fields) {
            if (isMappableField(field)) {
                final String fName = field.getName();


                if (fName.equals(identifier)) {

                    final String getterName = retrieveGetterName(field, fName);
                    final Method getter = updateObj.getClass().getDeclaredMethod(getterName);

                    Object value = getter.invoke(updateObj);

                    if (value != null && value.getClass().isEnum()) {
                        value = ((Enum) value).name();
                    }

                    idValue = value;

                }
            }
        }
        return idValue;
    }


    /**
     * Private Klasse mit dem SQL-Statement und der Parameter-Liste.
     *
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
