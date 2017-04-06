package org.t246osslab.easybuggy.performance;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.core.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/strplusopr" })
public class StringPlusOperationServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(StringPlusOperationServlet.class);

    private static final int MAX_LENGTH = 1000000;
    private static final String[] ALL_NUMBERS = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
    private static final String[] ALL_UPPER_CHARACTERS = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    private static final String[] ALL_LOWER_CHARACTERS = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
    private static final String[] ALL_SIGNS = { "!", "#", "$", "%", "&", "(", ")", "*", "+", ",", "-", ".", "/", ":",
            ";", "<", "=", ">", "?", "@", "[", "]", "^", "_", "{", "|", "}" };

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            int length = 0;
            try {
                length = Integer.parseInt(req.getParameter("length"));
            } catch (NumberFormatException e) {
            }
            String[] characters = req.getParameterValues("characters");
            Locale locale = req.getLocale();

            StringBuilder bodyHtml = new StringBuilder();
            bodyHtml.append("<form action=\"strplusopr\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("description.random.string.generator", locale));
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getMsg("label.character.count", locale) + ": ");
            bodyHtml.append("<br>");
            if (length > 0) {
                bodyHtml.append(
                        "<input type=\"text\" name=\"length\" size=\"6\" maxlength=\"6\" value=\"" + length + "\">");
            } else {
                bodyHtml.append("<input type=\"text\" name=\"length\" size=\"6\" maxlength=\"6\">");
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append("<p>" + MessageUtils.getMsg("label.available.characters", locale) + "</p>");

            appendCheckBox(characters, locale, bodyHtml, ALL_NUMBERS, "label.numbers");
            appendCheckBox(characters, locale, bodyHtml, ALL_UPPER_CHARACTERS, "label.uppercase.characters");
            appendCheckBox(characters, locale, bodyHtml, ALL_LOWER_CHARACTERS, "label.lowercase.characters");
            appendCheckBox(characters, locale, bodyHtml, ALL_SIGNS, "label.signs");

            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.submit", locale) + "\">");
            bodyHtml.append("<br><br>");

            if (length > 0) {
                // StringBuilder builder = new StringBuilder();
                String s = "";
                java.util.Random rand = new java.util.Random();
                Date startDate = new Date();
                log.info("Start Date: {}", startDate.toString());
                for (int i = 0; i < length && i < MAX_LENGTH; i++) {
                    s = s + characters[rand.nextInt(characters.length)];
                    // builder.append(characters[rand.nextInt(characters.length)]);
                }
                Date endDate = new Date();
                log.info("End Date: {}", endDate.toString());
                bodyHtml.append(MessageUtils.getMsg("label.execution.result", locale) + "<BR><BR>");
                // bodyHtml.append(ESAPI.encoder().encodeForHTML(builder.toString()));
                bodyHtml.append(ESAPI.encoder().encodeForHTML(s));
            } else {
                bodyHtml.append(MessageUtils.getMsg("msg.enter.positive.number", locale));
            }
            bodyHtml.append("<br><br>");
            bodyHtml.append(MessageUtils.getInfoMsg("msg.note.slow.string.plus.operation", locale));
            bodyHtml.append("</form>");
            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.random.string.generator", locale),
                    bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }

    private void appendCheckBox(String[] characters, Locale locale, StringBuilder bodyHtml, String[] allCharacters,
            String label) {
        bodyHtml.append("<p>" + MessageUtils.getMsg(label, locale) + "</p>");
        bodyHtml.append("<p>");
        for (int i = 0; i < allCharacters.length; i++) {
            bodyHtml.append("<input type=\"checkbox\" name=\"characters\" value=\"");
            bodyHtml.append(allCharacters[i]);
            if (characters == null || Arrays.asList(characters).contains(allCharacters[i])) {
                bodyHtml.append("\" checked=\"checked\">");
            } else {
                bodyHtml.append("\">");
            }
            bodyHtml.append(allCharacters[i]);
            bodyHtml.append(" ");
        }
        bodyHtml.append("</p>");
    }
}
