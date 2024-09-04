package com.danilkompaniets.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{
    static List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer jasmine = new Customer("Jasmine","asd@gmail.con", 10);
        Customer danil = new Customer("danil", "asd@gmail.con", 12);
        customers.add(jasmine);
        customers.add(danil);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers ;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers
                .stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomerById(Long id) {
        customers.removeIf(customer -> customer.getId().equals(id));
    }

    @Override
    public boolean existsCustomerById(Long id) {
        return customers.stream().anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void updateCustomerById(Long id, CustomerUpdateRequest customer) {
            customers.stream().filter(c -> c.getId().equals(id)).findFirst().ifPresent(c -> {
                    c.setName(customer.name());
                    c.setEmail(customer.email());
                    c.setAge(customer.age());
                }
            );
    }
}
