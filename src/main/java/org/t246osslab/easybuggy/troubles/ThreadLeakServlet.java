package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.util.Date;
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

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        StringBuilder bodyHtml = new StringBuilder();
        Locale locale = req.getLocale();
        bodyHtml.append(MessageUtils.getMsg("label.current.time", locale) + ": ");
        bodyHtml.append(new Date());
        bodyHtml.append("<br><br>");
        try {
        SubThread sub = new SubThread();
        sub.start();
        bodyHtml.append(MessageUtils.getInfoMsg("msg.thread.leak.occur", req.getLocale()));

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
            bodyHtml.append(MessageUtils.getErrMsg("msg.unknown.exception.occur", new String[] { e.getMessage() },
                    locale));
        } finally {
            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.current.time", locale),
                    bodyHtml.toString());
        }
    }
}

class SubThread extends Thread {
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
            }
        }
    }
}
