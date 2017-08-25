package org.t246osslab.easybuggy.core.listeners;

import java.io.OutputStream;
import java.io.PrintStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.owasp.esapi.ESAPI;

@WebListener
public class InitializationListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {

        /*
         * Suppress noisy messages output by the ESAPI library. For more detail:
         * https://stackoverflow.com/questions/45857064/how-to-suppress-messages-output-by-esapi-library
         */
        try {
            PrintStream original = System.out;
            PrintStream out = new PrintStream(new OutputStream() {
                public void write(int b) {
                    // Do nothing
                }
            });
            System.setOut(out);
            System.setErr(out);
            ESAPI.encoder();
            System.setOut(original);
        } catch (Exception e) {
            // Do nothing
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
