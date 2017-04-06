package org.t246osslab.easybuggy.core.servlets;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/admins/main" })
public class AdminsMainServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Locale locale = req.getLocale();
        StringBuilder bodyHtml = new StringBuilder();
        bodyHtml.append(MessageUtils.getMsg("msg.admin.page.top", locale));
        bodyHtml.append("<br><br>");
        bodyHtml.append("<a href=\"/uid/serverinfo.jsp\">" + MessageUtils.getMsg("section.server.info", locale) + "</a>");
        bodyHtml.append("<br><br>");
        bodyHtml.append("<a href=\"/admins/csrf\">" + MessageUtils.getMsg("section.change.password", locale) + "</a>");
        bodyHtml.append("<br><br>");
        bodyHtml.append("<a href=\"/admins/clickjacking\">" + MessageUtils.getMsg("section.change.mail", locale) + "</a>");
        HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.admins.main.page", locale),
                bodyHtml.toString());
    }
}
