package com.danilkompaniets.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJpaDataAccessService implements CustomerDao{
    private final CustomerRepository customerRepository;

    public CustomerJpaDataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customerRepository.findById(Math.toIntExact(id));
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public boolean existsCustomerById(Long id) {
        return customerRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public void updateCustomerById(Long id, CustomerUpdateRequest customer) {
        customerRepository.findById(Math.toIntExact(id)).ifPresent(c -> {
            c.setName(customer.name());
            c.setEmail(customer.email());
            c.setAge(customer.age());
            customerRepository.save(c);
        });
    }
}
