package org.t246osslab.easybuggy.utils;

import java.io.Closeable;
import java.io.IOException;

public class Closer {

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
