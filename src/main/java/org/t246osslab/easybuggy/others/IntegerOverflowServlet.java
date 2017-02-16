package org.t246osslab.easybuggy.others;

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
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/iof" })
public class IntegerOverflowServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(IntegerOverflowServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int days = -1;
        int hours = -1;
        PrintWriter writer = null;
        String errorMessage = "";
        try {
            Locale locale = req.getLocale();
            String strDays = req.getParameter("days");
            if (strDays != null) {
                try {
                    days = Integer.parseInt(strDays);
                } catch (NumberFormatException e) {
                    // ignore
                }
                if (days >= 0) {
                    // days * 24 => hours
                    hours = days * 24;
                } else {
                    errorMessage = "<font color=\"red\">" + MessageUtils.getMsg("msg.enter.positive.number", locale)
                            + "</font>";
                }
            }

            StringBuilder bodyHtml = new StringBuilder();
            bodyHtml.append("<form action=\"iof\" method=\"post\">");
            if (days >= 0) {
                bodyHtml.append("<input type=\"text\" name=\"days\" size=\"8\" maxlength=\"8\" value=" + strDays + ">");
            } else {
                bodyHtml.append("<input type=\"text\" name=\"days\" size=\"8\" maxlength=\"8\">");
            }
            bodyHtml.append(" * 24 = ");
            if (days >= 0) {
                bodyHtml.append(hours + " ");
            }
            bodyHtml.append("<br>");
            bodyHtml.append("<br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.calculate", locale) + "\">");
            bodyHtml.append("<br>");
            bodyHtml.append(errorMessage);
            bodyHtml.append("<br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.positive.number",
                    new String[] { String.valueOf(Integer.MAX_VALUE / 24) }, locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.integer.overflow.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(writer);
        }
    }
}
