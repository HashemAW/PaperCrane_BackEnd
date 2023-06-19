package ca.papercrane.api.repository;

import ca.papercrane.api.request.TimeOffRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A {@link JpaRepository} class pertaining to {@link TimeOffRequest} entities.
 */
@Repository
public interface TimeOffRequestRepository extends JpaRepository<TimeOffRequest, Integer> {

    /**
     * Finds a TimeOffRequest by its corresponding timeOffId
     *
     * @param timeOffId The timeOffId being checked for an existing request.
     * @return The found optional wrapped request object otherwise empty.
     */
    Optional<TimeOffRequest> findByTimeOffId(Integer timeOffId);

    /**
     * Finds all time off requests that have been submitted by the provided userId.
     *
     * @param userId The id of the user that the list of requests will belong to.
     * @return The found optional wrapped request list otherwise empty.
     */
    Optional<List<TimeOffRequest>> findAllByEmployeeId(Integer userId);

}