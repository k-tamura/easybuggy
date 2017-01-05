package org.t246osslab.easybuggy.others;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;
import org.t246osslab.easybuggy.utils.Closer;
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/iof" })
public class IntegerOverflowServlet extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter writer = null;
        try {
            Locale locale = req.getLocale();
            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"iof\" method=\"post\">");
            bodyHtml.append("<input type=\"text\" name=\"days\" size=\"8\" maxlength=\"8\">");
            bodyHtml.append(MessageUtils.getMsg("label.days", locale));
            bodyHtml.append("<br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.convert", locale) + "\">");
            bodyHtml.append("<br>");
            if (req.getParameter("days") != null) {
                int days = -1;
                try {
                    days = Integer.parseInt(req.getParameter("days"));
                } catch (NumberFormatException e) {
                    // ignore
                }
                if (days >= 0) {
                    bodyHtml.append(days + " " + MessageUtils.getMsg("label.days", locale) + " = " + days * 24 + " "
                            + MessageUtils.getMsg("label.hours", locale));
                } else {
                    bodyHtml.append("<font color=\"red\">" + MessageUtils.getMsg("msg.enter.positive.number", locale)
                            + "</font>");
                }
            }
            bodyHtml.append("<br>");
            bodyHtml.append("<br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.positive.number",
                    new String[] { String.valueOf(Integer.MAX_VALUE / 24) }, locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.integer.overflow.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            Logger.error(e);
        } finally {
            Closer.close(writer);
        }
    }
}
