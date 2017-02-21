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
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

// EncodingFilter excludes /mojibake. 
@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/mojibake" })
public class MojibakeServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(MojibakeServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter writer = null;
        req.setCharacterEncoding("Shift_JIS");
        res.setContentType("text/html; charset=UTF-8");
        try {
            String name = req.getParameter("name");
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"mojibake\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("description.reverse.name", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.name", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"name\" size=\"50\" maxlength=\"50\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (name != null && !name.equals("")) {
                String reverseName = getReverseName(name);
                bodyHtml.append(MessageUtils.getMsg("label.reversed.name", locale) + " -&gt; " + reverseName);
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.enter.name", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.mojibake", locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.xss.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
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
