package org.t246osslab.easybuggy.troubles;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/threadleak" })
public class ThreadLeakServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        SubThread sub = new SubThread();
        sub.start();
        StringBuilder bodyHtml = new StringBuilder();
        bodyHtml.append(MessageUtils.getMsg("msg.thread.leak.occur", req.getLocale()));
        HTTPResponseCreator.createSimpleResponse(res, null, bodyHtml.toString());
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
