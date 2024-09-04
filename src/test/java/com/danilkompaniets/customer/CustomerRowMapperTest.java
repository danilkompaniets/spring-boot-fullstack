package com.danilkompaniets.customer;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @Test
    void mapRow() throws SQLException {
        ResultSet inputRs = Mockito.mock(ResultSet.class);

        int rowNum = 1;
        Mockito.when(inputRs.getInt("id")).thenReturn(1);
        Mockito.when(inputRs.getString("name")).thenReturn("danil");
        Mockito.when(inputRs.getString("email")).thenReturn("email@email.com");
        Mockito.when(inputRs.getInt("age")).thenReturn(12);


        Customer result = customerRowMapper.mapRow(inputRs, rowNum);

        assertThat(result).isNotNull();
        assertEquals(result.getId(), 1);
        assertEquals(result.getName(), "danil");
        assertEquals(result.getEmail(), "email@email.com");
        assertEquals(result.getAge(), 12);
    }
}