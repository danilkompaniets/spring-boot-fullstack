package com.danilkompaniets.customer;

import com.danilkompaniets.AbstractTestcontainerUnitTest;
import com.danilkompaniets.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJdbcDataAccessServiceTest extends AbstractTestcontainerUnitTest {
    private CustomerJdbcDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJdbcDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        var toInsert = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress(),
                FAKER.number().numberBetween(1, 100)
        );

        underTest.insertCustomer(toInsert);

        underTest.selectAllCustomers();

        List<Customer> customers = underTest.selectAllCustomers();

        assertThat(customers).isNotNull();
    }

    @Test
    void selectCustomerById() {
        String email = FAKER.internet().emailAddress();
        var toInsert = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );
        underTest.insertCustomer(toInsert);
        Customer customer = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow();
        var id = customer.getId();
        Optional<Customer> res = underTest.selectCustomerById(id);

        assertThat(
            res
        ).isNotNull();
    }

    @Test
    void willReturnEmptyOptionalWhenCustomerDoesNotExist() {
        long id = -1;

        var res = underTest.selectCustomerById(id);

        assertThat(res).isEmpty();
    }

    @Test
    void insertCustomer() {
        String email = FAKER.internet().emailAddress();
        Customer toInsert = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );

        underTest.insertCustomer(toInsert);

        Customer customer = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow();

        assertThat(underTest.selectCustomerById(customer.getId())).isNotNull();
    }

    @Test
    void existsPersonWithEmail() {
        String email = FAKER.internet().emailAddress();
        Customer toInsert = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );

        underTest.insertCustomer(toInsert);

        boolean exists = underTest.existsPersonWithEmail(email);

        assertThat(exists).isTrue();
    }

    @Test
    void deleteCustomerById() {
        String email = FAKER.internet().emailAddress();
        Customer toInsert = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );

        underTest.insertCustomer(toInsert);

        Customer customer = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow();

        underTest.deleteCustomerById(customer.getId());

        assertThat(underTest.selectCustomerById(customer.getId())).isEmpty();
    }

    @Test
    void existsCustomerById() {
        String email = FAKER.internet().emailAddress();
        Customer toInsert = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );

        underTest.insertCustomer(toInsert);

        Customer customer = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow();

        assertThat(underTest.selectCustomerById(customer.getId())).isNotNull();
    }

    @Test
    void updateCustomerById() {
        String email = FAKER.internet().emailAddress();
        Customer toInsert = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );

        underTest.insertCustomer(toInsert);

        Customer customer = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow();

        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest(
                        "updated",
                        "updated",
                        10
                );

        underTest.updateCustomerById(customer.getId(), updateRequest);

        Customer updatedCustomer = underTest.selectCustomerById(customer.getId())
                .orElseThrow(() -> new ResourceNotFound("User with id not found"));

        assertThat(underTest.selectCustomerById(customer.getId())).isEqualTo(Optional.of(updatedCustomer));
    }
}