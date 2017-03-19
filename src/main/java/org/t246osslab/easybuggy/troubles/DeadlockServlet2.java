package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.dao.DBClient;
import org.t246osslab.easybuggy.core.utils.Closer;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/deadlock2" })
public class DeadlockServlet2 extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DeadlockServlet2.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            String order = req.getParameter("order");
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();
            bodyHtml.append("<form action=\"deadlock2\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("msg.select.asc.or.desc", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.order", locale) + ": ");
            bodyHtml.append("<input type=\"radio\" name=\"order\" value=\"asc\" checked>");
            bodyHtml.append(MessageUtils.getMsg("label.asc", locale));
            bodyHtml.append("&nbsp; ");
            bodyHtml.append("<input type=\"radio\" name=\"order\" value=\"desc\">");
            bodyHtml.append(MessageUtils.getMsg("label.desc", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.update", locale) + "\">");
            bodyHtml.append("<br><br>");

            if ("asc".equals(order)) {
                String message = updateUsersTable(new String[] {"1", "2"}, locale);
                bodyHtml.append(message);
            } else if ("desc".equals(order)) {
                String message = updateUsersTable(new String[] { "2", "1" }, locale);
                bodyHtml.append(message);
            }else{
                bodyHtml.append(MessageUtils.getMsg("msg.warn.select.asc.or.desc", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.sql.deadlock", locale));
            bodyHtml.append("</form>");
            HTTPResponseCreator.createSimpleResponse(res, null, bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }

    public String updateUsersTable(String[] ids, Locale locale) {

        PreparedStatement stmt = null;
        Connection conn = null;
        int executeUpdate = 0;
        String resultMessage = "";
        try {
            
            conn = DBClient.getConnection();
            conn.setAutoCommit(false);
            // conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            stmt = conn.prepareStatement("Update users set secret = ? where id = ?");
            stmt.setString(1, RandomStringUtils.randomNumeric(10));
            stmt.setString(2, ids[0]);
            executeUpdate = stmt.executeUpdate();

            Thread.sleep(5000);

            stmt.setString(1, RandomStringUtils.randomNumeric(10));
            stmt.setString(2, ids[1]);
            executeUpdate = executeUpdate + stmt.executeUpdate();
            conn.commit();
            resultMessage = MessageUtils.getMsg("msg.update.records", new Object[] { executeUpdate }, locale);

        } catch (SQLTransactionRollbackException e) {
            resultMessage = MessageUtils.getMsg("msg.deadlock.occurs", locale);
            log.error("SQLTransactionRollbackException occurs: ", e);
            rollbak(conn);
        } catch (SQLException e) {
            if ("41000".equals(e.getSQLState())) {
                resultMessage = MessageUtils.getMsg("msg.deadlock.occurs", locale);
            } else {
                resultMessage = MessageUtils.getMsg("msg.unknown.exception.occur", locale);
            }
            log.error("SQLException occurs: ", e);
            rollbak(conn);
        } catch (Exception e) {
            resultMessage = MessageUtils.getMsg("msg.unknown.exception.occur", locale);
            log.error("Exception occurs: ", e);
            rollbak(conn);
        } finally {
            Closer.close(stmt);
            Closer.close(conn);
        }
        return resultMessage;
    }

    private void rollbak(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                log.error("SQLException occurs: ", e1);
            }
        }
    }
}
