package org.t246osslab.easybuggy.troubles;

import java.io.IOException;
import java.util.zip.Deflater;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/memoryleak3" })
public class MemoryLeakServlet3 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String inputString = "string to compress";
        byte[] input = inputString.getBytes();
        byte[] output = new byte[100];
        // for (int i = 0; i < 100000; i++) {
        for (int i = 0; i < 100; i++) {
            Deflater compresser = new Deflater();
            compresser.setInput(input);
            compresser.deflate(output);
        }
    }
}
