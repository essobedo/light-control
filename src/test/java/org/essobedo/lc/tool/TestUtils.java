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

import org.essobedo.lc.service.ScreenCaptureManager;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * @author <a href="mailto:nicolas.filotto@exoplatform.com">Nicolas Filotto</a>
 * @version $Id$
 */
public class TestUtils {
    @Test
    public void testExtractPoint() {
        try {
            Utils.extractPoint(null);
            Assert.fail("A RuntimeException was expected");
        } catch (RuntimeException e) {
            //expected
        }
        try {
            Utils.extractPoint("foo");
            Assert.fail("A RuntimeException was expected");
        } catch (RuntimeException e) {
            //expected
        }
        try {
            Utils.extractPoint("1");
            Assert.fail("A RuntimeException was expected");
        } catch (RuntimeException e) {
            //expected
        }
        try {
            Utils.extractPoint("foo,1");
            Assert.fail("A RuntimeException was expected");
        } catch (RuntimeException e) {
            //expected
        }
        try {
            Utils.extractPoint("1,foo");
            Assert.fail("A RuntimeException was expected");
        } catch (RuntimeException e) {
            //expected
        }
        try {
            Utils.extractPoint("1,1,1");
            Assert.fail("A RuntimeException was expected");
        } catch (RuntimeException e) {
            //expected
        }
        Point p = Utils.extractPoint("1,1");
        Assert.assertEquals(1, p.x);
        Assert.assertEquals(1, p.y);
        p = Utils.extractPoint("  1,1  ");
        Assert.assertEquals(1, p.x);
        Assert.assertEquals(1, p.y);
        p = Utils.extractPoint("  1.1,1.8  ");
        Assert.assertEquals(1, p.x);
        Assert.assertEquals(1, p.y);
    }

    @Test
    public void testGetQuality() {
        Assert.assertEquals(ScreenCaptureManager.SCREEN_CAPTURE_DEFAULT_QUALITY, Utils.getQuality(null), 0f);
        try {
            Utils.getQuality("foo");
            Assert.fail("A RuntimeException was expected");
        } catch (RuntimeException e) {
            //expected
        }
        Assert.assertEquals(0.5f, Utils.getQuality("0.5"), 0f);
        Assert.assertEquals(1f, Utils.getQuality("1"), 0f);
        Assert.assertEquals(1f, Utils.getQuality("1.0"), 0f);
        Assert.assertEquals(ScreenCaptureManager.SCREEN_CAPTURE_DEFAULT_QUALITY, Utils.getQuality("0"), 0f);
        Assert.assertEquals(ScreenCaptureManager.SCREEN_CAPTURE_DEFAULT_QUALITY, Utils.getQuality("1.1"), 0f);
    }

    @Test
    public void testHash() {
        Assert.assertEquals("acbd18db4cc2f85cedef654fccc4a4d8", Utils.hash("foo".getBytes()));
        Assert.assertEquals("37b51d194a7513e45b56f6524f2d51f2", Utils.hash("bar".getBytes()));
    }

    @Test
    public void testExtractKeyCodes() {
        try {
            Utils.extractKeyCodes(null);
            Assert.fail("A RuntimeException was expected");
        } catch (RuntimeException e) {
            //expected
        }
        try {
            Utils.extractKeyCodes("foo");
            Assert.fail("A RuntimeException was expected");
        } catch (RuntimeException e) {
            //expected
        }
        try {
            Utils.extractKeyCodes("foo,1");
            Assert.fail("A RuntimeException was expected");
        } catch (RuntimeException e) {
            //expected
        }
        try {
            Utils.extractKeyCodes("1,foo");
            Assert.fail("A RuntimeException was expected");
        } catch (RuntimeException e) {
            //expected
        }
        List<Integer> codes = Utils.extractKeyCodes("1");
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals(1, codes.get(0).intValue());
        codes = Utils.extractKeyCodes("  1  ");
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals(1, codes.get(0).intValue());
        codes = Utils.extractKeyCodes("  1, 2 ");
        Assert.assertEquals(2, codes.size());
        Assert.assertEquals(1, codes.get(0).intValue());
        Assert.assertEquals(2, codes.get(1).intValue());
        codes = Utils.extractKeyCodes("  1, 2 ,3");
        Assert.assertEquals(3, codes.size());
        Assert.assertEquals(1, codes.get(0).intValue());
        Assert.assertEquals(2, codes.get(1).intValue());
        Assert.assertEquals(3, codes.get(2).intValue());
    }

    @Test
    public void testVersionInfo() {
        Assert.assertNotNull(Utils.getVersion());
        Assert.assertNotNull(Utils.getBuildDate());
    }

    @Test
    public void testLoadMapping() throws Exception {
        String rootDir = new File(TestUtils.class.getResource("/mapping.properties").toURI()).getParent();
        Map<Character, List<Integer>> map = Utils.loadMapping(rootDir, Locale.FRANCE);
        Assert.assertNotNull(map);
        Assert.assertEquals(12, map.size());
        List<Integer> codes = map.get('a');
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals(KeyEvent.VK_A, codes.get(0).intValue());
        codes = map.get('A');
        Assert.assertEquals(2, codes.size());
        Assert.assertEquals(-KeyEvent.VK_SHIFT, codes.get(0).intValue());
        Assert.assertEquals(KeyEvent.VK_A, codes.get(1).intValue());
        codes = map.get('`');
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals(KeyEvent.VK_BACK_QUOTE, codes.get(0).intValue());
        codes = map.get('0');
        Assert.assertEquals(2, codes.size());
        Assert.assertEquals(KeyEvent.VK_SHIFT, codes.get(0).intValue());
        Assert.assertEquals(KeyEvent.VK_0, codes.get(1).intValue());
        codes = map.get('1');
        Assert.assertEquals(2, codes.size());
        Assert.assertEquals(-KeyEvent.VK_SHIFT, codes.get(0).intValue());
        Assert.assertEquals(KeyEvent.VK_1, codes.get(1).intValue());
        codes = map.get('=');
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals(KeyEvent.VK_EQUALS, codes.get(0).intValue());
        codes = map.get('#');
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals(KeyEvent.VK_NUMBER_SIGN, codes.get(0).intValue());
        codes = map.get(':');
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals(KeyEvent.VK_COLON, codes.get(0).intValue());
        codes = map.get('?');
        Assert.assertEquals(2, codes.size());
        Assert.assertEquals(KeyEvent.VK_SHIFT, codes.get(0).intValue());
        Assert.assertEquals(KeyEvent.VK_SLASH, codes.get(1).intValue());
        codes = map.get(' ');
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals(KeyEvent.VK_SPACE, codes.get(0).intValue());
        codes = map.get('2');
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals(12, codes.get(0).intValue());
        codes = map.get('3');
        Assert.assertEquals(2, codes.size());
        Assert.assertEquals(KeyEvent.VK_SHIFT, codes.get(0).intValue());
        Assert.assertEquals(12, codes.get(1).intValue());
    }

    @Test
    public void testGetKeyCodes() {
        List<Integer> codes = Utils.getKeyCodes('a', OS.MAC);
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals(KeyEvent.VK_A, codes.get(0).intValue());
        codes = Utils.getKeyCodes('A', OS.MAC);
        Assert.assertEquals(2, codes.size());
        Assert.assertEquals(-KeyEvent.VK_SHIFT, codes.get(0).intValue());
        Assert.assertEquals(KeyEvent.VK_A, codes.get(1).intValue());
        codes = Utils.getKeyCodes('a', OS.WINDOWS);
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals(KeyEvent.VK_A, codes.get(0).intValue());
        codes = Utils.getKeyCodes('A', OS.WINDOWS);
        Assert.assertEquals(2, codes.size());
        Assert.assertEquals(-KeyEvent.VK_SHIFT, codes.get(0).intValue());
        Assert.assertEquals(KeyEvent.VK_A, codes.get(1).intValue());
        codes = Utils.getKeyCodes('a', OS.OTHER);
        Assert.assertEquals(1, codes.size());
        Assert.assertEquals(KeyEvent.VK_A, codes.get(0).intValue());
        codes = Utils.getKeyCodes('A', OS.OTHER);
        Assert.assertEquals(2, codes.size());
        Assert.assertEquals(-KeyEvent.VK_SHIFT, codes.get(0).intValue());
        Assert.assertEquals(KeyEvent.VK_A, codes.get(1).intValue());
        codes = Utils.getKeyCodes('4', OS.MAC);
        Assert.assertEquals(6, codes.size());
        Assert.assertEquals(-KeyEvent.VK_ALT, codes.get(0).intValue());
        Assert.assertEquals(KeyEvent.VK_MINUS, codes.get(1).intValue());
        Assert.assertEquals(KeyEvent.VK_0, codes.get(2).intValue());
        Assert.assertEquals(KeyEvent.VK_0, codes.get(3).intValue());
        Assert.assertEquals(KeyEvent.VK_3, codes.get(4).intValue());
        Assert.assertEquals(KeyEvent.VK_4, codes.get(5).intValue());
        codes = Utils.getKeyCodes('4', OS.OTHER);
        Assert.assertEquals(7, codes.size());
        Assert.assertEquals(-KeyEvent.VK_CONTROL, codes.get(0).intValue());
        Assert.assertEquals(-KeyEvent.VK_SHIFT, codes.get(1).intValue());
        Assert.assertEquals(KeyEvent.VK_U, codes.get(2).intValue());
        Assert.assertEquals(KeyEvent.VK_0, codes.get(3).intValue());
        Assert.assertEquals(KeyEvent.VK_0, codes.get(4).intValue());
        Assert.assertEquals(KeyEvent.VK_3, codes.get(5).intValue());
        Assert.assertEquals(KeyEvent.VK_4, codes.get(6).intValue());
        codes = Utils.getKeyCodes('4', OS.WINDOWS);
        Assert.assertEquals(5, codes.size());
        Assert.assertEquals(-KeyEvent.VK_ALT, codes.get(0).intValue());
        Assert.assertEquals(KeyEvent.VK_NUMPAD0, codes.get(1).intValue());
        Assert.assertEquals(KeyEvent.VK_NUMPAD0, codes.get(2).intValue());
        Assert.assertEquals(KeyEvent.VK_NUMPAD5, codes.get(3).intValue());
        Assert.assertEquals(KeyEvent.VK_NUMPAD2, codes.get(4).intValue());
    }
}
