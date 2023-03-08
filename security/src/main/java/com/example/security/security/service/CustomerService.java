package com.example.security.security.service;

import com.example.security.security.dto.CustomerDTO;
import com.example.security.security.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer saveCustomer(CustomerDTO customerDTO);

     List<Customer> getCustomers();

    Customer getCustomer(String username);

    void updatePassword();
}
