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
@WebServlet(urlPatterns = { "/lotd" })
public class LossOfTrailingDigitsServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(LossOfTrailingDigitsServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter writer = null;
        double number = Double.NaN;
        String strNumber = null;
        String errorMessage = null;
        try {
            Locale locale = req.getLocale();
            try {
                strNumber = req.getParameter("number");
                if (strNumber != null) {
                    number = Double.parseDouble(strNumber);
                }
            } catch (NumberFormatException e) {
                // ignore
            }
            if (Double.isNaN(number) || number <= -1 || number == 0 || 1 <= number) {
                errorMessage = MessageUtils.getMsg("msg.enter.decimal.value", locale);
            }

            StringBuilder bodyHtml = new StringBuilder();
            bodyHtml.append("<form action=\"lotd\" method=\"post\">");
            if (!Double.isNaN(number) && errorMessage == null) {
                bodyHtml.append("<input type=\"text\" name=\"number\" size=\"18\" maxlength=\"18\" value=" + strNumber
                        + ">");
            } else {
                bodyHtml.append("<input type=\"text\" name=\"number\" size=\"18\" maxlength=\"18\">");
            }
            bodyHtml.append(" + 1 = ");
            if (!Double.isNaN(number) && errorMessage == null) {
                bodyHtml.append(String.valueOf(number + 1));
            }
            bodyHtml.append("<br>");
            bodyHtml.append("<br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.calculate", locale) + "\">");
            bodyHtml.append("<br>");
            if (errorMessage != null && strNumber != null) {
                bodyHtml.append("<font color=\"red\">" + errorMessage + "</font>");
            }
            bodyHtml.append("<br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.enter.decimal.value", locale));
            bodyHtml.append("</form>");
            HTTPResponseCreator.createSimpleResponse(res,
                    MessageUtils.getMsg("title.loss.of.trailing.digits.page", locale), bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(writer);
        }
    }
}
