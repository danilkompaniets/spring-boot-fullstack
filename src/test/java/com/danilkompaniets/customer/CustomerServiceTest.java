package com.danilkompaniets.customer;

import com.danilkompaniets.exception.DuplicateResourceException;
import com.danilkompaniets.exception.ResourceNotFound;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getById() {
        long id = 1;
        Customer customer = new Customer(
                id, "danil", "email", 12
        );

        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getById((int) id);

        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void getByIdWillThrowWhenIdNotFound() {
        long id = 1;
        Customer customer = new Customer(
                id, "danil", "email", 12
        );

        Mockito.when(customerDao.selectCustomerById(id)).thenThrow(new ResourceNotFound("Customer not found"));

        assertThatThrownBy(() -> underTest.getById((int) id)).isInstanceOf(ResourceNotFound.class).hasMessage("Customer not found");
    }
    
    @Test
    void getBuIdWillThrowWhenNotFound() {
        long id = -1;

        assertThatThrownBy(() -> underTest.getById((int) id)).isInstanceOf(ResourceNotFound.class).hasMessageContaining("Customer not found");
    }

    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();

        Mockito.verify(customerDao).selectAllCustomers();
    }

    @Test
    void addCustomer() {
        String email = "danil";

        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "danil",
                email,
                12
        );

        underTest.addCustomer(request);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).insertCustomer(captor.capture());

        Customer actual = captor.getValue();

        assertThat(actual.getEmail()).isEqualTo(request.email());
        assertThat(actual.getName()).isEqualTo(request.name());
        assertThat(actual.getAge()).isEqualTo(request.age());
    }

    @Test
    void addCustomerWithExistingEmailWillThrow() {
        String email = "danil";

        Mockito.when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "danil",
                email,
                12
        );

        assertThatThrownBy(() -> underTest.addCustomer(request)).isInstanceOf(DuplicateResourceException.class).hasMessageContaining("Customer already exists");
    }

    @Test
    void deleteCustomerById() {
        long id = 1;
        Customer customer = new Customer(
                id, "danil", "email", 12
        );

        Mockito.when(customerDao.existsCustomerById(id)).thenReturn(true);

        underTest.deleteCustomerById((int) id);

        Mockito.verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void deleteCustomerByIdWillThrowWithIncorrectId() {
        long id = -1;
        Customer customer = new Customer(
                id, "danil", "email", 12
        );

        Mockito.when(customerDao.existsCustomerById(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteCustomerById((int) id)).isInstanceOf(ResourceNotFound.class).hasMessageContaining("Customer not found");
    }

    @Test
    void updateCustomer() {
        long id = 1;
        Customer existingCustomer = new Customer(
                id, "danil", "email", 12
        );

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "danil", "CHANGED", 12
        );

        // Mock the behavior of the customerDao
        Mockito.when(customerDao.existsCustomerById(id)).thenReturn(true);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(existingCustomer));

        // Call the method under test
        underTest.updateCustomerById((int) id, request);

        // Create ArgumentCaptor to capture the CustomerUpdateRequest
        ArgumentCaptor<CustomerUpdateRequest> requestArgumentCaptor = ArgumentCaptor.forClass(CustomerUpdateRequest.class);

        // Verify that the updateCustomerById method was called with the correct parameters
        Mockito.verify(customerDao).updateCustomerById(Mockito.eq(id), requestArgumentCaptor.capture());

        // Retrieve the captured CustomerUpdateRequest object
        CustomerUpdateRequest capturedRequest = requestArgumentCaptor.getValue();

        // Assert the captured request is as expected
        assertThat(capturedRequest.name()).isEqualTo(request.name());
        assertThat(capturedRequest.email()).isEqualTo(request.email());
        assertThat(capturedRequest.age()).isEqualTo(request.age());
    }




    @Test
    void updateCustomerByIdWillThrowWhenNoChanges() {
        long id = 1;
        Customer customer = new Customer(
                id, "danil", "email", 12
        );


        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "danil", "email", 12
        );

        Mockito.when(customerDao.existsCustomerById(id)).thenReturn(true);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> underTest.updateCustomerById((int) id, request)).isInstanceOf(ResourceNotFound.class).hasMessageContaining("No changes detected");
    }

    @Test
    void updateCustomerByIdWillThrowWhenNotFound() {
        long id = 1;
        Customer customer = new Customer(
                id, "danil", "email", 12
        );


        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "danil", "CHANGED", 12
        );

        Mockito.when(customerDao.existsCustomerById(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.updateCustomerById((int) id, request)).isInstanceOf(ResourceNotFound.class).hasMessageContaining("Customer not found");
    }
}