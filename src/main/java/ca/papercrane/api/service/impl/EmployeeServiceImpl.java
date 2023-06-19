package ca.papercrane.api.service.impl;

import ca.papercrane.api.entity.Employee;
import ca.papercrane.api.entity.role.EmployeeType;
import ca.papercrane.api.entity.role.UserRole;
import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.repository.EmployeeRepository;
import ca.papercrane.api.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAll() throws ResourceNotFoundException {
        val employeeList = employeeRepository.findAll().stream().filter(e -> e.getRole().equals(UserRole.EMPLOYEE)).collect(Collectors.toList());
        if (employeeList.isEmpty()) {
            throw new ResourceNotFoundException("No employees found!");
        }
        return employeeList;
    }

    @Override
    public List<Employee> getAllWithRole(UserRole role) {
        val employeeList = employeeRepository.findAll().stream().filter(e -> e.getRole().equals(role)).collect(Collectors.toList());
        if (employeeList.isEmpty()) {
            throw new ResourceNotFoundException("No users with role: " + role.toString() + " found!");
        }
        return employeeList;
    }

    @Override
    public List<Employee> getAllWithType(EmployeeType type) {
        return employeeRepository.findAll().stream().filter(e -> e.getType().equals(type)).collect(Collectors.toList());
    }

    @Override
    public Employee getByUserId(Integer userId) {
        return employeeRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + userId));
    }

    @Override
    public Employee getByEmail(String email) {
        return employeeRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
    }

    @Override
    public Integer addNewEmployee(Employee employee) {
        val employeeOptional = employeeRepository.findByEmail(employee.getEmail());
        if (employeeOptional.isPresent()) {
            throw new IllegalArgumentException("Email already taken.");
        }
        val savedEmployee = employeeRepository.save(employee);
        return savedEmployee.getUserId();
    }

    @Override
    @Transactional
    public void update(Integer userId, Employee employee) {
        val existingEmployee = getByUserId(userId);
        if (employee.getEmail() != null && employee.getEmail().length() > 0 && !Objects.equals(existingEmployee.getEmail(), employee.getEmail())) {
            final Optional<Employee> employeeOptional = employeeRepository.findByEmail(employee.getEmail());
            if (employeeOptional.isPresent()) {
                throw new IllegalArgumentException("Email is already taken.");
            }
            existingEmployee.setEmail(employee.getEmail());
        }
        if (employee.getPassword() != null && employee.getPassword().length() > 0 && !Objects.equals(existingEmployee.getPassword(), employee.getPassword())) {
            existingEmployee.setPassword(employee.getPassword());
        }
        if (employee.getFirstName() != null && employee.getFirstName().length() > 0 && !Objects.equals(existingEmployee.getFirstName(), employee.getFirstName())) {
            existingEmployee.setFirstName(employee.getFirstName());
        }
        if (employee.getLastName() != null && employee.getLastName().length() > 0 && !Objects.equals(existingEmployee.getLastName(), employee.getLastName())) {
            existingEmployee.setLastName(employee.getLastName());
        }
        save(existingEmployee);
    }

    @Override
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void deleteByUserId(Integer userId) {
        employeeRepository.findByUserId(userId).ifPresentOrElse(employeeRepository::delete, () -> {
            throw new ResourceNotFoundException("Employee not found for ID: " + userId);
        });
    }

    @Override
    public boolean exists(String email) {
        return employeeRepository.findByEmail(email).isPresent();
    }

    @Override
    public Long totalCount() {
        return employeeRepository.count();
    }

}