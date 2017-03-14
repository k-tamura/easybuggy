package org.t246osslab.easybuggy;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.TagLibConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

/**
 * Embedded Jetty Server.
 */
public class EmbeddedJettyServer {
    
    /**
     * Main method to start up the embedded Jetty server.
     * This method is usually called from 'mvn install exex:exec'.
     */
    public static void main(String[] args) throws Exception {

        Server server = new Server(Integer.parseInt(args[0]));
        String wardir = "target/easybuggy-1-SNAPSHOT";

        WebAppContext context = new WebAppContext();
        context.setResourceBase(wardir);
        context.setDescriptor(wardir + "WEB-INF/web.xml");
        context.setConfigurations(new Configuration[] { new AnnotationConfiguration(), new WebXmlConfiguration(),
                new WebInfConfiguration(), new TagLibConfiguration(), new PlusConfiguration(),
                new MetaInfConfiguration(), new FragmentConfiguration(), new EnvConfiguration() });
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        //context.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        server.setHandler(context);
        server.start();
        server.dump(System.err);
        server.join();
    }
}
