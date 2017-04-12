package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

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
        String[] tzIDs = TimeZone.getAvailableIDs();
        bodyHtml.append("<table class=\"table table-striped table-bordered table-hover\" style=\"font-size:small;\">");
        bodyHtml.append("<tr>");
        bodyHtml.append("<th>" + MessageUtils.getMsg("label.timezone.id", locale) + "</th>");
        bodyHtml.append("<th>" + MessageUtils.getMsg("label.timezone.name", locale) + "</th>");
        bodyHtml.append("</tr>");
        for (int i = 0; i < tzIDs.length; i++) {
            TimeZone tz1 = TimeZone.getTimeZone(tzIDs[i]);
            bodyHtml.append("<tr><td>" + tz1.getID() + "</td>");
            bodyHtml.append("<td>" +  tz1.getDisplayName() + "</td></tr>");
        }
        bodyHtml.append("</table>");
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
            bodyHtml.append(MessageUtils.getErrMsg("msg.unknown.exception.occur", new String[]{e.getMessage()}, locale));
        } finally {
            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.timezone.list", locale), bodyHtml.toString());
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
