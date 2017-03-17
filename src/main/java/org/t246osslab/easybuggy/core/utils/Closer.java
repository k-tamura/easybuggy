package org.t246osslab.easybuggy.core.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Utility class to safely close all Closeable objects.
 */
public class Closer {

    // squid:S1118: Utility classes should not have public constructors
    private Closer() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * Close all Closeable objects.
     *
     * @param closeables Closeable objects.
     */
    public static void close(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                try {
                    if(closeable != null){
                        closeable.close();
                    }
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }
}
