package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.utils.Closer;
import org.t246osslab.easybuggy.utils.DBClient;
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/deadlock2" })
public class DeadlockServlet2 extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(DeadlockServlet2.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter writer = null;
        try {
            String order = req.getParameter("order");
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();
            bodyHtml.append("<form action=\"deadlock2\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("msg.select.asc.or.desc", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.order", locale) + ": ");
            bodyHtml.append("<input type=\"radio\" name=\"order\" value=\"asc\" checked>");
            bodyHtml.append(MessageUtils.getMsg("label.asc", locale));
            bodyHtml.append("&nbsp; ");
            bodyHtml.append("<input type=\"radio\" name=\"order\" value=\"desc\">");
            bodyHtml.append(MessageUtils.getMsg("label.desc", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.update", locale) + "\">");
            bodyHtml.append("<br><br>");

            DBClient dbClient = new DBClient();
            if ("asc".equals(order)) {
                String message = dbClient.updateUsers2Table(new int[] { 1, 2 }, locale);
                bodyHtml.append(message);
            } else if ("desc".equals(order)) {
                String message = dbClient.updateUsers2Table(new int[] { 2, 1 }, locale);
                bodyHtml.append(message);
            }else{
                bodyHtml.append(MessageUtils.getMsg("msg.warn.select.asc.or.desc", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.sql.deadlock", locale));
            bodyHtml.append("</form>");
            HTTPResponseCreator.createSimpleResponse(res, null, bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(writer);
        }
    }
}
