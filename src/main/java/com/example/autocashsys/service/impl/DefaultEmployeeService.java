package com.example.autocashsys.service.impl;

import com.example.autocashsys.entity.Employee;
import com.example.autocashsys.repository.EmployeeRepository;
import com.example.autocashsys.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DefaultEmployeeService implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Optional<Employee> getForUsername(String username) {
        return employeeRepository.getEmployeesByUsername(username);
    }
}
