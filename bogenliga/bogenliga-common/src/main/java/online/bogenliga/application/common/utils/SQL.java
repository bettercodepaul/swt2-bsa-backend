package online.bogenliga.application.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper-Klasse um fuer komplexe SQL Spalten-Eindeutigkeit herzustellen, indem
 * jeder Spalte der zugehoerige Tabellenname mit _ vorangestellt wird.
 *
 * @author Alexander Jost
 */
public class SQL {
    /**
     * Erzeugt aus allen Feldern der uebergebene Klasse, die nicht als transient
     * deklariert sind, einen eindeutigen Namen durch Voranstellen des
     * Klassennamens. Als Trennzeichen wird _ verwendet. Die Felder werden durch
     * ein Komma getrennt in einen String uebertragen.
     *
     * @param clazz Klasse, fuer die kommaseparierte Feldnamen erzeugt werden
     * @return kommaseparierte Liste der eindeutigen Feldnamen
     */
    public static final String uniqueSelect(final Class clazz) {
        boolean first = true;
        final String suffix = clazz.getSimpleName();
        final StringBuilder builder = new StringBuilder();
        final Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            final Field field = fields[i];
            if (isMappableField(field)) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }

                builder.append(suffix).append(".").append(field.getName()).append(" as ").append(suffix).append("_")
                        .append(field.getName());
            }
        }
        return builder.toString();
    }


    /**
     * Erzeugt eine Map, um aus einem ResultSet die eindeutigen Felder den
     * zugehoerigen Attributen in der Klasse zuordnen zu koennen. Als transient
     * deklarierte Felder werden nicht betrachtet.
     *
     * @param clazz Klasse, für die das Mapping erzeugt werden soll
     * @return Mapping zwischen eindeutigem Spaltennamen und zugehoerigem
     * Attributnamen
     */
    public static final Map<String, String> getColumnMapping(final Class clazz) {
        final Map<String, String> mapping = new HashMap();

        try {

            final Field[] fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                if (isMappableField(field)) {
                    final String fName = field.getName();
                    final String sqlName = clazz.getSimpleName() + "_" + fName;
                    mapping.put(sqlName, fName);
                }
            }

        } catch (SecurityException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return mapping;
    }


    /**
     * Baut ein INSERT INTO table_name (column1,column2,column3,...) VALUES (?,
     * ?, ?,...); aus dem uebergebenen Object. Fuer die ?-Parameter werden auch
     * die Werte in der richtigen Reihenfolge ermittelt.
     *
     * @param insertObj Object, fuer das das Statement gebaut wird
     * @return INSERT SQL und zugehoerige Parameterliste
     */
    public static final SQLWithParameter insertSQL(final Object insertObj) {
        return insertSQL(insertObj, null, Collections.emptyMap());
    }


    public static final SQLWithParameter insertSQL(final Object insertObj, final String tableName,
                                                   final Map<String, String> columnToFieldMapping) {
        final SQLWithParameter sqlWithParameter = new SQL().new SQLWithParameter();
        final StringBuilder sql = new StringBuilder();
        final StringBuilder values = new StringBuilder();
        final List<Object> para = new ArrayList<>();
        boolean first = true;
        String suffix;

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

            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                if (isMappableField(field)) {
                    final String fName = field.getName();


                    if (boolean.class.isAssignableFrom(field.getType())
                            || Boolean.class.isAssignableFrom(field.getType())) {
                        suffix = "is";
                    } else {
                        suffix = "get";
                    }

                    final String getterName = suffix + fName.substring(0, 1).toUpperCase() + fName.substring(1);
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
        } catch (SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        sql.append(values);
        sql.append(");");

        sqlWithParameter.sql = sql.toString();
        sqlWithParameter.parameter = para.toArray();

        return sqlWithParameter;
    }


    private static String resolveColumName(final String fieldName, final Map<String, String> columnToFieldMapping) {
        for (final Map.Entry<String, String> entry : columnToFieldMapping.entrySet()) {
            if (fieldName.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        return fieldName;
    }


    /**
     * Baut ein UPDATE table_name SET column1=?,column2=?,column3=?,...) WHERE
     * id = ?; aus dem uebergebenen Object. Fuer die ?-Parameter werden auch die
     * Werte in der richtigen Reihenfolge ermittelt.
     *
     * @param updateObj Object, fuer das das Statement gebaut wird
     * @return UPDATE SQL und zugehoerige Parameterliste
     */
    public static final SQLWithParameter updateSQL(final Object updateObj) {
        return updateSQL(updateObj, null, null, Collections.emptyMap());
    }


    public static final SQLWithParameter updateSQL(final Object updateObj, final String tableName,
                                                   final String fieldSelector,
                                                   final Map<String, String> columnToFieldMapping) {
        final SQLWithParameter sqlWithParameter = new SQL().new SQLWithParameter();
        final StringBuilder sql = new StringBuilder();
        final List<Object> para = new ArrayList<>();
        boolean first = true;
        String suffix;
        Object idValue = null;

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

            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                if (isMappableField(field)) {
                    final String fName = field.getName();
                    if (boolean.class.isAssignableFrom(field.getType())
                            || Boolean.class.isAssignableFrom(field.getType())) {
                        suffix = "is";
                    } else {
                        suffix = "get";
                    }

                    final String getterName = suffix + fName.substring(0, 1).toUpperCase() + fName.substring(1);
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
        } catch (SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        sql.append(" WHERE ");
        if (fieldSelector != null) {
            sql.append(resolveColumName(fieldSelector, columnToFieldMapping));
        } else {
            sql.append("id");
        }
        sql.append(" = ?;");
        para.add(idValue);

        sqlWithParameter.sql = sql.toString();
        sqlWithParameter.parameter = para.toArray();

        return sqlWithParameter;
    }


    public static final SQLWithParameter deleteSQL(final Object updateObj) {
        return deleteSQL(updateObj, null, null, null);
    }


    public static final SQLWithParameter deleteSQL(final Object updateObj, final String tableName,
                                                   final String fieldSelector,
                                                   final Map<String, String> columnToFieldMapping) {
        final SQLWithParameter sqlWithParameter = new SQL().new SQLWithParameter();
        final StringBuilder sql = new StringBuilder();
        final List<Object> para = new ArrayList<>();
        String suffix;
        Object idValue = null;

        sql.append("DELETE FROM ");

        try {
            final String tName = updateObj.getClass().getSimpleName();

            if (tableName != null) {
                sql.append(tableName);
            } else {
                sql.append(tName);
            }

            final Field[] fields = updateObj.getClass().getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                if (isMappableField(field)) {
                    final String fName = field.getName();

                    if (fName.equals("id") || fName.equals(fieldSelector)) {

                        if (boolean.class.isAssignableFrom(field.getType())
                                || Boolean.class.isAssignableFrom(field.getType())) {
                            suffix = "is";
                        } else {
                            suffix = "get";
                        }

                        final String getterName = suffix + fName.substring(0, 1).toUpperCase() + fName.substring(1);
                        final Method getter = updateObj.getClass().getDeclaredMethod(getterName);
                        final Object value = getter.invoke(updateObj);

                        if (fName.equals("id") || fName.equals(fieldSelector)) {
                            idValue = value;
                            continue;
                        }
                    }
                }
            }
        } catch (SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        sql.append(" WHERE ");
        if (fieldSelector != null) {
            sql.append(resolveColumName(fieldSelector, columnToFieldMapping));
        } else {
            sql.append("id");
        }
        sql.append(" = ?;");
        para.add(idValue);

        sqlWithParameter.sql = sql.toString();
        sqlWithParameter.parameter = para.toArray();

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


    public class SQLWithParameter {
        public String sql;
        public Object[] parameter;
    }

}
