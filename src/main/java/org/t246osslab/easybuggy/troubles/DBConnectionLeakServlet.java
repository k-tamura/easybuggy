package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
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
@WebServlet(urlPatterns = { "/dbconnectionleak" })
public class DBConnectionLeakServlet extends HttpServlet {

    static final String dbUrl = ApplicationUtils.getDatabaseURL();
    static final String dbDriver = ApplicationUtils.getDatabaseDriver();
    boolean isLoad = false;

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter writer = null;
        Connection conn = null;
        Statement stmt = null;
        Locale locale = req.getLocale();
        StringBuilder bodyHtml = new StringBuilder();
        try {

            if (dbDriver != null && !dbDriver.equals("")) {
                try {
                    Class.forName(ApplicationUtils.getDatabaseDriver());
                } catch (ClassNotFoundException e) {
                    Logger.error(e);
                }
            }
            conn = DriverManager.getConnection(dbUrl);
            stmt = conn.createStatement();
            if (!isLoad) {
                isLoad = true;
                try {
                    stmt.executeUpdate("drop table users3");
                } catch (SQLException e) {
                    // ignore exception if exist the table
                }
                // create and insert users table
                stmt.executeUpdate("create table users3 (id int primary key, name varchar(30), password varchar(100))");
            }
            stmt.executeUpdate("insert into users3 select count(*)+1, 'name', 'password' from users3");
            bodyHtml.append(MessageUtils.getMsg("msg.db.connection.leak.occur", locale));

        } catch (SQLException e) {
            Logger.error(e);
            bodyHtml.append(MessageUtils.getMsg("msg.unknown.exception.occur", locale));
            bodyHtml.append(e.getLocalizedMessage());
        } finally {
            Closer.close(writer);
            HTTPResponseCreator.createSimpleResponse(res, null, bodyHtml.toString());
            /* A DB connection leaks because the following lines are commented out.
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
             */
        }
    }
}
