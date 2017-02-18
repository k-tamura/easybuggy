package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.utils.Closer;
import org.t246osslab.easybuggy.utils.DBClient;
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/sqlijc" })
public class SQLInjectionServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(SQLInjectionServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter writer = null;
        try {
            String name = req.getParameter("name");
            String password = req.getParameter("password");
            Locale locale = req.getLocale();
            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"sqlijc\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("msg.enter.name.and.passwd", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("msg.example.name.and.passwd", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.name", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"name\" size=\"20\" maxlength=\"20\">");
            bodyHtml.append("&nbsp;&nbsp;");
            bodyHtml.append(MessageUtils.getMsg("label.password", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"password\" size=\"20\" maxlength=\"20\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (name != null && password != null && !name.equals("") && !password.equals("") && password.length() >= 8) {
                bodyHtml.append(selectUsers(name, password, req));
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.warn.enter.name.and.passwd", locale) + "<br>");
            }
            bodyHtml.append("<br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.sql.injection", locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.sql.injection.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(writer);
        }
    }

    private String selectUsers(String name, String password, HttpServletRequest req) {

        String result = "<font color=\"red\">" + MessageUtils.getMsg("msg.error.user.not.exist", req.getLocale())+ "</font><br>";
        DBClient dbClient = new DBClient();
        ArrayList<String[]> users = dbClient.selectUsers(name, password);
        StringBuilder sb = new StringBuilder();
        for (String[] user : users) {
            sb.append(user[0] + ", " + user[1] + "<BR>");
        }
        if (sb.length() > 0) {
            result = MessageUtils.getMsg("user.table.column.names", req.getLocale()) + "<BR>" + sb.toString();
        }
        return result;
    }
}
