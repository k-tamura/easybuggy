package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;

import javax.naming.InvalidNameException;
import javax.naming.directory.InvalidAttributeValueException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.directory.server.core.entry.ClonedServerEntry;
import org.apache.directory.server.core.filtering.EntryFilteringCursor;
import org.apache.directory.shared.ldap.filter.ExprNode;
import org.apache.directory.shared.ldap.filter.FilterParser;
import org.apache.directory.shared.ldap.filter.SearchScope;
import org.apache.directory.shared.ldap.message.AliasDerefMode;
import org.apache.directory.shared.ldap.name.LdapDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.dao.EmbeddedADS;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/ldapijc" })
public class LDAPInjectionServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LDAPInjectionServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            String name = req.getParameter("name");
            String password = req.getParameter("password");
            Locale locale = req.getLocale();
            StringBuilder bodyHtml = new StringBuilder();

            bodyHtml.append("<form action=\"ldapijc\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("msg.enter.name.and.passwd", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("msg.example.name.and.passwd", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.name", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"name\" size=\"20\" maxlength=\"20\">");
            bodyHtml.append("&nbsp;&nbsp;");
            bodyHtml.append(MessageUtils.getMsg("label.password", locale) + ": ");
            bodyHtml.append("<input type=\"password\" name=\"password\" size=\"20\" maxlength=\"20\" autocomplete=\"off\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (name != null && password != null && !"".equals(name) && !"".equals(password) && password.length() >= 8) {

                bodyHtml.append(selectUsers(name, password, req));
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.warn.enter.name.and.passwd", locale));
                bodyHtml.append("<br><br>");
            }
            bodyHtml.append(MessageUtils.getInfoMsg("msg.note.ldap.injection", locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.sql.injection.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }

    private String selectUsers(String name, String password, HttpServletRequest req) 
            throws Exception, InvalidNameException, InvalidAttributeValueException {
        ExprNode filter = null;
        String result = MessageUtils.getErrMsg("msg.error.user.not.exist", req.getLocale());
        try {
            filter = FilterParser.parse("(&(uid=" + name.trim() + ")(userPassword=" + password.trim() + "))");
            EntryFilteringCursor cursor = EmbeddedADS.getAdminSession().search(
                    new LdapDN("ou=people,dc=t246osslab,dc=org"), SearchScope.SUBTREE, filter,
                    AliasDerefMode.NEVER_DEREF_ALIASES, null);
            StringBuilder sb = new StringBuilder();
            for (ClonedServerEntry e : cursor) {
                sb.append("<tr><td>" + e.get("displayName").getString() + "</td><td>" + e.get("employeeNumber").getString() + "</td></tr>");
            }
            if (sb.length() > 0) {
                result = "<div  class=\"container\"><table class=\"table table-striped table-bordered table-hover\"><th>"
                        + MessageUtils.getMsg("label.name", req.getLocale())
                        + "</th><th>"
                        + MessageUtils.getMsg("label.secret", req.getLocale()) + "</th>" + sb.toString() + "</table></div>";
            }
            cursor.close();
        } catch (ParseException e) {
            log.error("ParseException occurs: ", e);
        }
        return result;
    }
}
