/*
 * Copyright (C) 2015 eXo Platform SAS.
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
package org.essobedo.lc.tool;

import org.essobedo.lc.service.*;

import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author <a href="mailto:nicolas.filotto@exoplatform.com">Nicolas Filotto</a>
 * @version $Id$
 */
public class Utils {
    /**
     * The name of the algorithm used for the checksum
     */
    private static final String ALGORITHM = "MD5";

    private Utils() {}

    /**
     * Converts a String representation of the coordinates in the screen into a Point
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
        int x = Integer.parseInt(coordinates.substring(0, index));
        int y = Integer.parseInt(coordinates.substring(index + 1));
        return new Point(x, y);
    }

    /**
     * Extracts the quality from the given value it is valid, returns the default value otherwise
     * @param qualityValue the value from which we extract the quality
     * @return the related quality
     */
    public static float getQuality(String qualityValue) {
        float quality = org.essobedo.lc.service.Robot.SCREEN_SHOT_DEFAULT_QUALITY;
        if (qualityValue != null) {
            float q = Float.parseFloat(qualityValue);
            if (q >= 0.0F && q <= 1.0F) {
                quality = q;
            }
        }
        return quality;
    }

    /**
     * Generates a checksum of the given input
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
}
