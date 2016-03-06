package com.andreyprofdev.protractorendtoendtestsrunner.springboot;

import com.andreyprofdev.protractorendtoendtestsrunner.jetty.SystemProperty;
import com.andreyprofdev.protractorendtoendtestsrunner.protractor.ProcessFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpringBootServerFactoryImpl implements SpringBootServerFactory {
    private Logger logger = LoggerFactory.getLogger(SpringBootServerFactoryImpl.class);
    private SystemProperty[] systemProperties;
    private ProcessFactory processFactory;

    public SpringBootServerFactoryImpl(SystemProperty[] systemProperties, ProcessFactory processFactory) {
        this.systemProperties = systemProperties;
        this.processFactory = processFactory;
    }

    @Override
    public SpringBootServer buildSpringBootServer(SpringBootServerConfiguration springBootServerConfiguration) {
        return new SpringBootServerImpl(springBootServerConfiguration);
    }

    public class SpringBootServerImpl implements SpringBootServer {

        private com.andreyprofdev.protractorendtoendtestsrunner.protractor.Process server;
        private SpringBootServerConfiguration serverConfiguration;
        private String APP_STARTED_NOTIFICATION = "Started Application in";
        private List<String> processArguments;

        public SpringBootServerImpl(SpringBootServerConfiguration springBootServerConfiguration) {
            serverConfiguration = springBootServerConfiguration;
        }

        @Override
        public void start() throws Exception {
            File process = new File(serverConfiguration.getJarLocation());

            processArguments = new ArrayList<>();
            processArguments.add("java");
            processArguments.add("-Dserver.port=" + serverConfiguration.getPort());
            StringBuilder procDescription = new StringBuilder(process.getName());

            for (SystemProperty systemProperty : systemProperties) {
                String option = "-D" + systemProperty.getName() + "=" + systemProperty.getValue();
                processArguments.add(option);
                procDescription.append(" ").append(option);
            }

            processArguments.add("-jar");
            processArguments.add(process.getAbsolutePath());

            logger.info("Executing " + procDescription + ".\nCommand line: " + processArguments);

            try {
                int result;
                server = processFactory.createNew();
                result = server.startAndWaitForInfo(processArguments, APP_STARTED_NOTIFICATION);

                if (0 != result) {
                    throw new Exception(procDescription + " failed. (error code " + result + ")");
                }
                logger.info(procDescription + " successfully started");
            } catch (Exception e) {
                throw new Exception(procDescription + " failed.", e);
            }
        }

        @Override
        public void stop() throws Exception {
            server.stop();
            logger.info("Spring boot server successfully stopped.\nCommand line: " + processArguments);
        }

        @Override
        public String getJarLocation() {
            return serverConfiguration.getJarLocation();
        }
    }
}