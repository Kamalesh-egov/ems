package dev.kamalesh.ems.employee;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("employee with id " + id + " does not exists"));
        return employee;
    }

    public Employee createEmployee(Employee employee) {
        Optional<Employee> employeeByEmail = employeeRepository.findByEmail(employee.getEmail());
        if (employeeByEmail.isPresent()) {
            throw new IllegalStateException("Email taken");
        }
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee employee) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Employee with id " + id + " does not exist"));

        if (employee.getFirstName() != null && !employee.getFirstName().isEmpty() && !existingEmployee.getFirstName().equals(employee.getFirstName())) {
            existingEmployee.setFirstName(employee.getFirstName());
        }

        if (employee.getLastName() != null && !employee.getLastName().isEmpty() && !existingEmployee.getLastName().equals(employee.getLastName())) {
            existingEmployee.setLastName(employee.getLastName());
        }

        if (employee.getEmail() != null && !employee.getEmail().isEmpty() && !existingEmployee.getEmail().equals(employee.getEmail())) {
            Optional<Employee> employeeByEmail = employeeRepository.findByEmail(employee.getEmail());
            if (employeeByEmail.isPresent()) {
                throw new IllegalStateException("Email taken");
            }
            existingEmployee.setEmail(employee.getEmail());
        }

        if (employee.getDepartment() != null && !employee.getDepartment().isEmpty() && !existingEmployee.getDepartment().equals(employee.getDepartment())) {
            existingEmployee.setDepartment(employee.getDepartment());
        }

        if (employee.getSalary() != null && !existingEmployee.getSalary().equals(employee.getSalary())) {
            existingEmployee.setSalary(employee.getSalary());
        }

        return existingEmployee;
    }


    public void deleteEmployee(Long id) {
        Employee existingEmployee = getEmployeeById(id);
        employeeRepository.delete(existingEmployee);
    }
}
