package com.example.autocashsys.repository;

import com.example.autocashsys.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> getEmployeesByUsername(String username);
}
