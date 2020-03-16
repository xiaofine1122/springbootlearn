package com.xiaofine.springbootmongodb.dao;

import com.xiaofine.springbootmongodb.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerRepository extends MongoRepository<Customer,String> {

    public Customer findByFirstName(String firstName);
    public List<Customer> findByLastName(String lastName);

}
