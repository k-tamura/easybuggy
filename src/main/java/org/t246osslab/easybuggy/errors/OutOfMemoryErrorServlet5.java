package org.t246osslab.easybuggy.errors;

import java.io.IOException;

import javassist.ClassPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/oome5" })
public class OutOfMemoryErrorServlet5 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            for (int i = 0; i < 1000000; i++) {
                ClassPool pool = ClassPool.getDefault();
                pool.makeClass("eu.plumbr.demo.Generated" + i).toClass();
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}
