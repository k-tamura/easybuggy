package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.util.Locale;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/codeijc" })
public class CodeInjectionServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(CodeInjectionServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            String jsonString = req.getParameter("jsonString");
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"codeijc\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("description.parse.json", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.json.string", locale) + ": ");
            bodyHtml.append("<textarea name=\"jsonString\" cols=\"80\" rows=\"15\"></textarea>");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (jsonString != null && !"".equals(jsonString)) {
                jsonString = jsonString.replaceAll(" ", "");
                jsonString = jsonString.replaceAll("\r\n", "");
                jsonString = jsonString.replaceAll("\n", "");
                try {
                    ScriptEngineManager manager = new ScriptEngineManager();
                    ScriptEngine scriptEngine = manager.getEngineByName("JavaScript");
                    scriptEngine.eval("JSON.parse('" + jsonString + "')");
                    bodyHtml.append(MessageUtils.getMsg("msg.valid.json", locale));
                } catch (ScriptException e) {
                    bodyHtml.append(MessageUtils.getMsg("msg.invalid.json", locale) + " : ");
                    bodyHtml.append(ESAPI.encoder().encodeForHTML(e.getMessage()));
                }
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.enter.json.string", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.code.injection", locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.parse.json", locale),
                    bodyHtml.toString());
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }
}
