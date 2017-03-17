package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.directory.shared.ldap.entry.ModificationOperation;
import org.apache.directory.shared.ldap.entry.client.ClientModification;
import org.apache.directory.shared.ldap.entry.client.DefaultClientAttribute;
import org.apache.directory.shared.ldap.message.ModifyRequestImpl;
import org.apache.directory.shared.ldap.name.LdapDN;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.dao.EmbeddedADS;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/admins/csrf" })
public class CSRFServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(CSRFServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String userid = (String) session.getAttribute("userid");
        Locale locale = req.getLocale();

        StringBuilder bodyHtml = new StringBuilder();
        bodyHtml.append("<form action=\"/admins/csrf\" method=\"post\">");
        bodyHtml.append("<table width=\"760px\">");
        bodyHtml.append("<tr><td>");
        bodyHtml.append("<h2>");
        bodyHtml.append("<span class=\"glyphicon glyphicon-knight\"></span>&nbsp;");
        bodyHtml.append(MessageUtils.getMsg("section.change.password", locale));
        bodyHtml.append("</h2>");
        bodyHtml.append("</td><td align=\"right\">");
        bodyHtml.append(MessageUtils.getMsg("label.login.user.id", locale) + ": " + userid);
        bodyHtml.append("<br>");
        bodyHtml.append("<a href=\"/logout\">" + MessageUtils.getMsg("label.logout", locale) + "</a>");
        bodyHtml.append("</td></tr>");
        bodyHtml.append("</table>");
        bodyHtml.append("<hr/>");
        bodyHtml.append(MessageUtils.getMsg("msg.enter.passwd", locale));
        bodyHtml.append("<br><br>");
        bodyHtml.append(MessageUtils.getMsg("label.password", locale) + ": ");
        bodyHtml.append("<input type=\"password\" name=\"password\" size=\"30\" maxlength=\"30\">");
        bodyHtml.append("<br><br>");
        bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
        bodyHtml.append("<br><br>");
        bodyHtml.append(MessageUtils.getMsg("msg.note.csrf", locale));
        String errorMessage = (String) req.getAttribute("errorMessage");
        if (errorMessage != null) {
            bodyHtml.append("<br><br>" + errorMessage);
        }
        HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.admins.main.page", locale),
                bodyHtml.toString());
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Locale locale = req.getLocale();
        HttpSession session = req.getSession();
        if (session == null) {
            res.sendRedirect("/");
            return;
        }
        String userid = (String) session.getAttribute("userid");
        String password = req.getParameter("password");
        if (userid != null && password != null && !"".equals(userid) && !"".equals(password) && password.length() >= 8) {
            try {
                DefaultClientAttribute entryAttribute = new DefaultClientAttribute("userPassword", ESAPI.encoder()
                        .encodeForLDAP(password.trim()));
                ClientModification clientModification = new ClientModification();
                clientModification.setAttribute(entryAttribute);
                clientModification.setOperation(ModificationOperation.REPLACE_ATTRIBUTE);
                ModifyRequestImpl modifyRequest = new ModifyRequestImpl(1);
                modifyRequest.setName(new LdapDN("uid=" + ESAPI.encoder().encodeForLDAP(userid.trim())
                        + ",ou=people,dc=t246osslab,dc=org"));
                modifyRequest.addModification(clientModification);
                EmbeddedADS.service.getAdminSession().modify(modifyRequest);

                StringBuilder bodyHtml = new StringBuilder();
                bodyHtml.append("<form>");
                bodyHtml.append("<table width=\"760px\">");
                bodyHtml.append("<tr><td>");
                bodyHtml.append("<h2>");
                bodyHtml.append("<span class=\"glyphicon glyphicon-knight\"></span>&nbsp;");
                bodyHtml.append(MessageUtils.getMsg("section.change.password", locale));
                bodyHtml.append("</h2>");
                bodyHtml.append("</td><td align=\"right\">");
                bodyHtml.append(MessageUtils.getMsg("label.login.user.id", locale) + ": " + userid);
                bodyHtml.append("<br>");
                bodyHtml.append("<a href=\"/logout\">" + MessageUtils.getMsg("label.logout", locale) + "</a>");
                bodyHtml.append("</td></tr>");
                bodyHtml.append("</table>");
                bodyHtml.append("<hr/>");
                bodyHtml.append(MessageUtils.getMsg("msg.passwd.changed", locale));
                bodyHtml.append("<br><br>");
                bodyHtml.append("<a href=\"/admins/main\">" + MessageUtils.getMsg("label.goto.admin.page", locale)
                        + "</a>");
                HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.admins.main.page", locale),
                        bodyHtml.toString());
            } catch (Exception e) {
                log.error("Exception occurs: ", e);
                req.setAttribute("errorMessage", MessageUtils.getMsg("msg.passwd.change.failed", locale));
                doGet(req, res);
            }
        } else {
            if (password == null || "".equals(password) || password.length() < 8) {
                req.setAttribute("errorMessage", MessageUtils.getMsg("msg.passwd.is.too.short", locale));
            } else {
                req.setAttribute("errorMessage", MessageUtils.getMsg("msg.unknown.exception.occur", locale));
            }
            doGet(req, res);
        }
    }
}
