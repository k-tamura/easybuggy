package org.t246osslab.easybuggy.exceptions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.undo.UndoManager;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/cre" })
public class CannotRedoExceptionServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        new UndoManager().redo();
    }
}
