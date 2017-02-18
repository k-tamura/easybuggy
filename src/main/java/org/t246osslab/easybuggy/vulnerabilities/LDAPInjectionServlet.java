package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Locale;

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
import org.t246osslab.easybuggy.utils.Closer;
import org.t246osslab.easybuggy.utils.EmbeddedADS;
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/ldapijc" })
public class LDAPInjectionServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(LDAPInjectionServlet.class);

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter writer = null;
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
            bodyHtml.append("<input type=\"text\" name=\"name\" size=\"30\" maxlength=\"30\">");
            bodyHtml.append("&nbsp;&nbsp;");
            bodyHtml.append(MessageUtils.getMsg("label.password", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"password\" size=\"30\" maxlength=\"30\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (name != null && password != null && !name.equals("") && !password.equals("") && password.length() >= 8) {

                ExprNode filter = null;
                try {
                    filter = FilterParser.parse("(&(uid=" + name.trim() + ")(userPassword=" + password.trim() + "))");
                    EmbeddedADS ads = new EmbeddedADS();
                    EntryFilteringCursor cursor = ads.service.getAdminSession().search(
                            new LdapDN("ou=people,dc=t246osslab,dc=org"), SearchScope.SUBTREE, filter,
                            AliasDerefMode.NEVER_DEREF_ALIASES, null);
                    boolean isExist = false;
                    for (ClonedServerEntry e : cursor) {
                        if (!isExist) {
                            isExist = true;
                            bodyHtml.append(MessageUtils.getMsg("user.table.column.names", req.getLocale()) + "<BR>");
                        }
                        bodyHtml.append(e.get("displayName").getString() + ", " + e.get("employeeNumber").getString()
                                + "<BR>");
                    }
                    if (!isExist) {
                        bodyHtml.append("<font color=\"red\">" + MessageUtils.getMsg("msg.error.user.not.exist", req.getLocale()) + "</font><BR>");
                    }
                    cursor.close();
                } catch (ParseException e) {
                    bodyHtml.append("<font color=\"red\">" + MessageUtils.getMsg("msg.error.user.not.exist", req.getLocale()) + "</font><BR>");
                }
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.warn.enter.name.and.passwd", locale));
                bodyHtml.append("<br>");
            }
            bodyHtml.append("<br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.ldap.injection", locale));
            bodyHtml.append("</form>");

            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.sql.injection.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(writer);
        }
    }
}
