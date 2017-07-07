package org.t246osslab.easybuggy.troubles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/filedescriptorleak" })
public class FileDescriptorLeakServlet extends HttpServlet {

    private static final int MAX_DISPLAY_COUNT = 15;
    private static final Logger log = LoggerFactory.getLogger(FileDescriptorLeakServlet.class);
    private long count = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Locale locale = req.getLocale();
        StringBuilder bodyHtml = new StringBuilder();
        try {
            File file = new File(req.getServletContext().getAttribute("javax.servlet.context.tempdir").toString(),
                    "test.txt");
            FileOutputStream fos = new FileOutputStream(file, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write("<tr>");
            osw.write("<td>" + new Date().toString() + "</td>");
            osw.write("<td>" + req.getRemoteAddr() + "</td>");
            osw.write("<td>" + req.getRequestedSessionId() + "</td>");
            osw.write("</tr>" + System.getProperty("line.separator"));
            osw.flush();
            count++;

            BufferedReader br = new BufferedReader(new FileReader(file));
            bodyHtml.append("<p>" + MessageUtils.getMsg("description.access.history", req.getLocale()) + "</p>");
            bodyHtml.append(
                    "<table class=\"table table-striped table-bordered table-hover\" style=\"font-size:small;\">");
            bodyHtml.append("<th>" + MessageUtils.getMsg("label.access.time", locale) + "</th>");
            bodyHtml.append("<th>" + MessageUtils.getMsg("label.ip.address", locale) + "</th>");
            bodyHtml.append("<th>" + MessageUtils.getMsg("label.session.id", locale) + "</th>");
            int headerLength = bodyHtml.length();
            String line;
            long currentLineNum = 0;
            while ((line = br.readLine()) != null) {
                if (count - currentLineNum <= MAX_DISPLAY_COUNT) {
                    bodyHtml.insert(headerLength, line);
                }
                currentLineNum++;
            }
            bodyHtml.append("</table>");
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
            bodyHtml.append(
                    MessageUtils.getErrMsg("msg.unknown.exception.occur", new String[] { e.getMessage() }, locale));
            bodyHtml.append(e.getLocalizedMessage());
        } finally {
            bodyHtml.append(MessageUtils.getInfoMsg("msg.file.descriptor.leak.occur", req.getLocale()));
            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.access.history", locale),
                    bodyHtml.toString());
        }
    }
}
