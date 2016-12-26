package org.t246osslab.easybuggy.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.pmw.tinylog.Logger;
public class ApplicationUtils {
    
    // default port: 8989
    private static int openBuggyPort = 8989;
    
    // default database url: derby in-memory
    private static String databaseURL = "jdbc:derby:memory:demo;create=true";
    
    // default database url: null
    private static String databaseDriver = null;
    
    static {
        ResourceBundle bundle = null;
        try {
            bundle = ResourceBundle.getBundle("application");
        } catch (MissingResourceException e) {
            Logger.error(e);
        }
        try {
            openBuggyPort = Integer.parseInt(bundle.getString("easybuggy.port"));
        } catch (Exception e) {
            Logger.error(e);
        }
        try {
            databaseURL = bundle.getString("database.url");
        } catch (Exception e) {
            Logger.error(e);
        }
        try {
            if (!databaseURL.startsWith("jdbc:derby:memory")) {
                databaseDriver = bundle.getString("database.driver");
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public static int getEasyBuggyPort() {
        return openBuggyPort;
    }

    public static String getDatabaseURL() {
        return databaseURL;
    }

    public static String getDatabaseDriver() {
        return databaseDriver;
    }
}
