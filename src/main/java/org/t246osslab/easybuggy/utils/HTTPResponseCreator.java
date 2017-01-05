package org.t246osslab.easybuggy.utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;

public class HTTPResponseCreator {
    public static void createSimpleResponse(HttpServletResponse res, String title, String bodyHtml) {
        PrintWriter writer = null;
        try {
            res.setContentType("text/html");
            res.setCharacterEncoding("UTF-8");
            writer = res.getWriter();
            writer.write("<HTML>");
            writer.write("<HEAD>");
            if (title != null) {
                writer.write("<TITLE>" + title + "</TITLE>");
            }
            writer.write("</HEAD>");
            writer.write("<BODY>" + bodyHtml + "</BODY>");
            writer.write("</HTML>");

        } catch (Exception e) {
            Logger.error(e);
        } finally {
            Closer.close(writer);
        }
    }
}
