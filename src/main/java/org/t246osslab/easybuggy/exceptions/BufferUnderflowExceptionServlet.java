package org.t246osslab.easybuggy.exceptions;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/bue" })
public class BufferUnderflowExceptionServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ByteBuffer.wrap(new byte[]{1}).getDouble();
    }
}
