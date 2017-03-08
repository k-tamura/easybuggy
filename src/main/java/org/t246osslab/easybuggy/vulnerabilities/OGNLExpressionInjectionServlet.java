package org.t246osslab.easybuggy.vulnerabilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.utils.Closer;
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/ognleijc" })
public class OGNLExpressionInjectionServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(OGNLExpressionInjectionServlet.class);

    // OGNL Expression Injection occurs
    // /ognleijc?input=%23rt%3D%40java.lang.Runtime%40getRuntime()%2C%23rt.exec("calc.exe")
    // /ognleijc?input=%40java.lang.Math%40sqrt(144)
    // /ognleijc?input=%40Math%40sqrt(%40Math%40sqrt(144))
    // /ognleijc?input=%40java.lang.Math%40sqrt(144)%2C%23rt%3D%40java.lang.Runtime%40getRuntime()%2C%23rt.exec("calc.exe")
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        PrintWriter writer = null;
        try {
            Object expr = null;
            Object value = null;
            boolean isValid = true;
            Locale locale = req.getLocale();
            OgnlContext ctx = new OgnlContext();
            String expression = req.getParameter("expression");
            if(expression == null || expression.equals("")){
                isValid = false;
            }else{
                try {
                    expr = Ognl.parseExpression(expression);
                    value = Ognl.getValue(expr, ctx);
                } catch (OgnlException e) {
                    isValid = false;
                    log.error("Exception occurs: ", e);
                }
            }

            StringBuilder bodyHtml = new StringBuilder();
            bodyHtml.append("<form action=\"ognleijc\" method=\"post\">");
            bodyHtml.append(MessageUtils.getMsg("msg.enter.decimal.value", locale));
            bodyHtml.append("<br>");
            bodyHtml.append("<br>");
            if (isValid) {
                bodyHtml.append("<input type=\"text\" name=\"expression\" size=\"100\" maxlength=\"100\" value=" + value
                        + ">");
            } else {
                bodyHtml.append("<input type=\"text\" name=\"expression\" size=\"100\" maxlength=\"100\">");
            }
            bodyHtml.append(" = ");
            if (isValid) {
                bodyHtml.append(value);
            }
            bodyHtml.append("<br>");
            bodyHtml.append("<br>");
            bodyHtml.append("<input type=\"submit\" value=\"" + MessageUtils.getMsg("label.calculate", locale) + "\">");
            bodyHtml.append("<br>");
            bodyHtml.append("<br>");
            bodyHtml.append(MessageUtils.getMsg("msg.note.enter.decimal.value", locale));
            bodyHtml.append("</form>");
            HTTPResponseCreator.createSimpleResponse(res,
                    MessageUtils.getMsg("title.loss.of.trailing.digits.page", locale), bodyHtml.toString());

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            Closer.close(writer);
        }
    }
}
