package com.ticandroid.baley_labeye.utils;

/**
 * Safe setters for logo and integers, fixing FB possible issues when automaticly setting fields
 *
 * @author Labeye
 * @version 0.1
 */
public final class Setters {

    /**
     * Private constructor to avoid class instanciation
     **/
    private Setters() {
    }

    /**
     * Give a default value if null is given
     *
     * @param value        to return as int
     * @param defaultValue if the argument isn't an int or null value
     * @param nullValue    value which has to be replaced by the default
     * @return an integer value
     */
    public static int valueOrDefault(Object value, int defaultValue, int nullValue) {
        if (value instanceof Integer && !value.equals(nullValue)) {
            return (int) value;
        } else {
            return defaultValue;
        }
    }


    /**
     * Give a default value if null is given
     *
     * @param value        to return as double
     * @param defaultValue if the argument isn't a double or null value
     * @param nullValue    value which has to be replaced by the default
     * @return a double value
     */
    public static String valueOrDefault(Object value, String defaultValue, String nullValue) {
        if (value instanceof String && !value.equals(nullValue)) {
            return String.valueOf(value);
        } else {
            return defaultValue;
        }
    }


    /**
     * Give a default value if null is given
     *
     * @param value        to return as string
     * @param defaultValue if the argument isn't a string or null value
     * @param nullValue    value which has to be replaced by the default
     * @return a string value
     */
    public static double valueOrDefault(Object value, double defaultValue, double nullValue) {
        if (value instanceof Double && !value.equals(nullValue)) {
            return (double) value;
        } else {
            return defaultValue;
        }
    }

    /**
     * Give a default value if null is given
     *
     * @param value        to return as long
     * @param defaultValue if the argument isn't a long or null value
     * @param nullValue    value which has to be replaced by the default
     * @return a long value
     */
    public static long valueOrDefault(Object value, long defaultValue, long nullValue) {
        if (value instanceof Long && !value.equals(nullValue)) {
            return (long) value;
        } else {
            return defaultValue;
        }
    }

    /**
     * Returns an integer value
     *
     * @param value to return
     * @return value if integer, 0 if null
     */
    public static int valueOrDefaultInteger(Object value) {
        return valueOrDefault(value, 0, 0);
    }

    /**
     * Returns a long value
     *
     * @param value to return
     * @return value if long, 0 if null
     */
    public static long valueOrDefaultLong(Object value) {
        return valueOrDefault(value, 0, 0);
    }

    /**
     * Returns a double value
     *
     * @param value to return
     * @return value if double, 0d if null
     */
    public static double valueOrDefaultDouble(Object value) {
        return valueOrDefault(value, 0d, 0d);
    }

    /**
     * Returns a string value
     *
     * @param value to return
     * @return value if string, "" if null
     */
    public static String valueOrDefaultString(Object value) {
        return valueOrDefault(value, "", "");
    }

    /**
     * Returns a logo value
     *
     * @param value to return
     * @return value if logo, "et_inconnu" if null
     */
    public static String valueOrDefaultLogo(Object value) {
        return valueOrDefault(value, "et_inconnu", "");
    }
}
