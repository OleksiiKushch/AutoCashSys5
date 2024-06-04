package com.example.autocashsys.service.impl;

import com.example.autocashsys.entity.Employee;
import com.example.autocashsys.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DefaultEmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DefaultEmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getForUsernameTest() {
        String username = "username";
        Employee employee = new Employee();
        when(employeeRepository.getEmployeesByUsername(username)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getForUsername(username);

        assertEquals(Optional.of(employee), result);
    }
}