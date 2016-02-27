package com.andreyprofdev.protractorendtoendtestsrunner.jetty;

import com.AndreyProfDev.bookstoreadhelper.DataUtils;

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
        return DataUtils.equalsNullSafe(name, that.name) &&
                DataUtils.equalsNullSafe(value, that.value);
    }

    @Override
    public int hashCode() {
        return DataUtils.calculateHashCode(name, value);
    }
}
