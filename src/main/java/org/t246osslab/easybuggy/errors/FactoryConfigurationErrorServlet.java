package org.t246osslab.easybuggy.errors;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParserFactory;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/fce" })
public class FactoryConfigurationErrorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.setProperty("javax.xml.parsers.SAXParserFactory", "non-exist-factory");
        SAXParserFactory.newInstance();
    }
}
