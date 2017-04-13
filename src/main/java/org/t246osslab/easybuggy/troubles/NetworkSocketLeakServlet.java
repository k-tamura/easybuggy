package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
@WebServlet(urlPatterns = { "/netsocketleak" })
public class NetworkSocketLeakServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(NetworkSocketLeakServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpURLConnection connection = null;
        URL url = null;
        StringBuilder bodyHtml = new StringBuilder();
        Locale locale = req.getLocale();
        try {
            String pingURL = req.getParameter("pingurl");
            if (pingURL == null) {
                pingURL = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/ping";
            }
            url = new URL(pingURL);

            long start = System.currentTimeMillis();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            long end = System.currentTimeMillis();
            
            bodyHtml.append("<p>"+MessageUtils.getMsg("description.response.time", req.getLocale())+"</p>");
            bodyHtml.append("<table class=\"table table-striped table-bordered table-hover\" style=\"font-size:small;\">");
            bodyHtml.append("<tr><td>" + MessageUtils.getMsg("label.ping.url", locale) + "</td>");
            bodyHtml.append("<td>" + pingURL + "</td></tr>");
            bodyHtml.append("<tr><td>" + MessageUtils.getMsg("label.response.code", req.getLocale()) + "</td>");
            bodyHtml.append("<td>" + responseCode + "</td></tr>");
            bodyHtml.append("<tr><td>" + MessageUtils.getMsg("label.response.time", locale) + "</td>");
            bodyHtml.append("<td>" + (end - start) + "</td></tr>");
            bodyHtml.append("</table>");

            bodyHtml.append(MessageUtils.getInfoMsg("msg.socket.leak.occur", req.getLocale()));
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
            bodyHtml.append(MessageUtils.getErrMsg("msg.unknown.exception.occur", new String[] { e.getMessage() },
                    locale));
        } finally {
            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.response.time", locale),
                    bodyHtml.toString());
        }
    }
}
