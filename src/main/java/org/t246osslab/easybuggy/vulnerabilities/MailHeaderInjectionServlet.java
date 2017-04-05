package org.t246osslab.easybuggy.vulnerabilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.EmailUtils;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

/**
 * A servlet that takes message details from user and send it as a new mail through an SMTP server.
 * The mail may contain a attachment which is the file uploaded from client.
 */
@SuppressWarnings("serial")
@WebServlet("/mailheaderijct")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class MailHeaderInjectionServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MailHeaderInjectionServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Locale locale = req.getLocale();
        if (!EmailUtils.isReadyToSendEmail()) {
            HTTPResponseCreator.createSimpleResponse(res,
                    MessageUtils.getMsg("title.mail.header.injection.page", locale),
                    MessageUtils.getMsg("msg.smtp.server.not.setup", locale));
            return;
        }
        StringBuilder bodyHtml = new StringBuilder();
        bodyHtml.append(MessageUtils.getMsg("description.send.mail", locale));
        bodyHtml.append("<br><br>");
        bodyHtml.append("<form action=\"mailheaderijct\" method=\"post\" enctype=\"multipart/form-data\">");
        bodyHtml.append("<table>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>" + MessageUtils.getMsg("label.name", locale) + ":&nbsp;<br><br></td>");
        bodyHtml.append("<td><input type=\"text\" name=\"name\" size=\"50\"/><br><br></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>" + MessageUtils.getMsg("label.mail", locale) + ":&nbsp;<br><br></td>");
        bodyHtml.append("<td><input type=\"text\" name=\"mail\" size=\"50\"/><br><br></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>" + MessageUtils.getMsg("label.subject", locale) + ":&nbsp;<br><br></td>");
        bodyHtml.append("<td><input type=\"text\" name=\"subject\" size=\"50\"/><br><br></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>" + MessageUtils.getMsg("label.content", locale) + ":&nbsp;<br><br></td>");
        bodyHtml.append("<td><textarea rows=\"10\" cols=\"39\" name=\"content\"></textarea> <br><br></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>" + MessageUtils.getMsg("label.attach.file", locale) + ":&nbsp;<br><br></td>");
        bodyHtml.append("<td><input type=\"file\" name=\"file\" size=\"50\" /><br></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td colspan=\"2\" align=\"center\"><input type=\"submit\" value=\""
                + MessageUtils.getMsg("label.submit", locale) + "\"/></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("</table>");
        bodyHtml.append("<br>");
        bodyHtml.append(MessageUtils.getInfoMsg("msg.note.mail.header.injection", locale));
        if (req.getAttribute("message") != null) {
            bodyHtml.append("<br><br>");
            bodyHtml.append(req.getAttribute("message"));
            req.setAttribute("message", null);
        }
        bodyHtml.append("</form>");
        HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.mail.header.injection.page", locale),
                bodyHtml.toString());
    }

    /**
     * handles form submission
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        String resultMessage = "";
        Locale locale = req.getLocale();
        List<File> uploadedFiles = saveUploadedFiles(req);

        String name = req.getParameter("name");
        String mail = req.getParameter("mail");
        String subject = req.getParameter("subject");
        String content = req.getParameter("content");
        if (subject == null || "".equals(subject.trim()) || content == null || "".equals(content.trim())) {
            resultMessage = MessageUtils.getMsg("msg.mail.is.empty", locale);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(MessageUtils.getMsg("label.name", locale)).append(": ").append(name).append("<BR>");
        sb.append(MessageUtils.getMsg("label.mail", locale)).append(": ").append(mail).append("<BR>").append("<BR>");
        sb.append(MessageUtils.getMsg("label.content", locale)).append(": ").append(content).append("<BR>");
        try {
            EmailUtils.sendEmailWithAttachment(subject, sb.toString(), uploadedFiles);
            resultMessage = MessageUtils.getMsg("msg.sent.mail", locale);
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
            resultMessage = MessageUtils.getMsg("msg.unknown.exception.occur", locale) + e.getMessage();
        } finally {
            deleteUploadFiles(uploadedFiles);
            req.setAttribute("message", resultMessage);
            doGet(req, res);
        }
    }

    /**
     * Saves files uploaded from the client and return a list of these files which will be attached
     * to the mail message.
     */
    private List<File> saveUploadedFiles(HttpServletRequest request)
            throws IllegalStateException, IOException, ServletException {
        List<File> listFiles = new ArrayList<File>();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        Collection<Part> multiparts = request.getParts();
        if (multiparts.size() > 0) {
            for (Part part : request.getParts()) {
                // creates a file to be saved
                String fileName = extractFileName(part);
                if (fileName == null || fileName.equals("")) {
                    // not attachment part, continue
                    continue;
                }

                File saveFile = new File(fileName);
                log.debug("Uploaded file is saved on: " + saveFile.getAbsolutePath());
                FileOutputStream outputStream = new FileOutputStream(saveFile);

                // saves uploaded file
                InputStream inputStream = part.getInputStream();
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();

                listFiles.add(saveFile);
            }
        }
        return listFiles;
    }

    /**
     * Retrieves file name of a upload part from its HTTP header
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return null;
    }

    /**
     * Deletes all uploaded files, should be called after the e-mail was sent.
     */
    private void deleteUploadFiles(List<File> listFiles) {
        if (listFiles != null && listFiles.size() > 0) {
            for (File aFile : listFiles) {
                aFile.delete();
            }
        }
    }
}
