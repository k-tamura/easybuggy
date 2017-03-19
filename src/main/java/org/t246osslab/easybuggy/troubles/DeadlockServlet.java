package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
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

        try {
            Locale locale = req.getLocale();
            StringBuilder bodyHtml = new StringBuilder();

            if (isFirstLoad) {
                isFirstLoad = false;
                bodyHtml.append(MessageUtils.getMsg("msg.dead.lock.occur", locale));
            } else {
                switchFlag = !switchFlag;
                if (switchFlag) {
                    lock12();
                } else {
                    lock21();
                }
                bodyHtml.append(MessageUtils.getMsg("msg.dead.lock.not.occur", locale));
            }
            HTTPResponseCreator.createSimpleResponse(res, null, bodyHtml.toString());
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
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
