package org.t246osslab.easybuggy;

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
@WebServlet(urlPatterns = { "/members/main" })
public class MembersMainServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object userid = session.getAttribute("userid");
        Locale locale = req.getLocale();
        StringBuilder bodyHtml = new StringBuilder();
        bodyHtml.append("Well come to members page!!");
        bodyHtml.append("<br><br>");
        bodyHtml.append("Your name: " + userid);
        bodyHtml.append("<br><br>");
        bodyHtml.append("<a href=\"/logout\">Logout</a>");
        HTTPResponseCreator.createSimpleResponse(res, "Main page for members", bodyHtml.toString());
    }
}
