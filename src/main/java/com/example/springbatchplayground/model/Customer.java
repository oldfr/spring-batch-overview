package com.example.springbatchplayground.model;

import javax.batch.api.chunk.ItemReader;

public class Customer {


    private String Id;
    private String name;
    private String birthDay;

    public Customer() {
    }

    public Customer(String id, String name, String birthDay) {
        Id = id;
        this.name = name;
        this.birthDay = birthDay;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "Id='" + Id + '\'' +
                ", name='" + name + '\'' +
                ", birthDay='" + birthDay + '\'' +
                '}';
    }
}
