package ca.papercrane.api.controller;

import ca.papercrane.api.entity.Admin;
import ca.papercrane.api.entity.role.EmployeeType;
import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.service.impl.AdminServiceImpl;
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
@RequestMapping("api/v1/admins")
public class AdminController {

    private final AdminServiceImpl adminService;

    @PostConstruct
    public void init() {
        if (!adminService.exists("admin@papercrane.ca")) {
            adminService.addNewAdmin(new Admin("admin@papercrane.ca", "password", "John", "Doe", EmployeeType.DEVELOPER));
        }
    }

    /**
     * Gets a list of all Admin employees.
     *
     * @return The list of admins.
     */
    @GetMapping("")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        try {
            val adminList = adminService.getAll();
            return new ResponseEntity<>(adminList, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new Admin.
     *
     * @param admin The new Admin being created.
     * @return The admins generated user id.
     */
    @PostMapping("/create")
    public ResponseEntity<Integer> createAdmin(@RequestBody Admin admin) {
        try {
            val createdAdminId = adminService.addNewAdmin(admin);
            return new ResponseEntity<>(createdAdminId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes an Admin by their userId value.
     *
     * @param userId The admins user id.
     * @return The response status of the request.
     */
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<HttpStatus> deleteAdmin(@PathVariable Integer userId) {
        try {
            adminService.deleteByUserId(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing Admin.
     *
     * @param userId The id of the existing admin user.
     * @param admin  The new admin details.
     * @return The response status of the request.
     */
    @PutMapping("/update/{userId}")
    public ResponseEntity<HttpStatus> updateAdmin(@PathVariable Integer userId, @RequestBody Admin admin) {
        try {
            adminService.update(userId, admin);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets an Admin user by their userId.
     *
     * @param userId The user id of the admin being searched for.
     * @return The admin user data found.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Admin> getAdmin(@PathVariable Integer userId) {
        try {
            val admin = adminService.getByUserId(userId);
            return new ResponseEntity<>(admin, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}