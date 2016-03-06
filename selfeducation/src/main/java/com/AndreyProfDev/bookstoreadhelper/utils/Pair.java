package com.AndreyProfDev.bookstoreadhelper.utils;

import com.google.common.base.Objects;

public class Pair<T, K> {
    private T value1;
    private K value2;

    public Pair(T value1, K value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public T getValue1() {
        return value1;
    }

    public K getValue2() {
        return value2;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other){
            return true;
        }
        if (other == null || getClass() != other.getClass()){
            return false;
        }

        Pair that = (Pair) other;
        return Objects.equal(value1, that.value1) &&
                Objects.equal(value2, that.value2);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value1, value2);
    }
}
