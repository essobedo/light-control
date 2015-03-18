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

import org.essobedo.lc.service.Robot;
import org.essobedo.lc.tool.OS;
import org.essobedo.lc.tool.Utils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * This servlet will propagate the hit of a key on the local server
 *
 * @author <a href="mailto:nicolas.filotto@exoplatform.com">Nicolas Filotto</a>
 * @version $Id$
 */
@WebServlet(name = "hit", urlPatterns = {"/h"})
public class HitKeyServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String content = req.getParameter("s");
        if (content != null) {
            OS os = OS.CURRENT;
            String o = req.getParameter("o");
            if (o != null) {
                os = OS.values()[Integer.parseInt(o)];
            }
            Robot.transfer(content, os);
            return;
        }
        String codes = req.getParameter("c");
        List<Integer> lCodes = Utils.extractKeyCodes(codes);
        Robot.hit(lCodes);
    }
}