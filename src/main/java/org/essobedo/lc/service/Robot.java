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
package org.essobedo.lc.service;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

/**
 * This class is the facade of the class java.awt.Robot, allowing to simplify and reduce the code to
 * what we really need for the purpose of this project
 *
 * @author <a href="mailto:nicolas.filotto@exoplatform.com">Nicolas Filotto</a>
 * @version $Id$
 */
public class Robot {
    /**
     * The dimension of the screen
     */
    public static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    /**
     * The size of the screen
     */
    private static final Rectangle SCREEN_SIZE = new Rectangle(SCREEN_DIMENSION);

    private final java.awt.Robot robot;

    public Robot() {
        try {
            this.robot = new java.awt.Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Could not initialize the Robot", e);
        }
    }

    /**
     * @return Gives the content of the current screen capture
     */
    public BufferedImage getCurrentScreenCapture() {
        BufferedImage im = robot.createScreenCapture(SCREEN_SIZE);
        im.flush();
        return im;
    }

    /**
     * Moves the mouse at the provided place and emulate a mouse click
     *
     * @param x           the x position of the mouse
     * @param y           the y position of the mouse
     * @param mainButton  indicates whether the main button should be used or not. The main button is
     *                    actually the left button so if it is set to <code>true</code> a left click
     *                    will be emulated otherwise it will be a right click.
     * @param doubleClick indicates whether the click is a double click or a single click
     */
    public void click(int x, int y, boolean mainButton, boolean doubleClick) {
        move(x, y);
        int mask = mainButton ? InputEvent.BUTTON1_DOWN_MASK : InputEvent.BUTTON3_DOWN_MASK;
        robot.mousePress(mask);
        robot.mouseRelease(mask);
        if (doubleClick) {
            robot.mousePress(mask);
            robot.mouseRelease(mask);
        }
    }

    /**
     * Moves the mouse at the provided place
     *
     * @param x the x position of the mouse
     * @param y the y position of the mouse
     */
    public void move(int x, int y) {
        robot.mouseMove(Math.min(x, SCREEN_SIZE.width), Math.min(y, SCREEN_SIZE.height));
    }

    /**
     * Emulates a key press and a key release of the given key code
     *
     * @param keycode the code of the key
     */
    public void hit(int keycode) {
        robot.keyPress(keycode);
        robot.keyRelease(keycode);
    }
}
