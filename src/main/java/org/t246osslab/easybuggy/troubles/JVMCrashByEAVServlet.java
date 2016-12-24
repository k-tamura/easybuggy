package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;
import org.t246osslab.easybuggy.utils.Closer;
import org.t246osslab.easybuggy.utils.MessageUtils;

//import sun.dc.path.PathConsumer;
//import sun.dc.pr.PathDasher;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/jvmcrasheav" })
public class JVMCrashByEAVServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // PathDasher dasher = new PathDasher(null);
        PrintWriter writer = null;
        String message = "";
        try {
            Class<?> cl = Class.forName("sun.dc.pr.PathDasher");
            Class<?> cl2 = Class.forName("sun.dc.path.PathConsumer");
            Constructor<?> cunstructor = cl.getConstructor(new Class[] { cl2 });
            cunstructor.newInstance(new Object[] { null });
        } catch (ClassNotFoundException e) {
            message = MessageUtils.getMsg("msg.info.jvm.not.crash", req.getLocale());
        } catch (Exception e) {
            message = MessageUtils.getMsg("msg.unknown.exception.occur", req.getLocale());
            Logger.error(e);
        } finally {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("text/plain");
            writer =  res.getWriter();
            writer.write(message);
            Closer.close(writer);
        }
    }
}
