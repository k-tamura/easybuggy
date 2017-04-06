package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.ApplicationUtils;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/dbconnectionleak" })
public class DBConnectionLeakServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DBConnectionLeakServlet.class);

    boolean isLoad = false;

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Connection conn = null;
        Statement stmt = null;
        Locale locale = req.getLocale();
        StringBuilder bodyHtml = new StringBuilder();
        try {
            bodyHtml.append(MessageUtils.getMsg("label.current.time", locale) + ": ");
            bodyHtml.append(new Date());
            bodyHtml.append("<br><br>");

            final String dbUrl = ApplicationUtils.getDatabaseURL();
            final String dbDriver = ApplicationUtils.getDatabaseDriver();
            if(dbUrl == null || "".equals(dbUrl) || dbUrl.startsWith("jdbc:derby:memory:")){
                bodyHtml.append(MessageUtils.getInfoMsg("msg.note.not.use.ext.db", locale));
                return;
            }

            if (dbDriver != null && !"".equals(dbDriver)) {
                try {
                    Class.forName(dbDriver);
                } catch (Exception e) {
                    log.error("Exception occurs: ", e);
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
            bodyHtml.append(MessageUtils.getInfoMsg("msg.db.connection.leak.occur", locale));

        } catch (SQLException e) {
            log.error("Exception occurs: ", e);
            bodyHtml.append(MessageUtils.getErrMsg("msg.unknown.exception.occur", new String[]{e.getMessage()}, locale));
            bodyHtml.append(e.getLocalizedMessage());
        } finally {
            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.current.time", locale), bodyHtml.toString());
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
