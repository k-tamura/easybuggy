package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.util.zip.Deflater;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;
import org.t246osslab.easybuggy.utils.MessageUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/memoryleak3" })
public class MemoryLeakServlet3 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            String inputString = "inputString";
            byte[] input = inputString.getBytes();
            byte[] output = new byte[100];
            // for (int i = 0; i < 100000; i++) {
            for (int i = 0; i < 1000; i++) {
                Deflater compresser = new Deflater();
                compresser.setInput(input);
                compresser.deflate(output);
            }
            HTTPResponseCreator.createSimpleResponse(res, null,
                    MessageUtils.getMsg("msg.c.heap.space.leak.occur", req.getLocale()));
        } catch (Exception e) {
            Logger.error(e);
        } catch (Error e) {
            Logger.error(e);
        }
    }
}
