/*
 * Copyright (C) 2015 essobedo.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.github.essobedo.lc.tool;

import com.github.essobedo.lc.service.ScreenCaptureManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a class that provides a set of tools.
 *
 * @author <a href="mailto:nicolas.filotto@gmail.com">Nicolas Filotto</a>
 * @version $Id$
 */
public class Utils {
    /**
     * The name of the algorithm used for the checksum
     */
    private static final String ALGORITHM = "MD5";
    /**
     * The properties containing the information about the version
     */
    private static final Properties VERSION_INFO = new Properties();

    static {
        InputStream is = null;
        try {
            is = Utils.class.getResourceAsStream("/version.properties");
            if (is != null)
                VERSION_INFO.load(is);
        } catch (IOException e) {
            // ignore me as it is not blocking
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //ignore me
                }
            }
        }
    }

    private static final Map<Character, List<Integer>> BASIC_CHARACTER_MAPPING;

    static {
        String rootDir = System.getProperty("jetty.home");
        Map<Character, List<Integer>> map;
        if (rootDir != null) {
            rootDir += "/resources";
            map = loadMapping(rootDir, Locale.getDefault());
        } else {
            try {
                rootDir = new File(Utils.class.getResource("/mapping.properties").toURI()).getParent();
                map = loadMapping(rootDir, Locale.getDefault());
            } catch (Exception e) {
                System.err.println("Cannot open or find the file mapping.properties");
                map = new HashMap<Character, List<Integer>>();
            }
        }
        BASIC_CHARACTER_MAPPING = Collections.unmodifiableMap(map);
    }

    private Utils() {
    }

    /**
     * Converts a String representation of the coordinates in the screen into a Point
     *
     * @param coordinates the String representation of the coordinates
     * @return the corresponding Point
     * @throws java.lang.RuntimeException if the coordinates could not be extracted
     */
    public static Point extractPoint(String coordinates) {
        if (coordinates == null)
            throw new RuntimeException("The coordinates cannot be null");
        coordinates = coordinates.trim();
        int index = coordinates.indexOf(',');
        if (index == -1)
            throw new RuntimeException("The format of the coordinates is not correct, the separator is missing");
        int x = (int) Double.parseDouble(coordinates.substring(0, index));
        int y = (int) Double.parseDouble(coordinates.substring(index + 1));
        return new Point(x, y);
    }

    /**
     * Converts a String representation of a list of codes into a list of integers
     *
     * @param codes the String representation of the list of codes
     * @return a list of integers representing all the key codes
     */
    public static List<Integer> extractKeyCodes(String codes) {
        if (codes == null)
            throw new RuntimeException("The codes cannot be null");
        int index;
        int last = 0;
        List<Integer> result = new ArrayList<Integer>();
        while ((index = codes.indexOf(',', last)) != -1) {
            String code = codes.substring(last, index).trim();
            result.add(Integer.parseInt(code));
            last = index + 1;
        }
        String code = codes.substring(last).trim();
        result.add(Integer.parseInt(code));
        return result;
    }

    /**
     * Extracts the quality from the given value it is valid, returns the default value otherwise
     *
     * @param qualityValue the value from which we extract the quality
     * @return the related quality
     */
    public static float getQuality(String qualityValue) {
        float quality = ScreenCaptureManager.SCREEN_CAPTURE_DEFAULT_QUALITY;
        if (qualityValue != null) {
            float q = Float.parseFloat(qualityValue);
            if (q > 0.0F && q <= 1.0F) {
                quality = q;
            }
        }
        return quality;
    }

    /**
     * Generates a checksum of the given input
     *
     * @param input the data to convert into a checksum
     * @return the checksum of the related input
     */
    public static String hash(byte[] input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("The algorithm " + ALGORITHM + " cannot be found", e);
        }
        digest.update(input);
        byte[] hash = digest.digest();
        int length = hash.length;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
        return sb.toString();
    }

    /**
     * @return the id of the current version of the project
     */
    public static String getVersion() {
        return VERSION_INFO.getProperty("version");
    }

    /**
     * @return the date of the build
     */
    public static String getBuildDate() {
        return VERSION_INFO.getProperty("date");
    }

    /**
     * Gives the key codes corresponding to the given character. Negative key codes are keys that must be hold
     * the rest is supposed to be released just after been pressed
     *
     * @param c  the character for which we want the corresponding key codes
     * @param os the OS for which we want the key codes
     * @return an array of integers corresponding to the key codes that matches with the character.
     */
    public static List<Integer> getKeyCodes(char c, OS os) {
        List<Integer> result = BASIC_CHARACTER_MAPPING.get(c);
        if (result != null)
            return result;
        result = new ArrayList<Integer>();
        if (os == OS.WINDOWS) {
            result.add(-KeyEvent.VK_ALT);
            for (int i = 3; i >= 0; --i) {
                int num = (int) (c * Math.pow(10, -i)) % 10 + KeyEvent.VK_NUMPAD0;
                result.add(num);
            }
            return result;
        }
        if (os == OS.OTHER) {
            result.add(-KeyEvent.VK_CONTROL);
            result.add(-KeyEvent.VK_SHIFT);
            result.add(KeyEvent.VK_U);
        } else {
            // MAC
            result.add(-KeyEvent.VK_ALT);
            result.add(KeyEvent.VK_MINUS);
        }
        for (int i = 3; i >= 0; i--) {
            char current = Integer.toHexString((c & 15 << (4 * i)) >> (4 * i)).charAt(0);
            switch (current) {
                case 'a':
                    result.add(KeyEvent.VK_A);
                    break;
                case 'b':
                    result.add(KeyEvent.VK_B);
                    break;
                case 'c':
                    result.add(KeyEvent.VK_C);
                    break;
                case 'd':
                    result.add(KeyEvent.VK_D);
                    break;
                case 'e':
                    result.add(KeyEvent.VK_E);
                    break;
                case 'f':
                    result.add(KeyEvent.VK_F);
                    break;
                case '0':
                    result.add(KeyEvent.VK_0);
                    break;
                case '1':
                    result.add(KeyEvent.VK_1);
                    break;
                case '2':
                    result.add(KeyEvent.VK_2);
                    break;
                case '3':
                    result.add(KeyEvent.VK_3);
                    break;
                case '4':
                    result.add(KeyEvent.VK_4);
                    break;
                case '5':
                    result.add(KeyEvent.VK_5);
                    break;
                case '6':
                    result.add(KeyEvent.VK_6);
                    break;
                case '7':
                    result.add(KeyEvent.VK_7);
                    break;
                case '8':
                    result.add(KeyEvent.VK_8);
                    break;
                case '9':
                    result.add(KeyEvent.VK_9);
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected character: " + current);
            }
        }
        return result;
    }

    /**
     * Loads the resource bundles corresponding to the mapping for the given locale, it starts
     * to load mapping.properties then the one of the provided locale
     *
     * @param parentDir the parent directory in which the resource bundle will be loaded
     * @return A map corresponding to the mapping character to key codes for the given locale
     */
    static Map<Character, List<Integer>> loadMapping(String parentDir, Locale locale) {
        Map<Character, List<Integer>> map = new HashMap<Character, List<Integer>>();
        map.putAll(loadMapping(parentDir + "/mapping.properties"));
        map.putAll(loadMapping(parentDir + "/mapping_" + locale + ".properties"));
        return map;
    }

    /**
     * Loads the file at the given path and extracts the mapping between the characters and the key codes
     *
     * @param path the path of the file from which we extract the mapping
     * @return the map that contains the mapping between the characters and the key codes
     */
    private static Map<Character, List<Integer>> loadMapping(String path) {
        Map<Character, List<Integer>> map = new HashMap<Character, List<Integer>>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String line;
            int count = 0;
            Pattern pattern = Pattern.compile("-?(\\w+|\\d\\d+)(\\+-?(\\w+|\\d\\d+))*");
            Pattern p = Pattern.compile("(-?\\w+|-?\\d\\d+)");
            while ((line = reader.readLine()) != null) {
                count++;
                int index = line.lastIndexOf('=');
                if (index != 1) {
                    throw new RuntimeException("The line " + count + " of the file " + path
                            + " is incorrect: the character = is missing or not at the expected place");
                }
                char c = line.charAt(0);
                String combination = line.substring(2).trim();
                Matcher m = pattern.matcher(combination);
                if (!m.matches()) {
                    throw new RuntimeException("The line " + count + " of the file " + path
                            + " is incorrect: the combination of keys don't match with the expected syntax");
                }
                List<Integer> codes = new ArrayList<Integer>();
                m = p.matcher(combination);
                while (m.find()) {
                    String group = m.group(1);
                    boolean negative = (group.charAt(0) == '-');
                    if ((!negative && group.length() > 1 && Character.isDigit(group.charAt(0))) ||
                            (negative && group.length() > 2 && Character.isDigit(group.charAt(1)))) {
                        int keyCode = Integer.parseInt(group);
                        codes.add(keyCode);
                        continue;
                    }
                    String code = "VK_" + (negative ? group.substring(1) : group);

                    try {
                        Field f = KeyEvent.class.getField(code);
                        int keyCode = f.getInt(null);
                        codes.add(negative ? -keyCode : keyCode);
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException("The line " + count + " of the file " + path
                                + " is incorrect: the name " + code + " doesn't match with any field of the KeyEvent class as expected");
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("The line " + count + " of the file " + path
                                + " is incorrect: the field " + code + " of the KeyEvent is not of the correct type");
                    }
                }
                map.put(c, codes);
            }
        } catch (FileNotFoundException e) {
            // ignore me as it could be normal
        } catch (IOException e) {
            System.err.println("Cannot read the file " + path + ":" + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore me
                }
            }
        }
        return map;
    }
}
