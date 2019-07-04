package com.tz.jpa.repository;

import com.tz.jpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author:tongzhou
 * @date: 2019/07/03
 * @description: CustomerRepository
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * 根据lastName查询结果
     *
     * @param lastName
     * @return
     */
    List<Customer> findByLastName(String lastName);

    /**
     * 根据firstName查询结果
     *
     * @param firstName
     * @return
     */
    List<Customer> findByFirstName(String firstName);

    /**
     * 根据firstName查询结果
     *
     * @param firstName
     * @return
     */
    @Query("select c from Customer c where c.firstName = ?1")
    List<Customer> findByFirstName2(String firstName);

    /**
     * 根据lastName查询结果
     *
     * @param lastName
     * @return
     */
    @Query("select c from Customer c where c.lastName = ?1 order by c.id desc")
    List<Customer> findByLastName2(String lastName);

    /**
     * 根据名称查询
     * @param name
     * @return
     */
    @Query("select c from Customer c where c.firstName = :name or c.lastName = :name")
    List<Customer> findByName(String name);

    /**
     * 根据名称模糊查询
     * @param name
     * @return
     */
    @Query("select c from Customer c where c.firstName like %:name")
    List<Customer> findByName2(@Param("name") String name);

    /**
     * 原生sql查询
     * @param name
     * @return
     */
    @Query(nativeQuery = true, value = "select * from customer c where c.first_name like concat('%',:name,'%')")
    List<Customer> findByName3(String name);
}
