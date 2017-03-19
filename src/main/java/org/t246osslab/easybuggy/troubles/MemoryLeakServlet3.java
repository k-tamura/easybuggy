package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.util.zip.Deflater;

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
@WebServlet(urlPatterns = { "/memoryleak3" })
public class MemoryLeakServlet3 extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(MemoryLeakServlet3.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            String inputString = "inputString";
            byte[] input = inputString.getBytes();
            byte[] output = new byte[100];
            for (int i = 0; i < 1000; i++) {
                Deflater compresser = new Deflater();
                compresser.setInput(input);
                compresser.deflate(output);
            }
            HTTPResponseCreator.createSimpleResponse(res, null,
                    MessageUtils.getMsg("msg.c.heap.space.leak.occur", req.getLocale()));
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }
}
