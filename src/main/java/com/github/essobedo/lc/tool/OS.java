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

/**
 * Enumeration that defines all the supported platform
 *
 * @author <a href="mailto:nicolas.filotto@gmail.com">Nicolas Filotto</a>
 * @version $Id$
 */
public enum OS {
    WINDOWS, MAC, OTHER;

    public static final OS CURRENT;

    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            CURRENT = OS.WINDOWS;
        } else if (os.contains("mac")) {
            CURRENT = OS.MAC;
        } else {
            // For the sake of simplicity we assume that other is Unix or Linux environment
            CURRENT = OS.OTHER;
        }
    }
}
