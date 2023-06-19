package ca.papercrane.api.service.impl;

import ca.papercrane.api.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutHandler {

    /**
     * The repository for storing user tokens.
     */
    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        //the request header.
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        //validate the header.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        //find the token in the repository
        tokenRepository.findByToken(authHeader.substring(7)).ifPresent(token -> {

            //invalidate the token, so it cannot be used for future requests.
            token.invalidate();

            //save the invalidated token back to the database.
            tokenRepository.save(token);

        });

    }

}