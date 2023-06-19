package ca.papercrane.api.controller;

import ca.papercrane.api.entity.User;
import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("")
    public ResponseEntity<List<User>> getAll() {
        try {
            val userList = userService.getAll();
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Integer userId) {
        try {
            val user = userService.getByUserId(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}