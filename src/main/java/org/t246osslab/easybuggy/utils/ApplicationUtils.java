package org.t246osslab.easybuggy.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.pmw.tinylog.Logger;
public class ApplicationUtils {
    
    // default port: 8989
    private static int openBuggyPort = 8989;
    
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
    }

    public static int getEasyBuggyPort() {
        return openBuggyPort;
    }
}
