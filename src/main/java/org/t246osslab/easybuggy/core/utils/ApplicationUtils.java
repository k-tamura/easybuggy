package org.t246osslab.easybuggy.core.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to provide application properties.
 */
public final class ApplicationUtils {
    
    private static final Logger log = LoggerFactory.getLogger(ApplicationUtils.class);

    // default database url: derby in-memory
    private static String databaseURL = "jdbc:derby:memory:demo;create=true";

    // default database url: null
    private static String databaseDriver = null;
    
    // default account lock time: 3600000 (1 hour)
    private static long accountLockTime = 3600000;
    
    // default account lock limit count: 10
    private static int accountLockCount = 10;

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
        try {
            accountLockTime = Long.parseLong(bundle.getString("account.lock.time"));
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
        try {
            accountLockCount = Integer.parseInt(bundle.getString("account.lock.count"));
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
    }

    // squid:S1118: Utility classes should not have public constructors
    private ApplicationUtils() {
        throw new IllegalAccessError("Utility class");
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
    
    /**
     * Return the account lock time.
     * 
     * @return Account lock time
     */
    public static long getAccountLockTime() {
        return accountLockTime;
    }

     /**
      * Return the account lock count.
      * 
      * @return Account lock count
      */
     public static int getAccountLockCount() {
        return accountLockCount;
    }
}
