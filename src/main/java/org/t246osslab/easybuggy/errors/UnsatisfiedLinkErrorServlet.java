package org.t246osslab.easybuggy.errors;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet causes a JNI error.
 */
@WebServlet(urlPatterns = { "/jnicall" })
@SuppressWarnings("serial")
public class UnsatisfiedLinkErrorServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        getByName0("");
    }

    private native static NetworkInterface getByName0(String name)
            throws SocketException;
}
