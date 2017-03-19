package org.t246osslab.easybuggy.errors;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/eie" })
public class ExceptionInInitializerErrorServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ExceptionInInitializerErrorServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            Class<?> cl = Class.forName("org.t246osslab.easybuggy.errors.InitializerErrorThrower");
            Constructor<?> cunstructor = cl.getConstructor();
            cunstructor.newInstance(new Object[] { null });
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }
}

class InitializerErrorThrower {
    static {
        int i = 1 / 0;
    }
}
