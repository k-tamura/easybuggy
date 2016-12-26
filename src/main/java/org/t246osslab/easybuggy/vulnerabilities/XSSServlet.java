package org.t246osslab.easybuggy.vulnerabilities;

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
@WebServlet(urlPatterns = { "/xss" })
public class XSSServlet extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter writer = null;
        try {
            String name = req.getParameter("name");
            Locale locale = req.getLocale();

            res.setContentType("text/html");
            res.setCharacterEncoding("UTF-8");
            writer = res.getWriter();
            writer.write("<HTML>");
            writer.write("<HEAD>");
            writer.write("<TITLE>" + MessageUtils.getMsg("title.xss.page", locale) + "</TITLE>");
            writer.write("</HEAD>");
            writer.write("<BODY>");
            writer.write("<form action=\"xss\" method=\"post\">");
            writer.write(MessageUtils.getMsg("msg.enter.name", locale));
            writer.write("<br><br>");
            writer.write(MessageUtils.getMsg("msg.example.name", locale));
            writer.write("<br><br>");
            writer.write(MessageUtils.getMsg("msg.note.xss", locale));
            writer.write("<br><br>");
            writer.write(MessageUtils.getMsg("label.name", locale) + ": ");
            writer.write("<input type=\"text\" name=\"name\" size=\"50\" maxlength=\"50\">");
            writer.write("<br><br>");
            writer.write("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            writer.write("<br><br>");

            if (name != null && !name.equals("")) {
                String reverseName = getReverseName(name);
                writer.write(MessageUtils.getMsg("label.reversed.name", locale) + " -&gt; " + reverseName);
            } else {
                writer.write(MessageUtils.getMsg("msg.enter.name", locale));
            }
            writer.write("</form>");
            writer.write("</BODY>");
            writer.write("</HTML>");

        } catch (Exception e) {
            Logger.error(e);
        } finally {
            Closer.close(writer);
        }
    }

    public String getReverseName(String name) {
        StringBuffer sb = new StringBuffer(name);
        name = sb.reverse().toString();
        return name;
    }
}
