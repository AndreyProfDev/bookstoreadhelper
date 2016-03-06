package com.andreyprofdev.protractorendtoendtestsrunner.springboot;

public interface SpringBootServer {
    void start() throws Exception;

    void stop() throws Exception;

    String getJarLocation();
}
