package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.Unsafe;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/jvmcrasheav" })
public class JVMCrashByEAVServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(JVMCrashByEAVServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            getUnsafe().getByte(0);
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }

    private static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
        singleoneInstanceField.setAccessible(true);
        return (Unsafe) singleoneInstanceField.get(null);
    }
}
