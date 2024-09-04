package com.danilkompaniets.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        int age
) {
}
