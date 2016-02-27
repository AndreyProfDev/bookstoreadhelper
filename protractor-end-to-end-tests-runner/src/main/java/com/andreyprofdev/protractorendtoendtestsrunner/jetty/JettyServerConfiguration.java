package com.andreyprofdev.protractorendtoendtestsrunner.jetty;

import com.AndreyProfDev.bookstoreadhelper.DataUtils;

public class JettyServerConfiguration {
    private String warLocation;
    private int port;

    public JettyServerConfiguration(){}

    public JettyServerConfiguration(String warLocation, int port) {
        this.warLocation = warLocation;
        this.port = port;
    }

    public String getWarLocation() {
        return warLocation;
    }

    public void setWarLocation(String warLocation) {
        this.warLocation = warLocation;
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

        JettyServerConfiguration that = (JettyServerConfiguration) other;
        return DataUtils.equalsNullSafe(warLocation, that.warLocation) &&
                DataUtils.equalsNullSafe(port, that.port);
    }

    @Override
    public int hashCode() {
        return DataUtils.calculateHashCode(warLocation, port);
    }
}
