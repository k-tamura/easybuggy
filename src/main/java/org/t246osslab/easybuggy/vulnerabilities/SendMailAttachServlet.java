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

import org.t246osslab.easybuggy.core.utils.EmailUtility;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

/**
 * A servlet that takes message details from user and send it as a new e-mail through an SMTP
 * server. The e-mail message may contain attachments which are the files uploaded from client.
 */
@SuppressWarnings("serial")
@WebServlet("/SendMailAttachServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class SendMailAttachServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Locale locale = req.getLocale();
        StringBuilder bodyHtml = new StringBuilder();
        bodyHtml.append("<form action=\"SendMailAttachServlet\" method=\"post\" enctype=\"multipart/form-data\">");
        bodyHtml.append("<table>");
        bodyHtml.append("<caption><h2>Send New E-mail</h2></caption>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>Recipient address </td>");
        bodyHtml.append("<td><input type=\"text\" name=\"recipient\" size=\"50\"/></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>Subject </td>");
        bodyHtml.append("<td><textarea rows=\"10\" cols=\"39\" name=\"subject\"></textarea> </td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>Content </td>");
        bodyHtml.append("<td><textarea rows=\"10\" cols=\"39\" name=\"content\"></textarea> </td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td>Attach file </td>");
        bodyHtml.append("<td><input type=\"file\" name=\"file\" size=\"50\" /></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("<tr>");
        bodyHtml.append("<td colspan=\"2\" align=\"center\"><input type=\"submit\" value=\"Send\"/></td>");
        bodyHtml.append("</tr>");
        bodyHtml.append("</table>");
        bodyHtml.append("</form>");
        if (req.getAttribute("message") != null) {
            bodyHtml.append(req.getAttribute("message"));
            req.setAttribute("message", null);
        }
        HTTPResponseCreator.createSimpleResponse(res,
                MessageUtils.getMsg("title.ognl.expression.injection.page", locale), bodyHtml.toString());
    }

    /**
     * handles form submission
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        List<File> uploadedFiles = saveUploadedFiles(req);

        String recipient = req.getParameter("recipient");
        String subject = req.getParameter("subject");
        String content = req.getParameter("content");

        String resultMessage = "";

        try {
            EmailUtility.sendEmailWithAttachment(recipient, subject, content, uploadedFiles);

            resultMessage = "The e-mail was sent successfully";
        } catch (Exception ex) {
            ex.printStackTrace();
            resultMessage = "There were an error: " + ex.getMessage();
        } finally {
            deleteUploadFiles(uploadedFiles);
            req.setAttribute("message", resultMessage);
            doGet(req, res);
            // getServletContext().getRequestDispatcher("/Result.jsp").forward(request, response);
        }
    }

    /**
     * Saves files uploaded from the client and return a list of these files which will be attached
     * to the e-mail message.
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
                System.out.println("saveFile: " + saveFile.getAbsolutePath());
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
