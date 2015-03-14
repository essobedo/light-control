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
}
