package org.t246osslab.easybuggy.troubles;

import java.io.IOException;

import javassist.ClassPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/memoryleak2" })
public class MemoryLeakServlet2 extends HttpServlet {

    private static int i = 0;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            int j = i + 1000;
            for (; i < j; i++) {
                ClassPool pool = ClassPool.getDefault();
                pool.makeClass("eu.plumbr.demo.Generated" + i).toClass();
            }
        } catch (Exception e) {
            Logger.error(e);
        } catch (Error e) {
            Logger.error(e);
        }
        res.setContentType("text/plain");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(MessageUtils.getMsg("msg.permgen.space.leak.occur", req.getLocale()));
    }
}
