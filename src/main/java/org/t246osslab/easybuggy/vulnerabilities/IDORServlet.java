package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
@WebServlet(urlPatterns = { "/idor" })
public class IDORServlet extends AbstractServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            String userIdParam = req.getParameter("userid");
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"idor\" method=\"get\">");
            bodyHtml.append(getMsg("description.view.user.profile", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(getMsg("label.user.id", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"userid\" size=\"20\" value=\"");
            if (!StringUtils.isBlank(userIdParam)) {
                bodyHtml.append(encodeForHTML(userIdParam));
            }
            bodyHtml.append("\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (!StringUtils.isBlank(userIdParam)) {
                // VULNERABLE: No authorization check - allows IDOR attacks
                // The application directly uses user-supplied ID without verifying:
                // 1. If the current user is authenticated
                // 2. If the current user is authorized to view this user's data
                // 3. If the requested user's data should be accessible
                UserProfile profile = getUserProfile(userIdParam.trim(), locale);
                
                if (profile != null) {
                    bodyHtml.append("<h3>" + getMsg("label.user.profile", locale) + "</h3>");
                    bodyHtml.append("<table class=\"table table-striped table-bordered table-hover\" style=\"font-size:small;\">");
                    bodyHtml.append("<tr><td><strong>" + getMsg("label.user.id", locale) + "</strong></td><td>" + encodeForHTML(profile.userId) + "</td></tr>");
                    bodyHtml.append("<tr><td><strong>" + getMsg("label.name", locale) + "</strong></td><td>" + encodeForHTML(profile.name) + "</td></tr>");
                    bodyHtml.append("<tr><td><strong>" + getMsg("label.phone", locale) + "</strong></td><td>" + encodeForHTML(profile.phone) + "</td></tr>");
                    bodyHtml.append("<tr><td><strong>" + getMsg("label.mail", locale) + "</strong></td><td>" + encodeForHTML(profile.mail) + "</td></tr>");
                    bodyHtml.append("<tr><td><strong>" + getMsg("label.secret", locale) + "</strong></td><td>" + encodeForHTML(profile.secret) + "</td></tr>");
                    bodyHtml.append("</table>");
                } else {
                    bodyHtml.append(getErrMsg("msg.user.not.exist", locale));
                }
            } else {
                bodyHtml.append(getMsg("msg.enter.user.id", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(getInfoMsg("msg.note.idor", locale));
            bodyHtml.append("</form>");

            responseToClient(req, res, getMsg("title.idor.page", locale), bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }

    private UserProfile getUserProfile(String userId, Locale locale) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UserProfile profile = null;
        
        try {
            conn = DBClient.getConnection();
            // VULNERABLE: Direct query using user-supplied parameter without authorization check
            stmt = conn.prepareStatement("select id, name, phone, mail, secret from users where id = ?");
            stmt.setString(1, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                profile = new UserProfile();
                profile.userId = rs.getString("id");
                profile.name = rs.getString("name");
                profile.phone = rs.getString("phone");
                profile.mail = rs.getString("mail");
                profile.secret = rs.getString("secret");
            }
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(rs);
            Closer.close(stmt);
            Closer.close(conn);
        }
        
        return profile;
    }

    private static class UserProfile {
        String userId;
        String name;
        String phone;
        String mail;
        String secret;
    }
}

