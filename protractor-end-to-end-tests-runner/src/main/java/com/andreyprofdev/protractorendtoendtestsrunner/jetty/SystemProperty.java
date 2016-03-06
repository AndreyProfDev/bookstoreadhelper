package com.andreyprofdev.protractorendtoendtestsrunner.jetty;

import com.google.common.base.Objects;

public class SystemProperty {
    private String name;
    private String value;

    public SystemProperty() {
    }

    public SystemProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other){
            return true;
        }
        if (other == null || getClass() != other.getClass()){
            return false;
        }

        SystemProperty that = (SystemProperty) other;
        return Objects.equal(name, that.name) &&
                Objects.equal(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, value);
    }
}
