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
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/lotd" })
public class LossOfTrailingDigitsServlet extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter writer = null;
        double number = -1;
        String errorMessage = "";
        try {
            Locale locale = req.getLocale();
            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"lotd\" method=\"post\">");
            bodyHtml.append("<input type=\"text\" name=\"number\" size=\"18\" maxlength=\"18\">");
            bodyHtml.append(" + 1 = ");
            String strNumber = req.getParameter("number");
            if (strNumber != null) {
                try {
                    number = Double.parseDouble(strNumber);
                } catch (NumberFormatException e) {
                    // ignore
                }
                if (-1 < number && number != 0 && number < 1) {
                    bodyHtml.append(String.valueOf(number + 1));
                } else {
                    errorMessage = "<font color=\"red\">" + MessageUtils.getMsg("msg.enter.too.small.value", locale)
                            + "</font>";
                }
            }
            bodyHtml.append("<br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.calculate", locale) + "\">");
            bodyHtml.append("<br>");
            bodyHtml.append(errorMessage);
            bodyHtml.append("<br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.enter.too.small.value", locale));
            bodyHtml.append("</form>");
            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.loss.of.trailing.digits.page", locale),
                    bodyHtml.toString());
            
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            Closer.close(writer);
        }
    }
}
