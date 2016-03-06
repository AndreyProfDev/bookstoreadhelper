package com.andreyprofdev.protractorendtoendtestsrunner;

import com.andreyprofdev.protractorendtoendtestsrunner.ProtractorEndToEndTestsRunner;
import com.andreyprofdev.protractorendtoendtestsrunner.jetty.*;
import com.andreyprofdev.protractorendtoendtestsrunner.protractor.*;
import com.andreyprofdev.protractorendtoendtestsrunner.protractor.Process;
import com.andreyprofdev.protractorendtoendtestsrunner.springboot.SpringBootServer;
import com.andreyprofdev.protractorendtoendtestsrunner.springboot.SpringBootServerConfiguration;
import com.andreyprofdev.protractorendtoendtestsrunner.springboot.SpringBootServerFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

public class ProtractorEndToEndTestsRunnerTest {

    private Mockery context = new Mockery();

    private String WORKING_DIRECTORY = "/working_dir/";
    private String NODE_INSTALL_DIRECTORY = "/nodeInstallDirectory";
    private String PROTRACTOR_CONF_PATH = "/protractor_conf_path";

    private String WEB_MANAGER_ERROR = "Selenium Server is up and running";

    private static final String PROTRACTOR_LOCATION = "node_modules/protractor/bin".replace("/", File.separator);

    private static final String PROTRACTOR_RUNNER_LOCATION = PROTRACTOR_LOCATION + "/protractor".replace("/", File.separator);
    private static final String WEB_MANAGER_RUNNER_LOCATION = PROTRACTOR_LOCATION + "/webdriver-manager".replace("/", File.separator);


    private int JETTY_SERVER1_PORT = 11;
    private String JETTY_SERVER1_WAR_LOCATION = "/war_location1";

    private int JETTY_SERVER2_PORT = 12;
    private String JETTY_SERVER2_WAR_LOCATION = "/war_location2";

    private int SPRING_BOOT_SERVER_PORT = 13;
    private String SPRING_BOOT_SERVER_JAR_LOCATION = "/jar_location";

    @Test
    public void test() throws Exception {
        JettyServerFactory jettyFactory = context.mock(JettyServerFactory.class);
        JettyServer jettyServer1 = context.mock(JettyServer.class, "webappJettyServer");
        JettyServer jettyServer2 = context.mock(JettyServer.class, "restBackendJettyServer");

        SpringBootServerFactory  springBootServerFactory = context.mock(SpringBootServerFactory.class);
        SpringBootServer springBootServer = context.mock(SpringBootServer.class, "springBootServer");

        ProcessFactory processFactory = context.mock(ProcessFactory.class);

        ProtractorRunner protractorRunner = new ProtractorRunnerImpl(
                NODE_INSTALL_DIRECTORY, WORKING_DIRECTORY,
                PROTRACTOR_CONF_PATH,
                processFactory);

        com.andreyprofdev.protractorendtoendtestsrunner.protractor.Process webManagerUpdateProcess = context.mock(Process.class, "webManagerUpdateProcess");
        Process webManagerStartProcess = context.mock(Process.class, "webManagerStartProcess");
        Process protractorStartProcess = context.mock(Process.class, "protractorStartProcess");
        context.checking(new Expectations() {{
            /**
             * Start jetty servers
             */
            oneOf(jettyFactory).buildJettyServer(new JettyServerConfiguration(JETTY_SERVER1_WAR_LOCATION, JETTY_SERVER1_PORT));
            will(returnValue(jettyServer1));
            oneOf(jettyServer1).start();

            oneOf(jettyFactory).buildJettyServer(new JettyServerConfiguration(JETTY_SERVER2_WAR_LOCATION, JETTY_SERVER2_PORT));
            will(returnValue(jettyServer2));
            oneOf(springBootServer).start();

            /**
             * Start spring boot server
             */
            oneOf(springBootServerFactory).buildSpringBootServer(new SpringBootServerConfiguration(SPRING_BOOT_SERVER_JAR_LOCATION, SPRING_BOOT_SERVER_PORT));
            will(returnValue(springBootServer));
            oneOf(jettyServer2).start();

            /**
             * Perform protractor tests
             */
            oneOf(processFactory).createNew();
            will(returnValue(webManagerUpdateProcess));
            oneOf(webManagerUpdateProcess).startAndWait(with(equal(Arrays.asList(getNodePath(), WORKING_DIRECTORY + WEB_MANAGER_RUNNER_LOCATION, "update"))));

            oneOf(processFactory).createNew();
            will(returnValue(webManagerStartProcess));
            oneOf(webManagerStartProcess).startAndWaitForError(Arrays.asList(getNodePath(), WORKING_DIRECTORY + WEB_MANAGER_RUNNER_LOCATION, "start"), WEB_MANAGER_ERROR);

            oneOf(processFactory).createNew();
            will(returnValue(protractorStartProcess));
            oneOf(protractorStartProcess).startAndWait(Arrays.asList(getNodePath(), WORKING_DIRECTORY + PROTRACTOR_RUNNER_LOCATION, WORKING_DIRECTORY + PROTRACTOR_CONF_PATH));

            oneOf(webManagerStartProcess).stop();

            /**
             * Stop jetty servers
             */
            oneOf(jettyServer1).stop();
            oneOf(jettyServer1).getWarLocation();
            will(returnValue(JETTY_SERVER1_WAR_LOCATION));

            oneOf(jettyServer2).stop();
            oneOf(jettyServer2).getWarLocation();
            will(returnValue(JETTY_SERVER2_WAR_LOCATION));

            /**
             * Stop spring boot server
             */
            oneOf(springBootServer).stop();
            oneOf(springBootServer).getJarLocation();
            will(returnValue(SPRING_BOOT_SERVER_JAR_LOCATION));
        }});


        ProtractorEndToEndTestsRunner runner = new ProtractorEndToEndTestsRunner();

        runner.setJettyServerConfigurations(prepareJettyServerConfigurations());
        runner.setJettyServerFactory(jettyFactory);

        runner.setSpringBootServerConfiguration(prepareSpringBootServerConfigurations());
        runner.setSpringBootServerFactory(springBootServerFactory);

        runner.setProtractorRunner(protractorRunner);
        runner.start();
    }

    private String getNodePath(){
        return new NodePathResolver(NODE_INSTALL_DIRECTORY).getNodePath();
    }

    private JettyServerConfiguration[] prepareJettyServerConfigurations(){
        JettyServerConfiguration[] serverConfigurations = new JettyServerConfiguration[2];

        JettyServerConfiguration serverConfiguration = new JettyServerConfiguration();
        serverConfiguration.setPort(JETTY_SERVER1_PORT);
        serverConfiguration.setWarLocation(JETTY_SERVER1_WAR_LOCATION);
        serverConfigurations[0] = serverConfiguration;

        serverConfiguration = new JettyServerConfiguration();
        serverConfiguration.setPort(JETTY_SERVER2_PORT);
        serverConfiguration.setWarLocation(JETTY_SERVER2_WAR_LOCATION);
        serverConfigurations[1] = serverConfiguration;

        return serverConfigurations;
    }

    private SpringBootServerConfiguration[] prepareSpringBootServerConfigurations(){
        SpringBootServerConfiguration[] serverConfigurations = new SpringBootServerConfiguration[1];

        SpringBootServerConfiguration serverConfiguration = new SpringBootServerConfiguration();
        serverConfiguration.setPort(SPRING_BOOT_SERVER_PORT);
        serverConfiguration.setJarLocation(SPRING_BOOT_SERVER_JAR_LOCATION);
        serverConfigurations[0] = serverConfiguration;

        return serverConfigurations;
    }
}
