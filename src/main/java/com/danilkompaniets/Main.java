package com.danilkompaniets;

import com.danilkompaniets.customer.Customer;
import com.danilkompaniets.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {

        Faker faker = new Faker();

        return args -> {
            Customer jasmine = new Customer(faker.name().firstName() + " " + faker.name().lastName(), faker.internet().emailAddress(), faker.number().numberBetween(1, 100));
            Customer danil = new Customer(faker.name().firstName() + " " + faker.name().lastName(), faker.internet().emailAddress(), faker.number().numberBetween(1, 100));
            List<Customer> customerList = List.of(jasmine,danil);
            customerRepository.saveAll(customerList);
        };
    }
}
