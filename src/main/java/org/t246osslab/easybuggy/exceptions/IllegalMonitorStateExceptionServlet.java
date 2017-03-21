package org.t246osslab.easybuggy.exceptions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/imse" })
public class IllegalMonitorStateExceptionServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(IllegalMonitorStateExceptionServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Thread thread = new Thread();
        thread.start();
        try {
            thread.wait();
        } catch (InterruptedException e) {
            log.error("InterruptedException occurs: ", e);
        }
    }
}
