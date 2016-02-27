package com.andreyprofdev.protractorendtoendtestsrunner.jetty;

import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class JettyServerFactoryTest {

    private final int JETTY_SERVER_PORT = 8005;
    private final String SYSTEM_PROP_NAME = "sysPropName";
    private final String SYSTEM_PROP_VALUE = "sysPropValue";

    @Test
    public void test() throws Exception {
        SystemProperty systemProperty = new SystemProperty(SYSTEM_PROP_NAME, SYSTEM_PROP_VALUE);
        JettyServerFactory jettyServerFactory = new JettyServerFactoryImpl(new SystemProperty[]{systemProperty});
        File warFile = File.createTempFile("war-file", ".war");

        JettyServerConfiguration configuration = new JettyServerConfiguration(warFile.getAbsolutePath(), JETTY_SERVER_PORT);

        JettyServer server = jettyServerFactory.buildJettyServer(configuration);

        ServerConnector connector = ((ServerConnector) server.getServer().getConnectors()[0]);
        assertEquals(JETTY_SERVER_PORT, connector.getPort());

        WebAppContext webAppContextHandler = (WebAppContext) server.getServer().getHandler();
        assertEquals(warFile.getAbsolutePath(), webAppContextHandler.getWar());

        server.getServer().setHandler(null);

        server.start();
        assertEquals(System.getProperties().getProperty(SYSTEM_PROP_NAME), SYSTEM_PROP_VALUE);
    }
}
