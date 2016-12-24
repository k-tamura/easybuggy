package org.t246osslab.easybuggy.troubles;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/infiniteloop" })
public class InfiniteLoopServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        while (true) {
            String contextPath = req.getContextPath();
            int contentLength = req.getContentLength();
            Logger.debug(contextPath + contentLength);
        }
    }
}
