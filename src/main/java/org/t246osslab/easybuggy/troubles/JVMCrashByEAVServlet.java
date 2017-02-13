package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;

import sun.misc.Unsafe;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/jvmcrasheav" })
public class JVMCrashByEAVServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            getUnsafe().getByte(0);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private static Unsafe getUnsafe()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
        singleoneInstanceField.setAccessible(true);
        return (Unsafe) singleoneInstanceField.get(null);
    }
}
