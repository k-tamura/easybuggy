package org.t246osslab.easybuggy.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.pmw.tinylog.Logger;

public class MessageUtils {

    public static String getMsg(String propertyKey, Locale locale) {
        return getMsg(propertyKey, (Object[])null, locale);
    }

    public static String getMsg(String propertyKey, Object[] placeholders, Locale locale) {
        String propertyValue = null;
        try {
            propertyValue = ResourceBundle.getBundle("messages", locale).getString(propertyKey);
            if (placeholders != null) {
                propertyValue = MessageFormat.format(propertyValue, placeholders);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return propertyValue;
    }
}
