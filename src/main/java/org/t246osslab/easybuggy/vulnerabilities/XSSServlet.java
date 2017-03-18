package org.t246osslab.easybuggy.vulnerabilities;

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
@WebServlet(urlPatterns = { "/xss" })
public class XSSServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(XSSServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            String name = req.getParameter("name");
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"xss\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("description.reverse.name", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.name", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"name\" size=\"50\" maxlength=\"50\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (name != null && !"".equals(name)) {
                // Reverse name
                String reverseName = getReverseName(name);
                bodyHtml.append(MessageUtils.getMsg("label.reversed.name", locale) + " -&gt; " + reverseName);
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.enter.name", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.xss", locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.xss.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }

    /* Return the reversed name for a given name */
    private String getReverseName(String name) {
        StringBuilder sb = new StringBuilder(name);
        return sb.reverse().toString();
    }
}
