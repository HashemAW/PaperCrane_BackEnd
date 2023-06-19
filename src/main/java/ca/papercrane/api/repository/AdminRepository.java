package ca.papercrane.api.repository;

import ca.papercrane.api.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A {@link JpaRepository} class pertaining to {@link Admin} entities.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    /**
     * Finds an Admin by their respective user id.
     *
     * @param userId The id of the user.
     * @return The optional wrapped admin user object.
     */
    Optional<Admin> findByUserId(Integer userId);

    /**
     * Finds an Admin user by their email address.
     *
     * @param email The email address used to search for the user.
     * @return The optional wrapped admin user object.
     */
    Optional<Admin> findByEmail(String email);

}