package ca.papercrane.api.controller;

import ca.papercrane.api.entity.Employee;
import ca.papercrane.api.entity.role.EmployeeType;
import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.service.impl.EmployeeServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/employees")
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    @PostConstruct
    public void init() {
        if (!employeeService.exists("employee1@papercrane.ca")) {
            employeeService.addNewEmployee(new Employee("employee1@papercrane.ca", "password", "Employee", "#1", EmployeeType.DESIGNER));
        }
        if (!employeeService.exists("employee2@papercrane.ca")) {
            employeeService.addNewEmployee(new Employee("employee2@papercrane.ca", "password", "Employee", "#2", EmployeeType.DEVELOPER));
        }
    }

    /**
     * Gets a list of all normal Employee users.
     *
     * @return The list of admins.
     */
    @GetMapping("")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            val employeeList = employeeService.getAll();
            return new ResponseEntity<>(employeeList, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new Employee.
     *
     * @param employee The new employee being created.
     * @return The new employee generated user id.
     */
    @PostMapping("/create")
    public ResponseEntity<Integer> createEmployee(@RequestBody Employee employee) {
        try {
            val createdEmployeeId = employeeService.addNewEmployee(employee);
            return new ResponseEntity<>(createdEmployeeId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes an Employee by their userId value.
     *
     * @param userId The employees user id.
     * @return The response status of the request.
     */
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Integer userId) {
        try {
            employeeService.deleteByUserId(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing Employee.
     *
     * @param employee The new employee details.
     * @return The response status of the request.
     */
    @PutMapping("/update/{userId}")
    public ResponseEntity<HttpStatus> updateEmployee(@PathVariable Integer userId, @RequestBody Employee employee) {
        try {
            employeeService.update(userId, employee);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets an Employee by their userId.
     *
     * @param userId The id of the employee being searched for.
     * @return The found employee data.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Integer userId) {
        try {
            val employee = employeeService.getByUserId(userId);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}