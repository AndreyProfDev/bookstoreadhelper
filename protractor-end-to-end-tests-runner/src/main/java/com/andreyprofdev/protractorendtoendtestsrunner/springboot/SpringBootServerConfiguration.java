package com.andreyprofdev.protractorendtoendtestsrunner.springboot;

import com.google.common.base.Objects;

public class SpringBootServerConfiguration {
    private String jarLocation;
    private int port;

    public SpringBootServerConfiguration(){}

    public SpringBootServerConfiguration(String jarLocation, int port) {
        this.jarLocation = jarLocation;
        this.port = port;
    }

    public String getJarLocation() {
        return jarLocation;
    }

    public void setJarLocation(String jarLocation) {
        this.jarLocation = jarLocation;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other){
            return true;
        }
        if (other == null || getClass() != other.getClass()){
            return false;
        }

        SpringBootServerConfiguration that = (SpringBootServerConfiguration) other;
        return Objects.equal(jarLocation, that.jarLocation) &&
                Objects.equal(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(jarLocation, port);
    }
}
