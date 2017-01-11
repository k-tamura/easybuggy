package org.t246osslab.easybuggy.troubles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;
import org.t246osslab.easybuggy.utils.Closer;
import org.t246osslab.easybuggy.utils.HTTPResponseCreator;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/deadlock3" })
public class DeadlockServlet3 extends HttpServlet {

    private static final int MAX_COUNT = 10000;

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        try {
            /* create a batch file in the temp directory */
            File batFile = createBatchFile(req);
            
            if (batFile == null) {
                HTTPResponseCreator.createSimpleResponse(res, null, "Can't create a batch file.");
            } else {
                /* execte the batch */
                ProcessBuilder pb = new ProcessBuilder(batFile.getAbsolutePath());
                Process process = pb.start();
                process.waitFor();
                printInputStream(process.getInputStream(), res, batFile);
                printInputStream(process.getErrorStream(), res, batFile);
            }

        } catch (IOException e) {
            Logger.error(e);
        } catch (InterruptedException e) {
            Logger.error(e);
        }
    }

    private File createBatchFile(HttpServletRequest req) throws IOException {
        BufferedWriter filewriter = null;
        File batFile = null;
        try {
            String osName = System.getProperty("os.name").toLowerCase();
            String batFileName = null;
            String firstLine = null;
            if (osName.toLowerCase().startsWith("windows")) {
                batFileName = "test.bat";
                firstLine = "@echo off";
            } else {
                batFileName = "test.sh";
                firstLine = "#!/bin/sh";
            }

            batFile = new File(System.getProperty("java.io.tmpdir"), batFileName);
            batFile.setExecutable(true);
            filewriter = new BufferedWriter(new FileWriter(batFile));
            filewriter.write(firstLine);
            filewriter.newLine();
            int count = MAX_COUNT;
            try {
                count = Integer.valueOf(req.getParameter("count"));
            } catch (Exception e) {
            }
            if (count > MAX_COUNT) {
                count = MAX_COUNT;
            }
            for (int i = 0; i < count; i++) {
                if (i % 100 == 0) {
                    filewriter.newLine();
                    filewriter.write("echo ");
                }
                filewriter.write(String.valueOf(i % 10));
            }
            filewriter.close();
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            Closer.close(filewriter);
        }
        return batFile;
    }

    private static void printInputStream(InputStream is, HttpServletResponse res, File batFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            sb.append("Create and ececute the batch: " + batFile.getAbsolutePath() + "<BR><BR>");
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            HTTPResponseCreator.createSimpleResponse(res, null, sb.toString());
        } finally {
            Closer.close(br);
            Closer.close(is);
        }
    }
}
