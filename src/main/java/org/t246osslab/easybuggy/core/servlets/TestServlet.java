package org.t246osslab.easybuggy.core.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/test" })
public class TestServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HTTPResponseCreator.createSimpleResponse(res, "Test", "Test!!");
    }
}
