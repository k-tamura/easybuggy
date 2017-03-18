package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.dao.DBClient;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/sqlijc" })
public class SQLInjectionServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(SQLInjectionServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

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
            bodyHtml.append("<input type=\"password\" name=\"password\" size=\"20\" maxlength=\"20\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (name != null && password != null && !"".equals(name) && !"".equals(password) && password.length() >= 8) {
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
        }
    }

    private String selectUsers(String name, String password, HttpServletRequest req) {
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String result = "<font color=\"red\">" + MessageUtils.getMsg("msg.error.user.not.exist", req.getLocale())
                + "</font><br>";
        try {
            conn = DBClient.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users WHERE name='" + name + "' AND password='" + password + "'");
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append(rs.getString("name") + ", " + rs.getString("secret") + "<BR>");
            }
            if (sb.length() > 0) {
                result = MessageUtils.getMsg("user.table.column.names", req.getLocale()) + "<BR>" + sb.toString();
            }
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.error("Exception occurs: ", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    log.error("Exception occurs: ", e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("Exception occurs: ", e);
                }
            }
        }
        return result;
    }
}
