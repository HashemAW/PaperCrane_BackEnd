package ca.papercrane.api.repository;

import ca.papercrane.api.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A {@link JpaRepository} class pertaining to {@link Client} entities.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    /**
     * Finds a client by their respective user id.
     *
     * @param userId The id of the user.
     * @return The optional wrapped client user object.
     */
    Optional<Client> findByUserId(Integer userId);

    /**
     * Finds a client user by their email address.
     *
     * @param email The email address used to search for the user.
     * @return The optional wrapped client user object.
     */
    Optional<Client> findByEmail(String email);

}