package org.t246osslab.easybuggy.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to provide application properties.
 */
public class ApplicationUtils {

    private static Logger log = LoggerFactory.getLogger(ApplicationUtils.class);

    // default database url: derby in-memory
    private static String databaseURL = "jdbc:derby:memory:demo;create=true";

    // default database url: null
    private static String databaseDriver = null;

    static {
        ResourceBundle bundle = null;
        try {
            bundle = ResourceBundle.getBundle("application");
        } catch (MissingResourceException e) {
            log.error("Exception occurs: ", e);
        }
        try {
            databaseURL = bundle.getString("database.url");
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
        try {
            databaseDriver = bundle.getString("database.driver");
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
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
