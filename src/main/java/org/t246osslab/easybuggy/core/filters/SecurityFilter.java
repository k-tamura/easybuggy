package org.t246osslab.easybuggy.core.filters;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter for security
 */
@WebFilter(urlPatterns = { "/*" })
public class SecurityFilter implements Filter {
	
	/**
	 * Protected pages by safe mode.
	 */
	private static final String[] PROTECTED_PAGES = { "/dbconnectionleak", "/endlesswaiting", "/filedescriptorleak",
			"/forwardloop", "/infiniteloop", "/jvmcrasheav", "/memoryleak", "/memoryleak2", "/memoryleak3",
			"/netsocketleak", "/threadleak", "/oome", "/oome2", "/oome3", "/oome4", "/oome5", "/oome6",
			"/redirectloop" };
	
    /**
     * Default constructor.
     */
    public SecurityFilter() {
        // Do nothing
    }

    /**
     * Call {@link #doFilter(HttpServletRequest, HttpServletResponse, FilterChain)}.
     * 
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
		String target = request.getRequestURI();

		String property = System.getProperty("easybuggy.safe.mode");
		if (property != null && property.equalsIgnoreCase("true") && Arrays.asList(PROTECTED_PAGES).contains(target)) {
			response.sendRedirect("/safemode");
			return;
		}

        /* Prevent clickjacking if target is not /admins/clickjacking ... */
        if (!target.startsWith("/admins/clickjacking")) {
            response.addHeader("X-FRAME-OPTIONS", "DENY");
        }
        /* Prevent Content-Type sniffing */
        response.addHeader("X-Content-Type-Options", "nosniff");
        
        /* Prevent XSS if target is not /xss ... */
        if (!target.startsWith("/xss")) {
            response.addHeader("X-XSS-Protection", "1; mode=block");
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // Do nothing
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // Do nothing
    }
}
