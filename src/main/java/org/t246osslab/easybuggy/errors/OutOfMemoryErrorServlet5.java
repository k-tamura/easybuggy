package org.t246osslab.easybuggy.errors;

import java.io.IOException;

import javassist.ClassPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/oome5" })
public class OutOfMemoryErrorServlet5 extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(OutOfMemoryErrorServlet5.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            for (int i = 0; i < 1000000; i++) {
                ClassPool pool = ClassPool.getDefault();
                pool.makeClass("org.t246osslab.easybuggy.Generated" + i).toClass();
            }
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }
}
