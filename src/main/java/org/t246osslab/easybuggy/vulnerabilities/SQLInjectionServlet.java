package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.dao.DBClient;
import org.t246osslab.easybuggy.core.utils.Closer;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/sqlijc" })
public class SQLInjectionServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SQLInjectionServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            String name = StringUtils.trim(req.getParameter("name"));
            String password = StringUtils.trim(req.getParameter("password"));
            Locale locale = req.getLocale();
            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"sqlijc\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("msg.enter.name.and.passwd", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.name", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"name\" size=\"20\" maxlength=\"20\">");
            bodyHtml.append("&nbsp;&nbsp;");
            bodyHtml.append(MessageUtils.getMsg("label.password", locale) + ": ");
            bodyHtml.append("<input type=\"password\" name=\"password\" size=\"20\" maxlength=\"20\" autocomplete=\"off\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (!StringUtils.isBlank(name) && !StringUtils.isBlank(password) && password.length() >= 8) {
                bodyHtml.append(selectUsers(name, password, req));
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.warn.enter.name.and.passwd", locale));
                bodyHtml.append("<br><br>");
            }
            bodyHtml.append(MessageUtils.getInfoMsg("msg.note.sqlijc", locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.sqlijc.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }

    private String selectUsers(String name, String password, HttpServletRequest req) {
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String result = MessageUtils.getErrMsg("msg.error.user.not.exist", req.getLocale());
        try {
            conn = DBClient.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT name, secret FROM users WHERE ispublic = 'true' AND name='" + name + "' AND password='" + password + "'");
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("<tr><td>" + rs.getString("name") + "</td><td>" + rs.getString("secret") + "</td></tr>");
            }
            if (sb.length() > 0) {
                result = "<table class=\"table table-striped table-bordered table-hover\" style=\"font-size:small;\"><th>"
                        + MessageUtils.getMsg("label.name", req.getLocale())
                        + "</th><th>"
                        + MessageUtils.getMsg("label.secret", req.getLocale()) + "</th>" + sb.toString() + "</table>";
            }
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(rs);
            Closer.close(stmt);
            Closer.close(conn);
        }
        return result;
    }
}
