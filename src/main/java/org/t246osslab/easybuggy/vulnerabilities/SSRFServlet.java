package org.t246osslab.easybuggy.vulnerabilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.t246osslab.easybuggy.core.servlets.AbstractServlet;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/ssrf" })
public class SSRFServlet extends AbstractServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            String urlParam = req.getParameter("url");
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"ssrf\" method=\"post\">");
            bodyHtml.append(getMsg("description.fetch.url", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(getMsg("label.url", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"url\" size=\"100\" maxlength=\"500\" value=\"");
            if (!StringUtils.isBlank(urlParam)) {
                bodyHtml.append(encodeForHTML(urlParam));
            }
            bodyHtml.append("\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (!StringUtils.isBlank(urlParam)) {
                try {
                    // VULNERABLE: No validation of the URL - allows SSRF attacks
                    URL url = new URL(urlParam);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    
                    int responseCode = connection.getResponseCode();
                    bodyHtml.append("<p><strong>" + getMsg("label.response.code", locale) + ":</strong> " + responseCode + "</p>");
                    
                    // Read response body
                    BufferedReader reader = null;
                    try {
                        if (responseCode >= 200 && responseCode < 300) {
                            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        } else {
                            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        }
                        
                        StringBuilder responseBody = new StringBuilder();
                        String line;
                        int lineCount = 0;
                        while ((line = reader.readLine()) != null && lineCount < 100) {
                            responseBody.append(encodeForHTML(line)).append("<br>");
                            lineCount++;
                        }
                        
                        if (lineCount >= 100) {
                            responseBody.append("... (truncated)");
                        }
                        
                        bodyHtml.append("<p><strong>" + getMsg("label.content", locale) + ":</strong></p>");
                        bodyHtml.append("<div style=\"border: 1px solid #ccc; padding: 10px; max-height: 400px; overflow-y: auto;\">");
                        bodyHtml.append(responseBody.toString());
                        bodyHtml.append("</div>");
                    } finally {
                        if (reader != null) {
                            reader.close();
                        }
                    }
                    
                } catch (Exception e) {
                    bodyHtml.append(getErrMsg("msg.error.fetching.url", new String[] { encodeForHTML(e.getMessage()) }, locale));
                }
            } else {
                bodyHtml.append(getMsg("msg.enter.url", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(getInfoMsg("msg.note.ssrf", locale));
            bodyHtml.append("</form>");

            responseToClient(req, res, getMsg("title.ssrf.page", locale), bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }
}

