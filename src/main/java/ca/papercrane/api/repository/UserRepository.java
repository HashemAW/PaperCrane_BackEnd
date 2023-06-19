package ca.papercrane.api.repository;

import ca.papercrane.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A {@link JpaRepository} class pertaining to {@link User} entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their respective user id.
     *
     * @param userId The id of the user.
     * @return The optional wrapped user object.
     */
    Optional<User> findByUserId(Integer userId);

    /**
     * Finds a user by their email address.
     *
     * @param email The user email address.
     * @return The found optional wrapped user object otherwise empty.
     */
    Optional<User> findByEmail(String email);

}