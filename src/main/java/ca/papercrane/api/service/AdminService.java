package ca.papercrane.api.service;

import ca.papercrane.api.entity.Admin;
import ca.papercrane.api.entity.Employee;
import ca.papercrane.api.entity.role.EmployeeType;
import ca.papercrane.api.entity.role.UserRole;

import java.util.List;

public interface AdminService {

    List<Admin> getAll();

    List<Admin> getAllWithRole(UserRole role);

    List<Admin> getAllWithType(EmployeeType type);

    Admin getByUserId(Integer userId);

    Integer addNewEmployee(Employee employee);

    Integer addNewAdmin(Admin admin);

    void update(Integer userId, Admin Admin);

    void deleteByUserId(Integer userId);

    boolean exists(String email);

    Long totalCount();

}