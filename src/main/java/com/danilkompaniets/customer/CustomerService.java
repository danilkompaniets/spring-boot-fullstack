package com.danilkompaniets.customer;

import com.danilkompaniets.exception.DuplicateResourceException;
import com.danilkompaniets.exception.ResourceNotFound;
import org.hibernate.query.QueryArgumentException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public Customer getById(Integer id) {
        return (customerDao.selectCustomerById(Long.valueOf(id))).orElseThrow(() ->  new ResourceNotFound("Customer not found"));
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        if (customerDao.existsPersonWithEmail(customerRegistrationRequest.email())) {
            throw new DuplicateResourceException("Customer already exists");
        }
        else {
            Customer newCustomer = new Customer(customerRegistrationRequest.name(),  customerRegistrationRequest.email(), customerRegistrationRequest.age());
            customerDao.insertCustomer(newCustomer);
            return newCustomer;
        }
    }

    public void deleteCustomerById(Integer id) {
        if (customerDao.existsCustomerById(Long.valueOf(id))) {
            customerDao.deleteCustomerById(Long.valueOf(id));
        }
        else {
            throw new ResourceNotFound("Customer not found");
        }
    }

    public void updateCustomerById(Integer id, CustomerUpdateRequest updateRequest) {
        if (!customerDao.existsCustomerById(Long.valueOf(id))) {
                throw new ResourceNotFound("Customer not found");
        }
        Customer customer = getById(id);
        boolean changed = false;
        if (customer != null && updateRequest.name() != customer.getName() ) {
            customer.setName(updateRequest.name());
            changed = true;
        }

        if (customer != null && updateRequest.  email() != customer.getEmail() ) {
            customer.setName(updateRequest.name());
            changed = true;
        }

        if (customer != null && updateRequest.age() != customer.getAge() ) {
            customer.setAge(updateRequest.age());
            changed = true;
        }

        if (!changed) {
            throw new ResourceNotFound("No changes detected");
        }

        customerDao.updateCustomerById(Long.valueOf(id), updateRequest);
    }
}
