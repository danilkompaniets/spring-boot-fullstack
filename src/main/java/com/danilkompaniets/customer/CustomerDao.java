package com.danilkompaniets.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Long id);
    void insertCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    void deleteCustomerById(Long id);
    boolean existsCustomerById(Long id);
    void updateCustomerById(Long id, CustomerUpdateRequest customer);
}
