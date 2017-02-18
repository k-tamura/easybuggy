package org.t246osslab.easybuggy.troubles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.utils.Closer;
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/endlesswaiting" })
public class EndlessWaitingServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(EndlessWaitingServlet.class);

    private static final int MAX_COUNT = 100000;

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter writer = null;
        try {
            int count = 0;
            try {
                count = Integer.parseInt(req.getParameter("count"));
            } catch (Exception e) {
            }
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();
            bodyHtml.append("<form action=\"endlesswaiting\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("description.endless.waiting", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.character.count", locale) + ": ");
            bodyHtml.append("<input type=\"text\" name=\"count\" size=\"5\" maxlength=\"5\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.enter.count", locale));
            bodyHtml.append("<br><br>");

            if (count > 0) {
                /* create a batch file in the temp directory */
                File batFile = createBatchFile(req);

                if (batFile == null) {
                    bodyHtml.append(MessageUtils.getMsg("msg.cant.create.batch", locale));
                } else {
                    /* execte the batch */
                    ProcessBuilder pb = new ProcessBuilder(batFile.getAbsolutePath());
                    Process process = pb.start();
                    process.waitFor();
                    bodyHtml.append(MessageUtils.getMsg("msg.executed.batch", locale) + batFile.getAbsolutePath()
                    + "<BR><BR>");
                    bodyHtml.append(MessageUtils.getMsg("label.execution.result", locale) + "<BR><BR>");
                    bodyHtml.append(printInputStream(process.getInputStream(), res));
                    bodyHtml.append(printInputStream(process.getErrorStream(), res));
                }
                bodyHtml.append("</form>");
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.enter.positive.number", locale));
            }
            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.endless.waiting.page", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(writer);
        }
    }

    private File createBatchFile(HttpServletRequest req) throws IOException {
        BufferedWriter filewriter = null;
        File batFile = null;
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            String batFileName = null;
            String firstLine = null;
            if (osName.toLowerCase().startsWith("windows")) {
                batFileName = "test.bat";
                firstLine = "@echo off";
            } else {
                batFileName = "test.sh";
                firstLine = "#!/bin/sh";
            }

            batFile = new File(System.getProperty("java.io.tmpdir"), batFileName);
            batFile.setExecutable(true);
            filewriter = new BufferedWriter(new FileWriter(batFile));
            filewriter.write(firstLine);
            filewriter.newLine();
            int count = MAX_COUNT;
            try {
                count = Integer.valueOf(req.getParameter("count"));
            } catch (Exception e) {
            }
            if (count > MAX_COUNT) {
                count = MAX_COUNT;
            }
            for (int i = 0; i < count; i++) {
                if (i % 100 == 0) {
                    filewriter.newLine();
                    filewriter.write("echo ");
                }
                filewriter.write(String.valueOf(i % 10));
            }
            filewriter.close();
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(filewriter);
        }
        return batFile;
    }

    private static String printInputStream(InputStream is, HttpServletResponse res) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line + "<BR>");
            }
        } finally {
            Closer.close(br);
            Closer.close(is);
        }
        return sb.toString();
    }
}
