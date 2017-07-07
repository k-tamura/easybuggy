package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/threadleak" })
public class ThreadLeakServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ThreadLeakServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        StringBuilder bodyHtml = new StringBuilder();
        Locale locale = req.getLocale();
        try {
            ThreadCountLoggingThread sub = new ThreadCountLoggingThread();
            sub.start();

            ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            bodyHtml.append(MessageUtils.getMsg("label.current.thread.count", locale) + ": ");
            bodyHtml.append(bean.getAllThreadIds().length);
            bodyHtml.append("<br><br>");

            bodyHtml.append(MessageUtils.getInfoMsg("msg.thread.leak.occur", req.getLocale()));
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
            bodyHtml.append(MessageUtils.getErrMsg("msg.unknown.exception.occur", new String[] { e.getMessage() },
                    locale));
        } finally {
            HTTPResponseCreator.createSimpleResponse(req, res,
                    MessageUtils.getMsg("title.current.thread.count", locale), bodyHtml.toString());
        }
    }
}

class ThreadCountLoggingThread extends Thread {

    private static final Logger log = LoggerFactory.getLogger(ThreadCountLoggingThread.class);

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100000);
                ThreadMXBean bean = ManagementFactory.getThreadMXBean();
                log.info("Current thread count: " + bean.getAllThreadIds().length);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
}
