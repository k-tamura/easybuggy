package org.t246osslab.easybuggy.exceptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.Closer;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/boe" })
public class BufferOverflowExceptionServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(BufferOverflowExceptionServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RandomAccessFile raf = null;
        try {
            File f = new File("test.txt");
            raf = new RandomAccessFile(f, "rw");
            FileChannel ch = raf.getChannel();
            MappedByteBuffer buf = ch.map(MapMode.READ_WRITE, 0, f.length());
            final byte[] src = new byte[10];
            buf.put(src);
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException occurs: ", e);
        } catch (IOException e) {
            log.error("IOException occurs: ", e);
        } finally {
            Closer.close(raf);
        }
    }
}
