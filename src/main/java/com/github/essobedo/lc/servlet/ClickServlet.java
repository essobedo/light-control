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
package com.github.essobedo.lc.servlet;

import com.github.essobedo.lc.service.Robot;
import com.github.essobedo.lc.tool.Utils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

/**
 * This servlet will propagate the remote click on the local server
 *
 * @author <a href="mailto:nicolas.filotto@gmail.com">Nicolas Filotto</a>
 * @version $Id$
 */
@WebServlet(name = "click", urlPatterns = {"/c"})
public class ClickServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String positionValue = req.getParameter("p");
        Point coordinates = Utils.extractPoint(positionValue);
        boolean mainButton = req.getParameter("b") == null;
        boolean doubleClick = req.getParameter("t") != null;
        Robot.click(coordinates.x, coordinates.y, mainButton, doubleClick);
    }
}
