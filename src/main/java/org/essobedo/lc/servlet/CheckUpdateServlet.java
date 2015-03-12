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
package org.essobedo.lc.servlet;

import org.essobedo.lc.service.ScreenCaptureManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet checks if the provided hash matches with the hash of the current snapshot
 * if so it will answer with the HTTP code NOT MODIFIED otherwise it will answer OK with
 * the new hash has content of the response/
 *
 * @author <a href="mailto:nicolas.filotto@exoplatform.com">Nicolas Filotto</a>
 * @version $Id$
 */
@WebServlet(name = "check", urlPatterns = {"/u"})
public class CheckUpdateServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String prevHash = req.getParameter("h");
        String currentHash = ScreenCaptureManager.getCurrentHash();
        res.setHeader("Cache-Control", "no-cache");
        if (currentHash.equals(prevHash)) {
            res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        } else {
            res.getWriter().write(currentHash);
            res.setStatus(HttpServletResponse.SC_OK);
        }
    }
}