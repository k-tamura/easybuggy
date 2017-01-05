package org.t246osslab.easybuggy.errors;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/eie" })
public class ExceptionInInitializerErrorServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            Class<?> cl = Class.forName("org.t246osslab.easybuggy.errors.InitializerErrorThrower");
            Constructor<?> cunstructor = cl.getConstructor();
            cunstructor.newInstance(new Object[] { null });
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}
class InitializerErrorThrower
{
    static {
        int i = 1 / 0;
    }
}
