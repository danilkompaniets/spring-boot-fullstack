package com.danilkompaniets.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJdbcDataAccessService implements CustomerDao{
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJdbcDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        String sql = """
                SELECT * FROM customer
                """;

        return jdbcTemplate.query(sql,customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        String sql = """
            SELECT * FROM customer WHERE id=?
            """;

        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        String sql = """
                INSERT INTO customer (name, email, age)
                VALUES (?, ?, ?)
                """;
        var res = jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());

        System.out.println("Customer inserted:" + res);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        String sql = """
                SELECT * FROM customer WHERE email=?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, email).stream().findFirst().isPresent();
    }

    @Override
    public void deleteCustomerById(Long id) {
        String sql = """
                DELETE FROM customer WHERE id=?
                """;
        jdbcTemplate.update(sql, id);
        System.out.println("Deleted User: " + id);
    }

    @Override
    public boolean existsCustomerById(Long id) {
        String sql = """
               SELECT * FROM customer WHERE id=?
               """;
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst().isPresent();
    }

    @Override
    public void updateCustomerById(Long id, CustomerUpdateRequest customer) {
        String sql = """
                UPDATE customer SET name=?, email=?, age=? WHERE id=?
                """;

        jdbcTemplate.update(sql, customer.name(), customer.email(), customer.age(), id);
    }
}
