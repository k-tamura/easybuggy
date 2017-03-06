package org.t246osslab.easybuggy;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.directory.server.core.filtering.EntryFilteringCursor;
import org.apache.directory.shared.ldap.filter.ExprNode;
import org.apache.directory.shared.ldap.filter.FilterParser;
import org.apache.directory.shared.ldap.filter.SearchScope;
import org.apache.directory.shared.ldap.message.AliasDerefMode;
import org.apache.directory.shared.ldap.name.LdapDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.utils.EmbeddedADS;
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.LDAPUtils;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/login" })
public class DefaultLoginServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(DefaultLoginServlet.class);

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        Locale locale = req.getLocale();
        StringBuilder bodyHtml = new StringBuilder();

        bodyHtml.append("<div id=\"legend\">");
        bodyHtml.append("<legend class=\"\">" + MessageUtils.getMsg("label.login", locale) + "</legend>");
        bodyHtml.append("</div>");
        bodyHtml.append("<p>" + MessageUtils.getMsg("msg.need.admin.privilege", locale));
        bodyHtml.append(MessageUtils.getMsg("msg.enter.id.and.password", locale) + "</p>");
        bodyHtml.append(MessageUtils.getMsg("msg.example.name.and.passwd", locale) + "</p>");
        bodyHtml.append("<form method=\"POST\" action=\"" + req.getRequestURI() + "\">");
        bodyHtml.append("<table width=\"400px\" height=\"150px\">");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>" + MessageUtils.getMsg("label.user.id", locale) + " :&nbsp;</td>");
        bodyHtml.append("<td><input type=\"text\" name=\"userid\" size=\"20\"></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>" + MessageUtils.getMsg("label.password", locale) + " :&nbsp;</td>");
        bodyHtml.append("<td><input type=\"password\" name=\"password\" size=\"20\"></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td></td>");
        bodyHtml.append("<td><input type=\"submit\" value=\"" + MessageUtils.getMsg("label.login", locale) + "\"></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("</table>");
        if (req.getAttribute("login.page.note") != null) {
            bodyHtml.append("<br><p>" + MessageUtils.getMsg((String) req.getAttribute("login.page.note"), locale) + "<p>");
        }
        Enumeration<?> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = req.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                bodyHtml.append("<input type=\"hidden\" name=\"" + paramName + "\" value=\"" + paramValues[i] + "\">");
            }
        }
        bodyHtml.append("</form>");

        HttpSession session = req.getSession(true);

        if (session.getAttribute("authNFail") != null) {
            bodyHtml.append("<p>" + MessageUtils.getMsg("msg.authentication.fail", locale) + "</p>");
            session.setAttribute("authNFail", null);
        }
        HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.login.page", locale),
                bodyHtml.toString());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String user = request.getParameter("userid");
        String pass = request.getParameter("password");

        HttpSession session = request.getSession(true);

        boolean check = authUser(user, pass);
        if (check) {
            session.setAttribute("authenticated", "true");
            session.setAttribute("userid", user);

            String target = (String) session.getAttribute("target");
            if (target == null) {
                response.sendRedirect("/admins/main");
            } else {
                session.removeAttribute("target");
                response.sendRedirect(target);
            }
        } else {
            session.setAttribute("authNFail", "true");
            response.sendRedirect("/login");
        }
    }

    protected boolean authUser(String username, String password) {

        ExprNode filter = null;
        EntryFilteringCursor cursor = null;
        try {
            filter = FilterParser.parse("(&(uid=" + LDAPUtils.escapeLDAPSearchFilter(username.trim())
                    + ")(userPassword=" + LDAPUtils.escapeLDAPSearchFilter(password.trim()) + "))");
            cursor = EmbeddedADS.service.getAdminSession().search(new LdapDN("ou=people,dc=t246osslab,dc=org"),
                    SearchScope.SUBTREE, filter, AliasDerefMode.NEVER_DEREF_ALIASES, null);
            if (cursor.available()) {
                return true;
            }
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    log.error("Exception occurs: ", e);
                }
            }
        }
        return false;
    }
}
