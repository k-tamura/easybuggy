package org.t246osslab.easybuggy.core.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/exit" })
public class ExitServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ExitServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        log.info("EasyBuggy is successfully shut down by a /exit request.");
        System.exit(0);
    }
}
