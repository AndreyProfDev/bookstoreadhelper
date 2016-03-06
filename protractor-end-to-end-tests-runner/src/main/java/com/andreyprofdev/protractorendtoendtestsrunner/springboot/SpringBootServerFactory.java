package com.andreyprofdev.protractorendtoendtestsrunner.springboot;

public interface SpringBootServerFactory {
    SpringBootServer buildSpringBootServer(SpringBootServerConfiguration springBootServerConfiguration);
}
