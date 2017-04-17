package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

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
@WebServlet(urlPatterns = { "/memoryleak" })
public class MemoryLeakServlet extends HttpServlet {
    private HashMap<String, String> cache = new HashMap<String, String>();
    private static final Logger log = LoggerFactory.getLogger(MemoryLeakServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        StringBuilder bodyHtml = new StringBuilder();
        Locale locale = req.getLocale();
        try {
            String[] tzIDs = TimeZone.getAvailableIDs();
            bodyHtml.append("<table class=\"table table-striped table-bordered table-hover\" style=\"font-size:small;\">");
            bodyHtml.append("<tr>");
            bodyHtml.append("<th>#</th>");
            bodyHtml.append("<th>" + MessageUtils.getMsg("label.timezone.id", locale) + "</th>");
            bodyHtml.append("<th>" + MessageUtils.getMsg("label.timezone.name", locale) + "</th>");
            bodyHtml.append("<th>" + MessageUtils.getMsg("label.timezone.name", locale) + "</th>");
            bodyHtml.append("<th>" + "Raw Offset" + "</th>");
            bodyHtml.append("<th>" + "DST Savings" + "</th>");
            bodyHtml.append("<th>" + "has Same Rules" + "</th>");
            bodyHtml.append("<th>" + "in Daylight Time" + "</th>");
            bodyHtml.append("<th>" + "use Daylight Time" + "</th>");
            bodyHtml.append("</tr>");
            Date currentTime = new Date();
            for (int i = 0; i < tzIDs.length; i++) {
                TimeZone tz1 = TimeZone.getTimeZone(tzIDs[i]);
                String style = "";
                if(!tz1.hasSameRules(TimeZone.getDefault())){
                    style = "style=\"color: #777777; font-size: -1; font-style: italic\"";
                }else{
                    style = "style=\"color: #000000; font-weight: bold\"";
                }
                bodyHtml.append("<tr><td><span " + style + " \">" + i + "</span></td>");
                bodyHtml.append("<td><span " + style + " \">" + tz1.getID() + "</span></td>");
                bodyHtml.append("<td><span " + style + " \">" +  tz1.getDisplayName(Locale.ENGLISH)  + "</span></td>");
                bodyHtml.append("<td><span " + style + " \">" +  tz1.getDisplayName() + "</span></td>");
                bodyHtml.append("<td><span " + style + " \">" +  String.format("%1$,3d ms", tz1.getRawOffset()) + "</span></td>");
                bodyHtml.append("<td><span " + style + " \">" +  String.format("%1$,3d ms", tz1.getDSTSavings()) + "</span></td>");
                bodyHtml.append("<td><span " + style + " \">" +  tz1.hasSameRules(TimeZone.getDefault()) + "</span></td>");
                bodyHtml.append("<td><span " + style + "  \">" +  tz1.inDaylightTime(currentTime) + "</span></td>");
                bodyHtml.append("<td><span " + style + " \">" +  tz1.useDaylightTime() + "</span></td></tr>");
            }
            bodyHtml.append("</table>");
            cache.put(String.valueOf(bodyHtml.hashCode()), bodyHtml.toString());
            bodyHtml.append(MessageUtils.getInfoMsg("msg.java.heap.space.leak.occur", req.getLocale()));

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
            bodyHtml.append(MessageUtils.getErrMsg("msg.unknown.exception.occur", new String[] { e.getMessage() },
                    locale));
        } finally {
            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.timezone.list", locale), bodyHtml.toString());
        }
    }
}
