package ca.papercrane.api.entity;

import ca.papercrane.api.entity.role.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;

/**
 * Represents the base for all user accounts.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "user_account")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    /**
     * The user's auto-generated id.
     */
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    /**
     * The users' role.
     */
    @Enumerated(EnumType.STRING)
    private UserRole role;

    /**
     * The users email address.
     */
    @Column(name = "email", unique = true, length = 100, nullable = false)
    private String email;

    /**
     * The users' password.
     */
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    /**
     * Creates a new User.
     *
     * @param email    The user email address. Used for login.
     * @param password The user password. Used for login.
     */
    public User(String email, String password, UserRole role) {
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}