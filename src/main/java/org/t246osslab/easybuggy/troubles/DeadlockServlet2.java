package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;
import java.sql.Statement;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;
import org.t246osslab.easybuggy.utils.Closer;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/deadlock2" })
public class DeadlockServlet2 extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter writer = null;
        try {
            String order = req.getParameter("order");
            Locale locale = req.getLocale();

            res.setContentType("text/html");
            res.setCharacterEncoding("UTF-8");
            writer = res.getWriter();
            writer.write("<HTML>");
            writer.write("<HEAD>");
            writer.write("<TITLE>" + MessageUtils.getMsg("title.sql.deadlock.page", locale) + "</TITLE>");
            writer.write("</HEAD>");
            writer.write("<BODY>");
            writer.write("<form action=\"/deadlock2\" method=\"post\">");
            writer.write(MessageUtils.getMsg("msg.reset.all.users.passwd", locale));
            writer.write("<br><br>");
            writer.write(MessageUtils.getMsg("msg.note.sql.deadlock", locale));
            writer.write("<br><br>");
            writer.write(MessageUtils.getMsg("label.order", locale) + ": ");
            writer.write("<input type=\"radio\" name=\"order\" value=\"asc\" checked>");
            writer.write(MessageUtils.getMsg("label.asc", locale));
            writer.write("<input type=\"radio\" name=\"order\" value=\"desc\">");
            writer.write(MessageUtils.getMsg("label.desc", locale));
            writer.write("<br><br>");
            writer.write("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.update", locale) + "\">");
            writer.write("<br><br>");

            EmbeddedJavaDb2 app = new EmbeddedJavaDb2();
            if ("asc".equals(order)) {
                String message = app.update(new String[] { "Mark", "James" }, locale);
                writer.write(message);
            } else if ("desc".equals(order)) {
                String message = app.update(new String[] { "James", "Mark" }, locale);
                writer.write(message);
            } else {
                writer.write(MessageUtils.getMsg("msg.warn.enter.asc.or.desc", locale));
            }
            writer.write("</form>");
            writer.write("</BODY>");
            writer.write("</HTML>");

        } catch (Exception e) {
            Logger.error(e);
        } finally {
            Closer.close(writer);
        }
    }
}

class EmbeddedJavaDb2 {

    // static final String dbUrl = "jdbc:derby:demo;create=true";
    // In-memory database URL
    static final String dbUrl = "jdbc:derby:memory:demo;create=true";

    static {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(dbUrl);
            stmt = conn.createStatement();

            // create users table
            stmt.executeUpdate("Create table users (id int primary key, name varchar(30), password varchar(100))");

            // insert rows
            stmt.executeUpdate("insert into users values (0,'Mark','password')");
            stmt.executeUpdate("insert into users values (1,'James','pathwood')");

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
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    Logger.error(e);
                }
            }
        }
    }

    public String update(String[] names, Locale locale) {

        PreparedStatement stmt = null;
        Connection conn = null;
        int executeUpdate = 0;
        String message = "";
        try {
            conn = DriverManager.getConnection(dbUrl);
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement("Update users set password = ?  where name = ?");
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, names[0]);
            executeUpdate = stmt.executeUpdate();

            Thread.sleep(5000);

            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, names[1]);
            executeUpdate = executeUpdate + stmt.executeUpdate();
            conn.commit();
            message = MessageUtils.getMsg("msg.update.records", new Object[] { executeUpdate }, locale);

        } catch (SQLTransactionRollbackException e) {
            message = MessageUtils.getMsg("msg.deadlock.occurs", locale);
            Logger.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                Logger.error(e1);
            }
        } catch (Exception e) {
            Logger.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                Logger.error(e1);
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    Logger.error(e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    Logger.error(e);
                }
            }
        }
        return message;
    }
}
