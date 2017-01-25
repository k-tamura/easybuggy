package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;
import org.t246osslab.easybuggy.utils.ApplicationUtils;
import org.t246osslab.easybuggy.utils.Closer;
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/sqlijc" })
public class SQLInjectionServlet extends HttpServlet {

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
            bodyHtml.append(MessageUtils.getMsg("msg.note.sql.injection", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.name", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"name\" size=\"20\" maxlength=\"20\">");
            bodyHtml.append("&nbsp;&nbsp;");
            bodyHtml.append(MessageUtils.getMsg("label.password", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"password\" size=\"20\" maxlength=\"20\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (name != null && password != null && !name.equals("") && !password.equals("")) {
                EmbeddedJavaDb app = new EmbeddedJavaDb();
                String message = app.selectUser(name, password, req);
                bodyHtml.append(message);
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.warn.enter.name.and.passwd", locale));
            }
            bodyHtml.append("</form>");
            
            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.sql.injection.page", locale), bodyHtml.toString());

        } catch (Exception e) {
            Logger.error(e);
        } finally {
            Closer.close(writer);
        }
    }
}

class EmbeddedJavaDb {
    static Connection conn;

    static {
        Statement stmt = null;
        try {
            String dbDriver = ApplicationUtils.getDatabaseDriver();
            if (dbDriver != null && !dbDriver.equals("")) {
                try {
                    Class.forName(dbDriver);
                } catch (Exception e) {
                    Logger.error(e);
                }
            }
            String dbUrl = ApplicationUtils.getDatabaseURL();
            conn = DriverManager.getConnection(dbUrl);
            stmt = conn.createStatement();
            try {
                stmt.executeUpdate("drop table users");
            } catch (SQLException e) {
                // ignore exception if exist the table
            }
            // create users table
            stmt.executeUpdate("create table users (id int primary key, name varchar(30), password varchar(30), secret varchar(30))");

            // insert rows
            stmt.executeUpdate("insert into users values (0,'Mark','password','57249037993')");
            stmt.executeUpdate("insert into users values (1,'David','p@s2w0rd','42368923031')");
            stmt.executeUpdate("insert into users values (2,'Peter','pa33word','54238496555')");
            stmt.executeUpdate("insert into users values (3,'James','pathwood','70414823225')");
        } catch (SQLException e) {
            Logger.error(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    Logger.error(e);
                }
            }
        }
    }

    String selectUser(String name, String password, HttpServletRequest req) {

        String message = MessageUtils.getMsg("msg.error.user.not.exist", req.getLocale());
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();

            // query
            rs = stmt.executeQuery("SELECT * FROM users WHERE name='" + name + "' AND password='" + password + "'");

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append(rs.getString("name") + ", " + rs.getString("secret") + "<BR>");
            }
            if (sb.length() > 0) {
                message = MessageUtils.getMsg("user.table.column.names", req.getLocale()) + "<BR>" + sb.toString();
            }
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    Logger.error(e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    Logger.error(e);
                }
            }
        }
        return message;
    }
}
