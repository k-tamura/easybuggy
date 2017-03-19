package org.t246osslab.easybuggy.core.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to safely close all Closeable objects.
 */
public final class Closer {

    private static final Logger log = LoggerFactory.getLogger(MessageUtils.class);

    // squid:S1118: Utility classes should not have public constructors
    private Closer() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * Close all Closeable objects.
     *
     * @param closeables Closeable objects.
     */
    public static void close(AutoCloseable... closeables) {
        if (closeables != null) {
            for (AutoCloseable closeable : closeables) {
                try {
                    if(closeable != null){
                        closeable.close();
                    }
                } catch (IOException e) {
                    log.error("IOException occurs: ", e);
                } catch (Exception e) {
                    log.error("Exception occurs: ", e);
                }
            }
        }
    }
}
