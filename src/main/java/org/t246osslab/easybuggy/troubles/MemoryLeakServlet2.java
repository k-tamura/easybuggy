package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.List;
import java.util.Locale;

import javassist.CannotCompileException;
import javassist.ClassPool;

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
@WebServlet(urlPatterns = { "/memoryleak2" })
public class MemoryLeakServlet2 extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MemoryLeakServlet2.class);

    private int i = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        StringBuilder bodyHtml = new StringBuilder();
        Locale locale = req.getLocale();
        try {
            toDoRemove();
            
            List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
            for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
                if (MemoryType.NON_HEAP.equals(memoryPoolMXBean.getType())) {
                    bodyHtml.append("<p>" + memoryPoolMXBean.getName() + "</p>");
                    bodyHtml.append("<table class=\"table table-striped table-bordered table-hover\" style=\"font-size:small;\">");
                    bodyHtml.append("<tr><th></th>");
                    bodyHtml.append("<th width=\"18%\">" + MessageUtils.getMsg("label.memory.init", locale) + "</th>");
                    bodyHtml.append("<th width=\"18%\">" + MessageUtils.getMsg("label.memory.used", locale) + "</th>");
                    bodyHtml.append("<th width=\"18%\">" + MessageUtils.getMsg("label.memory.committed", locale) + "</th>");
                    bodyHtml.append("<th width=\"18%\">" + MessageUtils.getMsg("label.memory.max", locale) + "</th></tr>");
                    writeUsageRow(bodyHtml, memoryPoolMXBean.getUsage(), MessageUtils.getMsg("label.memory.usage", locale));
                    writeUsageRow(bodyHtml, memoryPoolMXBean.getPeakUsage(), MessageUtils.getMsg("label.memory.peak.usage", locale));
                    writeUsageRow(bodyHtml, memoryPoolMXBean.getCollectionUsage(), MessageUtils.getMsg("label.memory.collection.usage", locale));
                    bodyHtml.append("</table>");
                }
            }
            String permName = (System.getProperty("java.version").startsWith("1.6") || System.getProperty("java.version").startsWith("1.7"))
                    ? MessageUtils.getMsg("label.permgen.space", locale) : MessageUtils.getMsg("label.metaspace",locale);
            bodyHtml.append(MessageUtils.getInfoMsg("msg.permgen.space.leak.occur", new String[] { permName }, req.getLocale()));

        } catch (Exception e) {
            log.error("Exception occurs: ", e);
            bodyHtml.append(MessageUtils.getErrMsg("msg.unknown.exception.occur", new String[] { e.getMessage() },
                    locale));
        } finally {
            HTTPResponseCreator.createSimpleResponse(req, res, MessageUtils.getMsg("title.memoryleak2.page", locale),
                    bodyHtml.toString());
        }
    }

    private void writeUsageRow(StringBuilder bodyHtml, MemoryUsage usage, String usageName) {
        if (usage != null) {
            bodyHtml.append("<tr><td>" + usageName + "</td>");
            bodyHtml.append("<td>" + usage.getInit() + "</td>");
            bodyHtml.append("<td>" + usage.getUsed() + "</td>");
            bodyHtml.append("<td>" + usage.getCommitted() + "</td>");
            bodyHtml.append("<td>" + (usage.getMax() == -1 ? "[undefined]" : usage.getMax()) + "</td></tr>");
        }
    }

    private void toDoRemove() throws CannotCompileException {
        int j = i + 1000;
        ClassPool pool = ClassPool.getDefault();
        for (; i < j; i++) {
            pool.makeClass("org.t246osslab.easybuggy.core.model.TestClass" + i).toClass();
        }
    }
}
