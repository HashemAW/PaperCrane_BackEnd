package ca.papercrane.api.service.impl;

import ca.papercrane.api.entity.User;
import ca.papercrane.api.repository.TokenRepository;
import ca.papercrane.api.security.login.LoginRequest;
import ca.papercrane.api.security.login.LoginResponse;
import ca.papercrane.api.security.token.Token;
import ca.papercrane.api.service.JwtService;
import ca.papercrane.api.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final TokenRepository tokenRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse authenticateAndBuildResponse(LoginRequest request) {

        //authenticate the request.
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        //get the authenticated user
        var user = (User) authentication.getPrincipal();

        //build the authentication token.
        val token = Token.builder().user(user).token(jwtService.generateToken(user)).tokenType(Token.TokenType.BEARER).expired(false).revoked(false).build();

        //revokes the authenticated users previous tokens.
        invalidateAllTokensForUser(user);

        //saves the new token to the database.
        tokenRepository.save(token);

        //return the built authentication response.
        return LoginResponse.builder().id(user.getUserId()).email(user.getEmail()).role(user.getRole().toString()).token(token.getToken()).build();
    }

    @Override
    public void invalidateAllTokensForUser(User user) {

        //find all tokens for the specified user.
        val userTokenList = tokenRepository.findAllByUserUserId(user.getUserId());

        //filter the users token list to be only active tokens.
        val validTokenList = userTokenList.stream().filter(token -> !token.isExpired() && !token.isRevoked()).collect(Collectors.toList());

        //invalidate each of their tokens.
        validTokenList.forEach(Token::invalidate);

        //save all the invalidated tokens back to the repository.
        tokenRepository.saveAll(validTokenList);

    }

}