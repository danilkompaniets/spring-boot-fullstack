package com.danilkompaniets.customer;

import com.danilkompaniets.AbstractTestcontainerUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainerUnitTest {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsByEmail() {
        String email = FAKER.internet().emailAddress();
        Customer toInsert = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.number().numberBetween(1, 100)
        );

        customerRepository.save(toInsert);

        assertThat(customerRepository.existsByEmail(email)).isTrue();
    }

    @Test
    void existsByEmailFailsWhenEmailIsNotPresent() {
        String email = FAKER.internet().emailAddress();

        assertThat(customerRepository.existsByEmail(email)).isFalse();
    }
}