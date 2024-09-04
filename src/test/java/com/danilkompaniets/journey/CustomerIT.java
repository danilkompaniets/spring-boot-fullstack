package com.danilkompaniets.journey;

import com.danilkompaniets.customer.Customer;
import com.danilkompaniets.customer.CustomerRegistrationRequest;
import com.danilkompaniets.customer.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import com.github.javafaker.Faker;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class  CustomerIT {
    private final Faker FAKER = new Faker();


    @Autowired
    private WebTestClient webClient;

    @Test
    void canRegisterCustomer() {
        // Generate fake customer data
        final String name = FAKER.name().fullName();
        final String email = FAKER.internet().emailAddress();
        final int age = FAKER.number().numberBetween(1, 100);

        // Create a registration request
        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(name, email, age);

        // Send POST request to register the customer
        webClient.post()
                .uri("/api/v1/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(registrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Retrieve the list of customers
        List<Customer> customers = webClient.get()
                .uri("/api/v1/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Customer.class)
                .returnResult()
                .getResponseBody();

        // Create an expected customer object
        Customer expectedCustomer = new Customer(name, email, age);

        // Assert that the customer list contains the expected customer, ignoring the id field
        assertThat(customers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expectedCustomer);

        // Find the ID of the registered customer
        Long id = customers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Retrieve the specific customer by ID and validate
        webClient.get()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void canDeleteCustomer() {
        final String name = FAKER.name().fullName();
        final String email = FAKER.internet().emailAddress();
        final int age = FAKER.number().numberBetween(1, 100);

        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(name, email, age);

        // Send POST request to register the customer
        webClient.post()
                .uri("/api/v1/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(registrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Retrieve the list of customers
        List<Customer> customers = webClient.get()
                .uri("/api/v1/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Customer.class)
                .returnResult()
                .getResponseBody();

        // Create an expected customer object
        Customer expectedCustomer = new Customer(name, email, age);

        // Assert that the customer list contains the expected customer, ignoring the id field
        assertThat(customers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expectedCustomer);

        // Find the ID of the registered customer
        Long id = customers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        webClient.delete()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void canUpdateCustomerEmail() {
        final String name = FAKER.name().fullName();
        final String email = FAKER.internet().emailAddress();
        final int age = FAKER.number().numberBetween(1, 100);

        final String randomEmail = FAKER.internet().uuid();

        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(name, email, age);

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(name, email + randomEmail, age);

        // Send POST request to register the customer
        webClient.post()
                .uri("/api/v1/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(registrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Retrieve the list of customers
        List<Customer> customers = webClient.get()
                .uri("/api/v1/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Customer.class)
                .returnResult()
                .getResponseBody();

        // Create an expected customer object
        Customer expectedCustomer = new Customer(name, email, age);

        // Assert that the customer list contains the expected customer, ignoring the id field
        assertThat(customers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expectedCustomer);

        // Find the ID of the registered customer
        Long id = customers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        webClient.put()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Customer updatedCustomer = webClient.get()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(updatedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(updatedCustomer.getName()).isEqualTo(updateRequest.name());
    }


    @Test
    void canUpdateCustomerName() {
        final String name = FAKER.name().fullName();
        final String email = FAKER.internet().emailAddress();
        final int age = FAKER.number().numberBetween(1, 100);

        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(name, email, age);

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(name + "UPDATED", email, age);

        // Send POST request to register the customer
        webClient.post()
                .uri("/api/v1/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(registrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Retrieve the list of customers
        List<Customer> customers = webClient.get()
                .uri("/api/v1/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Customer.class)
                .returnResult()
                .getResponseBody();

        // Create an expected customer object
        Customer expectedCustomer = new Customer(name, email, age);

        // Assert that the customer list contains the expected customer, ignoring the id field
        assertThat(customers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expectedCustomer);

        // Find the ID of the registered customer
        Long id = customers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        webClient.put()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Customer updatedCustomer = webClient.get()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(updatedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(updatedCustomer.getName()).isEqualTo(updateRequest.name());
    }

    @Test
    void canUpdateCustomerAge() {
        final String name = FAKER.name().fullName();
        final String email = FAKER.internet().emailAddress();
        final int age = FAKER.number().numberBetween(1, 100);

        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(name, email, age );

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(name, email, age + 23);

        // Send POST request to register the customer
        webClient.post()
                .uri("/api/v1/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(registrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Retrieve the list of customers
        List<Customer> customers = webClient.get()
                .uri("/api/v1/customers/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Customer.class)
                .returnResult()
                .getResponseBody();

        // Create an expected customer object
        Customer expectedCustomer = new Customer(name, email, age);

        // Assert that the customer list contains the expected customer, ignoring the id field
        assertThat(customers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expectedCustomer);

        // Find the ID of the registered customer
        Long id = customers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        webClient.put()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Customer updatedCustomer = webClient.get()
                .uri("/api/v1/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(updatedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(updatedCustomer.getName()).isEqualTo(updateRequest.name());
    }

}
