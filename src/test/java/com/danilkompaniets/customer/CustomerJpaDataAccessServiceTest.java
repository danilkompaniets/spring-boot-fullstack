package com.danilkompaniets.customer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJpaDataAccessServiceTest {

    Faker FAKER = new Faker();
    private CustomerJpaDataAccessService underTest;

    @Mock
    private CustomerRepository customerRepository;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();

        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        long id = 12;
        underTest.selectCustomerById(id);

        Mockito.verify(customerRepository).findById((int) id);
    }

    @Test
    void insertCustomer() {
        Customer toInsert = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress(),
                FAKER.number().numberBetween(1, 100)
        );

        underTest.insertCustomer(toInsert);

        Mockito.verify(customerRepository).save(toInsert);
    }

    @Test
    void existsPersonWithEmail() {
        String email = FAKER.internet().emailAddress();

        underTest.existsPersonWithEmail(email);

        Mockito.verify(customerRepository).existsByEmail(email);
    }

    @Test
    void deleteCustomerById() {
        long id = 12;

        underTest.deleteCustomerById(id);

        Mockito.verify(customerRepository).deleteById((int) id);
    }

    @Test
    void existsCustomerById() {
        long id = 12;
        underTest.existsCustomerById(id);
        Mockito.verify(customerRepository).existsById((int) id);
    }

    @Test
    void updateCustomerById() {
        long id = 12;
        CustomerUpdateRequest toInsert = new CustomerUpdateRequest(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress(),
                FAKER.number().numberBetween(1, 100)
        );

        underTest.updateCustomerById(id, toInsert);
    }
}