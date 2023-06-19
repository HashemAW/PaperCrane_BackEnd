package ca.papercrane.api.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * This interface provides the service implementation with
 * methods to generate new tokens, validate existing tokens and extract the required data from tokens.
 */
public interface JwtService {

    /**
     * Extracts the username from the provided JWT token.
     *
     * @param token the JWT token.
     * @return the extracted username.
     */
    String extractUsername(String token);

    /**
     * Extracts the specified claim from the provided JWT token.
     *
     * @param token          the JWT token.
     * @param claimsResolver the function used to extract the claim.
     * @param <T>            the type of the claim being extracted.
     * @return the extracted claim.
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Generates a new JWT token for the provided user.
     *
     * @param userDetails the user details.
     * @return the JWT token.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Generates a new JWT token with additional claims for the provided user.
     *
     * @param extraClaims the additional claims to include in the JWT token.
     * @param userDetails the user details.
     * @return the JWT token.
     */
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * Checks whether the provided JWT token is valid for the specified user.
     *
     * @param token       the JWT token.
     * @param userDetails the user details.
     * @return true if the token is valid, otherwise false.
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Checks whether the provided token has expired.
     *
     * @param token the token being checked.
     * @return true if the token is expired otherwise false.
     */
    boolean isTokenExpired(String token);

    /**
     * Extracts the tokens expiration date.
     *
     * @param token the token that the date is being extracted from.
     * @return the date result.
     */
    Date extractExpiration(String token);

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the JWT token to extract the claims from
     * @return the {@link Claims} object containing all claims from the token
     */
    Claims extractAllClaims(String token);

}