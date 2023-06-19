package ca.papercrane.api.security.token;

import ca.papercrane.api.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a JWT (JSON Web Token)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public final class Token {

    /**
     * The unique identifier of the token.
     */
    @Id
    @Column(name = "token_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    /**
     * The token string.
     */
    @Column(unique = true, nullable = false)
    private String token;

    /**
     * The type of the token.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;

    /**
     * Has the token been revoked.
     */
    @Column(nullable = false)
    private boolean revoked;

    /**
     * Has the token has expired.
     */
    @Column(nullable = false)
    private boolean expired;

    /**
     * The user associated with the token.
     */
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Invalidates the token.
     */
    public void invalidate() {
        setRevoked(true);
        setExpired(true);
    }

    /**
     * The different types of tokens.
     */
    public enum TokenType {

        BEARER

    }

}