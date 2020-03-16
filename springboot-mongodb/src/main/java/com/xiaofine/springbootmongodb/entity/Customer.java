package com.xiaofine.springbootmongodb.entity;

import org.springframework.data.annotation.Id;

public class Customer {

    @Id
    public  String id;

    public  String firstName;
    public  String lastName;

    public Customer(String firstName,String lastName){
        this.firstName=firstName;
        this.lastName=lastName;
    }
    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
