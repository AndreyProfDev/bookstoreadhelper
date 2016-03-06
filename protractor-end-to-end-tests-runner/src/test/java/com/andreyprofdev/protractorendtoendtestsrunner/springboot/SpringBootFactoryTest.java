package com.andreyprofdev.protractorendtoendtestsrunner.springboot;

import com.andreyprofdev.protractorendtoendtestsrunner.jetty.JettyServer;
import com.andreyprofdev.protractorendtoendtestsrunner.jetty.JettyServerConfiguration;
import com.andreyprofdev.protractorendtoendtestsrunner.jetty.JettyServerFactory;
import com.andreyprofdev.protractorendtoendtestsrunner.jetty.JettyServerFactoryImpl;
import com.andreyprofdev.protractorendtoendtestsrunner.jetty.SystemProperty;
import com.andreyprofdev.protractorendtoendtestsrunner.protractor.*;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SpringBootFactoryTest {
    private final int SPRING_BOOT_SERVER_PORT = 8005;
    private final String SYSTEM_PROP_NAME = "sysPropName";
    private final String SYSTEM_PROP_VALUE = "sysPropValue";

    private String APP_STARTED_NOTIFICATION = "Started Application in";

    private Mockery context = new Mockery();

    @Test
    public void testAppStart() throws Exception {
        SystemProperty systemProperty = new SystemProperty(SYSTEM_PROP_NAME, SYSTEM_PROP_VALUE);

        ProcessFactory processFactory = context.mock(ProcessFactory.class);
        com.andreyprofdev.protractorendtoendtestsrunner.protractor.Process springBootProcess = context.mock(com.andreyprofdev.protractorendtoendtestsrunner.protractor.Process.class);
        SpringBootServerFactoryImpl springBootServerFactory = new SpringBootServerFactoryImpl(new SystemProperty[]{systemProperty}, processFactory);
        File jarFile = File.createTempFile("jar-file", ".jar");

        SpringBootServerConfiguration configuration = new SpringBootServerConfiguration(jarFile.getAbsolutePath(), SPRING_BOOT_SERVER_PORT);

        SpringBootServer server = springBootServerFactory.buildSpringBootServer(configuration);

        /**
         * Attach logger to check port and server
         */
        context.checking(new Expectations() {{

            /**
             * Perform protractor tests
             */
            oneOf(processFactory).createNew();
            will(returnValue(springBootProcess));
            oneOf(springBootProcess).startAndWaitForInfo(
                    with(equal(Arrays.asList("java", "-Dserver.port=" + SPRING_BOOT_SERVER_PORT,
                            "-D" + SYSTEM_PROP_NAME + "=" + SYSTEM_PROP_VALUE, "-jar",
                            jarFile.getAbsolutePath()))), with(equal(APP_STARTED_NOTIFICATION)));

//            oneOf(springBootProcess).stop();
        }});

        server.start();
    }

    @Test
    public void testAppStop() throws Exception {
        SystemProperty systemProperty = new SystemProperty(SYSTEM_PROP_NAME, SYSTEM_PROP_VALUE);

        ProcessFactory processFactory = context.mock(ProcessFactory.class);
        com.andreyprofdev.protractorendtoendtestsrunner.protractor.Process springBootProcess = context.mock(com.andreyprofdev.protractorendtoendtestsrunner.protractor.Process.class);
        SpringBootServerFactoryImpl springBootServerFactory = new SpringBootServerFactoryImpl(new SystemProperty[]{systemProperty}, processFactory);
        File jarFile = File.createTempFile("jar-file", ".jar");

        SpringBootServerConfiguration configuration = new SpringBootServerConfiguration(jarFile.getAbsolutePath(), SPRING_BOOT_SERVER_PORT);

        SpringBootServer server = springBootServerFactory.buildSpringBootServer(configuration);

        /**
         * Attach logger to check port and server
         */
        context.checking(new Expectations() {{

            /**
             * Perform protractor tests
             */
            oneOf(processFactory).createNew();
            will(returnValue(springBootProcess));
            oneOf(springBootProcess).startAndWaitForInfo(
                    with(equal(Arrays.asList("java", "-Dserver.port=" + SPRING_BOOT_SERVER_PORT,
                            "-D" + SYSTEM_PROP_NAME + "=" + SYSTEM_PROP_VALUE, "-jar",
                            jarFile.getAbsolutePath()))), with(equal(APP_STARTED_NOTIFICATION)));

            oneOf(springBootProcess).stop();
        }});

        server.start();
        server.stop();
    }
}
