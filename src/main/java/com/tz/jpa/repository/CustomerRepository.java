package com.tz.jpa.repository;

import com.tz.jpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author:tongzhou
 * @date: 2019/07/03
 * @description: CustomerRepository
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
