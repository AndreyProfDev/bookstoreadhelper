package com.andreyprofdev.protractorendtoendtestsrunner.protractor;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamHandler extends Thread {

    private interface LogLevelLogger {
        boolean log(String value);
    }

    private final InputStream inputStream;
    private final LogLevelLogger logger;

    private InputStreamHandler(InputStream inputStream, LogLevelLogger logger) {
        this.inputStream = inputStream;
        this.logger = logger;
    }

    public static InputStreamHandler attachLogInfoHandler(InputStream inputStream, final Logger logger, String stopMessage){
        return new InputStreamHandler(inputStream, new LogLevelLogger() {
            @Override
            public boolean log(String value) {
                logger.info(value);
                return value.contains(stopMessage);
            }
        });
    }

    public static InputStreamHandler attachLogInfoHandler(InputStream inputStream, final Logger logger){
        return new InputStreamHandler(inputStream, new LogLevelLogger() {
            @Override
            public boolean log(String value) {
                logger.info(value);
                return false;
            }
        });
    }

    public static InputStreamHandler attachLogErrorHandler(InputStream inputStream, final Logger logger){
        return new InputStreamHandler(inputStream, new LogLevelLogger() {
            @Override
            public boolean log(String value) {
                logger.error(value);
                return false;
            }
        });
    }

    public static InputStreamHandler attachLogErrorHandler(InputStream inputStream, final Logger logger, String stopMessage){
        return new InputStreamHandler(inputStream, new LogLevelLogger() {
            @Override
            public boolean log(String value) {
                logger.error(value);
                return value.contains(stopMessage);
            }
        });
    }

    @Override
    public void run(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while((line = reader.readLine()) != null) {
                if (logger.log(line)){
                    break;
                }
            }
        } catch (IOException e) {
            logger.log(e.getMessage());
        }
    }
}
