package org.t246osslab.easybuggy.vulnerabilities;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.Closer;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/ureupload" })
//2MB, 10MB, 50MB
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10,
maxRequestSize = 1024 * 1024 * 50)
public class UnrestrictedExtensionUploadServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(UnrestrictedSizeUploadServlet.class);

    // Name of the directory where uploaded files is saved
    private static final String SAVE_DIR = "uploadFiles";

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Locale locale = req.getLocale();

        StringBuilder bodyHtml = new StringBuilder();
        bodyHtml.append("<form method=\"post\" action=\"ureupload\" enctype=\"multipart/form-data\">");
        bodyHtml.append(MessageUtils.getMsg("msg.reverse.color", locale));
        bodyHtml.append("<br><br>");
        bodyHtml.append("<input type=\"file\" name=\"file\" size=\"60\" /><br>");
        bodyHtml.append(MessageUtils.getMsg("msg.select.upload.file", locale));
        bodyHtml.append("<br><br>");
        bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.upload", locale) + "\" />");
        bodyHtml.append("<br><br>");
        if (req.getAttribute("errorMessage") != null) {
            bodyHtml.append(req.getAttribute("errorMessage"));
            bodyHtml.append("<br><br>");
        }
        bodyHtml.append(MessageUtils.getMsg("msg.note.unrestricted.ext.upload", locale));
        bodyHtml.append("</form>");
        HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.unrestricted.upload", locale),
                bodyHtml.toString());
    }


    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Locale locale = req.getLocale();

        // Get absolute path of the web application
        String appPath = req.getServletContext().getRealPath("");

        // Create a directory to save the uploaded file if it does not exists
        String savePath = appPath + File.separator + SAVE_DIR;
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        // Save the file
        OutputStream out = null;
        InputStream in = null;
        Part filePart = null;
        try {
            filePart = req.getPart("file");
        } catch (Exception e) {
            req.setAttribute("errorMessage", MessageUtils.getMsg("msg.max.file.size.exceed", locale));
            doGet(req, res);
            return;
        }
        try {
            String fileName = getFileName(filePart);
            if (fileName == null || fileName.equals("")) {
                doGet(req, res);
                return;
            }
            // TODO Remove this try block that is a workaround of issue #9 (FileNotFoundException on Jetty * Windows)
            boolean isConverted = false;
            try {
                out = new FileOutputStream(savePath + File.separator + fileName);
                in = filePart.getInputStream();
                int read = 0;
                final byte[] bytes = new byte[1024];
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            } catch (FileNotFoundException e) {
                // Ignore because file already exists
                isConverted = true;
            }

            try {
                // Reverse the color of the upload image
                if(!isConverted){
                    revereColor(new File(savePath + File.separator + fileName).getAbsolutePath());
                    isConverted = true;
                }
            } catch (Exception e) {
                // Log and ignore the exception
                log.warn("Exception occurs: ", e);
            }

            StringBuilder bodyHtml = new StringBuilder();
            if (isConverted) {
                bodyHtml.append(MessageUtils.getMsg("msg.reverse.color.complete", locale));
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.reverse.color.fail", locale));
            }
            if (isConverted) {
                bodyHtml.append("<br><br>");
                bodyHtml.append("<img src=\"" + SAVE_DIR + "/" + fileName + "\">");
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append("<INPUT type=\"button\" onClick='history.back();' value=\""
                    + MessageUtils.getMsg("label.history.back", locale) + "\">");
            HTTPResponseCreator.createSimpleResponse(res, MessageUtils.getMsg("title.unrestricted.upload", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(out, in);
        }
    }

    // Get file name from content-disposition filename
    private String getFileName(final Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    // Reverse the color of the image file
    private void revereColor(String fileName) throws IOException {
        BufferedImage image = ImageIO.read(new File(fileName));
        WritableRaster raster = image.getRaster();
        int[] pixelBuffer = new int[raster.getNumDataElements()];
        for (int y = 0; y < raster.getHeight(); y++) {
            for (int x = 0; x < raster.getWidth(); x++) {
                raster.getPixel(x, y, pixelBuffer);
                pixelBuffer[0] = ~pixelBuffer[0];
                pixelBuffer[1] = ~pixelBuffer[1];
                pixelBuffer[2] = ~pixelBuffer[2];
                raster.setPixel(x, y, pixelBuffer);
            }
        }
        // Output the image
        ImageIO.write(image, "png", new File(fileName));
    }
}
