package com.andreyprofdev.protractorendtoendtestsrunner.protractor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProtractorRunnerImpl implements ProtractorRunner{
    private final Logger logger = LoggerFactory.getLogger(ProtractorRunnerImpl.class);

    private final String workingDirectory;
    private final File node;
    private final ProcessFactory processFactory;
    private final String protractorConfigurationPath;

    private static final String PROTRACTOR_LOCATION = "node_modules/protractor/bin".replace("/", File.separator);
    private static final String PROTRACTOR_RUNNER_LOCATION = PROTRACTOR_LOCATION + "/protractor".replace("/", File.separator);
    private static final String WEB_MANAGER_RUNNER_LOCATION = PROTRACTOR_LOCATION + "/webdriver-manager".replace("/", File.separator);

    public ProtractorRunnerImpl(String nodeInstallDirectory, String workingDirectory,
                                String protractorConfigurationPath, ProcessFactory processFactory) {
        this.workingDirectory = workingDirectory;
        this.node = new File(new NodePathResolver(nodeInstallDirectory).getNodePath());
        this.processFactory = processFactory;
        this.protractorConfigurationPath = protractorConfigurationPath;
    }

    @Override
    public void run() throws Exception {
        try {
            updateWebDriverManager();
            Process webDriverManager = startWebDriverManager();
            startProtractor(protractorConfigurationPath);
            webDriverManager.stop();
        } catch (Exception e) {
            throw new Exception("Protractor tests failed", e);
        }

    }

    private Process executeNodeProcessAndWatch(String processName, String location, String stringToWatch, String... options) throws Exception {
        File process = new File(workingDirectory, location);

        final List<String> arguments = new ArrayList<>();
        arguments.add(node.getAbsolutePath());
        arguments.add(process.getAbsolutePath());

        StringBuilder procDescription = new StringBuilder(processName);

        for (String option : options){
            arguments.add(option);
            procDescription.append(" ").append(option);
        }

        logger.info("Executing " + procDescription + ".\nCommand line: " + arguments);

        try {
            int result;
            Process proc = processFactory.createNew();
            if (stringToWatch != null){
                result = proc.startAndWaitForError(arguments, stringToWatch);
            } else {
                result = proc.startAndWait(arguments);
            }

            if (0 != result) {
                throw new Exception(procDescription + " failed. (error code " + result + ")");
            }
            logger.info(procDescription + " successfully finished");

            return proc;
        } catch (Exception e) {
            throw new Exception(procDescription + " failed.", e);
        }
    }

    private void updateWebDriverManager() throws Exception {
        executeNodeProcessAndWatch("Webdriver-manager", WEB_MANAGER_RUNNER_LOCATION, null, "update");
    }

    private Process startWebDriverManager() throws Exception {
        return executeNodeProcessAndWatch("Webdriver-manager", WEB_MANAGER_RUNNER_LOCATION, "Selenium Server is up and running", "start");
    }

    private void startProtractor(String protractorConfPath) throws Exception {
        executeNodeProcessAndWatch("Protractor", PROTRACTOR_RUNNER_LOCATION, null, workingDirectory + protractorConfPath);
    }
}
