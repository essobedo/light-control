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

import org.essobedo.lc.tool.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="mailto:nicolas.filotto@exoplatform.com">Nicolas Filotto</a>
 * @version $Id$
 */
public class TestScreenCaptureManager {
    @Test
    public void testWriteScreenCapture() throws Exception {
        final BufferedImage img = Robot.getCurrentScreenCapture();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ScreenCaptureManager.writeImage(img, byteArrayOutputStream, 1f);
        final int refSize = byteArrayOutputStream.size();
        final String refHash = Utils.hash(byteArrayOutputStream.toByteArray());
        int thread = 20;
        final int totalTimes = 50;
        final CountDownLatch startSignal = new CountDownLatch(1);
        final CountDownLatch doneSignal = new CountDownLatch(thread);
        final List<Exception> errors = Collections.synchronizedList(new ArrayList<Exception>());
        for (int i = 0; i < thread; i++) {
            final int index = i;
            Thread t = new Thread() {
                public void run() {
                    try {
                        startSignal.await();
                        for (int j = 0; j < totalTimes; j++) {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            ScreenCaptureManager.writeImage(img, byteArrayOutputStream, 1f);
                            if (refSize != byteArrayOutputStream.size())
                                throw new RuntimeException("Expected size is <" + refSize + "> but was:<" + byteArrayOutputStream.size() + ">");
                            String hash = Utils.hash(byteArrayOutputStream.toByteArray());
                            if (!refHash.equals(hash)) {
                                throw new RuntimeException("Expected hash is <" + refHash + "> but was:<" + hash + ">");
                            }
                            sleep(50);
                        }
                    } catch (Exception e) {
                        errors.add(e);
                    } finally {
                        doneSignal.countDown();
                    }
                }
            };
            t.start();
        }
        startSignal.countDown();
        doneSignal.await();
        if (!errors.isEmpty()) {
            for (Exception e : errors) {
                e.printStackTrace();
            }
            Assert.fail("Some error occurred");
        }
    }
}
