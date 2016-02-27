package com.andreyprofdev.protractorendtoendtestsrunner.jetty;

public interface JettyServerFactory {
    JettyServer buildJettyServer(JettyServerConfiguration jettyServerConfiguration);
}
