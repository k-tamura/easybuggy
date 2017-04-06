package org.t246osslab.easybuggy.troubles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.Closer;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/netsocketleak" })
public class NetworkSocketLeakServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(NetworkSocketLeakServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpURLConnection connection = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        StringBuilder bodyHtml = new StringBuilder();
        Locale locale = req.getLocale();
        bodyHtml.append(MessageUtils.getMsg("label.current.time", locale) + ": ");
        bodyHtml.append(new Date());
        bodyHtml.append("<br><br>");
        try {
            URL url = new URL(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/ping");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                log.error("Unexpected response, HTTP response code: {}", connection.getResponseCode());
            } else {
                // isr = new InputStreamReader(connection.getInputStream());
                // reader = new BufferedReader(isr);
                // String line = null;
                // while ((line = reader.readLine()) != null) {
                // sb.append(line);
                // }
            }
            bodyHtml.append(MessageUtils.getInfoMsg("msg.socket.leak.occur", req.getLocale()));
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
            bodyHtml.append(MessageUtils.getErrMsg("msg.unknown.exception.occur", new String[] { e.getMessage() },
                    locale));
        } finally {
            Closer.close(isr, reader);
            if (connection != null) {
                // connection.disconnect();
            }
            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.current.time", locale),
                    bodyHtml.toString());
        }
    }
}
