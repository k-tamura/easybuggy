package org.t246osslab.easybuggy.exceptions;

import java.io.IOException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.t246osslab.easybuggy.core.utils.Closer;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/ime" })
public class InputMismatchExceptionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Scanner scanner = null;
        try {
            scanner = new Scanner("a");
            scanner.nextInt();
        } finally {
            Closer.close(scanner);
        }
    }
}
