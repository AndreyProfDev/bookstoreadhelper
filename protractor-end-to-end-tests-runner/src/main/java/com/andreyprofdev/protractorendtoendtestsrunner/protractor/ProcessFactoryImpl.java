package com.andreyprofdev.protractorendtoendtestsrunner.protractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ProcessFactoryImpl implements ProcessFactory{

    private final Logger logger = LoggerFactory.getLogger(ProcessFactoryImpl.class);

    @Override
    public Process createNew(){
        return new ProcessImpl();
    }

    private class ProcessImpl implements Process{
        private java.lang.Process processHolder;

        @Override
        public int startAndWait(List<String> commands) throws InterruptedException, IOException {
            processHolder = new ProcessBuilder().command(commands).start();

            Thread infoLogThread = InputStreamHandler.attachLogInfoHandler(processHolder.getInputStream(), logger);
            infoLogThread.start();
            Thread errorLogThread = InputStreamHandler.attachLogErrorHandler(processHolder.getErrorStream(), logger);
            errorLogThread.start();

            processHolder.waitFor();
            infoLogThread.join();
            errorLogThread.join();
            return processHolder.exitValue();
        }

        @Override
        public int startAndWaitForError(List<String> commands, String errorMessage) throws IOException, InterruptedException {
            processHolder = new ProcessBuilder().command(commands).start();

            Thread infoLogThread = InputStreamHandler.attachLogInfoHandler(processHolder.getInputStream(), logger);
            infoLogThread.start();
            Thread watcherLogThread = InputStreamHandler.attachLogErrorHandler(processHolder.getErrorStream(), logger, errorMessage);
            watcherLogThread.start();

            watcherLogThread.join();
            return 0;
        }

        @Override
        public void stop(){
            if (processHolder != null){
                processHolder.destroy();
                processHolder = null;
            }
        }
    }

}
