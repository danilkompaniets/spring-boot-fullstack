package com.danilkompaniets.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        int age
)  {

}
