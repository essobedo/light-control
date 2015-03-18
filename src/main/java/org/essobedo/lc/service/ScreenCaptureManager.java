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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class allowing to access to the current screen capture and its corresponding hash.
 *
 * @author <a href="mailto:nicolas.filotto@exoplatform.com">Nicolas Filotto</a>
 * @version $Id$
 */
public class ScreenCaptureManager {
    /**
     * The content type to use for the screen captures.
     */
    public static final String SCREEN_CAPTURE_CONTENT_TYPE = "image/jpeg";
    /**
     * The default quality of the screen shots.
     */
    public static final float SCREEN_CAPTURE_DEFAULT_QUALITY = 0.2F;
    /**
     * The format of the screen captures.
     */
    private static final String SCREEN_CAPTURE_FORMAT = "jpg";
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    static {
        EXECUTOR.scheduleAtFixedRate(new Runnable() {
            public void run() {
                setCurrentScreenCapture();
            }
        }, 300, 300, TimeUnit.MILLISECONDS);
    }

    private static volatile ScreenCapture CURRENT_SCREEN_CAPTURE;

    static {
        setCurrentScreenCapture();
    }

    private static void setCurrentScreenCapture() {
        Robot robot = new Robot();
        CURRENT_SCREEN_CAPTURE = new ScreenCapture(robot.getCurrentScreenCapture());
    }

    /**
     * @return Gives the String representation of the has of the current snapshot
     */
    public static String getCurrentHash() {
        return CURRENT_SCREEN_CAPTURE.getHash();
    }

    /**
     * Compresses and writes the screen capture to the given output stream
     *
     * @param out     the stream where the capture will be written
     * @param quality the compression quality its value is between <code>0</code>
     *                and <code>1</code>
     * @throws java.io.IOException if any exception occurs
     */
    public static void writeScreenCapture(OutputStream out, float quality) throws IOException {
        writeImage(CURRENT_SCREEN_CAPTURE.getContent(), out, quality);
    }

    /**
     * Compresses and writes the given image to the given output stream
     *
     * @param image   the image to write
     * @param out     the stream where the image will be written
     * @param quality the compression quality its value is between <code>0</code>
     *                and <code>1</code>
     * @throws java.io.IOException if any exception occurs
     */
    static void writeImage(BufferedImage image, OutputStream out, float quality) throws IOException {
        ImageWriter writer = null;

        Iterator<ImageWriter> iterator = ImageIO.getImageWritersByFormatName(SCREEN_CAPTURE_FORMAT);
        if (iterator.hasNext()) {
            writer = iterator.next();
        }
        if (writer == null) {
            return;
        }
        ImageOutputStream output = new MemoryCacheImageOutputStream(out);
        writer.setOutput(output);

        ImageWriteParam param = writer.getDefaultWriteParam();
        // compress to a given quality
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        try {
            // appends a complete image stream containing a single image and
            //associated stream and image metadata and thumbnails to the output
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            image.flush();
            writer.dispose();
            out.flush();
            out.close();
        }
    }

    /**
     * The class representing a screen capture
     */
    private static class ScreenCapture {

        /**
         * The content of the screen capture
         */
        private final BufferedImage content;
        /**
         * The corresponding hash
         */
        private final String hash;

        ScreenCapture(BufferedImage content) {
            this.content = content;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                ImageIO.write(content, SCREEN_CAPTURE_FORMAT, byteArrayOutputStream);
            } catch (IOException e) {
                throw new RuntimeException("Could not get the content of the screen capture");
            }
            this.hash = Utils.hash(byteArrayOutputStream.toByteArray());
        }

        BufferedImage getContent() {
            return content;
        }

        String getHash() {
            return hash;
        }
    }
}
