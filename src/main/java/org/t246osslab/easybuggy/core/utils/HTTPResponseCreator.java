package org.t246osslab.easybuggy.core.utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to create a HTTP response.
 */
public final class HTTPResponseCreator {

    private static final Logger log = LoggerFactory.getLogger(HTTPResponseCreator.class);

    // squid:S1118: Utility classes should not have public constructors
    private HTTPResponseCreator() {
        throw new IllegalAccessError("Utility class");
    }

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
            writer = res.getWriter();
            writer.write("<HTML>");
            writer.write("<HEAD>");
            if (htmlTitle != null) {
                writer.write("<TITLE>" + htmlTitle + "</TITLE>");
            }
            writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">");
            writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\">");
            writer.write("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>");
            writer.write("</HEAD>");
            writer.write("<BODY STYLE=\"margin:20px;\">" + htmlBody + "</BODY>");
            writer.write("</HTML>");

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(writer);
        }
    }
}
