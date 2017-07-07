package org.t246osslab.easybuggy.errors;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/oome3" })
public class OutOfMemoryErrorServlet3 extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(OutOfMemoryErrorServlet3.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        while (true) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        log.error("Exception occurs: ", e);
                    }
                }
            }.start();
        }
    }
}
