package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.DefaultLoginServlet;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/bruteforce/login" })
public class BruteForceServlet extends DefaultLoginServlet {

    private static Logger log = LoggerFactory.getLogger(BruteForceServlet.class);

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        req.setAttribute("login.page.note", "msg.note.brute.force");
        super.doGet(req, res);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String userid = request.getParameter("userid");
        String password = request.getParameter("password");

        HttpSession session = request.getSession(true);
        if (authUser(userid, password)) {
            session.setAttribute("authNResult", "authenticated");
            session.setAttribute("userid", userid);

            String target = (String) session.getAttribute("target");
            if (target == null) {
                response.sendRedirect("/admins/main");
            } else {
                session.removeAttribute("target");
                response.sendRedirect(target);
            }
        } else {
            session.setAttribute("authNResult", "authNFailed");
            response.sendRedirect("/bruteforce/login");
        }
    }
}
