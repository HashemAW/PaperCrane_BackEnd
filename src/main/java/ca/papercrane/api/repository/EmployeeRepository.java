package ca.papercrane.api.repository;

import ca.papercrane.api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A {@link JpaRepository} class pertaining to {@link Employee} entities.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /**
     * Finds an Employee by their respective user id.
     *
     * @param userId The id of the user.
     * @return The optional wrapped employee user object.
     */
    Optional<Employee> findByUserId(Integer userId);

    /**
     * Finds an Employee user by their email address.
     *
     * @param email The email address used to search for the user.
     * @return The optional wrapped employee user object.
     */
    Optional<Employee> findByEmail(String email);

}