package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.text.DateFormat;
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
@WebServlet(urlPatterns = { "/deadlock" })
public class DeadlockServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DeadlockServlet.class);

    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    private boolean switchFlag = true;
    private boolean isFirstLoad = true;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        StringBuilder bodyHtml = new StringBuilder();
        Locale locale = req.getLocale();
        bodyHtml.append(MessageUtils.getMsg("label.current.time", locale) + ": ");
        bodyHtml.append(DateFormat.getTimeInstance().format(new Date()));
        bodyHtml.append("<br><br>");
        try {
            if (isFirstLoad) {
                isFirstLoad = false;
                bodyHtml.append(MessageUtils.getInfoMsg("msg.dead.lock.occur", locale));
            } else {
                switchFlag = !switchFlag;
                if (switchFlag) {
                    lock12();
                } else {
                    lock21();
                }
                bodyHtml.append(MessageUtils.getMsg("msg.dead.lock.not.occur", locale));
            }
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
            bodyHtml.append(
                    MessageUtils.getErrMsg("msg.unknown.exception.occur", new String[] { e.getMessage() }, locale));
        } finally {
            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.current.time", locale),
                    bodyHtml.toString());
        }
    }

    private void lock12() {
        synchronized (lock1) {
            sleep();
            synchronized (lock2) {
                sleep();
            }
        }
    }

    private void lock21() {
        synchronized (lock2) {
            sleep();
            synchronized (lock1) {
                sleep();
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error("Exception occurs: ", e);
        }
    }
}
