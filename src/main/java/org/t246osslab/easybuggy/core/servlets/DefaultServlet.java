package org.t246osslab.easybuggy.core.servlets;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
public abstract class DefaultServlet extends HttpServlet {
    Locale locale = null;
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        locale = req.getLocale();
    }
    
    protected String getMsg(String propertyKey){
        return MessageUtils.getMsg(propertyKey, locale);
    }
}
