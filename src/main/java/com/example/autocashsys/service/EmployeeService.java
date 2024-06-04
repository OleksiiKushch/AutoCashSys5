package com.example.autocashsys.service;

import com.example.autocashsys.entity.Employee;

import java.util.Optional;

public interface EmployeeService {

    Optional<Employee> getForUsername(String username);
}
