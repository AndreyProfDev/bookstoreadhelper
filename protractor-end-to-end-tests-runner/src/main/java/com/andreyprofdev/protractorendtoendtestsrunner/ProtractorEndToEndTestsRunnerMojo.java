package com.andreyprofdev.protractorendtoendtestsrunner;

import com.andreyprofdev.protractorendtoendtestsrunner.jetty.JettyServerConfiguration;
import com.andreyprofdev.protractorendtoendtestsrunner.jetty.JettyServerFactoryImpl;
import com.andreyprofdev.protractorendtoendtestsrunner.jetty.SystemProperty;
import com.andreyprofdev.protractorendtoendtestsrunner.protractor.ProcessFactoryImpl;
import com.andreyprofdev.protractorendtoendtestsrunner.protractor.ProtractorRunner;
import com.andreyprofdev.protractorendtoendtestsrunner.protractor.ProtractorRunnerImpl;
import com.andreyprofdev.protractorendtoendtestsrunner.springboot.SpringBootServerConfiguration;
import com.andreyprofdev.protractorendtoendtestsrunner.springboot.SpringBootServerFactoryImpl;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo( name = "runProtractorTests")
public class ProtractorEndToEndTestsRunnerMojo extends AbstractMojo
{
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    @Parameter( property = "runJetty.jettyServers", required = false)
    private JettyServerConfiguration[] jettyServers;

    @Parameter( property = "runJetty.springBootServers", required = false)
    private SpringBootServerConfiguration[] springBootServers;

    @Parameter(defaultValue = "conf.js", property = "protractorConfPath", required = false)
    private String protractorConfPath;

    @Parameter(defaultValue = "${basedir}", property = "workingDirectory", required = false)
    private File workingDirectory;

    @Parameter(property = "installDirectory", required = false)
    private File installDirectory;

    @Parameter(property = "systemProperties", required = false)
    private SystemProperty[] systemProperties;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            ProcessFactoryImpl processFactory = new ProcessFactoryImpl();

            ProtractorEndToEndTestsRunner runner = new ProtractorEndToEndTestsRunner();

            runner.setJettyServerConfigurations(jettyServers);
            runner.setJettyServerFactory(new JettyServerFactoryImpl(systemProperties));

            runner.setSpringBootServerConfiguration(springBootServers);
            runner.setSpringBootServerFactory(new SpringBootServerFactoryImpl(systemProperties, processFactory));

            ProtractorRunner protractorRunner = new ProtractorRunnerImpl(
                    installDirectory.getAbsolutePath(), workingDirectory.getAbsolutePath(),
                    protractorConfPath, processFactory);
            runner.setProtractorRunner(protractorRunner);

            runner.start();
        } catch (Exception e) {
            throw new MojoExecutionException("Protractor tests failed", e);
        }
    }
}