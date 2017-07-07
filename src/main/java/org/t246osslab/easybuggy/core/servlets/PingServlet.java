package org.t246osslab.easybuggy.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.Closer;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/ping" })
public class PingServlet extends HttpServlet {
    
    private static final Logger log = LoggerFactory.getLogger(PingServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter writer = null;
        try {
            res.setContentType("text/plain");
            writer = res.getWriter();
            writer.write("It works!");
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(writer);
        }
    }
}
