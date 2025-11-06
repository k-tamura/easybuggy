package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.t246osslab.easybuggy.core.dao.DBClient;
import org.t246osslab.easybuggy.core.servlets.AbstractServlet;
import org.t246osslab.easybuggy.core.utils.Closer;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/union-sqli" })
public class UnionBasedSQLInjectionServlet extends AbstractServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            String userId = StringUtils.trim(req.getParameter("userid"));
            Locale locale = req.getLocale();
            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"union-sqli\" method=\"get\">");
            bodyHtml.append(getMsg("description.search.user", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(getMsg("label.user.id", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"userid\" size=\"20\" maxlength=\"20\" value=\"");
            if (!StringUtils.isBlank(userId)) {
                bodyHtml.append(encodeForHTML(userId));
            }
            bodyHtml.append("\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (!StringUtils.isBlank(userId)) {
                bodyHtml.append(searchUsers(userId, locale));
            } else {
                bodyHtml.append(getMsg("msg.enter.user.id", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(getInfoMsg("msg.note.union.sqli", locale));
            bodyHtml.append("</form>");

            responseToClient(req, res, getMsg("title.union.sqli.page", locale), bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }

    private String searchUsers(String userId, Locale locale) {
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        StringBuilder result = new StringBuilder();
        
        try {
            conn = DBClient.getConnection();
            stmt = conn.createStatement();
            
            // VULNERABLE: Direct string concatenation allows UNION-based SQL injection
            // Attack example: userid = ' UNION SELECT id, name, password, secret FROM users WHERE '1'='1
            // This allows extracting all user data including passwords and secrets
            // Note: UNION SELECT must match the number of columns (4: id, name, phone, mail)
            // Password will appear in the phone column, secret in the mail column
            String query = "SELECT id, name, phone, mail FROM users WHERE ispublic = 'true' AND id = '" + userId + "'";
            
            rs = stmt.executeQuery(query);
            
            result.append("<h4>" + getMsg("label.search.results", locale) + "</h4>");
            result.append("<table class=\"table table-striped table-bordered table-hover\" style=\"font-size:small;\">");
            result.append("<thead><tr>");
            result.append("<th>" + getMsg("label.user.id", locale) + "</th>");
            result.append("<th>" + getMsg("label.name", locale) + "</th>");
            result.append("<th>" + getMsg("label.phone", locale) + "</th>");
            result.append("<th>" + getMsg("label.mail", locale) + "</th>");
            result.append("</tr></thead><tbody>");
            
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                result.append("<tr>");
                result.append("<td>").append(encodeForHTML(rs.getString("id"))).append("</td>");
                result.append("<td>").append(encodeForHTML(rs.getString("name"))).append("</td>");
                result.append("<td>").append(encodeForHTML(rs.getString("phone"))).append("</td>");
                result.append("<td>").append(encodeForHTML(rs.getString("mail"))).append("</td>");
                result.append("</tr>");
            }
            
            result.append("</tbody></table>");
            
            if (!hasResults) {
                result.append("<p>").append(getMsg("msg.no.results.found", locale)).append("</p>");
            }
            
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
            // VULNERABLE: Error messages might leak database structure
            result.append(getErrMsg("msg.error.executing.query", new String[] { encodeForHTML(e.getMessage()) }, locale));
        } finally {
            Closer.close(rs);
            Closer.close(stmt);
            Closer.close(conn);
        }
        
        return result.toString();
    }
}

