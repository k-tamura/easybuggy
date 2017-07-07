package org.t246osslab.easybuggy.exceptions;

import java.io.IOException;
import java.util.Stack;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/ese" })
public class EmptyStackExceptionServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(EmptyStackExceptionServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Stack<String> stack = new Stack<String>();
        String tmp;
        while (null != (tmp = stack.pop())) {
            log.debug("Stack.pop(): " + tmp);
        }
    }
}
