package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/xss" })
public class XSSServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(XSSServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            String string = req.getParameter("string");
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"xss\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("description.reverse.string", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.string", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"string\" size=\"100\" maxlength=\"100\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (!StringUtils.isBlank(string)) {
                // Reverse the given string
                String reversedName = StringUtils.reverse(string);
                bodyHtml.append(MessageUtils.getMsg("label.reversed.string", locale) + " : "
                        + reversedName);
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.enter.string", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getInfoMsg("msg.note.xss", locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.xss.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }
}
