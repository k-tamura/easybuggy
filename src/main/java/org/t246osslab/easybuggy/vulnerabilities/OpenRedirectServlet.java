package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/openredirect/login" })
public class OpenRedirectServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        Locale locale = req.getLocale();
        StringBuilder bodyHtml = new StringBuilder();

        bodyHtml.append("<div id=\"legend\">");
        bodyHtml.append("<legend class=\"\">" + MessageUtils.getMsg("label.login", locale) + " OR version</legend>");
        bodyHtml.append("</div>");
        bodyHtml.append("<p>" + MessageUtils.getMsg("msg.need.admin.privilege", locale) + "<br>");
        bodyHtml.append(MessageUtils.getMsg("msg.enter.id.and.password", locale) + "</p>");
        bodyHtml.append("<form method=\"POST\" action=\"/openredirect/login\">");
        bodyHtml.append("<table width=\"400px\" height=\"150px\">");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>" + MessageUtils.getMsg("label.user.id", locale) + " :&nbsp;</td>");
        bodyHtml.append("<td><input type=\"text\" name=\"user\" size=\"20\"></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>" + MessageUtils.getMsg("label.password", locale) + " :&nbsp;</td>");
        bodyHtml.append("<td><input type=\"password\" name=\"pass\" size=\"20\"></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td></td>");
        bodyHtml.append(
                "<td><input type=\"submit\" value=\"" + MessageUtils.getMsg("label.login", locale) + "\"></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("</table>");
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

        String user = request.getParameter("user");
        String pass = request.getParameter("pass");

        HttpSession session = request.getSession(true);

        boolean check = authUser(user, pass);
        if (check) {
             session.setAttribute("authenticated", "true");
            session.setAttribute("userid", user);

            String target = (String) session.getAttribute("target");
            if (target == null) {
                response.sendRedirect("/members/main");
            } else {
                response.sendRedirect(target);
            }
        } else {
            session.setAttribute("authNFail", "true");
            response.sendRedirect("/openredirect/login");
        }
    }

    protected boolean authUser(String user, String pass) {
        // TODO need to change
        if (user == null || user.length() <= 5 || pass == null || pass.length() <= 5) {
            return false;
        }
        return true;
    }
}
