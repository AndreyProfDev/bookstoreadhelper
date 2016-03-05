package com.AndreyProfDev.bookstoreadhelper.data;

import java.io.Serializable;

public class Book implements Serializable{
    public static class Builder{
        private int id;
        private String name;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Book build(){
            return new Book(id, name);
        }
    }

    private int id;

    private String name;

    public Book(){}

    public Book(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
