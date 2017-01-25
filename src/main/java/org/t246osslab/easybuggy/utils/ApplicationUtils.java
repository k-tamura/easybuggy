package org.t246osslab.easybuggy.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.pmw.tinylog.Logger;

/**
 * Utility class to provide application properties.
 */
public class ApplicationUtils {

    // default port: 8989
    private static int easyBuggyPort = 8989;

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
            easyBuggyPort = Integer.parseInt(bundle.getString("easybuggy.port"));
        } catch (Exception e) {
            Logger.error(e);
        }
        try {
            databaseURL = bundle.getString("database.url");
        } catch (Exception e) {
            Logger.error(e);
        }
        try {
            databaseDriver = bundle.getString("database.driver");
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    /**
     * Return a Port number of EasyBuggy.
     * 
     * @return Port number of EasyBuggy
     */
    public static int getEasyBuggyPort() {
        return easyBuggyPort;
    }

    /**
     * Return a Database URL of EasyBuggy.
     * 
     * @return Database URL of EasyBuggy
     */
    public static String getDatabaseURL() {
        return databaseURL;
    }

    /**
     * Return a Database driver of EasyBuggy.
     * 
     * @return Database driver of EasyBuggy
     */
    public static String getDatabaseDriver() {
        return databaseDriver;
    }
}
