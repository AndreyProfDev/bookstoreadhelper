package com.andreyprofdev.protractorendtoendtestsrunner;

import com.andreyprofdev.protractorendtoendtestsrunner.jetty.JettyServer;
import com.andreyprofdev.protractorendtoendtestsrunner.jetty.JettyServerConfiguration;
import com.andreyprofdev.protractorendtoendtestsrunner.jetty.JettyServerFactory;
import com.andreyprofdev.protractorendtoendtestsrunner.protractor.ProtractorRunner;

import java.util.ArrayList;
import java.util.List;

public class ProtractorEndToEndTestsRunner {
    private JettyServerConfiguration[] jettyServerConfigurations;
    private JettyServerFactory jettyServerFactory;
    private ProtractorRunner protractorRunner;

    public void setJettyServerConfigurations(JettyServerConfiguration[] jettyServerConfigurations) {
        this.jettyServerConfigurations = jettyServerConfigurations;
    }

    public void setJettyServerFactory(JettyServerFactory jettyServerFactory) {
        this.jettyServerFactory = jettyServerFactory;
    }

    public void setProtractorRunner(ProtractorRunner protractorRunner) {
        this.protractorRunner = protractorRunner;
    }

    public void start() throws Exception {

        List<JettyServer> servers = new ArrayList<>();
        try{
            for (JettyServerConfiguration conf : jettyServerConfigurations){
                JettyServer server = jettyServerFactory.buildJettyServer(conf);
                servers.add(server);
                server.start();
            }

            protractorRunner.run();
        } finally {
            for (JettyServer server : servers){
                server.stop();
            }
        }
    }
}
