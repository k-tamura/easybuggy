package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.utils.Closer;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/resourceleak" })
public class ResourceLeakServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(ResourceLeakServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter writer = null;
        try {
            writer = res.getWriter();
            for (int i = 0; i < 1000; i++) {
                Process process = null;
                try {
                    process = Runtime.getRuntime().exec("echo test");
                    process.waitFor();
                } catch (InterruptedException e) {
                    log.error("Exception occurs: ", e);
                } finally {
                    if (process != null) {
                        // process.getErrorStream().close();
                        // process.getInputStream().close();
                        // process.getOutputStream().close();
                        process.destroy();
                    }
                }
            }
            res.setContentType("text/plain");
            writer.write("Completes.");
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(writer);
        }
    }
}
