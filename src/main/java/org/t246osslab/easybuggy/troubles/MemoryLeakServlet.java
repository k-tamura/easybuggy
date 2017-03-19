package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/memoryleak" })
public class MemoryLeakServlet extends HttpServlet {
    private HashMap<String, String> hm = new HashMap<String, String>();
    private int cnt = 0;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb.append("Memory leak occurs!");
        }
        hm.put(String.valueOf(cnt++), sb.toString());
        StringBuilder bodyHtml = new StringBuilder();
        bodyHtml.append(MessageUtils.getMsg("msg.java.heap.space.leak.occur", req.getLocale()));
        
        HTTPResponseCreator.createSimpleResponse(res, null, bodyHtml.toString());
    }
}
