package com.andreyprofdev.protractorendtoendtestsrunner.jetty;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Properties;

public class JettyServerFactoryImpl implements JettyServerFactory {

    private Logger logger = LoggerFactory.getLogger(JettyServerFactoryImpl.class);
    private SystemProperty[] systemProperties;

    public JettyServerFactoryImpl(SystemProperty[] systemProperties) {
        this.systemProperties = systemProperties;
    }

    @Override
    public JettyServer buildJettyServer(JettyServerConfiguration jettyServerConfiguration) {
        return new JettyServerImpl(jettyServerConfiguration);
    }

    public class JettyServerImpl implements JettyServer{
        private Server server;
        private JettyServerConfiguration serverConfiguration;

        public JettyServerImpl(JettyServerConfiguration jettyServerConfiguration) {
            server = new Server(jettyServerConfiguration.getPort());
            serverConfiguration = jettyServerConfiguration;
            server.addLifeCycleListener(getSystemPropertiesListener(systemProperties));
            server.addBean(new MBeanContainer(ManagementFactory.getPlatformMBeanServer()));
            server.setHandler(getWebAppHandler(jettyServerConfiguration.getWarLocation(), server));
        }

        @Override
        public void start() throws Exception {
            try {
                server.start();
                logger.info("Server " + serverConfiguration.getWarLocation() + " started");
            } catch (Exception e) {
                throw new Exception("Server " + serverConfiguration.getWarLocation() + " failed to start", e);
            }
        }

        @Override
        public void stop() throws Exception {
            try {
                server.stop();
                server.join();
                logger.info("Server " + serverConfiguration.getWarLocation() + " stopped");
            } catch (Exception e) {
                throw new Exception("Server " + serverConfiguration.getWarLocation() + " failed to stop", e);
            }
        }

        @Override
        public String getWarLocation(){
            return serverConfiguration.getWarLocation();
        }

        @Override
        public Server getServer(){
            return server;
        }

        /**
         * Returns webapp handler which is able to handle war file
         */
        private Handler getWebAppHandler(String warLocation, Server server) {
            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/");
            File warFile = new File(warLocation);
            if (!warFile.exists()) {
                throw new RuntimeException("Unable to find WAR File: " + warFile.getAbsolutePath());
            }
            webapp.setWar(warFile.getAbsolutePath());
            webapp.setExtractWAR(true);

            Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);
            classlist.addBefore(
                    "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                    "org.eclipse.jetty.annotations.AnnotationConfiguration");

            webapp.setAttribute(
                    "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                    ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$");

            return webapp;
        }

        private LifeCycle.Listener getSystemPropertiesListener(SystemProperty[] systemProperties) {
            Properties props = new Properties();

            if (systemProperties != null) {
                for (SystemProperty prop : systemProperties) {
                    props.setProperty(prop.getName(), prop.getValue());
                }
            }

            return new SystemPropertiesLifeCycleListener(props);
        }

        private class SystemPropertiesLifeCycleListener extends AbstractLifeCycle.AbstractLifeCycleListener {
            private Properties toSet;

            public SystemPropertiesLifeCycleListener(Properties toSet) {
                this.toSet = toSet;
            }

            @Override
            public void lifeCycleStarting(LifeCycle anyLifeCycle) {
                // add to (don't replace) System.getProperties()
                System.getProperties().putAll(toSet);
            }
        }
    }
}
