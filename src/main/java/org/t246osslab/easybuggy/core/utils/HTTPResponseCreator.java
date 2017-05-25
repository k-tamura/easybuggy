package org.t246osslab.easybuggy.core.utils;

import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
     * @param req HTTP servlet request.
     * @param res HTTP servlet response.
     * @param htmlTitle Title of HTML page.
     * @param htmlBody Body of HTML page.
     */
    public static void createSimpleResponse(HttpServletRequest req, HttpServletResponse res, String htmlTitle, String htmlBody) {
        PrintWriter writer = null;
        HttpSession session = req.getSession();
        Object userid = session.getAttribute("userid");
        Locale locale = req.getLocale();
        try {
            writer = res.getWriter();
            writer.write("<HTML>");
            writer.write("<HEAD>");
            if (htmlTitle != null) {
                writer.write("<TITLE>" + htmlTitle + "</TITLE>");
            }
            writer.write("<link rel=\"icon\" type=\"image/vnd.microsoft.icon\" href=\"/images/favicon.ico\">");
            writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">");
            writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\">");
            writer.write("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js\"></script>");
            writer.write("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script>");
            writer.write("<script type=\"text/javascript\" src=\"https://cdn.rawgit.com/google/code-prettify/master/loader/run_prettify.js\"></script>");
            writer.write("<script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_CHTML\"></script>");
            
            writer.write("</HEAD>");
            writer.write("<BODY STYLE=\"margin-left:20px;margin-right:20px;\">");
            writer.write("<table style=\"width:100%;\">");
            writer.write("<tr><td>");
            writer.write("<h2>");
            writer.write("<span class=\"glyphicon glyphicon-globe\"></span>&nbsp;");
            if (htmlTitle != null) {
                writer.write(htmlTitle);
            }
            writer.write("</h2>");
            writer.write("</td>");
            if (userid != null && req.getServletPath().startsWith("/admins")) {
                writer.write("<td align=\"right\">");
                writer.write(MessageUtils.getMsg("label.login.user.id", locale) + ": " + userid);
                writer.write("<br>");
                writer.write("<a href=\"/logout\">" + MessageUtils.getMsg("label.logout", locale) + "</a>");
                writer.write("</td>");
            } else {
                writer.write("<td align=\"right\">");
                writer.write("<a href=\"/\">" + MessageUtils.getMsg("label.go.to.main", locale) + "</a>");
                writer.write("</td>");
            }
            writer.write("</tr>");
            writer.write("</table>");
            writer.write("<hr style=\"margin-top:0px\">");
            writer.write(htmlBody);
            writer.write("</BODY>");
            writer.write("</HTML>");

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(writer);
        }
    }
}
