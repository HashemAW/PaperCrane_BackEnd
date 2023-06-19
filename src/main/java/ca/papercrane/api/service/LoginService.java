package ca.papercrane.api.service;

import ca.papercrane.api.entity.User;
import ca.papercrane.api.security.login.LoginRequest;
import ca.papercrane.api.security.login.LoginResponse;

public interface LoginService {

    LoginResponse authenticateAndBuildResponse(LoginRequest request);

    void invalidateAllTokensForUser(User user);

}