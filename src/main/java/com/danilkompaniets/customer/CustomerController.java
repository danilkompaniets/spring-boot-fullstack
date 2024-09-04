package com.danilkompaniets.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers/")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(
            @PathVariable Integer id
    ) {
        return customerService.getById(id);
    }

    @PostMapping
    public Customer createCustomer(@RequestBody CustomerRegistrationRequest customer) {
        return customerService.addCustomer(customer);
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomerById(id);
    }

    @PutMapping("{id}")
    public void updateCustomerById(
            @RequestBody CustomerUpdateRequest customer,
            @PathVariable Integer id
    ) {
        customerService.updateCustomerById(id, customer);
    }

}
