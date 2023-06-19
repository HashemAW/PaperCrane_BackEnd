package ca.papercrane.api.service;

import ca.papercrane.api.entity.Employee;
import ca.papercrane.api.entity.role.EmployeeType;
import ca.papercrane.api.entity.role.UserRole;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAll();

    List<Employee> getAllWithRole(UserRole role);

    List<Employee> getAllWithType(EmployeeType type);

    Employee getByUserId(Integer userId);

    Employee getByEmail(String email);

    Integer addNewEmployee(Employee employee);

    void update(Integer userId, Employee employee);

    void save(Employee employee);

    void deleteByUserId(Integer userId);

    boolean exists(String email);

    Long totalCount();


}