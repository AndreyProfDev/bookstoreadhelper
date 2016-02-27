package com.andreyprofdev.protractorendtoendtestsrunner.protractor;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

public class ProtractorRunnerTest {

    private Mockery context = new Mockery();

    private String WORKING_DIRECTORY = "/working_dir/";
    private String NODE_INSTALL_DIRECTORY = "/nodeInstallDirectory";
    private String PROTRACTOR_CONF_PATH = "/protractor_conf_path";

    private String WEB_MANAGER_ERROR = "Selenium Server is up and running";

    private static final String PROTRACTOR_LOCATION = "node_modules/protractor/bin".replace("/", File.separator);

    private static final String PROTRACTOR_RUNNER_LOCATION = PROTRACTOR_LOCATION + "/protractor".replace("/", File.separator);
    private static final String WEB_MANAGER_RUNNER_LOCATION = PROTRACTOR_LOCATION + "/webdriver-manager".replace("/", File.separator);

    @Test
    public void test() throws Exception {
        ProcessFactory processFactory = context.mock(ProcessFactory.class);
        ProtractorRunner protractorRunner = new ProtractorRunnerImpl(NODE_INSTALL_DIRECTORY, WORKING_DIRECTORY,
                                                                    PROTRACTOR_CONF_PATH, processFactory);

        com.andreyprofdev.protractorendtoendtestsrunner.protractor.Process webManagerUpdateProcess = context.mock(Process.class, "webManagerUpdateProcess");
        Process webManagerStartProcess = context.mock(Process.class, "webManagerStartProcess");
        Process protractorStartProcess = context.mock(Process.class, "protractorStartProcess");

        context.checking(new Expectations() {{
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
        }});

        protractorRunner.run();
    }

    private String getNodePath(){
        return new NodePathResolver(NODE_INSTALL_DIRECTORY).getNodePath();
    }
}
