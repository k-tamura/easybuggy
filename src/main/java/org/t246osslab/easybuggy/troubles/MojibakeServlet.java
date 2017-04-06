package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

// EncodingFilter excludes /mojibake. 
@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/mojibake" })
public class MojibakeServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MojibakeServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

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

            if (name != null && !"".equals(name)) {
                String reverseName = getReverseName(name);
                bodyHtml.append(MessageUtils.getMsg("label.reversed.name", locale) + " -&gt; "
                        + ESAPI.encoder().encodeForHTML(reverseName));
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.enter.name", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getInfoMsg("msg.note.mojibake", locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.xss.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }

    public String getReverseName(String name) {
        StringBuilder sb = new StringBuilder(name);
        return sb.reverse().toString();
    }
}
