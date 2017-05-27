package org.t246osslab.easybuggy.troubles;

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
import org.t246osslab.easybuggy.core.utils.ApplicationUtils;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/dbconnectionleak" })
public class DBConnectionLeakServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DBConnectionLeakServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Locale locale = req.getLocale();
        StringBuilder bodyHtml = new StringBuilder();
        try {
            final String dbUrl = ApplicationUtils.getDatabaseURL();
            final String dbDriver = ApplicationUtils.getDatabaseDriver();

            if (!StringUtils.isBlank(dbDriver)) {
                try {
                    Class.forName(dbDriver);
                } catch (Exception e) {
                    log.error("Exception occurs: ", e);
                }
            }
            bodyHtml.append(selectUsers(locale));
            if (StringUtils.isBlank(dbUrl) || dbUrl.startsWith("jdbc:derby:memory:")) {
                bodyHtml.append(MessageUtils.getInfoMsg("msg.note.not.use.ext.db", locale));
            } else {
                bodyHtml.append(MessageUtils.getInfoMsg("msg.db.connection.leak.occur", locale));
            }

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
            bodyHtml.append(MessageUtils.getErrMsg("msg.unknown.exception.occur", new String[]{e.getMessage()}, locale));
            bodyHtml.append(e.getLocalizedMessage());
        } finally {
            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.user.list", locale), bodyHtml.toString());
        }
    }
    
    private String selectUsers(Locale locale) {
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String result = MessageUtils.getErrMsg("msg.error.user.not.exist", locale);
        try {
            conn = DBClient.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select id, name, phone, mail from users where ispublic = 'true'");
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("<tr><td>" + rs.getString("id") + "</td><td>" + rs.getString("name") + "</td><td>"
                        + rs.getString("phone") + "</td><td>" + rs.getString("mail") + "</td></tr>");
            }
            if (sb.length() > 0) {
                result = "<table class=\"table table-striped table-bordered table-hover\" style=\"font-size:small;\"><th>"
                        + MessageUtils.getMsg("label.user.id", locale)
                        + "</th><th>"
                        + MessageUtils.getMsg("label.name", locale)
                        + "</th><th>"
                        + MessageUtils.getMsg("label.phone", locale)
                        + "</th><th>"
                        + MessageUtils.getMsg("label.mail", locale) + "</th>" + sb.toString() + "</table>";
            }
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            /* A DB connection leaks because the following lines are commented out.
            Closer.close(rs);
            Closer.close(stmt);
            Closer.close(conn);
            */
        }
        return result;
    }
}
