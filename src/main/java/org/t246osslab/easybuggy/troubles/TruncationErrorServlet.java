package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/te" })
public class TruncationErrorServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(TruncationErrorServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Locale locale = req.getLocale();
        String strNumber = req.getParameter("number");
        double number = NumberUtils.toDouble(strNumber, -1);
        try {
            StringBuilder bodyHtml = new StringBuilder();
            bodyHtml.append("<form action=\"te\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("msg.enter.positive.number", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append("10.0 " + MessageUtils.getMsg("label.obelus", locale) + " ");
            if (0 < number && number < 10) {
                bodyHtml.append(
                        "<input type=\"text\" name=\"number\" size=\"1\" maxlength=\"1\" value=" + strNumber + ">");
            } else {
                bodyHtml.append("<input type=\"text\" name=\"number\" size=\"1\" maxlength=\"1\">");
            }
            bodyHtml.append(" = ");
            if (0 < number && number < 10) {
                bodyHtml.append(String.valueOf(10.0 / number));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.calculate", locale) + "\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getInfoMsg("msg.note.enter.specific.nembers", locale));
            bodyHtml.append("</form>");
            HTTPResponseCreator.createSimpleResponse(req, res,
                    MessageUtils.getMsg("title.truncation.error.page", locale), bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }
}
