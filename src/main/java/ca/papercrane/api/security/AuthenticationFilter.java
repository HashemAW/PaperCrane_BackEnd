package ca.papercrane.api.security;

import ca.papercrane.api.repository.TokenRepository;
import ca.papercrane.api.service.impl.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter intercepts incoming requests, extracts
 * the token, validates the token, and sets the authenticated user.
 * <p>
 * The filter utilizes the {@link OncePerRequestFilter} class, which ensures that the filter is only executed once
 * per request.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    /**
     * A service for generating, validating, and extracting data from a jwt token.
     */
    private final JwtServiceImpl jwtService;

    /**
     * A service used to validate users details.
     */
    private final UserDetailsService userDetailsService;

    /**
     * The repository to store user authentication tokens.
     */
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        //get the authorization header from the request.
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        //if the authorization header is missing or doesn't contain a Bearer token, continue the filter chain.
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //extract the token from the authorization header.
        val jwt = authorizationHeader.substring(7);

        //extract the username from the token.
        val userEmail = jwtService.extractUsername(jwt);

        //check if the user is authenticated and has a valid token.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //load the user details by the email string.
            val userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            //check if the token is valid.
            var isTokenValid = tokenRepository.findByToken(jwt).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);

            //if the token is valid, create an authentication object and set it in the security context.
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                val authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        //continue the filter chain.
        filterChain.doFilter(request, response);

    }


}