package org.t246osslab.easybuggy.others;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
@WebServlet(urlPatterns = { "/iof" })
public class IntegerOverflowServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(IntegerOverflowServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int times = -1;
        BigDecimal thickness = null;
        BigDecimal thicknessM = null;
        BigDecimal thicknessKm = null;
        PrintWriter writer = null;
        try {
            Locale locale = req.getLocale();
            String strTimes = req.getParameter("times");
            if (strTimes != null) {
                try {
                    times = Integer.parseInt(strTimes);
                } catch (NumberFormatException e) {
                    // ignore
                }
                int multipleNumber = 1;
                if (times >= 0) {
                    for (int i = 0; i < times; i++) {
                        multipleNumber = multipleNumber * 2;
                    }
                    thickness = new BigDecimal(multipleNumber).divide(new BigDecimal(10)); // mm
                    thicknessM = thickness.divide(new BigDecimal(1000)); // m
                    thicknessKm = thicknessM.divide(new BigDecimal(1000)); // km
                }
            }

            StringBuilder bodyHtml = new StringBuilder();
            bodyHtml.append("<form action=\"iof\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("msg.enter.positive.number", locale));
            bodyHtml.append("<br>");
            bodyHtml.append("<br>");
            if (times >= 0) {
                bodyHtml.append(
                        "<input type=\"text\" name=\"times\" size=\"2\" maxlength=\"2\" value=" + strTimes + ">");
            } else {
                bodyHtml.append("<input type=\"text\" name=\"times\" size=\"2\" maxlength=\"2\">");
            }
            bodyHtml.append(MessageUtils.getMsg("label.times", locale) + " : ");
            if (times >= 0) {
                bodyHtml.append(thickness + " mm");
                bodyHtml.append(thicknessM.intValue() >= 1 && thicknessKm.intValue() < 1 ? " = " + thicknessM + " m" : "");
                bodyHtml.append(thicknessKm.intValue() >= 1 ? " = " + thicknessKm + " km" : "");
            }
            bodyHtml.append("<br>");
            bodyHtml.append("<br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.calculate", locale) + "\">");
            bodyHtml.append("<br>");
            bodyHtml.append("<br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.positive.number", locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.integer.overflow.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(writer);
        }
    }
}
