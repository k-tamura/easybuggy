package org.t246osslab.easybuggy.troubles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;
import org.t246osslab.easybuggy.utils.ApplicationUtils;
import org.t246osslab.easybuggy.utils.Closer;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/netsocketleak" })
public class NetworkSocketLeakServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter writer = null;
        HttpURLConnection connection = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try {
            res.setContentType("text/plain");
            res.setCharacterEncoding("UTF-8");
            URL url = new URL("http://localhost:" + ApplicationUtils.getEasyBuggyPort() + "/ping");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // isr = new InputStreamReader(connection.getInputStream());
                // reader = new BufferedReader(isr);
                // String line = null;
                // while ((line = reader.readLine()) != null) {
                // sb.append(line);
                // }
                writer = res.getWriter();
                writer.write(MessageUtils.getMsg("msg.socket.leak.occur", req.getLocale()));
            }

        } catch (IOException e) {
            Logger.error(e);
        } finally {
            Closer.close(writer, isr, reader);
            if (connection != null) {
                //connection.disconnect();
            }
        }
    }
}
