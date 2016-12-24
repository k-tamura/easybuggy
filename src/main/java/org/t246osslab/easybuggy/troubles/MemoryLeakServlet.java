package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.openjdk.jol.info.GraphLayout;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/memoryleak" })
public class MemoryLeakServlet extends HttpServlet {
    private static HashMap<String, String> hm = new HashMap<String, String>();
    private static int cnt = 0;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb.append("Memory leak occurs!");
        }
        hm.put(String.valueOf(cnt++), sb.toString());
        //long totalSize = GraphLayout.parseInstance(sb).totalSize();
        res.setContentType("text/plain");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(MessageUtils.getMsg("msg.java.heap.space.leak.occur", req.getLocale()));
    }
}
