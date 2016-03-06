package com.andreyprofdev.protractorendtoendtestsrunner;

import com.andreyprofdev.protractorendtoendtestsrunner.jetty.JettyServer;
import com.andreyprofdev.protractorendtoendtestsrunner.jetty.JettyServerConfiguration;
import com.andreyprofdev.protractorendtoendtestsrunner.jetty.JettyServerFactory;
import com.andreyprofdev.protractorendtoendtestsrunner.protractor.ProtractorRunner;
import com.andreyprofdev.protractorendtoendtestsrunner.springboot.SpringBootServer;
import com.andreyprofdev.protractorendtoendtestsrunner.springboot.SpringBootServerConfiguration;
import com.andreyprofdev.protractorendtoendtestsrunner.springboot.SpringBootServerFactory;
import com.andreyprofdev.protractorendtoendtestsrunner.springboot.SpringBootServerFactoryImpl;

import java.util.ArrayList;
import java.util.List;

public class ProtractorEndToEndTestsRunner {

    private JettyServerConfiguration[] jettyServerConfigurations = new JettyServerConfiguration[0];
    private JettyServerFactory jettyServerFactory;

    private ProtractorRunner protractorRunner;

    private SpringBootServerConfiguration[] springBootServerConfiguration = new SpringBootServerConfiguration[0];
    private SpringBootServerFactory springBootServerFactory;

    public void setJettyServerConfigurations(JettyServerConfiguration[] jettyServerConfigurations) {
        this.jettyServerConfigurations = jettyServerConfigurations;
    }

    public void setJettyServerFactory(JettyServerFactory jettyServerFactory) {
        this.jettyServerFactory = jettyServerFactory;
    }

    public void setProtractorRunner(ProtractorRunner protractorRunner) {
        this.protractorRunner = protractorRunner;
    }

    public void setSpringBootServerConfiguration(SpringBootServerConfiguration[] springBootServerConfiguration) {
        this.springBootServerConfiguration = springBootServerConfiguration;
    }

    public void setSpringBootServerFactory(SpringBootServerFactory springBootServerFactory) {
        this.springBootServerFactory = springBootServerFactory;
    }

    public void start() throws Exception {

        List<JettyServer> jettyServers = new ArrayList<>();
        List<SpringBootServer> springBootServers = new ArrayList<>();
        try{
            for (JettyServerConfiguration conf : jettyServerConfigurations){
                JettyServer server = jettyServerFactory.buildJettyServer(conf);
                jettyServers.add(server);
                server.start();
            }

            for (SpringBootServerConfiguration conf : springBootServerConfiguration){
                SpringBootServer server = springBootServerFactory.buildSpringBootServer(conf);
                springBootServers.add(server);
                server.start();
            }

            protractorRunner.run();
        } finally {
            for (JettyServer server : jettyServers){
                server.stop();
            }

            for (SpringBootServer server : springBootServers){
                server.stop();
            }
        }
    }
}
