package ca.papercrane.api.service.impl;

import ca.papercrane.api.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This service generates new tokens, validates existing tokens and extracts the required data from tokens.
 */
@Service
public class JwtServiceImpl implements JwtService {

    /**
     * The key size used to generate the JWT signing key.
     */
    private static final int KEY_SIZE = 256;

    /**
     * The JWT signing key.
     */
    private Key key;

    /**
     * Generates a new random JWT signing key on service initialization.
     */
    @PostConstruct
    public void init() {
        val random = new SecureRandom();
        val keyBytes = new byte[KEY_SIZE / 8];
        random.nextBytes(keyBytes);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        val claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        //24 hours in ms.
        val EXPIRATION_TIME = 86400000;

        //the generated token.
        return Jwts.builder().
                setClaims(extraClaims).
                setSubject(userDetails.getUsername()).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).
                signWith(key, SignatureAlgorithm.HS256).compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        val username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}