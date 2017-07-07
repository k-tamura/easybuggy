package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.WordUtils;
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

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        req.setCharacterEncoding("Shift_JIS");
        res.setContentType("text/html; charset=UTF-8");
        try {
            String string = req.getParameter("string");
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"mojibake\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("description.capitalize.string", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.string", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"string\" size=\"100\" maxlength=\"100\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (string != null && !"".equals(string)) {
                // Capitalize the given string
                String capitalizeName = WordUtils.capitalize(string);
                bodyHtml.append(MessageUtils.getMsg("label.capitalized.string", locale) + " : " + ESAPI.encoder().encodeForHTML(capitalizeName));
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.enter.string", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getInfoMsg("msg.note.mojibake", locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.mojibake.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }
}
