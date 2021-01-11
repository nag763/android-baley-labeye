package com.ticandroid.baley_labeye.utils;

import android.util.Log;

import java.text.ParseException;
import java.util.Arrays;

/**
 * Util to cast classes to another
 *
 * @author Labeye
 */
public final class Caster {

    /**
     * Splittable element
     **/
    private static final String SPLITTER = ",";

    /**
     * Not instanciable class
     **/
    private Caster() {
    }

    /**
     * Parse the position as string to a array of double.
     *
     * @param position position to parse
     * @return position as double array
     */
    public static double[] positionToDoubleArray(String position) {

        try {
            final int numberOfSplittableRequired = 2;
            if (null == position || position.trim().isEmpty()) {
                throw new Exception("The string is empty");
            } else if (!position.contains(SPLITTER)) {
                throw new ParseException("Array doesn't contain the splitter museums", 0);
            } else if (position.split(SPLITTER).length != numberOfSplittableRequired) {
                throw new ParseException("Array got too many splittable args", position.lastIndexOf(SPLITTER));
            } else {
                return Arrays.stream(position.split(SPLITTER)).mapToDouble(Double::parseDouble).toArray();
            }
        } catch (Exception e) {
            Log.e("Parser", String.format("Exception occured\nException : %s", e));
            return null;
        }
    }
}
