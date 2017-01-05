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
import org.t246osslab.easybuggy.utils.ApplicationUtils;
import org.t246osslab.easybuggy.utils.Closer;
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/deadlock2" })
public class DeadlockServlet2 extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter writer = null;
        try {
            String order = req.getParameter("order");
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();
            bodyHtml.append("<form action=\"deadlock2\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("msg.note.sql.deadlock", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.order", locale) + ": ");
            bodyHtml.append("<input type=\"radio\" name=\"order\" value=\"asc\" checked>");
            bodyHtml.append(MessageUtils.getMsg("label.asc", locale));
            bodyHtml.append("<input type=\"radio\" name=\"order\" value=\"desc\">");
            bodyHtml.append(MessageUtils.getMsg("label.desc", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.update", locale) + "\">");
            bodyHtml.append("<br><br>");

            EmbeddedJavaDb2 app = new EmbeddedJavaDb2();
            if ("asc".equals(order)) {
                String message = app.update(new int[] { 1, EmbeddedJavaDb2.MAX_USER_COUNT }, locale);
                bodyHtml.append(message);
            } else if ("desc".equals(order)) {
                String message = app.update(new int[] { EmbeddedJavaDb2.MAX_USER_COUNT, 1 }, locale);
                bodyHtml.append(message);
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.warn.enter.asc.or.desc", locale));
            }
            bodyHtml.append("</form>");
            HTTPResponseCreator.createSimpleResponse(res, null, bodyHtml.toString());

        } catch (Exception e) {
            Logger.error(e);
        } finally {
            Closer.close(writer);
        }
    }
}

class EmbeddedJavaDb2 {

    static final int MAX_USER_COUNT = 1000;
    static final String dbUrl = ApplicationUtils.getDatabaseURL();
    static final String dbDriver = ApplicationUtils.getDatabaseDriver();

    static {
        Connection conn = null;
        Statement stmt = null;
        try {
            if (dbDriver != null && !dbDriver.equals("")) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    Logger.error(e);
                }
            }
            conn = DriverManager.getConnection(dbUrl);
            stmt = conn.createStatement();

            try {
                stmt.executeUpdate("drop table users2");
            } catch (SQLException e) {
                // ignore exception if exist the table
            }
            // create users table
            stmt.executeUpdate("create table users2 (id int primary key, name varchar(30), password varchar(100))");

            // insert rows
            for (int i = 1; i <= MAX_USER_COUNT; i++) {
                stmt.executeUpdate("insert into users2 values (" + i + ",'user" + i + "','password')");
            }

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

    public String update(int[] ids, Locale locale) {

        PreparedStatement stmt = null;
        Connection conn = null;
        int executeUpdate = 0;
        String message = "";
        try {
            if (dbDriver != null && !dbDriver.equals("")) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    Logger.error(e);
                }
            }
            conn = DriverManager.getConnection(dbUrl);
            conn.setAutoCommit(false);
            // conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            stmt = conn.prepareStatement("Update users2 set password = ?  where id = ?");
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setInt(2, ids[0]);
            executeUpdate = stmt.executeUpdate();

            Thread.sleep(5000);

            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setInt(2, ids[1]);
            executeUpdate = executeUpdate + stmt.executeUpdate();
            conn.commit();
            message = MessageUtils.getMsg("msg.update.records", new Object[] { executeUpdate }, locale);

        } catch (SQLTransactionRollbackException e) {
            message = MessageUtils.getMsg("msg.deadlock.occurs", locale);
            Logger.error(e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    Logger.error(e1);
                }
            }
        } catch (SQLException e) {
            if ("41000".equals(e.getSQLState())) {
                message = MessageUtils.getMsg("msg.deadlock.occurs", locale);
            } else {
                message = MessageUtils.getMsg("msg.unknown.exception.occur", locale);
            }
            Logger.error(e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    Logger.error(e1);
                }
            }
        } catch (Exception e) {
            message = MessageUtils.getMsg("easybuggy", locale);
            Logger.error(e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    Logger.error(e1);
                }
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
