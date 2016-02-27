package com.andreyprofdev.protractorendtoendtestsrunner.jetty;

import org.eclipse.jetty.server.Server;

public interface JettyServer {
    void start() throws Exception;

    void stop() throws Exception;

    String getWarLocation();

    Server getServer();
}
