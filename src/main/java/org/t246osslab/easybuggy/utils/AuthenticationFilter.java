package org.t246osslab.easybuggy.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter for authentication
 */
@WebFilter(urlPatterns = { "/*" })
public class AuthenticationFilter implements Filter {

    /**
     * Default constructor.
     */
    public AuthenticationFilter() {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (request.getRequestURI().startsWith("/members")) {
            String target = request.getRequestURI();
            String loginType = request.getParameter("logintype");
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("authenticated") == null
                    || !"true".equals(session.getAttribute("authenticated"))) {
                /* Not authenticated yet */
                session = request.getSession(true);
                session.setAttribute("target", target);
                if (loginType == null) {
                    response.sendRedirect("/login");
                } else {
                    response.sendRedirect("/" + loginType + "/login");
                }
            }
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
