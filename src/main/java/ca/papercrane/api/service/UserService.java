package ca.papercrane.api.service;

import ca.papercrane.api.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getByUserId(Integer userId);

    User getByEmail(String email);

    Long totalCount();

}