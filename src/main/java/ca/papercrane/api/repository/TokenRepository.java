package ca.papercrane.api.repository;

import ca.papercrane.api.security.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A {@link JpaRepository} class pertaining to {@link Token} entities.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    /**
     * Finds a list of tokens that belong to a specific userId.
     *
     * @param userId The user id that the tokens belong to.
     * @return The list of tokens.
     */
    List<Token> findAllByUserUserId(Integer userId);

    /**
     * Finds a token by it's string value.
     *
     * @param token The string value of the token.
     * @return The found token.
     */
    Optional<Token> findByToken(String token);

}