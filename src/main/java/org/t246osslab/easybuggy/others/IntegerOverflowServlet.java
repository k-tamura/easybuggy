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
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/iof" })
public class IntegerOverflowServlet extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter writer = null;
        try {
            Locale locale = req.getLocale();
            res.setContentType("text/html");
            res.setCharacterEncoding("UTF-8");
            writer = res.getWriter();
            writer.write("<HTML>");
            writer.write("<HEAD>");
            writer.write("<TITLE>" + MessageUtils.getMsg("title.integer.overflow.page", locale) + "</TITLE>");
            writer.write("</HEAD>");
            writer.write("<BODY>");
            writer.write("<form action=\"/iof\" method=\"post\">");
            writer.write("<input type=\"text\" name=\"days\" size=\"8\" maxlength=\"8\">");
            writer.write(MessageUtils.getMsg("label.days", locale));
            writer.write("<br>");
            writer.write("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.convert", locale) + "\">");
            writer.write("<br>");
            if (req.getParameter("days") != null) {
                int days = -1;
                try {
                    days = Integer.parseInt(req.getParameter("days"));
                } catch (NumberFormatException e) {
                    // ignore
                }
                if (days >= 0) {
                    writer.write(days + " " + MessageUtils.getMsg("label.days", locale) + " = " + days * 24 + " "
                            + MessageUtils.getMsg("label.hours", locale));
                } else {
                    writer.write("<font color=\"red\">" + MessageUtils.getMsg("msg.enter.positive.number", locale)
                            + "</font>");
                }
            }
            writer.write("<br>");
            writer.write("<br>");
            writer.write(MessageUtils.getMsg("msg.note.positive.number",
                    new String[] { String.valueOf(Integer.MAX_VALUE / 24) }, locale));
            writer.write("</form>");
            writer.write("</BODY>");
            writer.write("</HTML>");
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            Closer.close(writer);
        }
    }
}
