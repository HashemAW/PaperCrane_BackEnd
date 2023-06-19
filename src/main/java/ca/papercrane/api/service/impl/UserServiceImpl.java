package ca.papercrane.api.service.impl;

import ca.papercrane.api.entity.User;
import ca.papercrane.api.entity.role.UserRole;
import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.repository.UserRepository;
import ca.papercrane.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() throws ResourceNotFoundException {
        val userList = userRepository.findAll().stream().filter(e -> e.getRole().equals(UserRole.USER)).collect(Collectors.toList());
        if (userList.isEmpty()) {
            throw new ResourceNotFoundException("No users found!");
        }
        return userList;
    }

    @Override
    public User getByUserId(Integer userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public Long totalCount() {
        return userRepository.count();
    }

}