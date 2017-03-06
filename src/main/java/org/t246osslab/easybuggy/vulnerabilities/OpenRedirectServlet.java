package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.DefaultLoginServlet;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/openredirect/login" })
public class OpenRedirectServlet extends DefaultLoginServlet {

    private static Logger log = LoggerFactory.getLogger(OpenRedirectServlet.class);

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        req.setAttribute("login.page.note", "msg.note.open.redirect");
        super.doGet(req, res);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String user = request.getParameter("userid");
        String pass = request.getParameter("password");
        String loginQueryString = request.getParameter("loginquerystring");
        if (loginQueryString == null) {
            loginQueryString = "";
        } else {
            loginQueryString = "?" + loginQueryString;
        }

        HttpSession session = request.getSession(true);

        if (authUser(user, pass)) {
            session.setAttribute("authNResult", "authenticated");
            session.setAttribute("userid", user);

            String gotoUrl = request.getParameter("goto");
            try {
                URL u = new URL(gotoUrl);
                gotoUrl = u.toURI().toString();
            } catch (Exception e) {
                log.warn("Invalid goto Url: " + gotoUrl);
            }
            if (gotoUrl != null) {
                response.sendRedirect(gotoUrl);
            } else {
                String target = (String) session.getAttribute("target");
                if (target == null) {
                    response.sendRedirect("/admins/main");
                } else {
                    session.removeAttribute("target");
                    response.sendRedirect(target);
                }
            }
        } else if (isAccountLocked(user)) {
            session.setAttribute("authNResult", "accountLocked");
            response.sendRedirect("/openredirect/login" + loginQueryString);
        } else {
            session.setAttribute("authNResult", "authNFailed");
            response.sendRedirect("/openredirect/login" + loginQueryString);
        }
    }
}
