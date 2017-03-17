package org.t246osslab.easybuggy.core.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to provide message properties.
 */
public class MessageUtils {

    private static Logger log = LoggerFactory.getLogger(MessageUtils.class);

    // squid:S1118: Utility classes should not have public constructors
    private MessageUtils() {
        throw new IllegalAccessError("Utility class");
    }
    
    /**
     * Return a message for a given property key.
     * 
     * @return A message for a given property key
     */
    public static String getMsg(String propertyKey, Locale locale) {
        return getMsg(propertyKey, (Object[]) null, locale);
    }

    /**
     * Return a message for a given property key, replaced with placeholders.
     * 
     * @return A message for a given property key, replaced with placeholders
     */
    public static String getMsg(String propertyKey, Object[] placeholders, Locale locale) {
        String propertyValue = null;
        try {
            propertyValue = ResourceBundle.getBundle("messages", locale).getString(propertyKey);
            if (placeholders != null) {
                propertyValue = MessageFormat.format(propertyValue, placeholders);
            }
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        }
        return propertyValue;
    }
}
