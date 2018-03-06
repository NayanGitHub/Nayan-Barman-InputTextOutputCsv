package com.nayan.util;
/*
 *
 *   Copyright Utilibill  10/05/2010
 *
 */


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	 /**
     * is string null or zero length?
     *
     * @param s
     * @return true if string is null or zero length
     */
    public static boolean isNullOrEmpty(String s) {
        return (s == null || s.length() == 0);
    }
	 /**
     * create a space separated string from all these strings, but only one
     * space per break, and no spaces if a string is null
     *
     * @param strings
     * @return
     */
    public static String concatWithSpaces(String... strings) {
        StringBuilder out = new StringBuilder();
        for (String str : strings) {
            if (out.toString().length() > 0 && !isNullOrEmpty(str)) {
                out.append(" ");
            }

            if (!isNullOrEmpty(str)) {
                out.append(str.trim());
            }
        }

        return out.toString();
    }
    /**
     * if string is null, return an empty string, otherwise return the string
     *
     * @param s
     * @return if string is null, return an empty string, otherwise return the
     * string
     */
    public static String nullToEmpty(String s) {
        return (s == null ? "" : s);
    }
    /**
     * create a comma separated string from all these strings
     *
     * @param showNulls
     * @param noSpaces
     * @param strings
     * @return
     */
    public static String concatWithCommas(boolean showNulls, boolean noSpaces, String... strings) {
        StringBuilder out = new StringBuilder();
        int pos = 0;
        for (String str : strings) {

            if (pos > 0 && (showNulls || !isNullOrEmpty(str))) {
                out.append(",");
                if (!noSpaces) {
                    out.append(" ");
                }
            }

            if (showNulls || !isNullOrEmpty(str)) {
                out.append(StringUtil.nullToEmpty(str));
            }
            pos++;
        }

        return out.toString();
    }
}
