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

    /**
     * 保存
     * @param customer
     * @return
     */
    @PostMapping
    public Customer saveCustomer(@RequestBody Customer customer) {
        Customer customerSave = customerRepository.save(customer);
        return customerSave;
    }

    /**
     * 列表
     * @return
     */
    @GetMapping
    public List<Customer> listCustomer() {
        List<Customer> customerList = customerRepository.findAll();
        return customerList;
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id).get();
        return customer;
    }

    /**
     * 删除
     * @param id
     */
    @DeleteMapping("/{id}")
    public void delCustomer(@PathVariable Long id) {
        customerRepository.deleteById(id);
    }

    /**
     * 根据名称查找
     * @param lastName
     * @return
     */
    @RequestMapping("/findByLastName/{lastName}")
    public List<Customer> findByLastName(@PathVariable String lastName) {
        return customerRepository.findByLastName(lastName);
    }

    /**
     * 根据名称查找
     * @param firstName
     * @return
     */
    @RequestMapping("/findByFirstName/{firstName}")
    public List<Customer> findByFirstName(@PathVariable String firstName) {
        return customerRepository.findByFirstName(firstName);
    }

    /**
     * 根据名称查找
     * @param firstName
     * @return
     */
    @RequestMapping("/findByFirstName2/{firstName}")
    public List<Customer> findByFirstName2(@PathVariable String firstName) {
        return customerRepository.findByFirstName2(firstName);
    }

    /**
     * 根据名称查找
     * @param lastName
     * @return
     */
    @RequestMapping("/findByLastName2/{lastName}")
    public List<Customer> findByLastName2(@PathVariable String lastName) {
        return customerRepository.findByLastName2(lastName);
    }

    /**
     * 根据名称查找
     * @param name
     * @return
     */
    @RequestMapping("/findByName/{name}")
    public List<Customer> findByName(@PathVariable String name) {
        return customerRepository.findByName(name);
    }
    /**
     * 根据名称查找
     * @param name
     * @return
     */
    @RequestMapping("/findByName2/{name}")
    public List<Customer> findByName2(@PathVariable String name) {
        return customerRepository.findByName2(name);
    }
    /**
     * 根据名称查找
     * @param name
     * @return
     */
    @RequestMapping("/findByName3/{name}")
    public List<Customer> findByName3(@PathVariable String name) {
        return customerRepository.findByName3(name);
    }
}
