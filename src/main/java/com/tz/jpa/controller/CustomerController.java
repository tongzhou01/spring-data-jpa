package com.tz.jpa.controller;

import com.tz.jpa.entity.Customer;
import com.tz.jpa.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author:tongzhou
 * @date: 2019/07/03
 * @description: CustomerController
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @PostMapping
    public Customer saveCustomer(@RequestBody Customer customer) {
        Customer customerSave = customerRepository.save(customer);
        return customerSave;
    }

    @GetMapping
    public List<Customer> listCustomer() {
        List<Customer> customerList = customerRepository.findAll();
        return customerList;
    }
}
