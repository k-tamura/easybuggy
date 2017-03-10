package org.t246osslab.easybuggy.performance;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
@WebServlet(urlPatterns = { "/slowre" })
public class SlowRegularExpressionServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(SlowRegularExpressionServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            String word = req.getParameter("word");
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"slowre\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("description.test.regular.expression", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append("<img src=\"images/regular-expression.png\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.string", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"word\" size=\"50\" maxlength=\"50\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (word != null && !word.equals("")) {
                Date startDate = new Date();
                log.info("Start Date: " + startDate.toString());
                Pattern compile = Pattern.compile("^([a-z0-9]+[-]{0,1}){1,100}$");
                Matcher matcher = compile.matcher(word);
                boolean matches = matcher.matches();
                Date endDate = new Date();
                log.info("End Date: " + endDate.toString());
                if (matches) {
                    bodyHtml.append(MessageUtils.getMsg("msg.match.regular.expression", locale));
                } else {
                    bodyHtml.append(MessageUtils.getMsg("msg.not.match.regular.expression", locale));
                }
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.enter.word", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.slow.regular.expression", locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.slow.regular.expression.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }
}
