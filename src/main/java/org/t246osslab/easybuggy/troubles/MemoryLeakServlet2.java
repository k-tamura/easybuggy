package org.t246osslab.easybuggy.troubles;

import java.io.IOException;

import javassist.ClassPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/memoryleak2" })
public class MemoryLeakServlet2 extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(MemoryLeakServlet2.class);

    private static int i = 0;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            int j = i + 1000;
            for (; i < j; i++) {
                ClassPool pool = ClassPool.getDefault();
                pool.makeClass("eu.plumbr.demo.Generated" + i).toClass();
            }
            HTTPResponseCreator.createSimpleResponse(res, null,
                    MessageUtils.getMsg("msg.permgen.space.leak.occur", req.getLocale()));
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } catch (Error e) {
            log.error("Error occurs: ", e);
        }
    }
}
