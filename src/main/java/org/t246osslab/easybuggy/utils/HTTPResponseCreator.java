package org.t246osslab.easybuggy.utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;

/**
 * Utility class to create a HTTP response.
 */
public class HTTPResponseCreator {
    
    /**
     * Create a simple HTTP response.
     *
     * @param res HTTP servlet response.
     * @param htmlTitle Title of HTML page.
     * @param htmlBody Body of HTML page.
     */
    public static void createSimpleResponse(HttpServletResponse res, String htmlTitle, String htmlBody) {
        PrintWriter writer = null;
        try {
            res.setContentType("text/html");
            res.setCharacterEncoding("UTF-8");
            writer = res.getWriter();
            writer.write("<HTML>");
            writer.write("<HEAD>");
            if (htmlTitle != null) {
                writer.write("<TITLE>" + htmlTitle + "</TITLE>");
            }
            writer.write("</HEAD>");
            writer.write("<BODY>" + htmlBody + "</BODY>");
            writer.write("</HTML>");

        } catch (Exception e) {
            Logger.error(e);
        } finally {
            Closer.close(writer);
        }
    }
}
