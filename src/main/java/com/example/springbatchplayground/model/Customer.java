package com.example.springbatchplayground.model;

public class Customer {


    private String Id;
    private String name;
    private String department;

    public Customer() {
    }

    public Customer(String id, String name, String department) {
        Id = id;
        this.name = name;
        this.department = department;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "Id='" + Id + '\'' +
                ", name='" + name + '\'' +
                ", birthDay='" + department + '\'' +
                '}';
    }
}
